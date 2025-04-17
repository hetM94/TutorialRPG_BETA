package net.het.tutorialrpg.event;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.system.mana.IMana;
import net.het.tutorialrpg.util.ModCommands;
import net.het.tutorialrpg.util.NetworkUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventsBus {
    private static final int MANA_REGEN_INTERVAL = 20; // ticks
    private static final Map<UUID, Integer> tickCounters = new ConcurrentHashMap<>();
    private static final int MANA_REGEN_AMOUNT = 1;

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            IMana manaCap = player.getCapability(ModCapability.MANA);
            if (manaCap != null) {
                NetworkUtil.syncMana(player, manaCap.getMana(), manaCap.getMaxMana());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        UUID id = player.getUUID();
        int cnt = tickCounters.getOrDefault(id, 0) + 1;

        if (cnt >= MANA_REGEN_INTERVAL) {
            cnt = 0;
            IMana manaCap = player.getCapability(ModCapability.MANA);
            if (manaCap != null) {
                int newMana = Math.min(manaCap.getMana() + MANA_REGEN_AMOUNT, manaCap.getMaxMana());
                manaCap.setMana(newMana);
                if (player instanceof ServerPlayer serverPlayer) {
                    NetworkUtil.syncMana(serverPlayer, newMana, manaCap.getMaxMana());
                }
            }
        }

        tickCounters.put(id, cnt);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        tickCounters.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        syncAll(e.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        syncAll(e.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        syncAll(e.getEntity());
    }

    private static void syncAll(Player player) {
        if (!(player instanceof ServerPlayer sp)) return;
        IMana manaCap = player.getCapability(ModCapability.MANA);
        if (manaCap != null) {
            NetworkUtil.syncMana(sp, manaCap.getMana(), manaCap.getMaxMana());
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}

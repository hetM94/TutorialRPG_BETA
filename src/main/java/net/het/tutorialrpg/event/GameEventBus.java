package net.het.tutorialrpg.event;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.network.mana.PacketSyncMana;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber (modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventBus {

    private static final int REGEN_INTERVAL = 20; // ticks
    private static final int REGEN_AMOUNT = 1;
    private static final Map<UUID, Integer> tickMap = new ConcurrentHashMap<>();


    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();
        UUID id = player.getUUID();
        IMana cap = ModCapability.MANA.getCapability(player, null);
        int cnt = tickMap.getOrDefault(id, 0) + 1;
        regenMana(cnt, player, id, cap);
        tickMap.put(id, cnt);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) syncMana(sp);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) syncMana(sp);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) syncMana(sp);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        tickMap.remove(event.getEntity().getUUID());
    }

    private static void syncMana(ServerPlayer player) {
        IMana cap = ModCapability.MANA.getCapability(player, null);
        if(cap != null){
            PacketSyncMana packet = new PacketSyncMana(player.getUUID(), cap.getMana(), cap.getMaxMana());
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

    private static void regenMana(int cnt, Player player, UUID id, IMana cap) {
        if (cnt >= REGEN_INTERVAL) {
            cnt = 0;
            if(cap != null) {
                int old = cap.getMana();
                cap.addMana(REGEN_AMOUNT);
                if (cap.getMana() != old && player instanceof ServerPlayer sp) {
                    syncMana(sp);
                }
            }
        }
    }
}

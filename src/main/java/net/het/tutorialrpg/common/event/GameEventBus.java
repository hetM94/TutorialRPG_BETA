package net.het.tutorialrpg.common.event;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.common.util.ManaUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventBus {

    private static final int REGEN_INTERVAL = 20;
    private static final int REGEN_AMOUNT = 1;
    private static boolean regenEnabled = true;

    // Using WeakHashMap to allow garbage collection of player entries when no longer in use
    private static final Map<UUID, Integer> tickMap = new WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide() || !regenEnabled) return;

        Player player = event.getEntity();
        UUID playerId = player.getUUID();

        // Atomically update tick count
        int tick = tickMap.compute(playerId, (id, count) -> (count == null ? 1 : count + 1));

        if (tick >= REGEN_INTERVAL) {
            tickMap.put(playerId, 0);
            ManaUtil.addMana(player, REGEN_AMOUNT);

            if (player instanceof ServerPlayer serverPlayer) {
                ManaUtil.syncManaToClient(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ManaUtil.syncFromAttachment(player);
        ManaUtil.consumeMana(player, ManaUtil.getMana(player));

        if (player instanceof ServerPlayer serverPlayer) {
            ManaUtil.syncManaToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        ManaUtil.syncFromAttachment(player);
        tickMap.remove(player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ManaUtil.syncFromAttachment(player);
        ManaUtil.setMana(player, ManaUtil.getMana(player));

        if (player instanceof ServerPlayer serverPlayer) {
            ManaUtil.syncManaToClient(serverPlayer);
        }

    }

    public static void setRegen(boolean enabled) {
        regenEnabled = enabled;
    }

    public static boolean isRegenEnabled() {
        return regenEnabled;
    }
}

package net.het.tutorialrpg.event;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.util.ModUtil;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventBus {
    public static boolean REGEN = true;
    private static final int REGEN_INTERVAL = 20;
    private static final int REGEN_AMOUNT   = 1;
    private static final Map<UUID, Integer> tickMap = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        // Only run on the server side
        if (event.getEntity().level().isClientSide()) return;

        if(isRegen()){
            Player player = event.getEntity();
            UUID id = player.getUUID();
            int tick = tickMap.getOrDefault(id, 0) + 1;

            if (tick >= REGEN_INTERVAL) {
                tick = 0;
                // Use ModUtil to add mana (handles capability + attachment sync)
                ModUtil.addMana(player, REGEN_AMOUNT);
            }

            tickMap.put(id, tick);
        }
    }

    // No manual zeroing on respawn needed: copyOnDeath=false on the attachment handles it.
    // Logout cleanup:
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        ModUtil.syncFromAttachment(player);
        tickMap.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ModUtil.syncFromAttachment(player);
    }

    public static void setRegen(boolean regen) {
        REGEN = regen;
    }

    public static boolean isRegen(){
        return REGEN;
    }
}

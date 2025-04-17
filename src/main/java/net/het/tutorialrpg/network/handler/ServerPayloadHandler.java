package net.het.tutorialrpg.network.handler;

import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.network.ManaData;
import net.het.tutorialrpg.system.mana.IMana;
import net.het.tutorialrpg.util.NetworkUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleManaData(ManaData data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                IMana manaCap = serverPlayer.getCapability(ModCapability.MANA);
                if (manaCap != null) {
                    manaCap.setMaxMana(data.maxMana());
                    manaCap.setMana(data.currentMana());
                    NetworkUtil.syncMana(serverPlayer, manaCap.getMana(), manaCap.getMaxMana());
                }
                else {
                    // Handle the case where the capability is not present
                    System.err.println("Mana capability not found for player: " + serverPlayer.getName().getString());
                }
            }
        });
    }
}

package net.het.tutorialrpg.network.mana.handler;

import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.network.mana.PacketSyncMana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientHandler {
    public static void handleSyncMana(final PacketSyncMana msg, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null || !player.getUUID().equals(msg.playerId())) return;
            IMana cap = ModCapability.MANA.getCapability(player, null);
            if (cap != null) {
                cap.setMaxMana(msg.maxMana());
                cap.setMana(msg.mana());
//                System.out.println("Client mana updated to: " + cap.getMana());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.literal("Failed to sync mana: " + e.getMessage()));
            return null;
        });
    }
}

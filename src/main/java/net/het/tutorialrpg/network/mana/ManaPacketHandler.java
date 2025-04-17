package net.het.tutorialrpg.network.mana;

import net.het.tutorialrpg.common.util.ManaUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ManaPacketHandler {
    public static void handleSyncMana(final ManaSyncPacket msg, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null || !player.getUUID().equals(msg.playerId())) return;
            ManaUtil.setMaxMana(player, msg.maxMana());
            ManaUtil.setMana(player, msg.mana());
        }).exceptionally(e -> {
            context.disconnect(Component.literal("Failed to sync mana: " + e.getMessage()));
            return null;
        });
    }
}

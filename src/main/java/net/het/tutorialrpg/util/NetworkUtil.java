package net.het.tutorialrpg.util;

import net.het.tutorialrpg.network.ManaData;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkUtil {
    public static void syncMana(ServerPlayer player, int current, int max) {
        PacketDistributor.sendToPlayer(player, new ManaData(current, max));
    }
}

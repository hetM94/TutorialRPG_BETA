package net.het.tutorialrpg.network.mana;

import net.het.tutorialrpg.TutorialRPG;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record PacketSyncMana(UUID playerId, int mana, int maxMana) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketSyncMana> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "sync_mana"));

    public static final StreamCodec<FriendlyByteBuf, PacketSyncMana> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, PacketSyncMana::playerId,
            ByteBufCodecs.VAR_INT, PacketSyncMana::mana,
            ByteBufCodecs.VAR_INT, PacketSyncMana::maxMana,
            PacketSyncMana::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


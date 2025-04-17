package net.het.tutorialrpg.network.mana;

import net.het.tutorialrpg.TutorialRPG;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ManaSyncPacket(UUID playerId, int mana, int maxMana) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ManaSyncPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "sync_mana"));

    public static final StreamCodec<FriendlyByteBuf, ManaSyncPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ManaSyncPacket::playerId,
            ByteBufCodecs.VAR_INT, ManaSyncPacket::mana,
            ByteBufCodecs.VAR_INT, ManaSyncPacket::maxMana,
            ManaSyncPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


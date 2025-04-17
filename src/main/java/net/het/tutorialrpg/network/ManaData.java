package net.het.tutorialrpg.network;

import io.netty.buffer.ByteBuf;
import net.het.tutorialrpg.TutorialRPG;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ManaData(int currentMana, int maxMana) implements CustomPacketPayload {
    public static final Type<ManaData> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "mana_data"));

    public static final StreamCodec<ByteBuf, ManaData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ManaData::currentMana,
            ByteBufCodecs.VAR_INT, ManaData::maxMana,
            ManaData::new
    );

    @Override public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}

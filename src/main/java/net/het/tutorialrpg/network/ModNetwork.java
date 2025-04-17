package net.het.tutorialrpg.network;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.network.mana.ManaSyncPacket;
import net.het.tutorialrpg.network.mana.ManaPacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber (modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar= event.registrar("1");

        registrar.playToClient(
                ManaSyncPacket.TYPE,
                ManaSyncPacket.STREAM_CODEC,
                ManaPacketHandler::handleSyncMana
        );
    }
}

package net.het.tutorialrpg.network;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.network.mana.PacketSyncMana;
import net.het.tutorialrpg.network.mana.handler.ClientHandler;
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
                PacketSyncMana.TYPE,
                PacketSyncMana.STREAM_CODEC,
                ClientHandler::handleSyncMana
        );
    }
}

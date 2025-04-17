package net.het.tutorialrpg.network;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.network.handler.ClientPayloadHandler;
import net.het.tutorialrpg.network.handler.ServerPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                ManaData.TYPE,
                ManaData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleManaData,
                        ServerPayloadHandler::handleManaData
                )
        );
    }
}

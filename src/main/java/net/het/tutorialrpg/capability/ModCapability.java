package net.het.tutorialrpg.capability;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.capability.mana.Mana;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapability {

    public static final EntityCapability<IMana, Void> MANA =
            EntityCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "mana"),
                    IMana.class
            );

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        // Attach a fresh Mana(100,100) to every player
        event.registerEntity(MANA, EntityType.PLAYER, (player, ctx) ->
                new Mana(0, 100)
        );
    }
}

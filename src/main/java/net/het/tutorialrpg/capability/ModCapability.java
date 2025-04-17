// src/main/java/net/het/tutorialrpg/capability/ModCapability.java

package net.het.tutorialrpg.capability;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.system.mana.IMana;
import net.het.tutorialrpg.system.mana.Mana;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModCapability {
    public static final EntityCapability<IMana, Void> MANA =
            EntityCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "mana"),
                    IMana.class
            );

    public static void register(RegisterCapabilitiesEvent event) {
        // Attach a fresh Mana(100,100) to every player
        event.registerEntity(MANA, EntityType.PLAYER, (player, ctx) ->
                new Mana(100, 100)
        );
    }
}

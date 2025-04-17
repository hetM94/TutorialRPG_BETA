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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapability {
    private static final Map<UUID, Mana> MANA_STORE = new ConcurrentHashMap<>();

    public static final EntityCapability<IMana, Void> MANA =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, "mana"), IMana.class);

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerEntity(MANA, EntityType.PLAYER, (player, ctx) -> {
            UUID id = player.getUUID();
            return MANA_STORE.computeIfAbsent(id, uuid -> new Mana(0, 100));
        });
    }
}

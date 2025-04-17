package net.het.tutorialrpg.data;

import com.mojang.serialization.Codec;
import net.het.tutorialrpg.TutorialRPG;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TutorialRPG.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> MANA_CURRENT =
            ATTACHMENTS.register("mana_current", () ->
                    AttachmentType.<Integer>builder(() -> 0)
                            .serialize(Codec.INT)
                            .build()
            );

    public static final Supplier<AttachmentType<Integer>> MANA_MAX =
            ATTACHMENTS.register("mana_max", () ->
                    AttachmentType.<Integer>builder(() -> 100)
                            .serialize(Codec.INT)
                            .copyOnDeath()
                            .build()
            );
    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}

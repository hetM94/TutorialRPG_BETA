package net.het.tutorialrpg.common.event;

import com.mojang.brigadier.CommandDispatcher;
import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.common.command.ModCommands;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber (modid = TutorialRPG.MOD_ID)
public class CommonEventBus {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        ModCommands.register(dispatcher);
    }
}

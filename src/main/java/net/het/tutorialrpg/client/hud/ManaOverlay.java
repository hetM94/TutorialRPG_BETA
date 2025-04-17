package net.het.tutorialrpg.client.hud;

import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.common.util.ManaUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ManaOverlay {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if(player.level().isClientSide){
            int mana    = ManaUtil.getMana(player);
            int maxMana = ManaUtil.getMaxMana(player);

//        System.out.println(ManaUtil.getMana(player));
            String text = mana + " / " + maxMana;
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font, text, 10, 10, 0xFFFFFF);
        }
        }
}

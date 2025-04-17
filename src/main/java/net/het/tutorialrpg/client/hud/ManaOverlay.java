//package net.het.tutorialrpg.client.hud;
//
//import net.het.tutorialrpg.capability.ModCapability;
//import net.het.tutorialrpg.capability.mana.IMana;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
//
//@EventBusSubscriber
//public class ManaOverlay {
//
//    @SubscribeEvent
//    public static void onRenderGameOverlay(RenderGuiLayerEvent.Pre event) {
//        Player player = Minecraft.getInstance().player;
//        if (player == null) return;
//
//        IMana cap = ModCapability.MANA.getCapability(player, null);
//        if (cap == null) return;
//
//        String text = cap.getMana() + " / " + cap.getMaxMana();
//        event.getGuiGraphics().drawString(Minecraft.getInstance().font, text, 10, 10, 0xFFFFFF);
//    }
//}
//
package net.het.tutorialrpg.client.hud;

import net.het.tutorialrpg.data.ModData;
import net.het.tutorialrpg.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber
public class ManaOverlay {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int mana    = ModUtil.getMana(player);
        int maxMana = ModUtil.getMaxMana(player);

        String text = mana + " / " + maxMana;
        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font, text, 10, 10, 0xFFFFFF);
    }
}

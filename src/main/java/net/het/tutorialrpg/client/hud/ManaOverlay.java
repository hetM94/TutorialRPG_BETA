package net.het.tutorialrpg.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.het.tutorialrpg.TutorialRPG;
import net.het.tutorialrpg.common.util.ManaUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = TutorialRPG.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ManaOverlay {

    private static final ResourceLocation FILL_TEX    = rl("textures/gui/mana_bar.png");
    private static final ResourceLocation ORB_TEX     = rl("textures/gui/mana_orb.png");
    private static final ResourceLocation OUTLINE_TEX = rl("textures/gui/mana_outline.png");

    private static final int X = 5, Y = 210;
    private static final int ORB_W = 16, BAR_W = 48, HEIGHT = 16;

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, rl("mana_bar"),
                (GuiGraphics gui, DeltaTracker delta) -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player == null) return;

                    int current = ManaUtil.getMana(mc.player);
                    int max = ManaUtil.getMaxMana(mc.player);
                    if (max <= 0) return;

                    float ratio = (float) current / max;
                    int clipped = Math.round(ratio * BAR_W);
                    int percent = Math.round(ratio * 100);


                    int barStartX = X + ORB_W;

                    drawTexture(gui, ORB_TEX, X, Y, ORB_W, HEIGHT);
                    if (clipped > 0) {
                        drawTexture(gui, FILL_TEX, barStartX - 1, Y, clipped, HEIGHT, BAR_W, HEIGHT);
                    }
                    drawTexture(gui, OUTLINE_TEX, barStartX - 2, Y, BAR_W, HEIGHT, BAR_W, HEIGHT);

                    Font font = mc.font;
                    String txt = percent + "%";
                    int txtX = X + ((ORB_W + BAR_W - font.width(txt)) / 2);
                    gui.drawString(font, txt, txtX, Y + HEIGHT + 2, 0xFFFFFF);
                });
    }

    private static void drawTexture(GuiGraphics gui, ResourceLocation tex, int x, int y, int w, int h) {
        drawTexture(gui, tex, x, y, w, h, w, h);
    }

    private static void drawTexture(GuiGraphics gui, ResourceLocation tex, int x, int y, int w, int h, int texW, int texH) {
        RenderSystem.setShaderTexture(0, tex);
        gui.blit(tex, x, y, 0, 0, w, h, texW, texH);
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(TutorialRPG.MOD_ID, path);
    }
}

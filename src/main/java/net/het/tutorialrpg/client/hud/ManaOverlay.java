package net.het.tutorialrpg.client.hud;

import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class ManaOverlay {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int currentMana = getCurrentMana(player);
        int maxMana = getMaxMana(player);
        String text = currentMana + " / " + maxMana;

        event.getGuiGraphics()
                .drawString(Minecraft.getInstance().font, text, 10, 10, 0xFFFFFF);
    }

    private static @Nullable IMana getCap(Player player) {
        // null direction since Void
        return ModCapability.MANA.getCapability(player, null);
    }

    private static int getCurrentMana(Player player) {
        IMana cap = getCap(player);
        return cap != null ? cap.getMana() : 0;
    }

    private static int getMaxMana(Player player) {
        IMana cap = getCap(player);
        return cap != null ? cap.getMaxMana() : 0;
    }
}

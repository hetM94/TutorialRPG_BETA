package net.het.tutorialrpg.network.handler;

import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.network.ManaData;
import net.het.tutorialrpg.system.mana.IMana;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class ClientPayloadHandler {
    public static void handleManaData(ManaData data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                IMana manaCap = player.getCapability(ModCapability.MANA);
                if (manaCap != null) {
                    manaCap.setMaxMana(data.maxMana());
                    manaCap.setMana(data.currentMana());
                }
            }
        });
    }
}

package net.het.tutorialrpg.util;

import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.data.ModData;
import net.minecraft.world.entity.player.Player;

public class ModUtil {

    private static IMana getCap(Player player) {
        return ModCapability.MANA.getCapability(player, null);
    }

    private static void syncCurrent(Player player, IMana cap) {
        player.setData(ModData.MANA_CURRENT.get(), cap.getMana());
    }

    private static void syncMax(Player player, IMana cap) {
        player.setData(ModData.MANA_MAX.get(), cap.getMaxMana());
    }

    public static void syncFromAttachment(Player player) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMana(player.getData(ModData.MANA_CURRENT.get()));
            cap.setMaxMana(player.getData(ModData.MANA_MAX.get()));
        }
    }

    public static void addMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            syncFromAttachment(player);
            cap.addMana(amount);
            syncCurrent(player, cap);
        }
    }

    public static void consumeMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            syncFromAttachment(player);
            cap.consumeMana(amount);
            syncCurrent(player, cap);
        }
    }

    public static void setMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMana(amount);
            syncCurrent(player, cap);
        }
    }

    public static void setMaxMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMaxMana(amount);
            syncMax(player, cap);
            if (cap.getMana() > cap.getMaxMana()) {
                cap.setMana(cap.getMaxMana());
                syncCurrent(player, cap);
            }
        }
    }

    public static int getMana(Player player) {
        IMana cap = getCap(player);
            syncFromAttachment(player);
            return cap.getMana();

    }

    public static int getMaxMana(Player player) {
        IMana cap = getCap(player);
            syncFromAttachment(player);
            return cap.getMaxMana();
        }
}

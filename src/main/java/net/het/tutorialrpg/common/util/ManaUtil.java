package net.het.tutorialrpg.common.util;

import net.het.tutorialrpg.capability.mana.ManaCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.data.ModData;
import net.het.tutorialrpg.network.mana.ManaSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class ManaUtil {

    /** Internal: Gets the IMana capability. Returns null if not present. */
    private static IMana getCap(Player player) {
        return ManaCapability.MANA.getCapability(player, null);
    }

    /** Internal: Copies data from capability to player persistent data */
    private static void syncToAttachment(Player player, IMana cap) {
        player.setData(ModData.MANA_CURRENT.get(), cap.getMana());
        player.setData(ModData.MANA_MAX.get(), cap.getMaxMana());
    }

    /** Internal: Loads persistent data into capability */
    public static void syncFromAttachment(Player player) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMana(player.getData(ModData.MANA_CURRENT.get()));
            cap.setMaxMana(player.getData(ModData.MANA_MAX.get()));
        }
    }

    /** Syncs mana to the client side */
    public static void syncManaToClient(ServerPlayer player) {
        IMana cap = getCap(player);
        if (cap != null) {
            syncToAttachment(player, cap); // Always update stored data first
            ManaSyncPacket packet = new ManaSyncPacket(player.getUUID(), cap.getMana(), cap.getMaxMana());
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

    /** Adds mana and updates both capability and stored data */
    public static void addMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null && amount != 0) {
            syncFromAttachment(player);
            cap.addMana(amount);
            syncToAttachment(player, cap);
        }
    }

    /** Consumes mana and updates both capability and stored data */
    public static void consumeMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null && amount != 0) {
            syncFromAttachment(player);
            cap.consumeMana(amount);
            syncToAttachment(player, cap);
        }
    }

    /** Sets mana to a fixed amount, clamped if above max */
    public static void setMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMana(Math.min(amount, cap.getMaxMana()));
            syncToAttachment(player, cap);
        }
    }

    /** Sets maximum mana and clamps current mana to new max */
    public static void setMaxMana(Player player, int amount) {
        IMana cap = getCap(player);
        if (cap != null) {
            cap.setMaxMana(amount);
            if (cap.getMana() > amount) {
                cap.setMana(amount);
            }
            syncToAttachment(player, cap);
        }
    }

    /** Gets current mana (desync-safe) */
    public static int getMana(Player player) {
        IMana cap = getCap(player);
        return cap != null ? cap.getMana() : 0;
    }

    /** Gets max mana (desync-safe) */
    public static int getMaxMana(Player player) {
        IMana cap = getCap(player);
        return cap != null ? cap.getMaxMana() : 0;
    }
}

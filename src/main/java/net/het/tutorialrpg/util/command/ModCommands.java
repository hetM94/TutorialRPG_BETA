// src/main/java/net/het/tutorialrpg/util/ModCommands.java
package net.het.tutorialrpg.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.capability.mana.IMana;
import net.het.tutorialrpg.network.mana.PacketSyncMana;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("mana")
                        .then(Commands.literal("get")
                                .executes(ctx -> {
                                    ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                    IMana cap = sp.getCapability(ModCapability.MANA, null);
                                    if (cap != null) {
                                        int mana = cap.getMana();
                                        ctx.getSource().sendSuccess(() ->
                                                Component.literal("Current Mana is " + mana + "/"+cap.getMaxMana()), false);

                                    }
                                    return 1;
                                }))
                        .then(Commands.literal("add")
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                            IMana cap = sp.getCapability(ModCapability.MANA, null);
                                            if (cap != null) {
                                                cap.addMana(amt);
                                                syncMana(sp);
                                                ctx.getSource().sendSuccess(() -> Component.literal("Added " + amt + " mana."), true);
                                            }
                                            return 1;
                                        })))
                        .then(Commands.literal("consume")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            int consumed = IntegerArgumentType.getInteger(ctx, "amount");
                                            IMana cap = sp.getCapability(ModCapability.MANA, null);
                                            if (cap != null) {
                                                cap.consumeMana(consumed);
                                                syncMana(sp);
                                                ctx.getSource().sendSuccess(() -> Component.literal("Consumed " + consumed+ " mana"), true);
                                            }
                                            return 1;
                                        })))
                        .then(Commands.literal("setmax")
                                .then(Commands.argument("max", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            int max = IntegerArgumentType.getInteger(ctx, "max");
                                            IMana cap = sp.getCapability(ModCapability.MANA, null);
                                            if (cap != null) {
                                                cap.setMaxMana(max);
                                                syncMana(sp);
                                                ctx.getSource().sendSuccess(() -> Component.literal("Max mana set to " + max), true);
                                            }
                                            return 1;
                                        })))
        );
    }

    private static void syncMana(ServerPlayer player) {
        IMana cap = ModCapability.MANA.getCapability(player, null);
        if (cap != null) {
            PacketSyncMana packet = new PacketSyncMana(player.getUUID(), cap.getMana(), cap.getMaxMana());
            PacketDistributor.sendToPlayer(player, packet);
        }
    }
}

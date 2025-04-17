package net.het.tutorialrpg.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.het.tutorialrpg.capability.ModCapability;
import net.het.tutorialrpg.system.mana.IMana;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mana")
                .requires(src -> !src.getLevel().isClientSide)
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    Player player = context.getSource().getPlayerOrException();
                                    IMana manaCap = ModCapability.MANA.getCapability(player, null);
                                    if (manaCap != null) {
                                        manaCap.setMana(amount);
                                        NetworkUtil.syncMana((ServerPlayer) player, manaCap.getMana(), manaCap.getMaxMana());
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("Mana set to: " + amount), false);
                                    } else {
                                        context.getSource().sendFailure(Component.literal("Mana capability not found."));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    Player player = context.getSource().getPlayerOrException();
                                    IMana manaCap = ModCapability.MANA.getCapability(player, null);
                                    if (manaCap != null) {
                                        manaCap.addMana(amount);
                                        NetworkUtil.syncMana((ServerPlayer) player, manaCap.getMana(), manaCap.getMaxMana());
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("Added " + amount + " mana."), false);
                                    } else {
                                        context.getSource().sendFailure(Component.literal("Mana capability not found."));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("consume")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    Player player = context.getSource().getPlayerOrException();
                                    IMana manaCap = ModCapability.MANA.getCapability(player, null);
                                    if (manaCap != null) {
                                        manaCap.consumeMana(amount);
                                        NetworkUtil.syncMana((ServerPlayer) player, manaCap.getMana(), manaCap.getMaxMana());
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("Consumed " + amount + " mana."), false);
                                    } else {
                                        context.getSource().sendFailure(Component.literal("Mana capability not found."));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("get")
                        .executes(context -> {
                            Player player = context.getSource().getPlayerOrException();
                            IMana manaCap = ModCapability.MANA.getCapability(player, null);
                            if (manaCap != null) {
                                int mana = manaCap.getMana();
                                context.getSource().sendSuccess(() ->
                                        Component.literal("Current Mana is " + mana + " mana."), false);
                                NetworkUtil.syncMana((ServerPlayer) player, manaCap.getMana(), manaCap.getMaxMana());
                            } else {
                                context.getSource().sendFailure(Component.literal("Mana capability not found."));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}

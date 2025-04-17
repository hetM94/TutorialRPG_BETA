//package net.het.tutorialrpg.common.command;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.BoolArgumentType;
//import com.mojang.brigadier.arguments.IntegerArgumentType;
//import net.het.tutorialrpg.common.event.GameEventBus;
//import net.het.tutorialrpg.common.util.ManaUtil;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//
//public class ModCommands {
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(
//                Commands.literal("mana")
//                        .then(Commands.literal("add")
//                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
//                                        .executes(ctx -> {
//                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
//                                            ManaUtil.addMana(ctx.getSource().getPlayer(), amt);
//                                            ctx.getSource().sendSuccess(
//                                                    () -> Component.literal("Added " + amt + " mana."),
//                                                    true
//                                            );
//                                            return 1;
//                                        })
//                                )
//                        )
//                        .then(Commands.literal("consume")
//                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
//                                        .executes(ctx -> {
//                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
//                                            ManaUtil.consumeMana(ctx.getSource().getPlayer(), amt);
//                                            ctx.getSource().sendSuccess(
//                                                    () -> Component.literal("Consumed " + amt + " mana."),
//                                                    true
//                                            );
//                                            return 1;
//                                        })
//                                )
//                        )
//                        .then(Commands.literal("setmax")
//                                .then(Commands.argument("max", IntegerArgumentType.integer(0))
//                                        .executes(ctx -> {
//                                            int max = IntegerArgumentType.getInteger(ctx, "max");
//                                            ManaUtil.setMaxMana(ctx.getSource().getPlayer(), max);
//                                            ctx.getSource().sendSuccess(
//                                                    () -> Component.literal("Max mana set to " + max),
//                                                    true
//                                            );
//                                            return 1;
//                                        })
//                                )
//                        )
//                        .then(Commands.literal("get")
//                                .executes(ctx -> {
//                                    ServerPlayer sp = ctx.getSource().getPlayerOrException();
//                                    int curr = ManaUtil.getMana(ctx.getSource().getPlayer());
//                                    int max = ManaUtil.getMaxMana(ctx.getSource().getPlayer());
//                                    ctx.getSource().sendSuccess(
//                                            () -> Component.literal("Mana: " + curr + "/" + max),
//                                            false
//                                    );
//                                    return 1;
//                                })
//                        )
//                        .then(Commands.literal("regen")
//                                .then(Commands.argument("set", BoolArgumentType.bool())
//                                        .executes(ctx -> {
//                                            boolean set = BoolArgumentType.getBool(ctx, "set");
//                                            GameEventBus.setRegen(set);
//                                            ctx.getSource().sendSuccess(
//                                                    () -> Component.literal("Mana regen " + set),
//                                                    true
//                                            );
//                                            return 1;
//                                        })
//                                )
//                        )
//        );
//    }
//}
package net.het.tutorialrpg.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.het.tutorialrpg.common.event.GameEventBus;
import net.het.tutorialrpg.common.util.ManaUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("mana")
                        .then(Commands.literal("add")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                            ManaUtil.addMana(player, amt);
                                            ManaUtil.syncManaToClient(player);
                                            ctx.getSource().sendSuccess(() ->
                                                    Component.literal("Added " + amt + " mana."), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("consume")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                            ManaUtil.consumeMana(player, amt);
                                            ManaUtil.syncManaToClient(player);
                                            ctx.getSource().sendSuccess(() ->
                                                    Component.literal("Consumed " + amt + " mana."), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("setmax")
                                .then(Commands.argument("max", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            int max = IntegerArgumentType.getInteger(ctx, "max");
                                            ManaUtil.setMaxMana(player, max);
                                            ManaUtil.syncManaToClient(player);
                                            ctx.getSource().sendSuccess(() ->
                                                    Component.literal("Max mana set to " + max), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("get")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    int curr = ManaUtil.getMana(player);
                                    int max = ManaUtil.getMaxMana(player);
                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal("Mana: " + curr + "/" + max), false);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("regen")
                                .then(Commands.argument("set", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean set = BoolArgumentType.getBool(ctx, "set");
                                            GameEventBus.setRegen(set);
                                            ctx.getSource().sendSuccess(() ->
                                                    Component.literal("Mana regen " + (set ? "enabled" : "disabled")), true);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}

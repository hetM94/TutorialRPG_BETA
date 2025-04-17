/// / src/main/java/net/het/tutorialrpg/util/ModCommands.java
//package net.het.tutorialrpg.util.command;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.IntegerArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import net.het.tutorialrpg.capability.ModCapability;
//import net.het.tutorialrpg.capability.mana.IMana;
//import net.het.tutorialrpg.network.mana.PacketSyncMana;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.neoforge.network.PacketDistributor;
//
//public class ModCommands {
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(
//                Commands.literal("mana")
//                        .then(Commands.literal("get").executes(ctx -> {
//                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
//                            return withCap(sp, ctx, cap -> {
//                                ctx.getSource().sendSuccess(() ->
//                                        Component.literal("Mana: " + cap.getMana() + "/" + cap.getMaxMana()), false);
//                            });
//                        }))
//                        .then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(ctx -> {
//                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
//                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
//                            return withCap(sp, ctx, cap -> {
//                                cap.addMana(amt);
//                                syncMana(sp);
//
//                                ctx.getSource().sendSuccess(() -> Component.literal("Added " + amt + " mana."), true);
//                            });
//                        })))
//                        .then(Commands.literal("consume").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(ctx -> {
//                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
//                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
//                            return withCap(sp, ctx, cap -> {
//                                cap.consumeMana(amt);
//                                syncMana(sp);
//                                ctx.getSource().sendSuccess(() -> Component.literal("Consumed " + amt + " mana."), true);
//                            });
//                        })))
//                        .then(Commands.literal("setmax").then(Commands.argument("max", IntegerArgumentType.integer(0)).executes(ctx -> {
//                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
//                            int max = IntegerArgumentType.getInteger(ctx, "max");
//                            return withCap(sp, ctx, cap -> {
//                                cap.setMaxMana(max);
//                                syncMana(sp);
//                                ctx.getSource().sendSuccess(() -> Component.literal("Max mana set to " + max), true);
//                            });
//                        })))
//        );
//    }
//
//    private static int withCap(ServerPlayer player, CommandContext<CommandSourceStack> ctx, java.util.function.Consumer<IMana> fn) {
//        IMana cap = ModCapability.MANA.getCapability(player, null);
//        if (cap != null) {
//            fn.accept(cap);
//            return 1;
//        } else {
//            ctx.getSource().sendFailure(Component.literal("Mana capability missing."));
//            return 0;
//        }
//    }
//
//    private static void syncMana(ServerPlayer player) {
//        IMana cap = ModCapability.MANA.getCapability(player, null);
//        if (cap != null) {
//            PacketSyncMana packet = new PacketSyncMana(player.getUUID(), cap.getMana(), cap.getMaxMana());
//            PacketDistributor.sendToPlayer(player, packet);
//        }
//    }
//}
package net.het.tutorialrpg.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.het.tutorialrpg.event.GameEventBus;
import net.het.tutorialrpg.util.ModUtil;
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
                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                            ModUtil.addMana(ctx.getSource().getPlayer(), amt);
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Added " + amt + " mana."),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("consume")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                            ModUtil.consumeMana(ctx.getSource().getPlayer(), amt);
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Consumed " + amt + " mana."),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("setmax")
                                .then(Commands.argument("max", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            int max = IntegerArgumentType.getInteger(ctx, "max");
                                            ModUtil.setMaxMana(ctx.getSource().getPlayer(), max);
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Max mana set to " + max),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("get")
                                .executes(ctx -> {
                                    ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                    int curr = ModUtil.getMana(ctx.getSource().getPlayer());
                                    int max = ModUtil.getMaxMana(ctx.getSource().getPlayer());
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Mana: " + curr + "/" + max),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .then(Commands.literal("regen")
                                .then(Commands.argument("set", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean set = BoolArgumentType.getBool(ctx, "set");
                                            GameEventBus.setRegen(set);
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Mana regen " + set),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
        );
    }
}

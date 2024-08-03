package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ManaCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("mana").requires(p -> p.hasPermission(2))).then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(context -> changeMana((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), true)))))).then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(context -> changeMana((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), false)))))).then(Commands.literal("get").then(Commands.argument("targets", EntityArgument.player()).executes(context -> getMana((CommandSourceStack) context.getSource(), EntityArgument.getPlayer(context, "targets"))))));
    }

    private static int changeMana(CommandSourceStack source, Collection<ServerPlayer> targets, int amount, boolean set) {
        targets.forEach(serverPlayer -> {
            MagicData pmg = MagicData.getPlayerMagicData(serverPlayer);
            float base = set ? 0.0F : pmg.getMana();
            pmg.setMana((float) amount + base);
            Messages.sendToPlayer(new ClientboundSyncMana(pmg), serverPlayer);
        });
        String s = set ? "set" : "add";
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.mana." + s + ".success.single", amount, ((ServerPlayer) targets.iterator().next()).m_5446_()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.mana." + s + ".success.multiple", amount, targets.size()), true);
        }
        return targets.size();
    }

    private static int getMana(CommandSourceStack source, ServerPlayer serverPlayer) {
        MagicData pmg = MagicData.getPlayerMagicData(serverPlayer);
        int mana = (int) pmg.getMana();
        source.sendSuccess(() -> Component.translatable("commands.mana.get.success", serverPlayer.m_5446_(), mana), true);
        return mana;
    }
}
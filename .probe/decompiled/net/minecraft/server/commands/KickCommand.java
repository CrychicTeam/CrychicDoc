package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class KickCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("kick").requires(p_137800_ -> p_137800_.hasPermission(3))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(p_137806_ -> kickPlayers((CommandSourceStack) p_137806_.getSource(), EntityArgument.getPlayers(p_137806_, "targets"), Component.translatable("multiplayer.disconnect.kicked")))).then(Commands.argument("reason", MessageArgument.message()).executes(p_137798_ -> kickPlayers((CommandSourceStack) p_137798_.getSource(), EntityArgument.getPlayers(p_137798_, "targets"), MessageArgument.getMessage(p_137798_, "reason"))))));
    }

    private static int kickPlayers(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, Component component2) {
        for (ServerPlayer $$3 : collectionServerPlayer1) {
            $$3.connection.disconnect(component2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.kick.success", $$3.m_5446_(), component2), true);
        }
        return collectionServerPlayer1.size();
    }
}
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;

public class GameModeCommand {

    public static final int PERMISSION_LEVEL = 2;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("gamemode").requires(p_137736_ -> p_137736_.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("gamemode", GameModeArgument.gameMode()).executes(p_258228_ -> setMode(p_258228_, Collections.singleton(((CommandSourceStack) p_258228_.getSource()).getPlayerOrException()), GameModeArgument.getGameMode(p_258228_, "gamemode")))).then(Commands.argument("target", EntityArgument.players()).executes(p_258229_ -> setMode(p_258229_, EntityArgument.getPlayers(p_258229_, "target"), GameModeArgument.getGameMode(p_258229_, "gamemode"))))));
    }

    private static void logGamemodeChange(CommandSourceStack commandSourceStack0, ServerPlayer serverPlayer1, GameType gameType2) {
        Component $$3 = Component.translatable("gameMode." + gameType2.getName());
        if (commandSourceStack0.getEntity() == serverPlayer1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.gamemode.success.self", $$3), true);
        } else {
            if (commandSourceStack0.getLevel().m_46469_().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
                serverPlayer1.sendSystemMessage(Component.translatable("gameMode.changed", $$3));
            }
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.gamemode.success.other", serverPlayer1.m_5446_(), $$3), true);
        }
    }

    private static int setMode(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, GameType gameType2) {
        int $$3 = 0;
        for (ServerPlayer $$4 : collectionServerPlayer1) {
            if ($$4.setGameMode(gameType2)) {
                logGamemodeChange((CommandSourceStack) commandContextCommandSourceStack0.getSource(), $$4, gameType2);
                $$3++;
            }
        }
        return $$3;
    }
}
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;

public class SpectateCommand {

    private static final SimpleCommandExceptionType ERROR_SELF = new SimpleCommandExceptionType(Component.translatable("commands.spectate.self"));

    private static final DynamicCommandExceptionType ERROR_NOT_SPECTATOR = new DynamicCommandExceptionType(p_138688_ -> Component.translatable("commands.spectate.not_spectator", p_138688_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("spectate").requires(p_138682_ -> p_138682_.hasPermission(2))).executes(p_138692_ -> spectate((CommandSourceStack) p_138692_.getSource(), null, ((CommandSourceStack) p_138692_.getSource()).getPlayerOrException()))).then(((RequiredArgumentBuilder) Commands.argument("target", EntityArgument.entity()).executes(p_138690_ -> spectate((CommandSourceStack) p_138690_.getSource(), EntityArgument.getEntity(p_138690_, "target"), ((CommandSourceStack) p_138690_.getSource()).getPlayerOrException()))).then(Commands.argument("player", EntityArgument.player()).executes(p_138680_ -> spectate((CommandSourceStack) p_138680_.getSource(), EntityArgument.getEntity(p_138680_, "target"), EntityArgument.getPlayer(p_138680_, "player"))))));
    }

    private static int spectate(CommandSourceStack commandSourceStack0, @Nullable Entity entity1, ServerPlayer serverPlayer2) throws CommandSyntaxException {
        if (serverPlayer2 == entity1) {
            throw ERROR_SELF.create();
        } else if (serverPlayer2.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
            throw ERROR_NOT_SPECTATOR.create(serverPlayer2.m_5446_());
        } else {
            serverPlayer2.setCamera(entity1);
            if (entity1 != null) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.spectate.success.started", entity1.getDisplayName()), false);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.spectate.success.stopped"), false);
            }
            return 1;
        }
    }
}
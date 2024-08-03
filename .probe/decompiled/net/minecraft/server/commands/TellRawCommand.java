package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;

public class TellRawCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("tellraw").requires(p_139068_ -> p_139068_.hasPermission(2))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("message", ComponentArgument.textComponent()).executes(p_139066_ -> {
            int $$1 = 0;
            for (ServerPlayer $$2 : EntityArgument.getPlayers(p_139066_, "targets")) {
                $$2.sendSystemMessage(ComponentUtils.updateForEntity((CommandSourceStack) p_139066_.getSource(), ComponentArgument.getComponent(p_139066_, "message"), $$2, 0), false);
                $$1++;
            }
            return $$1;
        }))));
    }
}
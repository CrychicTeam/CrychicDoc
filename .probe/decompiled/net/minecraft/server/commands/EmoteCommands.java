package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.players.PlayerList;

public class EmoteCommands {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("me").then(Commands.argument("action", MessageArgument.message()).executes(p_248130_ -> {
            MessageArgument.resolveChatMessage(p_248130_, "action", p_248129_ -> {
                CommandSourceStack $$2 = (CommandSourceStack) p_248130_.getSource();
                PlayerList $$3 = $$2.getServer().getPlayerList();
                $$3.broadcastChatMessage(p_248129_, $$2, ChatType.bind(ChatType.EMOTE_COMMAND, $$2));
            });
            return 1;
        })));
    }
}
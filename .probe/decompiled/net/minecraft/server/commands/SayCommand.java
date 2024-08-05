package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.players.PlayerList;

public class SayCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("say").requires(p_138414_ -> p_138414_.hasPermission(2))).then(Commands.argument("message", MessageArgument.message()).executes(p_248171_ -> {
            MessageArgument.resolveChatMessage(p_248171_, "message", p_248170_ -> {
                CommandSourceStack $$2 = (CommandSourceStack) p_248171_.getSource();
                PlayerList $$3 = $$2.getServer().getPlayerList();
                $$3.broadcastChatMessage(p_248170_, $$2, ChatType.bind(ChatType.SAY_COMMAND, $$2));
            });
            return 1;
        })));
    }
}
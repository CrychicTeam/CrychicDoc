package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public class MsgCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        LiteralCommandNode<CommandSourceStack> $$1 = commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("msg").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("message", MessageArgument.message()).executes(p_248155_ -> {
            Collection<ServerPlayer> $$1x = EntityArgument.getPlayers(p_248155_, "targets");
            if (!$$1x.isEmpty()) {
                MessageArgument.resolveChatMessage(p_248155_, "message", p_248154_ -> sendMessage((CommandSourceStack) p_248155_.getSource(), $$1x, p_248154_));
            }
            return $$1x.size();
        }))));
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("tell").redirect($$1));
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("w").redirect($$1));
    }

    private static void sendMessage(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, PlayerChatMessage playerChatMessage2) {
        ChatType.Bound $$3 = ChatType.bind(ChatType.MSG_COMMAND_INCOMING, commandSourceStack0);
        OutgoingChatMessage $$4 = OutgoingChatMessage.create(playerChatMessage2);
        boolean $$5 = false;
        for (ServerPlayer $$6 : collectionServerPlayer1) {
            ChatType.Bound $$7 = ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, commandSourceStack0).withTargetName($$6.m_5446_());
            commandSourceStack0.sendChatMessage($$4, false, $$7);
            boolean $$8 = commandSourceStack0.shouldFilterMessageTo($$6);
            $$6.sendChatMessage($$4, $$8, $$3);
            $$5 |= $$8 && playerChatMessage2.isFullyFiltered();
        }
        if ($$5) {
            commandSourceStack0.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL);
        }
    }
}
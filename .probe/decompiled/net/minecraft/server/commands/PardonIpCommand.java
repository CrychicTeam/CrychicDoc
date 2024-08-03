package net.minecraft.server.commands;

import com.google.common.net.InetAddresses;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.IpBanList;

public class PardonIpCommand {

    private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(Component.translatable("commands.pardonip.invalid"));

    private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.pardonip.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("pardon-ip").requires(p_138116_ -> p_138116_.hasPermission(3))).then(Commands.argument("target", StringArgumentType.word()).suggests((p_138113_, p_138114_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_138113_.getSource()).getServer().getPlayerList().getIpBans().m_5875_(), p_138114_)).executes(p_138111_ -> unban((CommandSourceStack) p_138111_.getSource(), StringArgumentType.getString(p_138111_, "target")))));
    }

    private static int unban(CommandSourceStack commandSourceStack0, String string1) throws CommandSyntaxException {
        if (!InetAddresses.isInetAddress(string1)) {
            throw ERROR_INVALID.create();
        } else {
            IpBanList $$2 = commandSourceStack0.getServer().getPlayerList().getIpBans();
            if (!$$2.isBanned(string1)) {
                throw ERROR_NOT_BANNED.create();
            } else {
                $$2.m_11393_(string1);
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.pardonip.success", string1), true);
                return 1;
            }
        }
    }
}
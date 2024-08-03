package net.minecraft.server.commands;

import com.google.common.net.InetAddresses;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.IpBanList;
import net.minecraft.server.players.IpBanListEntry;

public class BanIpCommands {

    private static final SimpleCommandExceptionType ERROR_INVALID_IP = new SimpleCommandExceptionType(Component.translatable("commands.banip.invalid"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.banip.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("ban-ip").requires(p_136532_ -> p_136532_.hasPermission(3))).then(((RequiredArgumentBuilder) Commands.argument("target", StringArgumentType.word()).executes(p_136538_ -> banIpOrName((CommandSourceStack) p_136538_.getSource(), StringArgumentType.getString(p_136538_, "target"), null))).then(Commands.argument("reason", MessageArgument.message()).executes(p_136530_ -> banIpOrName((CommandSourceStack) p_136530_.getSource(), StringArgumentType.getString(p_136530_, "target"), MessageArgument.getMessage(p_136530_, "reason"))))));
    }

    private static int banIpOrName(CommandSourceStack commandSourceStack0, String string1, @Nullable Component component2) throws CommandSyntaxException {
        if (InetAddresses.isInetAddress(string1)) {
            return banIp(commandSourceStack0, string1, component2);
        } else {
            ServerPlayer $$3 = commandSourceStack0.getServer().getPlayerList().getPlayerByName(string1);
            if ($$3 != null) {
                return banIp(commandSourceStack0, $$3.getIpAddress(), component2);
            } else {
                throw ERROR_INVALID_IP.create();
            }
        }
    }

    private static int banIp(CommandSourceStack commandSourceStack0, String string1, @Nullable Component component2) throws CommandSyntaxException {
        IpBanList $$3 = commandSourceStack0.getServer().getPlayerList().getIpBans();
        if ($$3.isBanned(string1)) {
            throw ERROR_ALREADY_BANNED.create();
        } else {
            List<ServerPlayer> $$4 = commandSourceStack0.getServer().getPlayerList().getPlayersWithAddress(string1);
            IpBanListEntry $$5 = new IpBanListEntry(string1, null, commandSourceStack0.getTextName(), null, component2 == null ? null : component2.getString());
            $$3.m_11381_($$5);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.banip.success", string1, $$5.m_10962_()), true);
            if (!$$4.isEmpty()) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.banip.info", $$4.size(), EntitySelector.joinNames($$4)), true);
            }
            for (ServerPlayer $$6 : $$4) {
                $$6.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned"));
            }
            return $$4.size();
        }
    }
}
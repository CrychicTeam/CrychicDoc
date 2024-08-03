package net.minecraft.server.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;

public class WhitelistCommand {

    private static final SimpleCommandExceptionType ERROR_ALREADY_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.alreadyOn"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.alreadyOff"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_WHITELISTED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.add.failed"));

    private static final SimpleCommandExceptionType ERROR_NOT_WHITELISTED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.remove.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("whitelist").requires(p_139234_ -> p_139234_.hasPermission(3))).then(Commands.literal("on").executes(p_139236_ -> enableWhitelist((CommandSourceStack) p_139236_.getSource())))).then(Commands.literal("off").executes(p_139232_ -> disableWhitelist((CommandSourceStack) p_139232_.getSource())))).then(Commands.literal("list").executes(p_139228_ -> showList((CommandSourceStack) p_139228_.getSource())))).then(Commands.literal("add").then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((p_139216_, p_139217_) -> {
            PlayerList $$2 = ((CommandSourceStack) p_139216_.getSource()).getServer().getPlayerList();
            return SharedSuggestionProvider.suggest($$2.getPlayers().stream().filter(p_289303_ -> !$$2.getWhiteList().isWhiteListed(p_289303_.m_36316_())).map(p_289304_ -> p_289304_.m_36316_().getName()), p_139217_);
        }).executes(p_139224_ -> addPlayers((CommandSourceStack) p_139224_.getSource(), GameProfileArgument.getGameProfiles(p_139224_, "targets")))))).then(Commands.literal("remove").then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((p_139206_, p_139207_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_139206_.getSource()).getServer().getPlayerList().getWhiteListNames(), p_139207_)).executes(p_139214_ -> removePlayers((CommandSourceStack) p_139214_.getSource(), GameProfileArgument.getGameProfiles(p_139214_, "targets")))))).then(Commands.literal("reload").executes(p_139204_ -> reload((CommandSourceStack) p_139204_.getSource()))));
    }

    private static int reload(CommandSourceStack commandSourceStack0) {
        commandSourceStack0.getServer().getPlayerList().reloadWhiteList();
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.reloaded"), true);
        commandSourceStack0.getServer().kickUnlistedPlayers(commandSourceStack0);
        return 1;
    }

    private static int addPlayers(CommandSourceStack commandSourceStack0, Collection<GameProfile> collectionGameProfile1) throws CommandSyntaxException {
        UserWhiteList $$2 = commandSourceStack0.getServer().getPlayerList().getWhiteList();
        int $$3 = 0;
        for (GameProfile $$4 : collectionGameProfile1) {
            if (!$$2.isWhiteListed($$4)) {
                UserWhiteListEntry $$5 = new UserWhiteListEntry($$4);
                $$2.m_11381_($$5);
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.add.success", ComponentUtils.getDisplayName($$4)), true);
                $$3++;
            }
        }
        if ($$3 == 0) {
            throw ERROR_ALREADY_WHITELISTED.create();
        } else {
            return $$3;
        }
    }

    private static int removePlayers(CommandSourceStack commandSourceStack0, Collection<GameProfile> collectionGameProfile1) throws CommandSyntaxException {
        UserWhiteList $$2 = commandSourceStack0.getServer().getPlayerList().getWhiteList();
        int $$3 = 0;
        for (GameProfile $$4 : collectionGameProfile1) {
            if ($$2.isWhiteListed($$4)) {
                UserWhiteListEntry $$5 = new UserWhiteListEntry($$4);
                $$2.m_11386_($$5);
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.remove.success", ComponentUtils.getDisplayName($$4)), true);
                $$3++;
            }
        }
        if ($$3 == 0) {
            throw ERROR_NOT_WHITELISTED.create();
        } else {
            commandSourceStack0.getServer().kickUnlistedPlayers(commandSourceStack0);
            return $$3;
        }
    }

    private static int enableWhitelist(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        PlayerList $$1 = commandSourceStack0.getServer().getPlayerList();
        if ($$1.isUsingWhitelist()) {
            throw ERROR_ALREADY_ENABLED.create();
        } else {
            $$1.setUsingWhiteList(true);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.enabled"), true);
            commandSourceStack0.getServer().kickUnlistedPlayers(commandSourceStack0);
            return 1;
        }
    }

    private static int disableWhitelist(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        PlayerList $$1 = commandSourceStack0.getServer().getPlayerList();
        if (!$$1.isUsingWhitelist()) {
            throw ERROR_ALREADY_DISABLED.create();
        } else {
            $$1.setUsingWhiteList(false);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.disabled"), true);
            return 1;
        }
    }

    private static int showList(CommandSourceStack commandSourceStack0) {
        String[] $$1 = commandSourceStack0.getServer().getPlayerList().getWhiteListNames();
        if ($$1.length == 0) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.none"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.whitelist.list", $$1.length, String.join(", ", $$1)), false);
        }
        return $$1.length;
    }
}
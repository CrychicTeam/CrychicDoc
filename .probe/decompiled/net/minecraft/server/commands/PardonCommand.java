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
import net.minecraft.server.players.UserBanList;

public class PardonCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.pardon.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("pardon").requires(p_138101_ -> p_138101_.hasPermission(3))).then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((p_138098_, p_138099_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_138098_.getSource()).getServer().getPlayerList().getBans().getUserList(), p_138099_)).executes(p_138096_ -> pardonPlayers((CommandSourceStack) p_138096_.getSource(), GameProfileArgument.getGameProfiles(p_138096_, "targets")))));
    }

    private static int pardonPlayers(CommandSourceStack commandSourceStack0, Collection<GameProfile> collectionGameProfile1) throws CommandSyntaxException {
        UserBanList $$2 = commandSourceStack0.getServer().getPlayerList().getBans();
        int $$3 = 0;
        for (GameProfile $$4 : collectionGameProfile1) {
            if ($$2.isBanned($$4)) {
                $$2.m_11393_($$4);
                $$3++;
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.pardon.success", ComponentUtils.getDisplayName($$4)), true);
            }
        }
        if ($$3 == 0) {
            throw ERROR_NOT_BANNED.create();
        } else {
            return $$3;
        }
    }
}
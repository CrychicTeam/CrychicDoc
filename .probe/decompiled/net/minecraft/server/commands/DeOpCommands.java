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
import net.minecraft.server.players.PlayerList;

public class DeOpCommands {

    private static final SimpleCommandExceptionType ERROR_NOT_OP = new SimpleCommandExceptionType(Component.translatable("commands.deop.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("deop").requires(p_136896_ -> p_136896_.hasPermission(3))).then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((p_136893_, p_136894_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_136893_.getSource()).getServer().getPlayerList().getOpNames(), p_136894_)).executes(p_136891_ -> deopPlayers((CommandSourceStack) p_136891_.getSource(), GameProfileArgument.getGameProfiles(p_136891_, "targets")))));
    }

    private static int deopPlayers(CommandSourceStack commandSourceStack0, Collection<GameProfile> collectionGameProfile1) throws CommandSyntaxException {
        PlayerList $$2 = commandSourceStack0.getServer().getPlayerList();
        int $$3 = 0;
        for (GameProfile $$4 : collectionGameProfile1) {
            if ($$2.isOp($$4)) {
                $$2.deop($$4);
                $$3++;
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.deop.success", ((GameProfile) collectionGameProfile1.iterator().next()).getName()), true);
            }
        }
        if ($$3 == 0) {
            throw ERROR_NOT_OP.create();
        } else {
            commandSourceStack0.getServer().kickUnlistedPlayers(commandSourceStack0);
            return $$3;
        }
    }
}
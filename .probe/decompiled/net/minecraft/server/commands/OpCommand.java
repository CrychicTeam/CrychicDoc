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

public class OpCommand {

    private static final SimpleCommandExceptionType ERROR_ALREADY_OP = new SimpleCommandExceptionType(Component.translatable("commands.op.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("op").requires(p_138087_ -> p_138087_.hasPermission(3))).then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((p_138084_, p_138085_) -> {
            PlayerList $$2 = ((CommandSourceStack) p_138084_.getSource()).getServer().getPlayerList();
            return SharedSuggestionProvider.suggest($$2.getPlayers().stream().filter(p_289286_ -> !$$2.isOp(p_289286_.m_36316_())).map(p_289284_ -> p_289284_.m_36316_().getName()), p_138085_);
        }).executes(p_138082_ -> opPlayers((CommandSourceStack) p_138082_.getSource(), GameProfileArgument.getGameProfiles(p_138082_, "targets")))));
    }

    private static int opPlayers(CommandSourceStack commandSourceStack0, Collection<GameProfile> collectionGameProfile1) throws CommandSyntaxException {
        PlayerList $$2 = commandSourceStack0.getServer().getPlayerList();
        int $$3 = 0;
        for (GameProfile $$4 : collectionGameProfile1) {
            if (!$$2.isOp($$4)) {
                $$2.op($$4);
                $$3++;
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.op.success", ((GameProfile) collectionGameProfile1.iterator().next()).getName()), true);
            }
        }
        if ($$3 == 0) {
            throw ERROR_ALREADY_OP.create();
        } else {
            return $$3;
        }
    }
}
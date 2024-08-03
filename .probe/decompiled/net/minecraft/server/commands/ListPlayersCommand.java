package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

public class ListPlayersCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("list").executes(p_137830_ -> listPlayers((CommandSourceStack) p_137830_.getSource()))).then(Commands.literal("uuids").executes(p_137823_ -> listPlayersWithUuids((CommandSourceStack) p_137823_.getSource()))));
    }

    private static int listPlayers(CommandSourceStack commandSourceStack0) {
        return format(commandSourceStack0, Player::m_5446_);
    }

    private static int listPlayersWithUuids(CommandSourceStack commandSourceStack0) {
        return format(commandSourceStack0, p_289283_ -> Component.translatable("commands.list.nameAndId", p_289283_.m_7755_(), p_289283_.m_36316_().getId()));
    }

    private static int format(CommandSourceStack commandSourceStack0, Function<ServerPlayer, Component> functionServerPlayerComponent1) {
        PlayerList $$2 = commandSourceStack0.getServer().getPlayerList();
        List<ServerPlayer> $$3 = $$2.getPlayers();
        Component $$4 = ComponentUtils.formatList($$3, functionServerPlayerComponent1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.list.players", $$3.size(), $$2.getMaxPlayers(), $$4), false);
        return $$3.size();
    }
}
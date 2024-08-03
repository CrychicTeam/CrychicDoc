package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerFactionData;

public class CmdFaction {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("faction").requires(source -> source.hasPermission(2))).then(Commands.argument("players", EntityArgument.players()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("faction", IntegerArgumentType.integer(0)).then(Commands.literal("add").then(Commands.argument("points", IntegerArgumentType.integer()).executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Faction faction = (Faction) FactionController.instance.factions.get(IntegerArgumentType.getInteger(context, "faction"));
                if (faction == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown FactionID"));
                } else {
                    int points = IntegerArgumentType.getInteger(context, "points");
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        PlayerFactionData playerfactiondata = data.factionData;
                        playerfactiondata.increasePoints(player, faction.id, points);
                        data.save(true);
                    }
                    return 1;
                }
            }
        })))).then(Commands.literal("set").then(Commands.argument("points", IntegerArgumentType.integer()).executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Faction faction = (Faction) FactionController.instance.factions.get(IntegerArgumentType.getInteger(context, "faction"));
                if (faction == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown FactionID"));
                } else {
                    int points = IntegerArgumentType.getInteger(context, "points");
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        PlayerFactionData playerfactiondata = data.factionData;
                        playerfactiondata.factionData.put(faction.id, points);
                        data.save(true);
                    }
                    return 1;
                }
            }
        })))).then(Commands.literal("reset").executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Faction faction = (Faction) FactionController.instance.factions.get(IntegerArgumentType.getInteger(context, "faction"));
                if (faction == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown FactionID"));
                } else {
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        data.factionData.factionData.put(faction.id, faction.defaultPoints);
                        data.save(true);
                    }
                    return 1;
                }
            }
        }))).then(Commands.literal("drop").executes(context -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
            if (players.isEmpty()) {
                return 1;
            } else {
                Faction faction = (Faction) FactionController.instance.factions.get(IntegerArgumentType.getInteger(context, "faction"));
                if (faction == null) {
                    throw new CommandRuntimeException(Component.literal("Unknown FactionID"));
                } else {
                    for (ServerPlayer player : players) {
                        PlayerData data = PlayerData.get(player);
                        data.factionData.factionData.remove(faction.id);
                        data.save(true);
                    }
                    return 1;
                }
            }
        }))));
    }
}
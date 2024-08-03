package journeymap.common.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.awt.Color;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import journeymap.common.Journeymap;
import journeymap.common.util.PermissionsManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class CreateWaypoint {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> waypoint = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("waypoint").requires(CreateWaypoint::canUseCommand)).then(Commands.literal("create").then(Commands.argument("name", StringArgumentType.string()).then(Commands.argument("dimension", DimensionArgument.dimension()).then(Commands.argument("location", Vec3Argument.vec3()).then(Commands.argument("color", ColorArgument.color()).then(((RequiredArgumentBuilder) Commands.argument("players", EntityArgument.players()).executes(ctx -> createWaypoint((CommandSourceStack) ctx.getSource(), EntityArgument.getPlayers(ctx, "players"), StringArgumentType.getString(ctx, "name"), DimensionArgument.getDimension(ctx, "dimension"), Vec3Argument.getCoordinates(ctx, "location"), ColorArgument.getColor(ctx, "color"), false))).then(Commands.argument("announce", BoolArgumentType.bool()).executes(ctx -> createWaypoint((CommandSourceStack) ctx.getSource(), EntityArgument.getPlayers(ctx, "players"), StringArgumentType.getString(ctx, "name"), DimensionArgument.getDimension(ctx, "dimension"), Vec3Argument.getCoordinates(ctx, "location"), ColorArgument.getColor(ctx, "color"), BoolArgumentType.getBool(ctx, "announce"))))))))))).then(Commands.literal("delete").then(Commands.argument("name", StringArgumentType.string()).then(((RequiredArgumentBuilder) Commands.argument("players", EntityArgument.players()).executes(ctx -> deleteWaypoint(EntityArgument.getPlayers(ctx, "players"), StringArgumentType.getString(ctx, "name"), false))).then(Commands.argument("announce", BoolArgumentType.bool()).executes(ctx -> deleteWaypoint(EntityArgument.getPlayers(ctx, "players"), StringArgumentType.getString(ctx, "name"), BoolArgumentType.getBool(ctx, "announce"))))))));
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("wp").requires(CreateWaypoint::canUseCommand)).redirect(waypoint));
    }

    private static boolean canUseCommand(CommandSourceStack ctx) {
        return ctx.hasPermission(2) || ctx.getEntity() != null && ctx.isPlayer() && PermissionsManager.getInstance().canServerAdmin((ServerPlayer) ctx.getEntity()) || !ctx.getServer().isDedicatedServer();
    }

    private static int deleteWaypoint(Collection<ServerPlayer> players, String name, boolean announce) {
        CreateWaypoint.CommandWaypoint wp = new CreateWaypoint.CommandWaypoint(name);
        sendPacket(players, wp, "delete", announce);
        return 1;
    }

    private static int createWaypoint(CommandSourceStack source, Collection<ServerPlayer> players, String name, ServerLevel dimension, Coordinates location, ChatFormatting textFormatting, boolean announce) {
        CreateWaypoint.CommandWaypoint wp = new CreateWaypoint.CommandWaypoint(name, location.getBlockPos(source), new Color(textFormatting.getColor()), dimension.m_46472_().location().toString());
        sendPacket(players, wp, "create", announce);
        return 1;
    }

    private static void sendPacket(Collection<ServerPlayer> players, CreateWaypoint.CommandWaypoint wp, String action, boolean announce) {
        players.forEach(player -> Journeymap.getInstance().getDispatcher().sendWaypointPacket(player, wp.toString(), announce, action));
    }

    public static class CommandWaypoint implements Serializable {

        public static final Gson GSON = new GsonBuilder().create();

        String id;

        public String name;

        String icon = "journeymap:ui/img/waypoint-icon.png";

        boolean enable = true;

        String type = "Normal";

        public String origin = "command";

        int x;

        int y;

        int z;

        int r;

        int g;

        int b;

        boolean persistent = true;

        List<String> dimensions;

        public CommandWaypoint(String name) {
            this.name = name;
        }

        public CommandWaypoint(String name, BlockPos pos, Color color, String dimensions) {
            this.name = name;
            this.x = pos.m_123341_();
            this.y = pos.m_123342_();
            this.z = pos.m_123343_();
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
            this.id = name + "_" + this.x + "," + this.y + "," + this.z;
            this.dimensions = Collections.singletonList(dimensions);
        }

        public String toString() {
            return GSON.toJson(this);
        }

        public static CreateWaypoint.CommandWaypoint fromString(String json) {
            return (CreateWaypoint.CommandWaypoint) GSON.fromJson(json, CreateWaypoint.CommandWaypoint.class);
        }
    }
}
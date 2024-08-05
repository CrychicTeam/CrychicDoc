package net.minecraft.server.commands;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.chase.ChaseClient;
import net.minecraft.server.chase.ChaseServer;
import net.minecraft.world.level.Level;

public class ChaseCommand {

    private static final String DEFAULT_CONNECT_HOST = "localhost";

    private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";

    private static final int DEFAULT_PORT = 10000;

    private static final int BROADCAST_INTERVAL_MS = 100;

    public static BiMap<String, ResourceKey<Level>> DIMENSION_NAMES = ImmutableBiMap.of("o", Level.OVERWORLD, "n", Level.NETHER, "e", Level.END);

    @Nullable
    private static ChaseServer chaseServer;

    @Nullable
    private static ChaseClient chaseClient;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("chase").then(((LiteralArgumentBuilder) Commands.literal("follow").then(((RequiredArgumentBuilder) Commands.argument("host", StringArgumentType.string()).executes(p_196104_ -> follow((CommandSourceStack) p_196104_.getSource(), StringArgumentType.getString(p_196104_, "host"), 10000))).then(Commands.argument("port", IntegerArgumentType.integer(1, 65535)).executes(p_196102_ -> follow((CommandSourceStack) p_196102_.getSource(), StringArgumentType.getString(p_196102_, "host"), IntegerArgumentType.getInteger(p_196102_, "port")))))).executes(p_196100_ -> follow((CommandSourceStack) p_196100_.getSource(), "localhost", 10000)))).then(((LiteralArgumentBuilder) Commands.literal("lead").then(((RequiredArgumentBuilder) Commands.argument("bind_address", StringArgumentType.string()).executes(p_196098_ -> lead((CommandSourceStack) p_196098_.getSource(), StringArgumentType.getString(p_196098_, "bind_address"), 10000))).then(Commands.argument("port", IntegerArgumentType.integer(1024, 65535)).executes(p_196096_ -> lead((CommandSourceStack) p_196096_.getSource(), StringArgumentType.getString(p_196096_, "bind_address"), IntegerArgumentType.getInteger(p_196096_, "port")))))).executes(p_196088_ -> lead((CommandSourceStack) p_196088_.getSource(), "0.0.0.0", 10000)))).then(Commands.literal("stop").executes(p_196080_ -> stop((CommandSourceStack) p_196080_.getSource()))));
    }

    private static int stop(CommandSourceStack commandSourceStack0) {
        if (chaseClient != null) {
            chaseClient.stop();
            commandSourceStack0.sendSuccess(() -> Component.literal("You have now stopped chasing"), false);
            chaseClient = null;
        }
        if (chaseServer != null) {
            chaseServer.stop();
            commandSourceStack0.sendSuccess(() -> Component.literal("You are no longer being chased"), false);
            chaseServer = null;
        }
        return 0;
    }

    private static boolean alreadyRunning(CommandSourceStack commandSourceStack0) {
        if (chaseServer != null) {
            commandSourceStack0.sendFailure(Component.literal("Chase server is already running. Stop it using /chase stop"));
            return true;
        } else if (chaseClient != null) {
            commandSourceStack0.sendFailure(Component.literal("You are already chasing someone. Stop it using /chase stop"));
            return true;
        } else {
            return false;
        }
    }

    private static int lead(CommandSourceStack commandSourceStack0, String string1, int int2) {
        if (alreadyRunning(commandSourceStack0)) {
            return 0;
        } else {
            chaseServer = new ChaseServer(string1, int2, commandSourceStack0.getServer().getPlayerList(), 100);
            try {
                chaseServer.start();
                commandSourceStack0.sendSuccess(() -> Component.literal("Chase server is now running on port " + int2 + ". Clients can follow you using /chase follow <ip> <port>"), false);
            } catch (IOException var4) {
                var4.printStackTrace();
                commandSourceStack0.sendFailure(Component.literal("Failed to start chase server on port " + int2));
                chaseServer = null;
            }
            return 0;
        }
    }

    private static int follow(CommandSourceStack commandSourceStack0, String string1, int int2) {
        if (alreadyRunning(commandSourceStack0)) {
            return 0;
        } else {
            chaseClient = new ChaseClient(string1, int2, commandSourceStack0.getServer());
            chaseClient.start();
            commandSourceStack0.sendSuccess(() -> Component.literal("You are now chasing " + string1 + ":" + int2 + ". If that server does '/chase lead' then you will automatically go to the same position. Use '/chase stop' to stop chasing."), false);
            return 0;
        }
    }
}
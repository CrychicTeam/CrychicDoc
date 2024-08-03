package com.corosus.coroutil.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandCoroConfigClient {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal(getCommandName()).then(Commands.literal("config").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("client").then(CommandCoroConfig.argumentSave())).then(CommandCoroConfig.argumentGet())).then(CommandCoroConfig.argumentSet()))));
    }

    public static String getCommandName() {
        return "coro";
    }
}
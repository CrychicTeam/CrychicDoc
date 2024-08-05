package com.nameless.impactful.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nameless.impactful.network.CameraShake;
import com.nameless.impactful.network.NetWorkManger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class ShakeCameraCommand implements Command<CommandSourceStack> {

    private static final ShakeCameraCommand COMMAND = new ShakeCameraCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("shake").then(Commands.argument("time", IntegerArgumentType.integer(0, 400)).then(Commands.argument("amplitude", FloatArgumentType.floatArg(0.0F, 10.0F)).then(Commands.argument("frequency", FloatArgumentType.floatArg(0.0F, 10.0F)).executes(COMMAND))));
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        int time = IntegerArgumentType.getInteger(context, "time");
        float amplitude = FloatArgumentType.getFloat(context, "amplitude");
        float frequency = FloatArgumentType.getFloat(context, "frequency");
        NetWorkManger.sendToPlayer(new CameraShake(time, amplitude, frequency), player);
        return 1;
    }
}
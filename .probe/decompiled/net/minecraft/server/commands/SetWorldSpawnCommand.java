package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class SetWorldSpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("setworldspawn").requires(p_138665_ -> p_138665_.hasPermission(2))).executes(p_274830_ -> setSpawn((CommandSourceStack) p_274830_.getSource(), BlockPos.containing(((CommandSourceStack) p_274830_.getSource()).getPosition()), 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_138671_ -> setSpawn((CommandSourceStack) p_138671_.getSource(), BlockPosArgument.getSpawnablePos(p_138671_, "pos"), 0.0F))).then(Commands.argument("angle", AngleArgument.angle()).executes(p_138663_ -> setSpawn((CommandSourceStack) p_138663_.getSource(), BlockPosArgument.getSpawnablePos(p_138663_, "pos"), AngleArgument.getAngle(p_138663_, "angle"))))));
    }

    private static int setSpawn(CommandSourceStack commandSourceStack0, BlockPos blockPos1, float float2) {
        commandSourceStack0.getLevel().setDefaultSpawnPos(blockPos1, float2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.setworldspawn.success", blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_(), float2), true);
        return 1;
    }
}
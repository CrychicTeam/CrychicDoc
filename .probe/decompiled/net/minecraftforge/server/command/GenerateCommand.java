package net.minecraftforge.server.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.WorldWorkerManager;

class GenerateCommand {

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("generate").requires(cs -> cs.hasPermission(4))).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("count", IntegerArgumentType.integer(1)).then(((RequiredArgumentBuilder) Commands.argument("dim", DimensionArgument.dimension()).then(Commands.argument("interval", IntegerArgumentType.integer()).executes(ctx -> execute((CommandSourceStack) ctx.getSource(), BlockPosArgument.getSpawnablePos(ctx, "pos"), getInt(ctx, "count"), DimensionArgument.getDimension(ctx, "dim"), getInt(ctx, "interval"))))).executes(ctx -> execute((CommandSourceStack) ctx.getSource(), BlockPosArgument.getSpawnablePos(ctx, "pos"), getInt(ctx, "count"), DimensionArgument.getDimension(ctx, "dim"), -1)))).executes(ctx -> execute((CommandSourceStack) ctx.getSource(), BlockPosArgument.getSpawnablePos(ctx, "pos"), getInt(ctx, "count"), ((CommandSourceStack) ctx.getSource()).getLevel(), -1))));
    }

    private static int getInt(CommandContext<CommandSourceStack> ctx, String name) {
        return IntegerArgumentType.getInteger(ctx, name);
    }

    private static int execute(CommandSourceStack source, BlockPos pos, int count, ServerLevel dim, int interval) throws CommandRuntimeException {
        BlockPos chunkpos = new BlockPos(pos.m_123341_() >> 4, 0, pos.m_123343_() >> 4);
        ChunkGenWorker worker = new ChunkGenWorker(source, chunkpos, count, dim, interval);
        source.sendSuccess(() -> worker.getStartMessage(source), true);
        WorldWorkerManager.addWorker(worker);
        return 0;
    }
}
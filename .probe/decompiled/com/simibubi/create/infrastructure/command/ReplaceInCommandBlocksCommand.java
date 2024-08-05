package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableInt;

public class ReplaceInCommandBlocksCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("replaceInCommandBlocks").requires(cs -> cs.hasPermission(2))).then(Commands.argument("begin", BlockPosArgument.blockPos()).then(Commands.argument("end", BlockPosArgument.blockPos()).then(Commands.argument("toReplace", StringArgumentType.string()).then(Commands.argument("replaceWith", StringArgumentType.string()).executes(ctx -> {
            doReplace((CommandSourceStack) ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "begin"), BlockPosArgument.getLoadedBlockPos(ctx, "end"), StringArgumentType.getString(ctx, "toReplace"), StringArgumentType.getString(ctx, "replaceWith"));
            return 1;
        })))));
    }

    private static void doReplace(CommandSourceStack source, BlockPos from, BlockPos to, String toReplace, String replaceWith) {
        ServerLevel world = source.getLevel();
        MutableInt blocks = new MutableInt(0);
        BlockPos.betweenClosedStream(from, to).forEach(pos -> {
            BlockState blockState = world.m_8055_(pos);
            if (blockState.m_60734_() instanceof CommandBlock) {
                if (world.m_7702_(pos) instanceof CommandBlockEntity cb) {
                    BaseCommandBlock commandBlockLogic = cb.getCommandBlock();
                    String command = commandBlockLogic.getCommand();
                    if (command.indexOf(toReplace) != -1) {
                        blocks.increment();
                    }
                    commandBlockLogic.setCommand(command.replaceAll(toReplace, replaceWith));
                    cb.m_6596_();
                    world.sendBlockUpdated(pos, blockState, blockState, 2);
                }
            }
        });
        int intValue = blocks.intValue();
        if (intValue == 0) {
            source.sendSuccess(() -> Components.literal("Couldn't find \"" + toReplace + "\" anywhere."), true);
        } else {
            source.sendSuccess(() -> Components.literal("Replaced occurrences in " + intValue + " blocks."), true);
        }
    }
}
package com.simibubi.create.infrastructure.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.foundation.utility.Components;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CloneCommand {

    private static final Dynamic2CommandExceptionType CLONE_TOO_BIG_EXCEPTION = new Dynamic2CommandExceptionType((arg1, arg2) -> Components.translatable("commands.clone.toobig", arg1, arg2));

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("clone").requires(cs -> cs.hasPermission(2))).then(Commands.argument("begin", BlockPosArgument.blockPos()).then(Commands.argument("end", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("destination", BlockPosArgument.blockPos()).then(Commands.literal("skipBlocks").executes(ctx -> doClone((CommandSourceStack) ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "begin"), BlockPosArgument.getLoadedBlockPos(ctx, "end"), BlockPosArgument.getLoadedBlockPos(ctx, "destination"), false)))).executes(ctx -> doClone((CommandSourceStack) ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "begin"), BlockPosArgument.getLoadedBlockPos(ctx, "end"), BlockPosArgument.getLoadedBlockPos(ctx, "destination"), true)))))).executes(ctx -> {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Components.literal("Clones all blocks as well as super glue from the specified area to the target destination"), true);
            return 1;
        });
    }

    private static int doClone(CommandSourceStack source, BlockPos begin, BlockPos end, BlockPos destination, boolean cloneBlocks) throws CommandSyntaxException {
        BoundingBox sourceArea = BoundingBox.fromCorners(begin, end);
        BlockPos destinationEnd = destination.offset(sourceArea.getLength());
        BoundingBox destinationArea = BoundingBox.fromCorners(destination, destinationEnd);
        int i = sourceArea.getXSpan() * sourceArea.getYSpan() * sourceArea.getZSpan();
        if (i > 32768) {
            throw CLONE_TOO_BIG_EXCEPTION.create(32768, i);
        } else {
            ServerLevel world = source.getLevel();
            if (world.m_46832_(begin, end) && world.m_46832_(destination, destinationEnd)) {
                BlockPos diffToTarget = new BlockPos(destinationArea.minX() - sourceArea.minX(), destinationArea.minY() - sourceArea.minY(), destinationArea.minZ() - sourceArea.minZ());
                int blockPastes = cloneBlocks ? cloneBlocks(sourceArea, world, diffToTarget) : 0;
                int gluePastes = cloneGlue(sourceArea, world, diffToTarget);
                if (cloneBlocks) {
                    source.sendSuccess(() -> Components.literal("Successfully cloned " + blockPastes + " Blocks"), true);
                }
                source.sendSuccess(() -> Components.literal("Successfully applied glue " + gluePastes + " times"), true);
                return blockPastes + gluePastes;
            } else {
                throw BlockPosArgument.ERROR_NOT_LOADED.create();
            }
        }
    }

    private static int cloneGlue(BoundingBox sourceArea, ServerLevel world, BlockPos diffToTarget) {
        int gluePastes = 0;
        AABB bb = new AABB((double) sourceArea.minX(), (double) sourceArea.minY(), (double) sourceArea.minZ(), (double) (sourceArea.maxX() + 1), (double) (sourceArea.maxY() + 1), (double) (sourceArea.maxZ() + 1));
        for (SuperGlueEntity g : SuperGlueEntity.collectCropped(world, bb)) {
            g.m_146884_(g.m_20182_().add(Vec3.atLowerCornerOf(diffToTarget)));
            world.addFreshEntity(g);
            gluePastes++;
        }
        return gluePastes;
    }

    private static int cloneBlocks(BoundingBox sourceArea, ServerLevel world, BlockPos diffToTarget) {
        int blockPastes = 0;
        List<StructureTemplate.StructureBlockInfo> blocks = Lists.newArrayList();
        List<StructureTemplate.StructureBlockInfo> beBlocks = Lists.newArrayList();
        for (int z = sourceArea.minZ(); z <= sourceArea.maxZ(); z++) {
            for (int y = sourceArea.minY(); y <= sourceArea.maxY(); y++) {
                for (int x = sourceArea.minX(); x <= sourceArea.maxX(); x++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockPos newPos = currentPos.offset(diffToTarget);
                    BlockInWorld cached = new BlockInWorld(world, currentPos, false);
                    BlockState state = cached.getState();
                    BlockEntity be = world.m_7702_(currentPos);
                    if (be != null) {
                        CompoundTag nbt = be.saveWithFullMetadata();
                        beBlocks.add(new StructureTemplate.StructureBlockInfo(newPos, state, nbt));
                    } else {
                        blocks.add(new StructureTemplate.StructureBlockInfo(newPos, state, null));
                    }
                }
            }
        }
        List<StructureTemplate.StructureBlockInfo> allBlocks = Lists.newArrayList();
        allBlocks.addAll(blocks);
        allBlocks.addAll(beBlocks);
        List<StructureTemplate.StructureBlockInfo> reverse = Lists.reverse(allBlocks);
        for (StructureTemplate.StructureBlockInfo info : reverse) {
            BlockEntity be = world.m_7702_(info.pos());
            Clearable.tryClear(be);
            world.m_7731_(info.pos(), Blocks.BARRIER.defaultBlockState(), 2);
        }
        for (StructureTemplate.StructureBlockInfo info : allBlocks) {
            if (world.m_7731_(info.pos(), info.state(), 2)) {
                blockPastes++;
            }
        }
        for (StructureTemplate.StructureBlockInfo infox : beBlocks) {
            BlockEntity be = world.m_7702_(infox.pos());
            if (be != null && infox.nbt() != null) {
                infox.nbt().putInt("x", infox.pos().m_123341_());
                infox.nbt().putInt("y", infox.pos().m_123342_());
                infox.nbt().putInt("z", infox.pos().m_123343_());
                be.load(infox.nbt());
                be.setChanged();
            }
            world.m_7731_(infox.pos(), infox.state(), 2);
        }
        for (StructureTemplate.StructureBlockInfo infox : reverse) {
            world.blockUpdated(infox.pos(), infox.state().m_60734_());
        }
        world.getBlockTicks().copyArea(sourceArea, diffToTarget);
        return blockPastes;
    }
}
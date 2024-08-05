package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class FillCommand {

    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((p_137392_, p_137393_) -> Component.translatable("commands.fill.toobig", p_137392_, p_137393_));

    static final BlockInput HOLLOW_CORE = new BlockInput(Blocks.AIR.defaultBlockState(), Collections.emptySet(), null);

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.fill.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("fill").requires(p_137384_ -> p_137384_.hasPermission(2))).then(Commands.argument("from", BlockPosArgument.blockPos()).then(Commands.argument("to", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("block", BlockStateArgument.block(commandBuildContext1)).executes(p_137405_ -> fillBlocks((CommandSourceStack) p_137405_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137405_, "from"), BlockPosArgument.getLoadedBlockPos(p_137405_, "to")), BlockStateArgument.getBlock(p_137405_, "block"), FillCommand.Mode.REPLACE, null))).then(((LiteralArgumentBuilder) Commands.literal("replace").executes(p_137403_ -> fillBlocks((CommandSourceStack) p_137403_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137403_, "from"), BlockPosArgument.getLoadedBlockPos(p_137403_, "to")), BlockStateArgument.getBlock(p_137403_, "block"), FillCommand.Mode.REPLACE, null))).then(Commands.argument("filter", BlockPredicateArgument.blockPredicate(commandBuildContext1)).executes(p_137401_ -> fillBlocks((CommandSourceStack) p_137401_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137401_, "from"), BlockPosArgument.getLoadedBlockPos(p_137401_, "to")), BlockStateArgument.getBlock(p_137401_, "block"), FillCommand.Mode.REPLACE, BlockPredicateArgument.getBlockPredicate(p_137401_, "filter")))))).then(Commands.literal("keep").executes(p_137399_ -> fillBlocks((CommandSourceStack) p_137399_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137399_, "from"), BlockPosArgument.getLoadedBlockPos(p_137399_, "to")), BlockStateArgument.getBlock(p_137399_, "block"), FillCommand.Mode.REPLACE, p_180225_ -> p_180225_.getLevel().isEmptyBlock(p_180225_.getPos()))))).then(Commands.literal("outline").executes(p_137397_ -> fillBlocks((CommandSourceStack) p_137397_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137397_, "from"), BlockPosArgument.getLoadedBlockPos(p_137397_, "to")), BlockStateArgument.getBlock(p_137397_, "block"), FillCommand.Mode.OUTLINE, null)))).then(Commands.literal("hollow").executes(p_137395_ -> fillBlocks((CommandSourceStack) p_137395_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137395_, "from"), BlockPosArgument.getLoadedBlockPos(p_137395_, "to")), BlockStateArgument.getBlock(p_137395_, "block"), FillCommand.Mode.HOLLOW, null)))).then(Commands.literal("destroy").executes(p_137382_ -> fillBlocks((CommandSourceStack) p_137382_.getSource(), BoundingBox.fromCorners(BlockPosArgument.getLoadedBlockPos(p_137382_, "from"), BlockPosArgument.getLoadedBlockPos(p_137382_, "to")), BlockStateArgument.getBlock(p_137382_, "block"), FillCommand.Mode.DESTROY, null)))))));
    }

    private static int fillBlocks(CommandSourceStack commandSourceStack0, BoundingBox boundingBox1, BlockInput blockInput2, FillCommand.Mode fillCommandMode3, @Nullable Predicate<BlockInWorld> predicateBlockInWorld4) throws CommandSyntaxException {
        int $$5 = boundingBox1.getXSpan() * boundingBox1.getYSpan() * boundingBox1.getZSpan();
        int $$6 = commandSourceStack0.getLevel().m_46469_().getInt(GameRules.RULE_COMMAND_MODIFICATION_BLOCK_LIMIT);
        if ($$5 > $$6) {
            throw ERROR_AREA_TOO_LARGE.create($$6, $$5);
        } else {
            List<BlockPos> $$7 = Lists.newArrayList();
            ServerLevel $$8 = commandSourceStack0.getLevel();
            int $$9 = 0;
            for (BlockPos $$10 : BlockPos.betweenClosed(boundingBox1.minX(), boundingBox1.minY(), boundingBox1.minZ(), boundingBox1.maxX(), boundingBox1.maxY(), boundingBox1.maxZ())) {
                if (predicateBlockInWorld4 == null || predicateBlockInWorld4.test(new BlockInWorld($$8, $$10, true))) {
                    BlockInput $$11 = fillCommandMode3.filter.filter(boundingBox1, $$10, blockInput2, $$8);
                    if ($$11 != null) {
                        BlockEntity $$12 = $$8.m_7702_($$10);
                        Clearable.tryClear($$12);
                        if ($$11.place($$8, $$10, 2)) {
                            $$7.add($$10.immutable());
                            $$9++;
                        }
                    }
                }
            }
            for (BlockPos $$13 : $$7) {
                Block $$14 = $$8.m_8055_($$13).m_60734_();
                $$8.blockUpdated($$13, $$14);
            }
            if ($$9 == 0) {
                throw ERROR_FAILED.create();
            } else {
                int $$15 = $$9;
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.fill.success", $$15), true);
                return $$9;
            }
        }
    }

    static enum Mode {

        REPLACE((p_137433_, p_137434_, p_137435_, p_137436_) -> p_137435_), OUTLINE((p_137428_, p_137429_, p_137430_, p_137431_) -> p_137429_.m_123341_() != p_137428_.minX() && p_137429_.m_123341_() != p_137428_.maxX() && p_137429_.m_123342_() != p_137428_.minY() && p_137429_.m_123342_() != p_137428_.maxY() && p_137429_.m_123343_() != p_137428_.minZ() && p_137429_.m_123343_() != p_137428_.maxZ() ? null : p_137430_), HOLLOW((p_137423_, p_137424_, p_137425_, p_137426_) -> p_137424_.m_123341_() != p_137423_.minX() && p_137424_.m_123341_() != p_137423_.maxX() && p_137424_.m_123342_() != p_137423_.minY() && p_137424_.m_123342_() != p_137423_.maxY() && p_137424_.m_123343_() != p_137423_.minZ() && p_137424_.m_123343_() != p_137423_.maxZ() ? FillCommand.HOLLOW_CORE : p_137425_), DESTROY((p_137418_, p_137419_, p_137420_, p_137421_) -> {
            p_137421_.m_46961_(p_137419_, true);
            return p_137420_;
        });

        public final SetBlockCommand.Filter filter;

        private Mode(SetBlockCommand.Filter p_137416_) {
            this.filter = p_137416_;
        }
    }
}
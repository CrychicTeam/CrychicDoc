package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class CloneCommands {

    private static final SimpleCommandExceptionType ERROR_OVERLAP = new SimpleCommandExceptionType(Component.translatable("commands.clone.overlap"));

    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((p_136743_, p_136744_) -> Component.translatable("commands.clone.toobig", p_136743_, p_136744_));

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.clone.failed"));

    public static final Predicate<BlockInWorld> FILTER_AIR = p_284652_ -> !p_284652_.getState().m_60795_();

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("clone").requires(p_136734_ -> p_136734_.hasPermission(2))).then(beginEndDestinationAndModeSuffix(commandBuildContext1, p_264757_ -> ((CommandSourceStack) p_264757_.getSource()).getLevel()))).then(Commands.literal("from").then(Commands.argument("sourceDimension", DimensionArgument.dimension()).then(beginEndDestinationAndModeSuffix(commandBuildContext1, p_264743_ -> DimensionArgument.getDimension(p_264743_, "sourceDimension"))))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> beginEndDestinationAndModeSuffix(CommandBuildContext commandBuildContext0, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, ServerLevel> cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1) {
        return Commands.argument("begin", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("end", BlockPosArgument.blockPos()).then(destinationAndModeSuffix(commandBuildContext0, cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1, p_264751_ -> ((CommandSourceStack) p_264751_.getSource()).getLevel()))).then(Commands.literal("to").then(Commands.argument("targetDimension", DimensionArgument.dimension()).then(destinationAndModeSuffix(commandBuildContext0, cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1, p_264756_ -> DimensionArgument.getDimension(p_264756_, "targetDimension"))))));
    }

    private static CloneCommands.DimensionAndPosition getLoadedDimensionAndPosition(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ServerLevel serverLevel1, String string2) throws CommandSyntaxException {
        BlockPos $$3 = BlockPosArgument.getLoadedBlockPos(commandContextCommandSourceStack0, serverLevel1, string2);
        return new CloneCommands.DimensionAndPosition(serverLevel1, $$3);
    }

    private static ArgumentBuilder<CommandSourceStack, ?> destinationAndModeSuffix(CommandBuildContext commandBuildContext0, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, ServerLevel> cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, ServerLevel> cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel2) {
        CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> $$3 = p_264737_ -> getLoadedDimensionAndPosition(p_264737_, cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1.apply(p_264737_), "begin");
        CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> $$4 = p_264735_ -> getLoadedDimensionAndPosition(p_264735_, cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel1.apply(p_264735_), "end");
        CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> $$5 = p_264768_ -> getLoadedDimensionAndPosition(p_264768_, cloneCommandsCommandFunctionCommandContextCommandSourceStackServerLevel2.apply(p_264768_), "destination");
        return ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("destination", BlockPosArgument.blockPos()).executes(p_264761_ -> clone((CommandSourceStack) p_264761_.getSource(), $$3.apply(p_264761_), $$4.apply(p_264761_), $$5.apply(p_264761_), p_180033_ -> true, CloneCommands.Mode.NORMAL))).then(wrapWithCloneMode($$3, $$4, $$5, p_264738_ -> p_180041_ -> true, Commands.literal("replace").executes(p_264755_ -> clone((CommandSourceStack) p_264755_.getSource(), $$3.apply(p_264755_), $$4.apply(p_264755_), $$5.apply(p_264755_), p_180039_ -> true, CloneCommands.Mode.NORMAL))))).then(wrapWithCloneMode($$3, $$4, $$5, p_264744_ -> FILTER_AIR, Commands.literal("masked").executes(p_264742_ -> clone((CommandSourceStack) p_264742_.getSource(), $$3.apply(p_264742_), $$4.apply(p_264742_), $$5.apply(p_264742_), FILTER_AIR, CloneCommands.Mode.NORMAL))))).then(Commands.literal("filtered").then(wrapWithCloneMode($$3, $$4, $$5, p_264745_ -> BlockPredicateArgument.getBlockPredicate(p_264745_, "filter"), Commands.argument("filter", BlockPredicateArgument.blockPredicate(commandBuildContext0)).executes(p_264733_ -> clone((CommandSourceStack) p_264733_.getSource(), $$3.apply(p_264733_), $$4.apply(p_264733_), $$5.apply(p_264733_), BlockPredicateArgument.getBlockPredicate(p_264733_, "filter"), CloneCommands.Mode.NORMAL)))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> wrapWithCloneMode(CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition0, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition1, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, CloneCommands.DimensionAndPosition> cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition2, CloneCommands.CommandFunction<CommandContext<CommandSourceStack>, Predicate<BlockInWorld>> cloneCommandsCommandFunctionCommandContextCommandSourceStackPredicateBlockInWorld3, ArgumentBuilder<CommandSourceStack, ?> argumentBuilderCommandSourceStack4) {
        return argumentBuilderCommandSourceStack4.then(Commands.literal("force").executes(p_264773_ -> clone((CommandSourceStack) p_264773_.getSource(), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition0.apply(p_264773_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition1.apply(p_264773_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition2.apply(p_264773_), cloneCommandsCommandFunctionCommandContextCommandSourceStackPredicateBlockInWorld3.apply(p_264773_), CloneCommands.Mode.FORCE))).then(Commands.literal("move").executes(p_264766_ -> clone((CommandSourceStack) p_264766_.getSource(), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition0.apply(p_264766_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition1.apply(p_264766_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition2.apply(p_264766_), cloneCommandsCommandFunctionCommandContextCommandSourceStackPredicateBlockInWorld3.apply(p_264766_), CloneCommands.Mode.MOVE))).then(Commands.literal("normal").executes(p_264750_ -> clone((CommandSourceStack) p_264750_.getSource(), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition0.apply(p_264750_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition1.apply(p_264750_), cloneCommandsCommandFunctionCommandContextCommandSourceStackCloneCommandsDimensionAndPosition2.apply(p_264750_), cloneCommandsCommandFunctionCommandContextCommandSourceStackPredicateBlockInWorld3.apply(p_264750_), CloneCommands.Mode.NORMAL)));
    }

    private static int clone(CommandSourceStack commandSourceStack0, CloneCommands.DimensionAndPosition cloneCommandsDimensionAndPosition1, CloneCommands.DimensionAndPosition cloneCommandsDimensionAndPosition2, CloneCommands.DimensionAndPosition cloneCommandsDimensionAndPosition3, Predicate<BlockInWorld> predicateBlockInWorld4, CloneCommands.Mode cloneCommandsMode5) throws CommandSyntaxException {
        BlockPos $$6 = cloneCommandsDimensionAndPosition1.position();
        BlockPos $$7 = cloneCommandsDimensionAndPosition2.position();
        BoundingBox $$8 = BoundingBox.fromCorners($$6, $$7);
        BlockPos $$9 = cloneCommandsDimensionAndPosition3.position();
        BlockPos $$10 = $$9.offset($$8.getLength());
        BoundingBox $$11 = BoundingBox.fromCorners($$9, $$10);
        ServerLevel $$12 = cloneCommandsDimensionAndPosition1.dimension();
        ServerLevel $$13 = cloneCommandsDimensionAndPosition3.dimension();
        if (!cloneCommandsMode5.canOverlap() && $$12 == $$13 && $$11.intersects($$8)) {
            throw ERROR_OVERLAP.create();
        } else {
            int $$14 = $$8.getXSpan() * $$8.getYSpan() * $$8.getZSpan();
            int $$15 = commandSourceStack0.getLevel().m_46469_().getInt(GameRules.RULE_COMMAND_MODIFICATION_BLOCK_LIMIT);
            if ($$14 > $$15) {
                throw ERROR_AREA_TOO_LARGE.create($$15, $$14);
            } else if ($$12.m_46832_($$6, $$7) && $$13.m_46832_($$9, $$10)) {
                List<CloneCommands.CloneBlockInfo> $$16 = Lists.newArrayList();
                List<CloneCommands.CloneBlockInfo> $$17 = Lists.newArrayList();
                List<CloneCommands.CloneBlockInfo> $$18 = Lists.newArrayList();
                Deque<BlockPos> $$19 = Lists.newLinkedList();
                BlockPos $$20 = new BlockPos($$11.minX() - $$8.minX(), $$11.minY() - $$8.minY(), $$11.minZ() - $$8.minZ());
                for (int $$21 = $$8.minZ(); $$21 <= $$8.maxZ(); $$21++) {
                    for (int $$22 = $$8.minY(); $$22 <= $$8.maxY(); $$22++) {
                        for (int $$23 = $$8.minX(); $$23 <= $$8.maxX(); $$23++) {
                            BlockPos $$24 = new BlockPos($$23, $$22, $$21);
                            BlockPos $$25 = $$24.offset($$20);
                            BlockInWorld $$26 = new BlockInWorld($$12, $$24, false);
                            BlockState $$27 = $$26.getState();
                            if (predicateBlockInWorld4.test($$26)) {
                                BlockEntity $$28 = $$12.m_7702_($$24);
                                if ($$28 != null) {
                                    CompoundTag $$29 = $$28.saveWithoutMetadata();
                                    $$17.add(new CloneCommands.CloneBlockInfo($$25, $$27, $$29));
                                    $$19.addLast($$24);
                                } else if (!$$27.m_60804_($$12, $$24) && !$$27.m_60838_($$12, $$24)) {
                                    $$18.add(new CloneCommands.CloneBlockInfo($$25, $$27, null));
                                    $$19.addFirst($$24);
                                } else {
                                    $$16.add(new CloneCommands.CloneBlockInfo($$25, $$27, null));
                                    $$19.addLast($$24);
                                }
                            }
                        }
                    }
                }
                if (cloneCommandsMode5 == CloneCommands.Mode.MOVE) {
                    for (BlockPos $$30 : $$19) {
                        BlockEntity $$31 = $$12.m_7702_($$30);
                        Clearable.tryClear($$31);
                        $$12.m_7731_($$30, Blocks.BARRIER.defaultBlockState(), 2);
                    }
                    for (BlockPos $$32 : $$19) {
                        $$12.m_7731_($$32, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                List<CloneCommands.CloneBlockInfo> $$33 = Lists.newArrayList();
                $$33.addAll($$16);
                $$33.addAll($$17);
                $$33.addAll($$18);
                List<CloneCommands.CloneBlockInfo> $$34 = Lists.reverse($$33);
                for (CloneCommands.CloneBlockInfo $$35 : $$34) {
                    BlockEntity $$36 = $$13.m_7702_($$35.pos);
                    Clearable.tryClear($$36);
                    $$13.m_7731_($$35.pos, Blocks.BARRIER.defaultBlockState(), 2);
                }
                int $$37 = 0;
                for (CloneCommands.CloneBlockInfo $$38 : $$33) {
                    if ($$13.m_7731_($$38.pos, $$38.state, 2)) {
                        $$37++;
                    }
                }
                for (CloneCommands.CloneBlockInfo $$39 : $$17) {
                    BlockEntity $$40 = $$13.m_7702_($$39.pos);
                    if ($$39.tag != null && $$40 != null) {
                        $$40.load($$39.tag);
                        $$40.setChanged();
                    }
                    $$13.m_7731_($$39.pos, $$39.state, 2);
                }
                for (CloneCommands.CloneBlockInfo $$41 : $$34) {
                    $$13.blockUpdated($$41.pos, $$41.state.m_60734_());
                }
                $$13.getBlockTicks().copyAreaFrom($$12.getBlockTicks(), $$8, $$20);
                if ($$37 == 0) {
                    throw ERROR_FAILED.create();
                } else {
                    int $$42 = $$37;
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.clone.success", $$42), true);
                    return $$37;
                }
            } else {
                throw BlockPosArgument.ERROR_NOT_LOADED.create();
            }
        }
    }

    static class CloneBlockInfo {

        public final BlockPos pos;

        public final BlockState state;

        @Nullable
        public final CompoundTag tag;

        public CloneBlockInfo(BlockPos blockPos0, BlockState blockState1, @Nullable CompoundTag compoundTag2) {
            this.pos = blockPos0;
            this.state = blockState1;
            this.tag = compoundTag2;
        }
    }

    @FunctionalInterface
    interface CommandFunction<T, R> {

        R apply(T var1) throws CommandSyntaxException;
    }

    static record DimensionAndPosition(ServerLevel f_263735_, BlockPos f_263824_) {

        private final ServerLevel dimension;

        private final BlockPos position;

        DimensionAndPosition(ServerLevel f_263735_, BlockPos f_263824_) {
            this.dimension = f_263735_;
            this.position = f_263824_;
        }
    }

    static enum Mode {

        FORCE(true), MOVE(true), NORMAL(false);

        private final boolean canOverlap;

        private Mode(boolean p_136795_) {
            this.canOverlap = p_136795_;
        }

        public boolean canOverlap() {
            return this.canOverlap;
        }
    }
}
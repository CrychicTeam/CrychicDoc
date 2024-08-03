package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fluids.FluidType;

public class DrainBlock extends AbstractGlassBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    private static final int MAXIMUM_BLOCKS_DRAINED = 64;

    public static final int MAX_FLUID_SPREAD = 10;

    private static final Direction[] DRAIN_DIRECTIONS = ACMath.HORIZONTAL_DIRECTIONS;

    private static final Direction[] FIND_WATER_DIRECTIONS = new Direction[] { Direction.UP, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH };

    private static final Direction[] FILL_DIRECTIONS = new Direction[] { Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH };

    private static final int DRAIN_TIME = 20;

    public DrainBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.IGNORE).noOcclusion().requiresCorrectToolForDrops().strength(5.0F, 15.0F).sound(SoundType.METAL));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(OPEN, context.m_43725_().m_276867_(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateState(state, worldIn, pos, blockIn);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        if (!worldIn.f_46443_) {
            this.updateState(state, worldIn, pos, state.m_60734_());
            if ((Boolean) state.m_61143_(OPEN)) {
                this.drainLogic(state, worldIn, pos);
            }
        }
    }

    public void drainLogic(BlockState state, Level worldIn, BlockPos pos) {
        BlockPos above = pos.above();
        if (!worldIn.getFluidState(above).isEmpty() && (worldIn.m_46859_(pos.below()) || !worldIn.getFluidState(pos.below()).isEmpty())) {
            BlockPos.MutableBlockPos highestWaterMutable = new BlockPos.MutableBlockPos();
            highestWaterMutable.set(pos);
            highestWaterMutable.move(0, 1, 0);
            while (!worldIn.getFluidState(highestWaterMutable).isEmpty() && highestWaterMutable.m_123342_() < worldIn.m_151558_()) {
                highestWaterMutable.move(0, 1, 0);
            }
            highestWaterMutable.move(0, -1, 0);
            BlockPos highestWater = this.findHighestWater(worldIn, highestWaterMutable.immutable());
            FluidState copyState = worldIn.getFluidState(highestWater);
            BlockState fluidBlockCopyState = worldIn.getBlockState(highestWater);
            if (!copyState.isEmpty()) {
                int count = this.removeWaterBreadthFirstSearch(worldIn, highestWater);
                if (count > 0) {
                    AlexsCaves.sendMSGToAll(new WorldEventMessage(3, pos.m_123341_(), pos.m_123342_(), pos.m_123343_()));
                }
                BlockPos.MutableBlockPos lowestAir = new BlockPos.MutableBlockPos();
                lowestAir.set(pos);
                lowestAir.move(0, -1, 0);
                while ((!worldIn.getFluidState(lowestAir).isEmpty() || worldIn.m_46859_(lowestAir)) && lowestAir.m_123342_() > worldIn.m_141937_()) {
                    lowestAir.move(0, -1, 0);
                }
                lowestAir.move(0, 1, 0);
                BlockPos lowest = lowestAir.immutable();
                BlockState fullBlock = fluidBlockCopyState.m_60819_().isEmpty() ? Blocks.AIR.defaultBlockState() : fluidBlockCopyState.m_60819_().createLegacyBlock();
                boolean flag = false;
                for (int i = 0; i < count; i++) {
                    List<BlockPos> ignoredPoses = Lists.newArrayList();
                    BlockPos setPos = this.getFirstEmptyNeighborPosition(worldIn, lowest, copyState.getFluidType(), 0, ignoredPoses);
                    if (setPos == null) {
                        lowest = lowest.above();
                        if (lowest.m_123342_() >= pos.m_123342_()) {
                            break;
                        }
                        i--;
                    } else {
                        worldIn.setBlockAndUpdate(setPos, fullBlock);
                        flag = true;
                    }
                }
                if (flag) {
                    AlexsCaves.sendMSGToAll(new WorldEventMessage(4, pos.m_123341_(), pos.m_123342_(), pos.m_123343_()));
                }
            }
            worldIn.m_186460_(pos, this, 20);
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag = (Boolean) state.m_61143_(OPEN);
        boolean flag1 = worldIn.m_276867_(pos);
        if (flag1 != flag) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(OPEN, flag1), 3);
            worldIn.updateNeighborsAt(pos.below(), this);
            worldIn.m_186460_(pos, this, 20);
        }
    }

    private BlockPos findHighestWater(Level level, BlockPos pos) {
        BlockPos highest = pos;
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));
        int i = 0;
        int maxDist = 5;
        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = (Tuple<BlockPos, Integer>) queue.poll();
            BlockPos blockpos = tuple.getA();
            if (blockpos.m_123342_() > highest.m_123342_()) {
                highest = blockpos;
            }
            int j = tuple.getB();
            for (Direction direction : FIND_WATER_DIRECTIONS) {
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos1);
                if (!blockstate.m_60819_().isEmpty() && !(blockstate.m_60734_() instanceof SimpleWaterloggedBlock)) {
                    i++;
                    if (j < maxDist) {
                        queue.add(new Tuple<>(blockpos1, j + 1));
                    }
                }
            }
            if (i > 10024) {
                break;
            }
        }
        return highest;
    }

    private BlockPos getFirstEmptyNeighborPosition(Level level, BlockPos pos, FluidType ourType, int tries, List<BlockPos> ignoredPoses) {
        if (tries < 20 && !ignoredPoses.contains(pos)) {
            ignoredPoses.add(pos);
            if (this.canMergeWith(level, pos)) {
                return pos;
            }
            for (Direction direction : FILL_DIRECTIONS) {
                BlockPos pos1 = pos.relative(direction);
                if (this.canMergeWith(level, pos1)) {
                    return pos1;
                }
                if (level.getFluidState(pos1).getFluidType() == ourType) {
                    BlockPos pos2 = this.getFirstEmptyNeighborPosition(level, pos1, ourType, tries + 1, ignoredPoses);
                    if (pos2 != null) {
                        return pos2;
                    }
                }
                ignoredPoses.add(pos1);
            }
        }
        return null;
    }

    private boolean canMergeWith(Level level, BlockPos pos) {
        return level.getBlockState(pos).m_60795_() && level.getFluidState(pos).isEmpty() || !level.getFluidState(pos).isEmpty() && !level.getFluidState(pos).isSource();
    }

    private int removeWaterBreadthFirstSearch(Level level, BlockPos pos) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));
        int i = 0;
        int fullBlocks = 0;
        FluidState lastFluidState = null;
        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = (Tuple<BlockPos, Integer>) queue.poll();
            BlockPos blockpos = tuple.getA();
            BlockState state = level.getBlockState(blockpos);
            int j = tuple.getB();
            if (!state.m_60819_().isEmpty()) {
                fullBlocks++;
                if (state.m_60734_() instanceof BucketPickup) {
                    ((BucketPickup) state.m_60734_()).pickupBlock(level, blockpos, state);
                } else {
                    level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                }
            }
            for (Direction direction : DRAIN_DIRECTIONS) {
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos1);
                FluidState fluidstate = level.getFluidState(blockpos1);
                if (lastFluidState == null || fluidstate.isEmpty() || lastFluidState.getFluidType() == fluidstate.getFluidType()) {
                    if (blockstate.m_60734_() instanceof SimpleWaterloggedBlock) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        i++;
                        fullBlocks++;
                        level.setBlockAndUpdate(blockpos1, (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, false));
                        if (j < 10) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_60734_() instanceof BucketPickup) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        i++;
                        fullBlocks++;
                        ((BucketPickup) blockstate.m_60734_()).pickupBlock(level, blockpos1, blockstate);
                        if (j < 10) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_60734_() instanceof LiquidBlock) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        level.setBlockAndUpdate(blockpos1, Blocks.AIR.defaultBlockState());
                        i++;
                        if (blockstate.m_60819_().isSource()) {
                            fullBlocks++;
                        }
                        if (j < 10) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_204336_(ACTagRegistry.DRAIN_BREAKS)) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        BlockEntity blockentity = blockstate.m_155947_() ? level.getBlockEntity(blockpos1) : null;
                        m_49892_(blockstate, level, blockpos1, blockentity);
                        if (blockstate.m_60819_().isSource()) {
                            fullBlocks++;
                        }
                        level.setBlockAndUpdate(blockpos1, Blocks.AIR.defaultBlockState());
                        i++;
                        if (j < 10) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }
                }
            }
            if (i > 64) {
                break;
            }
        }
        return fullBlocks;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
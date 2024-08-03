package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.google.common.collect.Lists;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockBananaSlugSlime extends HalfTransparentBlock {

    protected static final VoxelShape SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 15.0, 15.0);

    private static final int MAXIMUM_BLOCKS_DRAINED = 64;

    public static final int MAX_FLUID_SPREAD = 6;

    public BlockBananaSlugSlime() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).speedFactor(0.4F).jumpFactor(0.5F).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion());
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8));
        super.m_7892_(state, level, pos, entity);
    }

    public boolean isSlimeBlock(BlockState state) {
        return true;
    }

    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    public boolean canStickTo(BlockState state, @NotNull BlockState other) {
        return !other.isStickyBlock() || other.m_60734_() == this;
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            this.tryAbsorbWater(level1, blockPos2);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        this.tryAbsorbWater(level1, blockPos2);
        super.m_6861_(blockState0, level1, blockPos2, block3, blockPos4, boolean5);
    }

    protected void tryAbsorbWater(Level level, BlockPos pos) {
        if (this.removeWaterBreadthFirstSearch(level, pos)) {
            level.playSound(null, pos, AMSoundRegistry.BANANA_SLUG_SLIME_EXPAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean removeWaterBreadthFirstSearch(Level level, BlockPos pos) {
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
                    if (level.getBlockState(blockpos).m_60795_()) {
                        level.setBlockAndUpdate(blockpos, AMBlockRegistry.CRYSTALIZED_BANANA_SLUG_MUCUS.get().defaultBlockState());
                    }
                } else {
                    level.setBlockAndUpdate(blockpos, AMBlockRegistry.CRYSTALIZED_BANANA_SLUG_MUCUS.get().defaultBlockState());
                }
            }
            for (Direction direction : Direction.values()) {
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
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_60734_() instanceof BucketPickup) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        i++;
                        fullBlocks++;
                        ((BucketPickup) blockstate.m_60734_()).pickupBlock(level, blockpos1, blockstate);
                        if (level.getBlockState(blockpos).m_60795_()) {
                            level.setBlockAndUpdate(blockpos, AMBlockRegistry.CRYSTALIZED_BANANA_SLUG_MUCUS.get().defaultBlockState());
                        }
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_60734_() instanceof LiquidBlock) {
                        if (!fluidstate.isEmpty()) {
                            lastFluidState = fluidstate;
                        }
                        level.setBlockAndUpdate(blockpos1, AMBlockRegistry.CRYSTALIZED_BANANA_SLUG_MUCUS.get().defaultBlockState());
                        i++;
                        if (blockstate.m_60819_().isSource()) {
                            fullBlocks++;
                        }
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }
                }
            }
            if (i > 64) {
                break;
            }
        }
        return fullBlocks > 0;
    }
}
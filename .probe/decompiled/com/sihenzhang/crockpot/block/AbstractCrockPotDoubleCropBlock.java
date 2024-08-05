package com.sihenzhang.crockpot.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public abstract class AbstractCrockPotDoubleCropBlock extends AbstractCrockPotCropBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Shapes.block(), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Shapes.block() };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.m_7959_())];
    }

    public boolean isUpperBlock(BlockState state) {
        return this.m_52305_(state) > this.m_7419_() / 2;
    }

    public int getMaxGrowthAge(BlockState state) {
        return this.isUpperBlock(state) ? this.m_7419_() : this.m_7419_() / 2;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && this.isUpperBlock(state)) {
            BlockPos lowerPos = pos.below();
            BlockState lowerState = level.getBlockState(lowerPos);
            if (lowerState.m_60713_(this)) {
                level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), 35);
                level.m_5898_(player, 2001, lowerPos, Block.getId(lowerState));
            }
        }
        super.m_5707_(level, pos, state, player);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (level.m_45524_(pos, 0) >= 9) {
                int age = this.m_52305_(state);
                if (age < this.m_7419_()) {
                    BlockPos blockPos = this.isUpperBlock(state) && level.m_8055_(pos.below()).m_60734_() == this ? pos.below() : pos;
                    float growthChance = getGrowthChance(this, level, blockPos);
                    if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                        if (age != this.getMaxGrowthAge(state)) {
                            level.m_7731_(pos, this.m_52289_(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(level, pos, state);
                        } else if (level.m_46859_(pos.above())) {
                            level.m_7731_(pos.above(), this.m_52289_(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(level, pos, state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int age = this.m_52305_(state);
        int maxAge = this.m_7419_();
        if (age < maxAge) {
            int maxGrowthAge = this.getMaxGrowthAge(state);
            if (age != maxGrowthAge) {
                int expectedAge = age + this.m_7125_(level);
                if (expectedAge > maxAge) {
                    expectedAge = maxAge;
                }
                if (expectedAge > maxGrowthAge) {
                    level.setBlock(pos, this.m_52289_(maxGrowthAge), 2);
                    if (level.m_46859_(pos.above())) {
                        level.setBlock(pos.above(), this.m_52289_(expectedAge), 2);
                    }
                } else {
                    level.setBlock(pos, this.m_52289_(expectedAge), 2);
                }
            } else {
                BlockState stateUp = level.getBlockState(pos.above());
                if (stateUp.m_60734_() == this && this.m_52305_(stateUp) != this.getMaxGrowthAge(stateUp)) {
                    int expectedAgex = this.m_52305_(stateUp) + this.m_7125_(level);
                    if (expectedAgex > maxAge) {
                        expectedAgex = maxAge;
                    }
                    level.setBlock(pos.above(), this.m_52289_(expectedAgex), 2);
                } else if (level.m_46859_(pos.above())) {
                    int expectedAgex = age + this.m_7125_(level);
                    if (expectedAgex > maxAge) {
                        expectedAgex = maxAge;
                    }
                    level.setBlock(pos.above(), this.m_52289_(expectedAgex), 2);
                }
            }
        }
    }

    protected static float getGrowthChance(AbstractCrockPotDoubleCropBlock block, BlockGetter level, BlockPos pos) {
        return block.isUpperBlock(level.getBlockState(pos)) ? CropBlock.getGrowthSpeed(block, level, pos.below()) : CropBlock.getGrowthSpeed(block, level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!this.isUpperBlock(state)) {
            return super.m_7898_(state, level, pos);
        } else {
            BlockState stateDown = level.m_8055_(pos.below());
            return (level.m_45524_(pos, 0) >= 8 || level.m_45527_(pos)) && stateDown.m_60734_() == this && this.m_52305_(stateDown) == this.getMaxGrowthAge(stateDown);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        if (this.m_52305_(state) < this.m_7419_()) {
            if (this.m_52305_(state) != this.getMaxGrowthAge(state)) {
                return true;
            } else {
                BlockState stateUp = level.m_8055_(pos.above());
                return stateUp.m_60734_() == this && this.m_52305_(stateUp) != this.getMaxGrowthAge(stateUp) || stateUp.m_60795_();
            }
        } else {
            return false;
        }
    }
}
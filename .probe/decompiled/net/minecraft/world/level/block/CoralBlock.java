package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class CoralBlock extends Block {

    private final Block deadBlock;

    public CoralBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.deadBlock = block0;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!this.scanForWater(serverLevel1, blockPos2)) {
            serverLevel1.m_7731_(blockPos2, this.deadBlock.defaultBlockState(), 2);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!this.scanForWater(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 60 + levelAccessor3.getRandom().nextInt(40));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    protected boolean scanForWater(BlockGetter blockGetter0, BlockPos blockPos1) {
        for (Direction $$2 : Direction.values()) {
            FluidState $$3 = blockGetter0.getFluidState(blockPos1.relative($$2));
            if ($$3.is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        if (!this.scanForWater(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos())) {
            blockPlaceContext0.m_43725_().m_186460_(blockPlaceContext0.getClickedPos(), this, 60 + blockPlaceContext0.m_43725_().getRandom().nextInt(40));
        }
        return this.m_49966_();
    }
}
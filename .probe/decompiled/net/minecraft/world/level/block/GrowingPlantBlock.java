package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class GrowingPlantBlock extends Block {

    protected final Direction growthDirection;

    protected final boolean scheduleFluidTicks;

    protected final VoxelShape shape;

    protected GrowingPlantBlock(BlockBehaviour.Properties blockBehaviourProperties0, Direction direction1, VoxelShape voxelShape2, boolean boolean3) {
        super(blockBehaviourProperties0);
        this.growthDirection = direction1;
        this.shape = voxelShape2;
        this.scheduleFluidTicks = boolean3;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos().relative(this.growthDirection));
        return !$$1.m_60713_(this.getHeadBlock()) && !$$1.m_60713_(this.getBodyBlock()) ? this.getStateForPlacement(blockPlaceContext0.m_43725_()) : this.getBodyBlock().defaultBlockState();
    }

    public BlockState getStateForPlacement(LevelAccessor levelAccessor0) {
        return this.m_49966_();
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.relative(this.growthDirection.getOpposite());
        BlockState $$4 = levelReader1.m_8055_($$3);
        return !this.canAttachTo($$4) ? false : $$4.m_60713_(this.getHeadBlock()) || $$4.m_60713_(this.getBodyBlock()) || $$4.m_60783_(levelReader1, $$3, this.growthDirection);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    protected boolean canAttachTo(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.shape;
    }

    protected abstract GrowingPlantHeadBlock getHeadBlock();

    protected abstract Block getBodyBlock();
}
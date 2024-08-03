package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class GrowingPlantBodyBlock extends GrowingPlantBlock implements BonemealableBlock {

    protected GrowingPlantBodyBlock(BlockBehaviour.Properties blockBehaviourProperties0, Direction direction1, VoxelShape voxelShape2, boolean boolean3) {
        super(blockBehaviourProperties0, direction1, voxelShape2, boolean3);
    }

    protected BlockState updateHeadAfterConvertedFromBody(BlockState blockState0, BlockState blockState1) {
        return blockState1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == this.f_53859_.getOpposite() && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        GrowingPlantHeadBlock $$6 = this.m_7272_();
        if (direction1 == this.f_53859_ && !blockState2.m_60713_(this) && !blockState2.m_60713_($$6)) {
            return this.updateHeadAfterConvertedFromBody(blockState0, $$6.getStateForPlacement(levelAccessor3));
        } else {
            if (this.f_53860_) {
                levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(this.m_7272_());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        Optional<BlockPos> $$4 = this.getHeadPos(levelReader0, blockPos1, blockState2.m_60734_());
        return $$4.isPresent() && this.m_7272_().canGrowInto(levelReader0.m_8055_(((BlockPos) $$4.get()).relative(this.f_53859_)));
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        Optional<BlockPos> $$4 = this.getHeadPos(serverLevel0, blockPos2, blockState3.m_60734_());
        if ($$4.isPresent()) {
            BlockState $$5 = serverLevel0.m_8055_((BlockPos) $$4.get());
            ((GrowingPlantHeadBlock) $$5.m_60734_()).performBonemeal(serverLevel0, randomSource1, (BlockPos) $$4.get(), $$5);
        }
    }

    private Optional<BlockPos> getHeadPos(BlockGetter blockGetter0, BlockPos blockPos1, Block block2) {
        return BlockUtil.getTopConnectedBlock(blockGetter0, blockPos1, block2, this.f_53859_, this.m_7272_());
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        boolean $$2 = super.m_6864_(blockState0, blockPlaceContext1);
        return $$2 && blockPlaceContext1.m_43722_().is(this.m_7272_().m_5456_()) ? false : $$2;
    }

    @Override
    protected Block getBodyBlock() {
        return this;
    }
}
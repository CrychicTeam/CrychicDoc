package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SmartChuteBlock extends AbstractChuteBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SmartChuteBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, true));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isClientSide) {
            if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
                worldIn.m_186460_(pos, this, 0);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
        if (previouslyPowered != worldIn.m_276867_(pos)) {
            worldIn.m_7731_(pos, (BlockState) state.m_61122_(POWERED), 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return (BlockState) super.m_5573_(p_196258_1_).m_61124_(POWERED, p_196258_1_.m_43725_().m_276867_(p_196258_1_.getClickedPos()));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockEntityType<? extends ChuteBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ChuteBlockEntity>) AllBlockEntityTypes.SMART_CHUTE.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.m_7926_(p_206840_1_.add(POWERED));
    }

    @Override
    public BlockState updateChuteState(BlockState state, BlockState above, BlockGetter world, BlockPos pos) {
        return state;
    }
}
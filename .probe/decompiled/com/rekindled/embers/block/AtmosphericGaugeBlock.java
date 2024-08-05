package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.AtmosphericGaugeBlockEntity;
import com.rekindled.embers.util.EmberGenUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AtmosphericGaugeBlock extends DirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {

    public AtmosphericGaugeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((Direction) pState.m_61143_(f_52588_)) {
            case UP:
                return DialBaseBlock.UP_AABB;
            case DOWN:
                return DialBaseBlock.DOWN_AABB;
            case EAST:
                return DialBaseBlock.EAST_AABB;
            case WEST:
                return DialBaseBlock.WEST_AABB;
            case SOUTH:
                return DialBaseBlock.SOUTH_AABB;
            case NORTH:
            default:
                return DialBaseBlock.NORTH_AABB;
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, ((Direction) pState.m_61143_(f_52588_)).getOpposite());
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        return !pReader.m_8055_(pPos.relative(pDirection)).m_60795_();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        for (Direction direction : pContext.getNearestLookingDirections()) {
            BlockState blockstate = (BlockState) this.m_49966_().m_61124_(f_52588_, direction.getOpposite());
            if (blockstate.m_60710_(pContext.m_43725_(), pContext.getClickedPos())) {
                return (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return ((Direction) pState.m_61143_(f_52588_)).getOpposite() == pFacing && !pState.m_60710_(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(f_52588_, pRot.rotate((Direction) pState.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(f_52588_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(f_52588_).add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level instanceof ServerLevel server ? Mth.clamp(Math.round(15.0F * EmberGenUtil.getEmberDensity(server.getSeed(), pos.m_123341_(), pos.m_123343_())), 0, 15) : 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.ATMOSPHERIC_GAUGE_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : BaseEntityBlock.createTickerHelper(pBlockEntityType, RegistryManager.ATMOSPHERIC_GAUGE_ENTITY.get(), AtmosphericGaugeBlockEntity::serverTick);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}
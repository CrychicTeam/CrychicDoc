package com.rekindled.embers.block;

import com.rekindled.embers.api.block.IDial;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageDialUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class DialBaseBlock extends DirectionalBlock implements IDial, EntityBlock, SimpleWaterloggedBlock {

    protected static final VoxelShape UP_AABB = Shapes.box(0.3125, 0.0, 0.3125, 0.6875, 0.125, 0.6875);

    protected static final VoxelShape DOWN_AABB = Shapes.box(0.3125, 0.875, 0.3125, 0.6875, 1.0, 0.6875);

    protected static final VoxelShape NORTH_AABB = Shapes.box(0.3125, 0.3125, 0.875, 0.6875, 0.6875, 1.0);

    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.3125, 0.3125, 0.0, 0.6875, 0.6875, 0.125);

    protected static final VoxelShape WEST_AABB = Shapes.box(0.875, 0.3125, 0.3125, 1.0, 0.6875, 0.6875);

    protected static final VoxelShape EAST_AABB = Shapes.box(0.0, 0.3125, 0.3125, 0.125, 0.6875, 0.6875);

    public DialBaseBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH)).m_61124_(BlockStateProperties.POWER, 0)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((Direction) pState.m_61143_(f_52588_)) {
            case UP:
                return UP_AABB;
            case DOWN:
                return DOWN_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case NORTH:
            default:
                return NORTH_AABB;
        }
    }

    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        if (state.m_60807_()) {
            BlockEntity blockEntity = level.m_7702_(pos.relative((Direction) state.m_61143_(f_52588_), -1));
            if (blockEntity != null && blockEntity.hasLevel()) {
                int power = state.m_60674_(blockEntity.getLevel(), pos);
                if ((Integer) state.m_61143_(BlockStateProperties.POWER) != power) {
                    blockEntity.getLevel().setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.POWER, power), 1);
                    blockEntity.getLevel().updateNeighbourForOutputSignal(pos, state.m_60734_());
                }
            }
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
        pBuilder.add(f_52588_).add(BlockStateProperties.POWER).add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public List<Component> getDisplayInfo(Level world, BlockPos pos, BlockState state, int maxLines) {
        ArrayList<Component> text = new ArrayList();
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity != null) {
            Direction facing = (Direction) state.m_61143_(f_52588_);
            this.getBEData(facing, text, tileEntity, maxLines);
            if (world.getBlockEntity(pos.relative(facing, -1)) instanceof IExtraDialInformation facingTile) {
                facingTile.addDialInformation(facing, text, this.getDialType());
            }
        }
        return text;
    }

    protected abstract void getBEData(Direction var1, ArrayList<Component> var2, BlockEntity var3, int var4);

    @Override
    public void updateBEData(BlockPos pos, int maxLines) {
        PacketHandler.INSTANCE.sendToServer(new MessageDialUpdateRequest(pos, maxLines));
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}
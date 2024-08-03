package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.foundation.utility.VoxelShaper;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopycatStepBlock extends WaterloggedCopycatBlock {

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final int placementHelperId = PlacementHelpers.register(new CopycatStepBlock.PlacementHelper());

    public CopycatStepBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(HALF, Half.BOTTOM)).m_61124_(FACING, Direction.SOUTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(hand);
            IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
            if (helper.matchesItem(heldItem)) {
                return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
            }
        }
        return super.m_6227_(state, world, pos, player, hand, ray);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.m_8055_(toPos);
        if (!toState.m_60713_(this)) {
            return true;
        } else {
            Direction facing = (Direction) state.m_61143_(FACING);
            BlockPos diff = fromPos.subtract(toPos);
            int coord = facing.getAxis().choose(diff.m_123341_(), diff.m_123342_(), diff.m_123343_());
            Half half = (Half) state.m_61143_(HALF);
            return half != toState.m_61143_(HALF) ? diff.m_123342_() == 0 : facing == ((Direction) toState.m_61143_(FACING)).getOpposite() && (coord == 0 || coord == facing.getAxisDirection().getStep());
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        Direction facing = (Direction) state.m_61143_(FACING);
        BlockState toState = reader.m_8055_(toPos);
        BlockPos diff = fromPos.subtract(toPos);
        if (fromPos.equals(toPos.relative(facing))) {
            return false;
        } else if (!toState.m_60713_(this)) {
            return false;
        } else if (diff.m_123342_() != 0) {
            return isOccluded(toState, state, diff.m_123342_() > 0 ? Direction.UP : Direction.DOWN);
        } else if (isOccluded(state, toState, facing)) {
            return true;
        } else {
            int coord = facing.getAxis().choose(diff.m_123341_(), diff.m_123342_(), diff.m_123343_());
            return state.m_61124_(WATERLOGGED, false) == toState.m_61124_(WATERLOGGED, false) && coord == 0;
        }
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return face.getAxis() == Direction.Axis.Y ? state.m_61143_(HALF) == Half.TOP == (face == Direction.UP) : state.m_61143_(FACING) == face;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return this.canFaceBeOccluded(state, face.getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = (BlockState) super.getStateForPlacement(pContext).m_61124_(FACING, pContext.m_8125_());
        Direction direction = pContext.m_43719_();
        if (direction == Direction.UP) {
            return stateForPlacement;
        } else {
            return direction != Direction.DOWN && !(pContext.m_43720_().y - (double) pContext.getClickedPos().m_123342_() > 0.5) ? stateForPlacement : (BlockState) stateForPlacement.m_61124_(HALF, Half.TOP);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(HALF, FACING));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShaper voxelShaper = pState.m_61143_(HALF) == Half.BOTTOM ? AllShapes.STEP_BOTTOM : AllShapes.STEP_TOP;
        return voxelShaper.get((Direction) pState.m_61143_(FACING));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return state.m_60713_(this) == neighborState.m_60713_(this) && getMaterial(level, pos).m_60719_(getMaterial(level, pos.relative(dir)), dir.getOpposite()) ? isOccluded(state, neighborState, dir) : false;
    }

    public static boolean isOccluded(BlockState state, BlockState other, Direction pDirection) {
        state = (BlockState) state.m_61124_(WATERLOGGED, false);
        other = (BlockState) other.m_61124_(WATERLOGGED, false);
        Half half = (Half) state.m_61143_(HALF);
        boolean vertical = pDirection.getAxis() == Direction.Axis.Y;
        if (half == other.m_61143_(HALF)) {
            if (vertical) {
                return false;
            } else {
                Direction facing = (Direction) state.m_61143_(FACING);
                if (facing.getOpposite() == other.m_61143_(FACING) && pDirection == facing) {
                    return true;
                } else {
                    return other.m_61143_(FACING) != facing ? false : pDirection.getAxis() != facing.getAxis();
                }
            }
        } else {
            return vertical && pDirection == Direction.UP == (half == Half.TOP);
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(FACING, pRot.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    private static class PlacementHelper extends PoleHelper<Direction> {

        public PlacementHelper() {
            super(AllBlocks.COPYCAT_STEP::has, state -> ((Direction) state.m_61143_(CopycatStepBlock.FACING)).getClockWise().getAxis(), CopycatStepBlock.FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.COPYCAT_STEP::isIn;
        }
    }
}
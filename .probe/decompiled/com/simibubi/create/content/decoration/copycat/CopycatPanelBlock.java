package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopycatPanelBlock extends WaterloggedCopycatBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final int placementHelperId = PlacementHelpers.register(new CopycatPanelBlock.PlacementHelper());

    public CopycatPanelBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FACING, Direction.UP));
    }

    @Override
    public boolean isAcceptedRegardless(BlockState material) {
        return CopycatSpecialCases.isBarsMaterial(material) || CopycatSpecialCases.isTrapdoorMaterial(material);
    }

    @Override
    public BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        if (!CopycatSpecialCases.isTrapdoorMaterial(material)) {
            return super.prepareMaterial(pLevel, pPos, pState, pPlayer, pHand, pHit, material);
        } else {
            Direction panelFacing = (Direction) pState.m_61143_(FACING);
            if (panelFacing == Direction.DOWN) {
                material = (BlockState) material.m_61124_(TrapDoorBlock.HALF, Half.TOP);
            }
            if (panelFacing.getAxis() == Direction.Axis.Y) {
                return (BlockState) ((BlockState) material.m_61124_(TrapDoorBlock.f_54117_, pPlayer.m_6350_())).m_61124_(TrapDoorBlock.OPEN, false);
            } else {
                boolean clickedNearTop = pHit.m_82450_().y - 0.5 > (double) pPos.m_123342_();
                return (BlockState) ((BlockState) ((BlockState) material.m_61124_(TrapDoorBlock.OPEN, true)).m_61124_(TrapDoorBlock.HALF, clickedNearTop ? Half.TOP : Half.BOTTOM)).m_61124_(TrapDoorBlock.f_54117_, panelFacing);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(hand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
                return InteractionResult.SUCCESS;
            }
        }
        return super.m_6227_(state, world, pos, player, hand, ray);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        Direction facing = (Direction) state.m_61143_(FACING);
        BlockState toState = reader.m_8055_(toPos);
        if (!toState.m_60713_(this)) {
            return facing != face.getOpposite();
        } else {
            BlockPos diff = fromPos.subtract(toPos);
            int coord = facing.getAxis().choose(diff.m_123341_(), diff.m_123342_(), diff.m_123343_());
            return facing == ((Direction) toState.m_61143_(FACING)).getOpposite() && (coord == 0 || coord != facing.getAxisDirection().getStep());
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        Direction facing = (Direction) state.m_61143_(FACING);
        BlockState toState = reader.m_8055_(toPos);
        if (toPos.equals(fromPos.relative(facing))) {
            return false;
        } else {
            BlockPos diff = fromPos.subtract(toPos);
            int coord = facing.getAxis().choose(diff.m_123341_(), diff.m_123342_(), diff.m_123343_());
            if (!toState.m_60713_(this)) {
                return coord != -facing.getAxisDirection().getStep();
            } else {
                return isOccluded(state, toState, facing) ? true : toState.m_61124_(WATERLOGGED, false) == state.m_61124_(WATERLOGGED, false) && coord == 0;
            }
        }
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return ((Direction) state.m_61143_(FACING)).getOpposite() == face;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return this.canFaceBeOccluded(state, face.getOpposite());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        return (BlockState) stateForPlacement.m_61124_(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.CASING_3PX.get((Direction) pState.m_61143_(FACING));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        if (state.m_60713_(this) == neighborState.m_60713_(this)) {
            if (CopycatSpecialCases.isBarsMaterial(getMaterial(level, pos)) && CopycatSpecialCases.isBarsMaterial(getMaterial(level, pos.relative(dir)))) {
                return state.m_61143_(FACING) == neighborState.m_61143_(FACING);
            }
            if (getMaterial(level, pos).m_60719_(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                return isOccluded(state, neighborState, dir.getOpposite());
            }
        }
        return state.m_61143_(FACING) == dir.getOpposite() && getMaterial(level, pos).m_60719_(neighborState, dir.getOpposite());
    }

    public static boolean isOccluded(BlockState state, BlockState other, Direction pDirection) {
        state = (BlockState) state.m_61124_(WATERLOGGED, false);
        other = (BlockState) other.m_61124_(WATERLOGGED, false);
        Direction facing = (Direction) state.m_61143_(FACING);
        if (facing.getOpposite() == other.m_61143_(FACING) && pDirection == facing) {
            return true;
        } else {
            return other.m_61143_(FACING) != facing ? false : pDirection.getAxis() != facing.getAxis();
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.COPYCAT_PANEL::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return AllBlocks.COPYCAT_PANEL::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), ((Direction) state.m_61143_(CopycatPanelBlock.FACING)).getAxis(), dir -> world.getBlockState(pos.relative(dir)).m_247087_());
            return directions.isEmpty() ? PlacementOffset.fail() : PlacementOffset.success(pos.relative((Direction) directions.get(0)), s -> (BlockState) s.m_61124_(CopycatPanelBlock.FACING, (Direction) state.m_61143_(CopycatPanelBlock.FACING)));
        }
    }
}
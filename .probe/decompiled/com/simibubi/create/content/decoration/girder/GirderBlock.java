package com.simibubi.create.content.decoration.girder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.trains.display.FlapDisplayBlock;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackShape;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GirderBlock extends Block implements SimpleWaterloggedBlock, IWrenchable {

    private static final int placementHelperId = PlacementHelpers.register(new GirderPlacementHelper());

    public static final BooleanProperty X = BooleanProperty.create("x");

    public static final BooleanProperty Z = BooleanProperty.create("z");

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public GirderBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(AXIS, Direction.Axis.Y)).m_61124_(TOP, false)).m_61124_(BOTTOM, false)).m_61124_(X, false)).m_61124_(Z, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(X, Z, TOP, BOTTOM, AXIS, BlockStateProperties.WATERLOGGED));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return Shapes.or(super.m_7947_(pState, pReader, pPos), AllShapes.EIGHT_VOXEL_POLE.get(Direction.Axis.Y));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer == null) {
            return InteractionResult.PASS;
        } else {
            ItemStack itemInHand = pPlayer.m_21120_(pHand);
            if (!AllBlocks.SHAFT.isIn(itemInHand)) {
                if (!AllItems.WRENCH.isIn(itemInHand) || pPlayer.m_6144_()) {
                    IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
                    return helper.matchesItem(itemInHand) ? helper.getOffset(pPlayer, pLevel, pState, pPos, pHit).placeInWorld(pLevel, (BlockItem) itemInHand.getItem(), pPlayer, pHand, pHit) : InteractionResult.PASS;
                } else {
                    return GirderWrenchBehavior.handleClick(pLevel, pPos, pState, pHit) ? InteractionResult.sidedSuccess(pLevel.isClientSide) : InteractionResult.FAIL;
                }
            } else {
                KineticBlockEntity.switchToBlockState(pLevel, pPos, (BlockState) ((BlockState) ((BlockState) ((BlockState) AllBlocks.METAL_GIRDER_ENCASED_SHAFT.getDefaultState().m_61124_(BlockStateProperties.WATERLOGGED, (Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED))).m_61124_(TOP, (Boolean) pState.m_61143_(TOP))).m_61124_(BOTTOM, (Boolean) pState.m_61143_(BOTTOM))).m_61124_(GirderEncasedShaftBlock.HORIZONTAL_AXIS, !pState.m_61143_(X) && pHit.getDirection().getAxis() != Direction.Axis.Z ? Direction.Axis.X : Direction.Axis.Z));
                pLevel.playSound(null, pPos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.BLOCKS, 0.5F, 1.25F);
                if (!pLevel.isClientSide && !pPlayer.isCreative()) {
                    itemInHand.shrink(1);
                    if (itemInHand.isEmpty()) {
                        pPlayer.m_21008_(pHand, ItemStack.EMPTY);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        Block.updateOrDestroy(blockState0, Block.updateFromNeighbourShapes(blockState0, serverLevel1, blockPos2), serverLevel1, blockPos2, 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        Direction.Axis axis = direction.getAxis();
        if (direction.getAxis() != Direction.Axis.Y) {
            if (state.m_61143_(AXIS) != direction.getAxis()) {
                Property<Boolean> updateProperty = axis == Direction.Axis.X ? X : (axis == Direction.Axis.Z ? Z : (direction == Direction.UP ? TOP : BOTTOM));
                if (!isConnected(world, pos, state, direction) && !isConnected(world, pos, state, direction.getOpposite())) {
                    state = (BlockState) state.m_61124_(updateProperty, false);
                }
            }
        } else if (state.m_61143_(AXIS) != Direction.Axis.Y) {
            if (world.m_8055_(pos.above()).m_60816_(world, pos.above()).isEmpty()) {
                state = (BlockState) state.m_61124_(TOP, false);
            }
            if (world.m_8055_(pos.below()).m_60816_(world, pos.below()).isEmpty()) {
                state = (BlockState) state.m_61124_(BOTTOM, false);
            }
        }
        for (Direction d : Iterate.directionsInAxis(axis)) {
            state = updateState(world, pos, state, d);
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        Direction face = context.m_43719_();
        FluidState ifluidstate = level.getFluidState(pos);
        BlockState state = super.getStateForPlacement(context);
        state = (BlockState) state.m_61124_(X, face.getAxis() == Direction.Axis.X);
        state = (BlockState) state.m_61124_(Z, face.getAxis() == Direction.Axis.Z);
        state = (BlockState) state.m_61124_(AXIS, face.getAxis());
        for (Direction d : Iterate.directions) {
            state = updateState(level, pos, state, d);
        }
        return (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    public static BlockState updateState(LevelAccessor level, BlockPos pos, BlockState state, Direction d) {
        Direction.Axis axis = d.getAxis();
        Property<Boolean> updateProperty = axis == Direction.Axis.X ? X : (axis == Direction.Axis.Z ? Z : (d == Direction.UP ? TOP : BOTTOM));
        BlockState sideState = level.m_8055_(pos.relative(d));
        if (axis.isVertical()) {
            return updateVerticalProperty(level, pos, state, updateProperty, sideState, d);
        } else {
            if (state.m_61143_(AXIS) == axis) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            } else if (sideState.m_60734_() instanceof GirderEncasedShaftBlock && sideState.m_61143_(GirderEncasedShaftBlock.HORIZONTAL_AXIS) != axis) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            } else if (sideState.m_60734_() == state.m_60734_() && (Boolean) sideState.m_61143_(updateProperty)) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            } else if (sideState.m_60734_() instanceof NixieTubeBlock && NixieTubeBlock.getFacing(sideState) == d) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            } else if (sideState.m_60734_() instanceof PlacardBlock && PlacardBlock.connectedDirection(sideState) == d) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            } else if (isFacingBracket(level, pos, d)) {
                state = (BlockState) state.m_61124_(updateProperty, true);
            }
            for (Direction d2 : Iterate.directionsInAxis(axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)) {
                BlockState above = level.m_8055_(pos.above().relative(d2));
                if (AllTags.AllBlockTags.GIRDABLE_TRACKS.matches(above)) {
                    TrackShape shape = (TrackShape) above.m_61143_(TrackBlock.SHAPE);
                    if (shape == (axis == Direction.Axis.X ? TrackShape.XO : TrackShape.ZO)) {
                        state = (BlockState) state.m_61124_(updateProperty, true);
                    }
                }
            }
            return state;
        }
    }

    public static boolean isFacingBracket(BlockAndTintGetter level, BlockPos pos, Direction d) {
        if (level.m_7702_(pos.relative(d)) instanceof SmartBlockEntity sbe) {
            BracketedBlockEntityBehaviour behaviour = sbe.getBehaviour(BracketedBlockEntityBehaviour.TYPE);
            if (behaviour == null) {
                return false;
            } else {
                BlockState bracket = behaviour.getBracket();
                return bracket != null && bracket.m_61138_(BracketBlock.f_52588_) ? bracket.m_61143_(BracketBlock.f_52588_) == d : false;
            }
        } else {
            return false;
        }
    }

    public static BlockState updateVerticalProperty(LevelAccessor level, BlockPos pos, BlockState state, Property<Boolean> updateProperty, BlockState sideState, Direction d) {
        boolean canAttach = false;
        if (state.m_61138_(AXIS) && state.m_61143_(AXIS) == Direction.Axis.Y) {
            canAttach = true;
        } else if (isGirder(sideState) && isXGirder(sideState) == isZGirder(sideState)) {
            canAttach = true;
        } else if (isGirder(sideState)) {
            canAttach = true;
        } else if (sideState.m_61138_(WallBlock.UP) && (Boolean) sideState.m_61143_(WallBlock.UP)) {
            canAttach = true;
        } else if (sideState.m_60734_() instanceof NixieTubeBlock && NixieTubeBlock.getFacing(sideState) == d) {
            canAttach = true;
        } else if (sideState.m_60734_() instanceof FlapDisplayBlock) {
            canAttach = true;
        } else if (sideState.m_60734_() instanceof LanternBlock && d == Direction.DOWN == (Boolean) sideState.m_61143_(LanternBlock.HANGING)) {
            canAttach = true;
        } else if (sideState.m_60734_() instanceof ChainBlock && sideState.m_61143_(ChainBlock.f_55923_) == Direction.Axis.Y) {
            canAttach = true;
        } else if (sideState.m_61138_(FaceAttachedHorizontalDirectionalBlock.FACE)) {
            if (sideState.m_61143_(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING && d == Direction.DOWN) {
                canAttach = true;
            } else if (sideState.m_61143_(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.FLOOR && d == Direction.UP) {
                canAttach = true;
            }
        } else if (sideState.m_60734_() instanceof PlacardBlock && PlacardBlock.connectedDirection(sideState) == d) {
            canAttach = true;
        } else if (isFacingBracket(level, pos, d)) {
            canAttach = true;
        }
        return canAttach ? (BlockState) state.m_61124_(updateProperty, true) : state;
    }

    public static boolean isGirder(BlockState state) {
        return state.m_60734_() instanceof GirderBlock || state.m_60734_() instanceof GirderEncasedShaftBlock;
    }

    public static boolean isXGirder(BlockState state) {
        return state.m_60734_() instanceof GirderBlock && (Boolean) state.m_61143_(X) || state.m_60734_() instanceof GirderEncasedShaftBlock && state.m_61143_(GirderEncasedShaftBlock.HORIZONTAL_AXIS) == Direction.Axis.Z;
    }

    public static boolean isZGirder(BlockState state) {
        return state.m_60734_() instanceof GirderBlock && (Boolean) state.m_61143_(Z) || state.m_60734_() instanceof GirderEncasedShaftBlock && state.m_61143_(GirderEncasedShaftBlock.HORIZONTAL_AXIS) == Direction.Axis.X;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        boolean x = (Boolean) state.m_61143_(X);
        boolean z = (Boolean) state.m_61143_(Z);
        return x ? (z ? AllShapes.GIRDER_CROSS : AllShapes.GIRDER_BEAM.get(Direction.Axis.X)) : (z ? AllShapes.GIRDER_BEAM.get(Direction.Axis.Z) : AllShapes.EIGHT_VOXEL_POLE.get(Direction.Axis.Y));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static boolean isConnected(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction side) {
        Direction.Axis axis = side.getAxis();
        if (state.m_60734_() instanceof GirderBlock && !(Boolean) state.m_61143_(axis == Direction.Axis.X ? X : Z)) {
            return false;
        } else if (state.m_60734_() instanceof GirderEncasedShaftBlock && state.m_61143_(GirderEncasedShaftBlock.HORIZONTAL_AXIS) == axis) {
            return false;
        } else {
            BlockPos relative = pos.relative(side);
            BlockState blockState = world.m_8055_(relative);
            if (blockState.m_60795_()) {
                return false;
            } else if (blockState.m_60734_() instanceof NixieTubeBlock && NixieTubeBlock.getFacing(blockState) == side) {
                return true;
            } else if (isFacingBracket(world, pos, side)) {
                return true;
            } else if (blockState.m_60734_() instanceof PlacardBlock && PlacardBlock.connectedDirection(blockState) == side) {
                return true;
            } else {
                VoxelShape shape = blockState.m_60808_(world, relative);
                if (shape.isEmpty()) {
                    return false;
                } else {
                    return Block.isFaceFull(shape, side.getOpposite()) && blockState.m_280296_() ? true : AbstractChuteBlock.getChuteFacing(blockState) == Direction.DOWN;
                }
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        state = (BlockState) state.m_61124_(AXIS, rot.rotate(Direction.fromAxisAndDirection((Direction.Axis) state.m_61143_(AXIS), Direction.AxisDirection.POSITIVE)).getAxis());
        return rot.rotate(Direction.EAST).getAxis() == Direction.Axis.X ? state : (BlockState) ((BlockState) state.m_61124_(X, (Boolean) state.m_61143_(Z))).m_61124_(Z, (Boolean) state.m_61143_(Z));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }
}
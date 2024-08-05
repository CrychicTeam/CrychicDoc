package com.simibubi.create.content.kinetics.gantry;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GantryShaftBlock extends DirectionalKineticBlock implements IBE<GantryShaftBlockEntity> {

    public static final Property<GantryShaftBlock.Part> PART = EnumProperty.create("part", GantryShaftBlock.Part.class);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final int placementHelperId = PlacementHelpers.register(new GantryShaftBlock.PlacementHelper());

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(PART, POWERED));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack heldItem = player.m_21120_(hand);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        return !placementHelper.matchesItem(heldItem) ? InteractionResult.PASS : placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.EIGHT_VOXEL_POLE.get(((Direction) state.m_61143_(FACING)).getAxis());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        Direction facing = (Direction) state.m_61143_(FACING);
        Direction.Axis axis = facing.getAxis();
        if (direction.getAxis() != axis) {
            return state;
        } else {
            boolean connect = AllBlocks.GANTRY_SHAFT.has(neighbour) && neighbour.m_61143_(FACING) == facing;
            GantryShaftBlock.Part part = (GantryShaftBlock.Part) state.m_61143_(PART);
            if (direction.getAxisDirection() == facing.getAxisDirection()) {
                if (connect) {
                    if (part == GantryShaftBlock.Part.END) {
                        part = GantryShaftBlock.Part.MIDDLE;
                    }
                    if (part == GantryShaftBlock.Part.SINGLE) {
                        part = GantryShaftBlock.Part.START;
                    }
                } else {
                    if (part == GantryShaftBlock.Part.MIDDLE) {
                        part = GantryShaftBlock.Part.END;
                    }
                    if (part == GantryShaftBlock.Part.START) {
                        part = GantryShaftBlock.Part.SINGLE;
                    }
                }
            } else if (connect) {
                if (part == GantryShaftBlock.Part.START) {
                    part = GantryShaftBlock.Part.MIDDLE;
                }
                if (part == GantryShaftBlock.Part.SINGLE) {
                    part = GantryShaftBlock.Part.END;
                }
            } else {
                if (part == GantryShaftBlock.Part.MIDDLE) {
                    part = GantryShaftBlock.Part.START;
                }
                if (part == GantryShaftBlock.Part.END) {
                    part = GantryShaftBlock.Part.SINGLE;
                }
            }
            return (BlockState) state.m_61124_(PART, part);
        }
    }

    public GantryShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(PART, GantryShaftBlock.Part.SINGLE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        BlockPos pos = context.getClickedPos();
        Level world = context.m_43725_();
        Direction face = context.m_43719_();
        BlockState neighbour = world.getBlockState(pos.relative(((Direction) state.m_61143_(FACING)).getOpposite()));
        BlockState clickedState = AllBlocks.GANTRY_SHAFT.has(neighbour) ? neighbour : world.getBlockState(pos.relative(face.getOpposite()));
        if (AllBlocks.GANTRY_SHAFT.has(clickedState) && ((Direction) clickedState.m_61143_(FACING)).getAxis() == ((Direction) state.m_61143_(FACING)).getAxis()) {
            Direction facing = (Direction) clickedState.m_61143_(FACING);
            state = (BlockState) state.m_61124_(FACING, context.m_43723_() != null && context.m_43723_().m_6144_() ? facing.getOpposite() : facing);
        }
        return (BlockState) state.m_61124_(POWERED, this.shouldBePowered(state, world, pos));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = super.onWrenched(state, context);
        if (onWrenched.consumesAction()) {
            BlockPos pos = context.getClickedPos();
            Level world = context.getLevel();
            this.neighborChanged(world.getBlockState(pos), world, pos, state.m_60734_(), pos, false);
        }
        return onWrenched;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
        if (!worldIn.isClientSide() && oldState.m_60713_((Block) AllBlocks.GANTRY_SHAFT.get())) {
            GantryShaftBlock.Part oldPart = (GantryShaftBlock.Part) oldState.m_61143_(PART);
            GantryShaftBlock.Part part = (GantryShaftBlock.Part) state.m_61143_(PART);
            if (oldPart != GantryShaftBlock.Part.MIDDLE && part == GantryShaftBlock.Part.MIDDLE || oldPart == GantryShaftBlock.Part.SINGLE && part != GantryShaftBlock.Part.SINGLE) {
                BlockEntity be = worldIn.getBlockEntity(pos);
                if (be instanceof GantryShaftBlockEntity) {
                    ((GantryShaftBlockEntity) be).checkAttachedCarriageBlocks();
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            boolean shouldPower = worldIn.m_276867_(pos);
            if (!previouslyPowered && !shouldPower && this.shouldBePowered(state, worldIn, pos)) {
                worldIn.setBlock(pos, (BlockState) state.m_61124_(POWERED, true), 3);
            } else if (previouslyPowered != shouldPower) {
                List<BlockPos> toUpdate = new ArrayList();
                Direction facing = (Direction) state.m_61143_(FACING);
                Direction.Axis axis = facing.getAxis();
                for (Direction d : Iterate.directionsInAxis(axis)) {
                    for (BlockPos currentPos = pos.relative(d); worldIn.isLoaded(currentPos); currentPos = currentPos.relative(d)) {
                        BlockState currentState = worldIn.getBlockState(currentPos);
                        if (!(currentState.m_60734_() instanceof GantryShaftBlock) || currentState.m_61143_(FACING) != facing) {
                            break;
                        }
                        if (!shouldPower && (Boolean) currentState.m_61143_(POWERED) && worldIn.m_276867_(currentPos)) {
                            return;
                        }
                        if ((Boolean) currentState.m_61143_(POWERED) == shouldPower) {
                            break;
                        }
                        toUpdate.add(currentPos);
                    }
                }
                toUpdate.add(pos);
                for (BlockPos blockPos : toUpdate) {
                    BlockState blockState = worldIn.getBlockState(blockPos);
                    BlockEntity be = worldIn.getBlockEntity(blockPos);
                    if (be instanceof KineticBlockEntity) {
                        ((KineticBlockEntity) be).detachKinetics();
                    }
                    if (blockState.m_60734_() instanceof GantryShaftBlock) {
                        worldIn.setBlock(blockPos, (BlockState) blockState.m_61124_(POWERED, shouldPower), 2);
                    }
                }
            }
        }
    }

    protected boolean shouldBePowered(BlockState state, Level worldIn, BlockPos pos) {
        boolean shouldPower = worldIn.m_276867_(pos);
        Direction facing = (Direction) state.m_61143_(FACING);
        for (Direction d : Iterate.directionsInAxis(facing.getAxis())) {
            BlockPos neighbourPos = pos.relative(d);
            if (worldIn.isLoaded(neighbourPos)) {
                BlockState neighbourState = worldIn.getBlockState(neighbourPos);
                if (neighbourState.m_60734_() instanceof GantryShaftBlock && neighbourState.m_61143_(FACING) == facing) {
                    shouldPower |= neighbourState.m_61143_(POWERED);
                }
            }
        }
        return shouldPower;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return super.areStatesKineticallyEquivalent(oldState, newState) && oldState.m_61143_(POWERED) == newState.m_61143_(POWERED);
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.35F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.25F;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<GantryShaftBlockEntity> getBlockEntityClass() {
        return GantryShaftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GantryShaftBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends GantryShaftBlockEntity>) AllBlockEntityTypes.GANTRY_SHAFT.get();
    }

    public static enum Part implements StringRepresentable {

        START, MIDDLE, END, SINGLE;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }

    public static class PlacementHelper extends PoleHelper<Direction> {

        public PlacementHelper() {
            super(AllBlocks.GANTRY_SHAFT::has, s -> ((Direction) s.m_61143_(DirectionalKineticBlock.FACING)).getAxis(), DirectionalKineticBlock.FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.GANTRY_SHAFT::isIn;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
            offset.withTransform(offset.getTransform().andThen(s -> (BlockState) s.m_61124_(GantryShaftBlock.POWERED, (Boolean) state.m_61143_(GantryShaftBlock.POWERED))));
            return offset;
        }
    }
}
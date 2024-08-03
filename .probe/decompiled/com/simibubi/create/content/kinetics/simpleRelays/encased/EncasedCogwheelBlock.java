package com.simibubi.create.content.kinetics.simpleRelays.encased;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VoxelShaper;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class EncasedCogwheelBlock extends RotatedPillarKineticBlock implements ICogWheel, IBE<SimpleKineticBlockEntity>, ISpecialBlockItemRequirement, ITransformableBlock, EncasedBlock {

    public static final BooleanProperty TOP_SHAFT = BooleanProperty.create("top_shaft");

    public static final BooleanProperty BOTTOM_SHAFT = BooleanProperty.create("bottom_shaft");

    protected final boolean isLarge;

    private final Supplier<Block> casing;

    public EncasedCogwheelBlock(BlockBehaviour.Properties properties, boolean large, Supplier<Block> casing) {
        super(properties);
        this.isLarge = large;
        this.casing = casing;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(TOP_SHAFT, false)).m_61124_(BOTTOM_SHAFT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TOP_SHAFT, BOTTOM_SHAFT));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult) {
            return ((BlockHitResult) target).getDirection().getAxis() != this.getRotationAxis(state) ? (this.isLarge ? AllBlocks.LARGE_COGWHEEL.asStack() : AllBlocks.COGWHEEL.asStack()) : this.getCasing().asItem().getDefaultInstance();
        } else {
            return super.getCloneItemStack(state, target, world, pos, player);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placedOn = context.m_43725_().getBlockState(context.getClickedPos().relative(context.m_43719_().getOpposite()));
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (ICogWheel.isSmallCog(placedOn)) {
            stateForPlacement = (BlockState) stateForPlacement.m_61124_(AXIS, ((IRotate) placedOn.m_60734_()).getRotationAxis(placedOn));
        }
        return stateForPlacement;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pState.m_60734_() == pAdjacentBlockState.m_60734_() && pState.m_61143_(AXIS) == pAdjacentBlockState.m_61143_(AXIS);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace().getAxis() != state.m_61143_(AXIS)) {
            return super.onWrenched(state, context);
        } else {
            Level level = context.getLevel();
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockPos pos = context.getClickedPos();
                KineticBlockEntity.switchToBlockState(level, pos, (BlockState) state.m_61122_(context.getClickedFace().getAxisDirection() == Direction.AxisDirection.POSITIVE ? TOP_SHAFT : BOTTOM_SHAFT));
                this.playRotateSound(level, pos);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        originalState = this.swapShaftsForRotation(originalState, Rotation.CLOCKWISE_90, targetedFace.getAxis());
        return (BlockState) originalState.m_61124_(RotatedPillarKineticBlock.AXIS, VoxelShaper.axisAsFace((Direction.Axis) originalState.m_61143_(RotatedPillarKineticBlock.AXIS)).getClockWise(targetedFace.getAxis()).getAxis());
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            context.getLevel().m_46796_(2001, context.getClickedPos(), Block.getId(state));
            KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), (BlockState) (this.isLarge ? AllBlocks.LARGE_COGWHEEL : AllBlocks.COGWHEEL).getDefaultState().m_61124_(AXIS, (Direction.Axis) state.m_61143_(AXIS)));
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(AXIS) && (Boolean) state.m_61143_(face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? TOP_SHAFT : BOTTOM_SHAFT);
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (newState.m_60734_() instanceof EncasedCogwheelBlock && oldState.m_60734_() instanceof EncasedCogwheelBlock) {
            if (newState.m_61143_(TOP_SHAFT) != oldState.m_61143_(TOP_SHAFT)) {
                return false;
            }
            if (newState.m_61143_(BOTTOM_SHAFT) != oldState.m_61143_(BOTTOM_SHAFT)) {
                return false;
            }
        }
        return super.areStatesKineticallyEquivalent(oldState, newState);
    }

    @Override
    public boolean isSmallCog() {
        return !this.isLarge;
    }

    @Override
    public boolean isLargeCog() {
        return this.isLarge;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return CogWheelBlock.isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, (Direction.Axis) state.m_61143_(AXIS));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    public BlockState swapShafts(BlockState state) {
        boolean bottom = (Boolean) state.m_61143_(BOTTOM_SHAFT);
        boolean top = (Boolean) state.m_61143_(TOP_SHAFT);
        state = (BlockState) state.m_61124_(BOTTOM_SHAFT, top);
        return (BlockState) state.m_61124_(TOP_SHAFT, bottom);
    }

    public BlockState swapShaftsForRotation(BlockState state, Rotation rotation, Direction.Axis rotationAxis) {
        if (rotation == Rotation.NONE) {
            return state;
        } else {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(AXIS);
            if (axis == rotationAxis) {
                return state;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                return this.swapShafts(state);
            } else {
                boolean clockwise = rotation == Rotation.CLOCKWISE_90;
                if (rotationAxis == Direction.Axis.X) {
                    if (axis == Direction.Axis.Z && !clockwise || axis == Direction.Axis.Y && clockwise) {
                        return this.swapShafts(state);
                    }
                } else if (rotationAxis == Direction.Axis.Y) {
                    if (axis == Direction.Axis.X && !clockwise || axis == Direction.Axis.Z && clockwise) {
                        return this.swapShafts(state);
                    }
                } else if (rotationAxis == Direction.Axis.Z && (axis == Direction.Axis.Y && !clockwise || axis == Direction.Axis.X && clockwise)) {
                    return this.swapShafts(state);
                }
                return state;
            }
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(AXIS);
        return (axis != Direction.Axis.X || mirror != Mirror.FRONT_BACK) && (axis != Direction.Axis.Z || mirror != Mirror.LEFT_RIGHT) ? state : this.swapShafts(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        state = this.swapShaftsForRotation(state, rotation, Direction.Axis.Y);
        return super.rotate(state, rotation);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.mirror(state, transform.mirror);
        }
        if (transform.rotationAxis == Direction.Axis.Y) {
            return this.rotate(state, transform.rotation);
        } else {
            state = this.swapShaftsForRotation(state, transform.rotation, transform.rotationAxis);
            return (BlockState) state.m_61124_(AXIS, transform.rotateAxis((Direction.Axis) state.m_61143_(AXIS)));
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(this.isLarge ? AllBlocks.LARGE_COGWHEEL.getDefaultState() : AllBlocks.COGWHEEL.getDefaultState(), be);
    }

    @Override
    public Class<SimpleKineticBlockEntity> getBlockEntityClass() {
        return SimpleKineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SimpleKineticBlockEntity> getBlockEntityType() {
        return this.isLarge ? (BlockEntityType) AllBlockEntityTypes.ENCASED_LARGE_COGWHEEL.get() : (BlockEntityType) AllBlockEntityTypes.ENCASED_COGWHEEL.get();
    }

    @Override
    public Block getCasing() {
        return (Block) this.casing.get();
    }

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        BlockState encasedState = (BlockState) this.m_49966_().m_61124_(AXIS, (Direction.Axis) state.m_61143_(AXIS));
        for (Direction d : Iterate.directionsInAxis((Direction.Axis) state.m_61143_(AXIS))) {
            BlockState adjacentState = level.getBlockState(pos.relative(d));
            if (adjacentState.m_60734_() instanceof IRotate) {
                IRotate def = (IRotate) adjacentState.m_60734_();
                if (def.hasShaftTowards(level, pos.relative(d), adjacentState, d.getOpposite())) {
                    encasedState = (BlockState) encasedState.m_61122_(d.getAxisDirection() == Direction.AxisDirection.POSITIVE ? TOP_SHAFT : BOTTOM_SHAFT);
                }
            }
        }
        KineticBlockEntity.switchToBlockState(level, pos, encasedState);
    }
}
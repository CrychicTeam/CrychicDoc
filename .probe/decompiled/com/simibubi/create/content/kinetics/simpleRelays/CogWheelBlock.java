package com.simibubi.create.content.kinetics.simpleRelays;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Iterate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CogWheelBlock extends AbstractSimpleShaftBlock implements ICogWheel, EncasableBlock {

    boolean isLarge;

    protected CogWheelBlock(boolean large, BlockBehaviour.Properties properties) {
        super(properties);
        this.isLarge = large;
    }

    public static CogWheelBlock small(BlockBehaviour.Properties properties) {
        return new CogWheelBlock(false, properties);
    }

    public static CogWheelBlock large(BlockBehaviour.Properties properties) {
        return new CogWheelBlock(true, properties);
    }

    @Override
    public boolean isLargeCog() {
        return this.isLarge;
    }

    @Override
    public boolean isSmallCog() {
        return !this.isLarge;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (this.isLarge ? AllShapes.LARGE_GEAR : AllShapes.SMALL_GEAR).get((Direction.Axis) state.m_61143_(AXIS));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, (Direction.Axis) state.m_61143_(AXIS));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.m_6402_(worldIn, pos, state, placer, stack);
        if (placer instanceof Player player) {
            this.triggerShiftingGearsAdvancement(worldIn, pos, state, player);
        }
    }

    protected void triggerShiftingGearsAdvancement(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player != null) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(AXIS);
            for (Direction.Axis perpendicular1 : Iterate.axes) {
                if (perpendicular1 != axis) {
                    Direction d1 = Direction.get(Direction.AxisDirection.POSITIVE, perpendicular1);
                    for (Direction.Axis perpendicular2 : Iterate.axes) {
                        if (perpendicular1 != perpendicular2 && axis != perpendicular2) {
                            Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, perpendicular2);
                            for (int offset1 : Iterate.positiveAndNegative) {
                                for (int offset2 : Iterate.positiveAndNegative) {
                                    BlockPos connectedPos = pos.relative(d1, offset1).relative(d2, offset2);
                                    BlockState blockState = world.getBlockState(connectedPos);
                                    if (blockState.m_60734_() instanceof CogWheelBlock && blockState.m_61143_(AXIS) == axis && ICogWheel.isLargeCog(blockState) != this.isLarge) {
                                        AllAdvancements.COGS.awardTo(player);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(hand);
            InteractionResult result = this.tryEncase(state, world, pos, heldItem, player, hand, ray);
            return result.consumesAction() ? result : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static boolean isValidCogwheelPosition(boolean large, LevelReader worldIn, BlockPos pos, Direction.Axis cogAxis) {
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() != cogAxis) {
                BlockPos offsetPos = pos.relative(facing);
                BlockState blockState = worldIn.m_8055_(offsetPos);
                if ((!blockState.m_61138_(AXIS) || facing.getAxis() != blockState.m_61143_(AXIS)) && (ICogWheel.isLargeCog(blockState) || large && ICogWheel.isSmallCog(blockState))) {
                    return false;
                }
            }
        }
        return true;
    }

    protected Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
            return context.m_43719_().getAxis();
        } else {
            Level world = context.m_43725_();
            BlockState stateBelow = world.getBlockState(context.getClickedPos().below());
            if (AllBlocks.ROTATION_SPEED_CONTROLLER.has(stateBelow) && this.isLargeCog()) {
                return stateBelow.m_61143_(SpeedControllerBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            } else {
                BlockPos placedOnPos = context.getClickedPos().relative(context.m_43719_().getOpposite());
                BlockState placedAgainst = world.getBlockState(placedOnPos);
                Block block = placedAgainst.m_60734_();
                if (ICogWheel.isSmallCog(placedAgainst)) {
                    return ((IRotate) block).getRotationAxis(placedAgainst);
                } else {
                    Direction.Axis preferredAxis = getPreferredAxis(context);
                    return preferredAxis != null ? preferredAxis : context.m_43719_().getAxis();
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean shouldWaterlog = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(AXIS, this.getAxisForPlacement(context))).m_61124_(BlockStateProperties.WATERLOGGED, shouldWaterlog);
    }

    @Override
    public float getParticleTargetRadius() {
        return this.isLargeCog() ? 1.125F : 0.65F;
    }

    @Override
    public float getParticleInitialRadius() {
        return this.isLargeCog() ? 1.0F : 0.75F;
    }

    @Override
    public boolean isDedicatedCogWheel() {
        return true;
    }
}
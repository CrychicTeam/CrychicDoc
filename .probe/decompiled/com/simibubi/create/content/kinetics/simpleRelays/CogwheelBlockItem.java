package com.simibubi.create.content.kinetics.simpleRelays;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CogwheelBlockItem extends BlockItem {

    boolean large;

    private final int placementHelperId;

    private final int integratedCogHelperId;

    public CogwheelBlockItem(CogWheelBlock block, Item.Properties builder) {
        super(block, builder);
        this.large = block.isLarge;
        this.placementHelperId = PlacementHelpers.register((IPlacementHelper) (this.large ? new CogwheelBlockItem.LargeCogHelper() : new CogwheelBlockItem.SmallCogHelper()));
        this.integratedCogHelperId = PlacementHelpers.register((IPlacementHelper) (this.large ? new CogwheelBlockItem.IntegratedLargeCogHelper() : new CogwheelBlockItem.IntegratedSmallCogHelper()));
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        IPlacementHelper helper = PlacementHelpers.get(this.placementHelperId);
        Player player = context.getPlayer();
        BlockHitResult ray = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, true);
        if (helper.matchesState(state) && player != null && !player.m_6144_()) {
            return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, context.getHand(), ray);
        } else {
            if (this.integratedCogHelperId != -1) {
                helper = PlacementHelpers.get(this.integratedCogHelperId);
                if (helper.matchesState(state) && player != null && !player.m_6144_()) {
                    return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, context.getHand(), ray);
                }
            }
            return super.onItemUseFirst(stack, context);
        }
    }

    @MethodsReturnNonnullByDefault
    public abstract static class DiagonalCogHelper implements IPlacementHelper {

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> ICogWheel.isSmallCog(s) || ICogWheel.isLargeCog(s);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction.Axis axis = ((IRotate) state.m_60734_()).getRotationAxis(state);
            Direction closest = (Direction) IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), axis).get(0);
            for (Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), axis, d -> d.getAxis() != closest.getAxis())) {
                BlockPos newPos = pos.relative(dir).relative(closest);
                if (world.getBlockState(newPos).m_247087_() && CogWheelBlock.isValidCogwheelPosition(ICogWheel.isLargeCog(state), world, newPos, axis)) {
                    return PlacementOffset.success(newPos, s -> (BlockState) s.m_61124_(RotatedPillarKineticBlock.AXIS, axis));
                }
            }
            return PlacementOffset.fail();
        }

        protected boolean hitOnShaft(BlockState state, BlockHitResult ray) {
            return AllShapes.SIX_VOXEL_POLE.get(((IRotate) state.m_60734_()).getRotationAxis(state)).bounds().inflate(0.001).contains(ray.m_82450_().subtract(ray.m_82450_().align(Iterate.axisSet)));
        }
    }

    @MethodsReturnNonnullByDefault
    public static class IntegratedLargeCogHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (ICogWheel::isLargeCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> !ICogWheel.isDedicatedCogWheel(s.m_60734_()) && ICogWheel.isSmallCog(s);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction face = ray.getDirection();
            Direction.Axis newAxis;
            if (state.m_61138_(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                newAxis = ((Direction) state.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)).getAxis();
            } else if (state.m_61138_(DirectionalKineticBlock.FACING)) {
                newAxis = ((Direction) state.m_61143_(DirectionalKineticBlock.FACING)).getAxis();
            } else if (state.m_61138_(RotatedPillarKineticBlock.AXIS)) {
                newAxis = (Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS);
            } else {
                newAxis = Direction.Axis.Y;
            }
            if (face.getAxis() == newAxis) {
                return PlacementOffset.fail();
            } else {
                for (Direction d : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), face.getAxis(), newAxis)) {
                    BlockPos newPos = pos.relative(face).relative(d);
                    if (world.getBlockState(newPos).m_247087_()) {
                        if (!CogWheelBlock.isValidCogwheelPosition(false, world, newPos, newAxis)) {
                            return PlacementOffset.fail();
                        }
                        return PlacementOffset.success(newPos, s -> (BlockState) s.m_61124_(CogWheelBlock.AXIS, newAxis));
                    }
                }
                return PlacementOffset.fail();
            }
        }
    }

    @MethodsReturnNonnullByDefault
    public static class IntegratedSmallCogHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (ICogWheel::isSmallCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> !ICogWheel.isDedicatedCogWheel(s.m_60734_()) && ICogWheel.isSmallCog(s);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction face = ray.getDirection();
            Direction.Axis newAxis;
            if (state.m_61138_(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                newAxis = ((Direction) state.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)).getAxis();
            } else if (state.m_61138_(DirectionalKineticBlock.FACING)) {
                newAxis = ((Direction) state.m_61143_(DirectionalKineticBlock.FACING)).getAxis();
            } else if (state.m_61138_(RotatedPillarKineticBlock.AXIS)) {
                newAxis = (Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS);
            } else {
                newAxis = Direction.Axis.Y;
            }
            if (face.getAxis() == newAxis) {
                return PlacementOffset.fail();
            } else {
                for (Direction d : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), newAxis)) {
                    BlockPos newPos = pos.relative(d);
                    if (world.getBlockState(newPos).m_247087_()) {
                        if (!CogWheelBlock.isValidCogwheelPosition(false, world, newPos, newAxis)) {
                            return PlacementOffset.fail();
                        }
                        return PlacementOffset.success().at(newPos).withTransform(s -> (BlockState) s.m_61124_(CogWheelBlock.AXIS, newAxis));
                    }
                }
                return PlacementOffset.fail();
            }
        }
    }

    @MethodsReturnNonnullByDefault
    private static class LargeCogHelper extends CogwheelBlockItem.DiagonalCogHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (ICogWheel::isLargeCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (this.hitOnShaft(state, ray)) {
                return PlacementOffset.fail();
            } else if (ICogWheel.isLargeCog(state)) {
                Direction.Axis axis = ((IRotate) state.m_60734_()).getRotationAxis(state);
                Direction side = (Direction) IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.m_82450_(), axis).get(0);
                for (Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), axis)) {
                    BlockPos newPos = pos.relative(dir).relative(side);
                    if (CogWheelBlock.isValidCogwheelPosition(true, world, newPos, dir.getAxis()) && world.getBlockState(newPos).m_247087_()) {
                        return PlacementOffset.success(newPos, s -> (BlockState) s.m_61124_(RotatedPillarKineticBlock.AXIS, dir.getAxis()));
                    }
                }
                return PlacementOffset.fail();
            } else {
                return super.getOffset(player, world, state, pos, ray);
            }
        }
    }

    @MethodsReturnNonnullByDefault
    private static class SmallCogHelper extends CogwheelBlockItem.DiagonalCogHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (ICogWheel::isSmallCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (this.hitOnShaft(state, ray)) {
                return PlacementOffset.fail();
            } else if (!ICogWheel.isLargeCog(state)) {
                Direction.Axis axis = ((IRotate) state.m_60734_()).getRotationAxis(state);
                for (Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), axis)) {
                    BlockPos newPos = pos.relative(dir);
                    if (CogWheelBlock.isValidCogwheelPosition(false, world, newPos, axis) && world.getBlockState(newPos).m_247087_()) {
                        return PlacementOffset.success(newPos, s -> (BlockState) s.m_61124_(RotatedPillarKineticBlock.AXIS, axis));
                    }
                }
                return PlacementOffset.fail();
            } else {
                return super.getOffset(player, world, state, pos, ray);
            }
        }
    }
}
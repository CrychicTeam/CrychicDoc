package com.simibubi.create.content.kinetics.speedController;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpeedControllerBlock extends HorizontalAxisKineticBlock implements IBE<SpeedControllerBlockEntity> {

    private static final int placementHelperId = PlacementHelpers.register(new SpeedControllerBlock.PlacementHelper());

    public SpeedControllerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState above = context.m_43725_().getBlockState(context.getClickedPos().above());
        return ICogWheel.isLargeCog(above) && ((Direction.Axis) above.m_61143_(CogWheelBlock.AXIS)).isHorizontal() ? (BlockState) this.m_49966_().m_61124_(HORIZONTAL_AXIS, above.m_61143_(CogWheelBlock.AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X) : super.getStateForPlacement(context);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos neighbourPos, boolean p_220069_6_) {
        if (neighbourPos.equals(pos.above())) {
            this.withBlockEntityDo(world, pos, SpeedControllerBlockEntity::updateBracket);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack heldItem = player.m_21120_(hand);
        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        return helper.matchesItem(heldItem) ? helper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray) : InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.SPEED_CONTROLLER;
    }

    @Override
    public Class<SpeedControllerBlockEntity> getBlockEntityClass() {
        return SpeedControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpeedControllerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SpeedControllerBlockEntity>) AllBlockEntityTypes.ROTATION_SPEED_CONTROLLER.get();
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (ICogWheel::isLargeCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return AllBlocks.ROTATION_SPEED_CONTROLLER::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            BlockPos newPos = pos.above();
            if (!world.getBlockState(newPos).m_247087_()) {
                return PlacementOffset.fail();
            } else {
                Direction.Axis newAxis = state.m_61143_(HorizontalAxisKineticBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                return !CogWheelBlock.isValidCogwheelPosition(true, world, newPos, newAxis) ? PlacementOffset.fail() : PlacementOffset.success(newPos, s -> (BlockState) s.m_61124_(CogWheelBlock.AXIS, newAxis));
            }
        }
    }
}
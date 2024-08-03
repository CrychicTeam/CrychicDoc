package com.simibubi.create.content.kinetics.simpleRelays;

import com.google.common.base.Predicates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShaftBlock extends AbstractSimpleShaftBlock implements EncasableBlock {

    public static final int placementHelperId = PlacementHelpers.register(new ShaftBlock.PlacementHelper());

    public ShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static boolean isShaft(BlockState state) {
        return AllBlocks.SHAFT.has(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.m_5573_(context);
        return pickCorrectShaftType(stateForPlacement, context.m_43725_(), context.getClickedPos());
    }

    public static BlockState pickCorrectShaftType(BlockState stateForPlacement, Level level, BlockPos pos) {
        return PoweredShaftBlock.stillValid(stateForPlacement, level, pos) ? PoweredShaftBlock.getEquivalent(stateForPlacement) : stateForPlacement;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.SIX_VOXEL_POLE.get((Direction.Axis) state.m_61143_(AXIS));
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.35F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.125F;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(hand);
            InteractionResult result = this.tryEncase(state, world, pos, heldItem, player, hand, ray);
            if (result.consumesAction()) {
                return result;
            } else if (AllBlocks.METAL_GIRDER.isIn(heldItem) && state.m_61143_(AXIS) != Direction.Axis.Y) {
                KineticBlockEntity.switchToBlockState(world, pos, (BlockState) ((BlockState) AllBlocks.METAL_GIRDER_ENCASED_SHAFT.getDefaultState().m_61124_(WATERLOGGED, (Boolean) state.m_61143_(WATERLOGGED))).m_61124_(GirderEncasedShaftBlock.HORIZONTAL_AXIS, state.m_61143_(AXIS) == Direction.Axis.Z ? Direction.Axis.Z : Direction.Axis.X));
                if (!world.isClientSide && !player.isCreative()) {
                    heldItem.shrink(1);
                    if (heldItem.isEmpty()) {
                        player.m_21008_(hand, ItemStack.EMPTY);
                    }
                }
                return InteractionResult.SUCCESS;
            } else {
                IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
                return helper.matchesItem(heldItem) ? helper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray) : InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper extends PoleHelper<Direction.Axis> {

        private PlacementHelper() {
            super(state -> state.m_60734_() instanceof AbstractSimpleShaftBlock || state.m_60734_() instanceof PoweredShaftBlock, state -> (Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS), RotatedPillarKineticBlock.AXIS);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem) i.getItem()).getBlock() instanceof AbstractSimpleShaftBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return Predicates.or(AllBlocks.SHAFT::has, AllBlocks.POWERED_SHAFT::has);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
            if (offset.isSuccessful()) {
                offset.withTransform(offset.getTransform().andThen(s -> ShaftBlock.pickCorrectShaftType(s, world, offset.getBlockPos())));
            }
            return offset;
        }
    }
}
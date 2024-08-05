package com.simibubi.create.content.decoration.girder;

import com.google.common.base.Predicates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;

public class GirderPlacementHelper implements IPlacementHelper {

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return AllBlocks.METAL_GIRDER::isIn;
    }

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return Predicates.or(AllBlocks.METAL_GIRDER::has, AllBlocks.METAL_GIRDER_ENCASED_SHAFT::has);
    }

    private boolean canExtendToward(BlockState state, Direction side) {
        Direction.Axis axis = side.getAxis();
        if (state.m_60734_() instanceof GirderBlock) {
            boolean x = (Boolean) state.m_61143_(GirderBlock.X);
            boolean z = (Boolean) state.m_61143_(GirderBlock.Z);
            if (!x && !z) {
                return axis == Direction.Axis.Y;
            } else {
                return x && z ? true : axis == (x ? Direction.Axis.X : Direction.Axis.Z);
            }
        } else {
            return !(state.m_60734_() instanceof GirderEncasedShaftBlock) ? false : axis != Direction.Axis.Y && axis != state.m_61143_(GirderEncasedShaftBlock.HORIZONTAL_AXIS);
        }
    }

    private int attachedPoles(Level world, BlockPos pos, Direction direction) {
        BlockPos checkPos = pos.relative(direction);
        BlockState state = world.getBlockState(checkPos);
        int count;
        for (count = 0; this.canExtendToward(state, direction); state = world.getBlockState(checkPos)) {
            count++;
            checkPos = checkPos.relative(direction);
        }
        return count;
    }

    private BlockState withAxis(BlockState state, Direction.Axis axis) {
        if (state.m_60734_() instanceof GirderBlock) {
            return (BlockState) ((BlockState) ((BlockState) state.m_61124_(GirderBlock.X, axis == Direction.Axis.X)).m_61124_(GirderBlock.Z, axis == Direction.Axis.Z)).m_61124_(GirderBlock.AXIS, axis);
        } else {
            return state.m_60734_() instanceof GirderEncasedShaftBlock && axis.isHorizontal() ? (BlockState) state.m_61124_(GirderEncasedShaftBlock.HORIZONTAL_AXIS, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X) : state;
        }
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        for (Direction dir : IPlacementHelper.orderedByDistance(pos, ray.m_82450_(), (Predicate<Direction>) (dirx -> this.canExtendToward(state, dirx)))) {
            int range = AllConfigs.server().equipment.placementAssistRange.get();
            if (player != null) {
                AttributeInstance reach = player.m_21051_(ForgeMod.BLOCK_REACH.get());
                if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier)) {
                    range += 4;
                }
            }
            int poles = this.attachedPoles(world, pos, dir);
            if (poles < range) {
                BlockPos newPos = pos.relative(dir, poles + 1);
                BlockState newState = world.getBlockState(newPos);
                if (newState.m_247087_()) {
                    return PlacementOffset.success(newPos, bState -> Block.updateFromNeighbourShapes(this.withAxis(bState, dir.getAxis()), world, newPos));
                }
            }
        }
        return PlacementOffset.fail();
    }
}
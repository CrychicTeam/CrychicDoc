package com.simibubi.create.foundation.placement;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;

@MethodsReturnNonnullByDefault
public abstract class PoleHelper<T extends Comparable<T>> implements IPlacementHelper {

    protected final Predicate<BlockState> statePredicate;

    protected final Property<T> property;

    protected final Function<BlockState, Direction.Axis> axisFunction;

    public PoleHelper(Predicate<BlockState> statePredicate, Function<BlockState, Direction.Axis> axisFunction, Property<T> property) {
        this.statePredicate = statePredicate;
        this.axisFunction = axisFunction;
        this.property = property;
    }

    public boolean matchesAxis(BlockState state, Direction.Axis axis) {
        return !this.statePredicate.test(state) ? false : this.axisFunction.apply(state) == axis;
    }

    public int attachedPoles(Level world, BlockPos pos, Direction direction) {
        BlockPos checkPos = pos.relative(direction);
        BlockState state = world.getBlockState(checkPos);
        int count;
        for (count = 0; this.matchesAxis(state, direction.getAxis()); state = world.getBlockState(checkPos)) {
            count++;
            checkPos = checkPos.relative(direction);
        }
        return count;
    }

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return this.statePredicate;
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        for (Direction dir : IPlacementHelper.orderedByDistance(pos, ray.m_82450_(), (Predicate<Direction>) (dirx -> dirx.getAxis() == this.axisFunction.apply(state)))) {
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
                    return PlacementOffset.success(newPos, bState -> (BlockState) bState.m_61124_(this.property, state.m_61143_(this.property)));
                }
            }
        }
        return PlacementOffset.fail();
    }
}
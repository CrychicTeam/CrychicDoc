package vazkii.patchouli.common.multiblock;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.TriPredicate;

public class StateMatcher implements IStateMatcher {

    public static final StateMatcher ANY = displayOnly(Blocks.AIR.defaultBlockState());

    public static final StateMatcher AIR = fromPredicate(Blocks.AIR.defaultBlockState(), (TriPredicate<BlockGetter, BlockPos, BlockState>) ((w, p, s) -> s.m_60795_()));

    private final BlockState displayState;

    private final TriPredicate<BlockGetter, BlockPos, BlockState> statePredicate;

    private StateMatcher(BlockState displayState, TriPredicate<BlockGetter, BlockPos, BlockState> statePredicate) {
        this.displayState = displayState;
        this.statePredicate = statePredicate;
    }

    public static StateMatcher fromPredicate(BlockState display, Predicate<BlockState> predicate) {
        return new StateMatcher(display, (world, pos, state) -> predicate.test(state));
    }

    public static StateMatcher fromPredicate(Block display, Predicate<BlockState> predicate) {
        return fromPredicate(display.defaultBlockState(), predicate);
    }

    public static StateMatcher fromPredicate(BlockState display, TriPredicate<BlockGetter, BlockPos, BlockState> predicate) {
        return new StateMatcher(display, predicate);
    }

    public static StateMatcher fromPredicate(Block display, TriPredicate<BlockGetter, BlockPos, BlockState> predicate) {
        return new StateMatcher(display.defaultBlockState(), predicate);
    }

    public static StateMatcher fromState(BlockState displayState, boolean strict) {
        return fromPredicate(displayState, strict ? state -> state == displayState : state -> state.m_60734_() == displayState.m_60734_());
    }

    public static StateMatcher fromStateWithFilter(BlockState state, Predicate<Property<?>> filter) {
        return fromPredicate(state, state1 -> state.m_60734_() != state1.m_60734_() ? false : state1.m_61147_().stream().filter(filter).allMatch(property -> state1.m_61138_(property) && state.m_61138_(property) && Objects.equals(state.m_61143_(property), state1.m_61143_(property))));
    }

    public static StateMatcher fromState(BlockState displayState) {
        return fromState(displayState, true);
    }

    public static StateMatcher fromBlockLoose(Block block) {
        return fromState(block.defaultBlockState(), false);
    }

    public static StateMatcher fromBlockStrict(Block block) {
        return fromState(block.defaultBlockState(), true);
    }

    public static StateMatcher displayOnly(BlockState state) {
        return new StateMatcher(state, (w, p, s) -> true);
    }

    public static StateMatcher displayOnly(Block block) {
        return displayOnly(block.defaultBlockState());
    }

    @Override
    public BlockState getDisplayedState(long ticks) {
        return this.displayState;
    }

    @Override
    public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
        return this.statePredicate;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            StateMatcher that = (StateMatcher) o;
            return this.statePredicate.equals(that.statePredicate);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.statePredicate });
    }
}
package vazkii.patchouli.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IStateMatcher {

    BlockState getDisplayedState(long var1);

    TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate();
}
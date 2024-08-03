package org.violetmoon.zeta.api;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public interface IIndirectConnector {

    List<Pair<Predicate<BlockState>, IIndirectConnector>> INDIRECT_STICKY_BLOCKS = new LinkedList();

    default boolean isEnabled() {
        return true;
    }

    default IConditionalSticky getStickyCondition() {
        return (w, pp, op, sp, os, ss, d) -> this.canConnectIndirectly(w, op, sp, os, ss);
    }

    boolean canConnectIndirectly(Level var1, BlockPos var2, BlockPos var3, BlockState var4, BlockState var5);
}
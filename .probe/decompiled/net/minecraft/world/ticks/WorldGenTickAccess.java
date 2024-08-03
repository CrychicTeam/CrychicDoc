package net.minecraft.world.ticks;

import java.util.function.Function;
import net.minecraft.core.BlockPos;

public class WorldGenTickAccess<T> implements LevelTickAccess<T> {

    private final Function<BlockPos, TickContainerAccess<T>> containerGetter;

    public WorldGenTickAccess(Function<BlockPos, TickContainerAccess<T>> functionBlockPosTickContainerAccessT0) {
        this.containerGetter = functionBlockPosTickContainerAccessT0;
    }

    @Override
    public boolean hasScheduledTick(BlockPos blockPos0, T t1) {
        return ((TickContainerAccess) this.containerGetter.apply(blockPos0)).m_183582_(blockPos0, t1);
    }

    @Override
    public void schedule(ScheduledTick<T> scheduledTickT0) {
        ((TickContainerAccess) this.containerGetter.apply(scheduledTickT0.pos())).m_183393_(scheduledTickT0);
    }

    @Override
    public boolean willTickThisTick(BlockPos blockPos0, T t1) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }
}
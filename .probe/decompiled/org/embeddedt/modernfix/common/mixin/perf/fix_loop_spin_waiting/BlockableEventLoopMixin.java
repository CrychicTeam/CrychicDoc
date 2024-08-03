package org.embeddedt.modernfix.common.mixin.perf.fix_loop_spin_waiting;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = { BlockableEventLoop.class }, priority = 500)
public class BlockableEventLoopMixin {

    private static final long MFIX$TICK_WAIT_TIME = TimeUnit.MILLISECONDS.toNanos(2L);

    @Overwrite
    protected void waitForTasks() {
        LockSupport.parkNanos("waiting for tasks", MFIX$TICK_WAIT_TIME);
    }
}
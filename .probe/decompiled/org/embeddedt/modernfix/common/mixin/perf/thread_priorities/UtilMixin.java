package org.embeddedt.modernfix.common.mixin.perf.thread_priorities;

import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ Util.class })
public class UtilMixin {

    @ModifyArg(method = { "makeExecutor" }, at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ForkJoinPool;<init>(ILjava/util/concurrent/ForkJoinPool$ForkJoinWorkerThreadFactory;Ljava/lang/Thread$UncaughtExceptionHandler;Z)V"), index = 1)
    private static ForkJoinWorkerThreadFactory adjustPriorityOfThreadFactory(ForkJoinWorkerThreadFactory factory) {
        return pool -> {
            ForkJoinWorkerThread thread = factory.newThread(pool);
            int pri = 4;
            thread.setPriority(pri);
            return thread;
        };
    }
}
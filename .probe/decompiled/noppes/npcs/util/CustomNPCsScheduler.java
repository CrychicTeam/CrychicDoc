package noppes.npcs.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomNPCsScheduler {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void runTack(Runnable task, int delay) {
        executor.schedule(task, (long) delay, TimeUnit.MILLISECONDS);
    }

    public static void runTack(Runnable task) {
        executor.schedule(task, 0L, TimeUnit.MILLISECONDS);
    }
}
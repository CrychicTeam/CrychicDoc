package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import com.github.alexthe666.citadel.Citadel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

public final class Pathfinding {

    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque();

    private static ThreadPoolExecutor executor;

    private Pathfinding() {
    }

    public static boolean isDebug() {
        return PathfindingConstants.isDebugMode;
    }

    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, PathfindingConstants.pathfindingThreads, 10L, TimeUnit.SECONDS, jobQueue, new Pathfinding.CitadelThreadFactory());
        }
        return executor;
    }

    public static class CitadelThreadFactory implements ThreadFactory {

        public static int id;

        public Thread newThread(@NotNull Runnable runnable) throws RuntimeException {
            BlockableEventLoop<?> workqueue = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            ClassLoader classLoader;
            if (workqueue.isSameThread()) {
                classLoader = Thread.currentThread().getContextClassLoader();
            } else if (workqueue instanceof MinecraftServer server) {
                classLoader = server.getRunningThread().getContextClassLoader();
            } else {
                classLoader = (ClassLoader) CompletableFuture.supplyAsync(() -> Thread.currentThread().getContextClassLoader(), workqueue).orTimeout(10L, TimeUnit.SECONDS).exceptionally(ex -> {
                    throw new RuntimeException(String.format("Couldn't join threads within timeout range. Tried joining '%s' on '%s'", Thread.currentThread().getName(), workqueue.name()));
                }).join();
            }
            Thread thread = new Thread(runnable, "Citadel Pathfinding Worker #" + id++);
            thread.setDaemon(true);
            thread.setPriority(10);
            if (thread.getContextClassLoader() != classLoader) {
                Citadel.LOGGER.info("Corrected CCL of new Citadel Pathfinding Thread, was: " + thread.getContextClassLoader().toString());
                thread.setContextClassLoader(classLoader);
            }
            thread.setUncaughtExceptionHandler((thread1, throwable) -> Citadel.LOGGER.error("Citadel Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
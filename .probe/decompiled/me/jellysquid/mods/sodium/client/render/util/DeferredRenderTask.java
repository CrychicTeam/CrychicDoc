package me.jellysquid.mods.sodium.client.render.util;

import java.util.concurrent.ConcurrentLinkedDeque;

public class DeferredRenderTask {

    private static final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque();

    public static void schedule(Runnable runnable) {
        queue.add(runnable);
    }

    public static void runAll() {
        RenderAsserts.validateCurrentThread();
        Runnable runnable;
        while ((runnable = (Runnable) queue.poll()) != null) {
            try {
                runnable.run();
            } catch (Throwable var2) {
                throw new RuntimeException("Failed to execute deferred render task", var2);
            }
        }
    }
}
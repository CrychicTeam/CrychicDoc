package io.github.apace100.origins.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Scheduler {

    private final Int2ObjectMap<List<Consumer<MinecraftServer>>> taskQueue = new Int2ObjectOpenHashMap();

    private int currentTick = 0;

    public Scheduler() {
        MinecraftForge.EVENT_BUS.addListener(event -> {
            if (event.phase == TickEvent.Phase.END) {
                MinecraftServer m = ServerLifecycleHooks.getCurrentServer();
                this.currentTick = m.getTickCount();
                List<Consumer<MinecraftServer>> runnables = (List<Consumer<MinecraftServer>>) this.taskQueue.remove(this.currentTick);
                if (runnables != null) {
                    for (Consumer<MinecraftServer> runnable : runnables) {
                        runnable.accept(m);
                        if (runnable instanceof Scheduler.Repeating) {
                            Scheduler.Repeating repeating = (Scheduler.Repeating) runnable;
                            if (repeating.shouldQueue(this.currentTick)) {
                                this.queue(runnable, repeating.next);
                            }
                        }
                    }
                }
            }
        });
    }

    public void queue(Consumer<MinecraftServer> task, int tick) {
        ((List) this.taskQueue.computeIfAbsent(this.currentTick + tick + 1, t -> new ArrayList())).add(task);
    }

    public void repeating(Consumer<MinecraftServer> task, int tick, int interval) {
        this.repeatWhile(task, null, tick, interval);
    }

    public void repeatWhile(Consumer<MinecraftServer> task, IntPredicate requeue, int tick, int interval) {
        this.queue(new Scheduler.Repeating(task, requeue, interval), tick);
    }

    public void repeatN(Consumer<MinecraftServer> task, final int times, int tick, int interval) {
        this.repeatWhile(task, new IntPredicate() {

            private int remaining = times;

            public boolean test(int value) {
                return this.remaining-- > 0;
            }
        }, tick, interval);
    }

    private static final class Repeating implements Consumer<MinecraftServer> {

        private final Consumer<MinecraftServer> task;

        private final IntPredicate requeue;

        public final int next;

        private Repeating(Consumer<MinecraftServer> task, IntPredicate requeue, int interval) {
            this.task = task;
            this.requeue = requeue;
            this.next = interval;
        }

        public boolean shouldQueue(int predicate) {
            return this.requeue == null ? true : this.requeue.test(predicate);
        }

        public void accept(MinecraftServer server) {
            this.task.accept(server);
        }
    }
}
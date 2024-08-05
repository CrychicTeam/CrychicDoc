package me.lucko.spark.common.tick;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractTickHook implements TickHook {

    private final Set<TickHook.Callback> tasks = new CopyOnWriteArraySet();

    private int tick = 0;

    protected void onTick() {
        for (TickHook.Callback r : this.tasks) {
            r.onTick(this.tick);
        }
        this.tick++;
    }

    @Override
    public int getCurrentTick() {
        return this.tick;
    }

    @Override
    public void addCallback(TickHook.Callback runnable) {
        this.tasks.add(runnable);
    }

    @Override
    public void removeCallback(TickHook.Callback runnable) {
        this.tasks.remove(runnable);
    }
}
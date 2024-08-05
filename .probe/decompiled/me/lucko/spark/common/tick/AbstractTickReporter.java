package me.lucko.spark.common.tick;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractTickReporter implements TickReporter {

    private final Set<TickReporter.Callback> tasks = new CopyOnWriteArraySet();

    protected void onTick(double duration) {
        for (TickReporter.Callback r : this.tasks) {
            r.onTick(duration);
        }
    }

    @Override
    public void addCallback(TickReporter.Callback runnable) {
        this.tasks.add(runnable);
    }

    @Override
    public void removeCallback(TickReporter.Callback runnable) {
        this.tasks.remove(runnable);
    }
}
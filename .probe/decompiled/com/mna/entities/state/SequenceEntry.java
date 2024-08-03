package com.mna.entities.state;

import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;

public class SequenceEntry<T extends Entity> {

    private int delay;

    private Runnable completeCallback;

    private Runnable startCallback;

    private Consumer<Integer> tickCallback;

    public SequenceEntry(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public SequenceEntry<T> onComplete(Runnable completeCallback) {
        this.completeCallback = completeCallback;
        return this;
    }

    public SequenceEntry<T> onStart(Runnable startCallback) {
        this.startCallback = startCallback;
        return this;
    }

    public SequenceEntry<T> onTick(Consumer<Integer> tickCallback) {
        this.tickCallback = tickCallback;
        return this;
    }

    public void start() {
        if (this.startCallback != null) {
            this.startCallback.run();
        }
    }

    public void tick(int count) {
        if (this.tickCallback != null) {
            this.tickCallback.accept(count);
        }
    }

    public void complete() {
        if (this.completeCallback != null) {
            this.completeCallback.run();
        }
    }
}
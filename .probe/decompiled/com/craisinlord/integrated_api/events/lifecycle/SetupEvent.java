package com.craisinlord.integrated_api.events.lifecycle;

import com.craisinlord.integrated_api.events.base.EventHandler;
import java.util.function.Consumer;

public record SetupEvent(Consumer<Runnable> enqueue) {

    public static final EventHandler<SetupEvent> EVENT = new EventHandler<>();

    public void enqueueWork(Runnable runnable) {
        this.enqueue.accept(runnable);
    }
}
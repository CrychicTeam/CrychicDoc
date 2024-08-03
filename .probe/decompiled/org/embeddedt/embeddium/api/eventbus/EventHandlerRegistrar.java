package org.embeddedt.embeddium.api.eventbus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraftforge.common.MinecraftForge;

public class EventHandlerRegistrar<T extends EmbeddiumEvent> {

    private final List<EventHandlerRegistrar.Handler<T>> handlerList = new CopyOnWriteArrayList();

    public void addListener(EventHandlerRegistrar.Handler<T> listener) {
        this.handlerList.add(listener);
    }

    public boolean post(T event) {
        boolean canceled = false;
        if (!this.handlerList.isEmpty()) {
            boolean isCancelable = event.isCancelable();
            for (EventHandlerRegistrar.Handler<T> handler : this.handlerList) {
                handler.acceptEvent(event);
                if (isCancelable && event.isCanceled()) {
                    canceled = true;
                }
            }
        }
        return canceled | postPlatformSpecificEvent(event);
    }

    private static <T extends EmbeddiumEvent> boolean postPlatformSpecificEvent(T event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @FunctionalInterface
    public interface Handler<T extends EmbeddiumEvent> {

        void acceptEvent(T var1);
    }
}
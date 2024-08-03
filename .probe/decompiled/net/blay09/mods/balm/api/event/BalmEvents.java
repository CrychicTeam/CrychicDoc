package net.blay09.mods.balm.api.event;

import java.util.function.Consumer;

public interface BalmEvents {

    default <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        this.onEvent(eventClass, handler, EventPriority.Normal);
    }

    <T> void onEvent(Class<T> var1, Consumer<T> var2, EventPriority var3);

    <T> void fireEvent(T var1);

    <T> void onTickEvent(TickType<T> var1, TickPhase var2, T var3);
}
package mezz.jei.forge.events;

import java.util.function.Consumer;
import mezz.jei.core.util.WeakConsumer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventSubscription<T extends Event> {

    private final IEventBus eventBus;

    private final Consumer<T> listener;

    private final WeakConsumer<T> registeredListener;

    public static <T extends Event> EventSubscription<T> register(IEventBus eventBus, Class<T> eventType, Consumer<T> listener) {
        return new EventSubscription<>(eventBus, eventType, listener);
    }

    private EventSubscription(IEventBus eventBus, Class<T> eventType, Consumer<T> listener) {
        this.eventBus = eventBus;
        this.listener = listener;
        WeakConsumer<T> weakListener = new WeakConsumer<>(listener);
        eventBus.addListener(EventPriority.NORMAL, false, eventType, weakListener);
        this.registeredListener = weakListener;
    }

    public void unregister() {
        this.eventBus.unregister(this.registeredListener);
    }
}
package mezz.jei.forge.events;

import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;

public class PermanentEventSubscriptions {

    private final IEventBus eventBus;

    private final IEventBus modEventBus;

    public PermanentEventSubscriptions(IEventBus eventBus, IEventBus modEventBus) {
        this.eventBus = eventBus;
        this.modEventBus = modEventBus;
    }

    public <T extends Event> void register(Class<T> eventType, Consumer<T> listener) {
        if (IModBusEvent.class.isAssignableFrom(eventType)) {
            this.modEventBus.addListener(EventPriority.NORMAL, false, eventType, listener);
        } else {
            this.eventBus.addListener(EventPriority.NORMAL, false, eventType, listener);
        }
    }
}
package dev.architectury.platform.forge;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public final class EventBuses {

    private static final Map<String, IEventBus> EVENT_BUS_MAP = Collections.synchronizedMap(new HashMap());

    private static final Multimap<String, Consumer<IEventBus>> ON_REGISTERED = Multimaps.synchronizedMultimap(LinkedListMultimap.create());

    private EventBuses() {
    }

    public static void registerModEventBus(String modId, IEventBus bus) {
        if (EVENT_BUS_MAP.putIfAbsent(modId, bus) != null) {
            throw new IllegalStateException("Can't register event bus for mod '" + modId + "' because it was previously registered!");
        } else {
            for (Consumer<IEventBus> consumer : ON_REGISTERED.get(modId)) {
                consumer.accept(bus);
            }
        }
    }

    public static void onRegistered(String modId, Consumer<IEventBus> busConsumer) {
        if (EVENT_BUS_MAP.containsKey(modId)) {
            busConsumer.accept((IEventBus) EVENT_BUS_MAP.get(modId));
        } else {
            ON_REGISTERED.put(modId, busConsumer);
        }
    }

    public static Optional<IEventBus> getModEventBus(String modId) {
        return Optional.ofNullable((IEventBus) EVENT_BUS_MAP.get(modId));
    }
}
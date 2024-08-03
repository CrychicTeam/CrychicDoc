package fuzs.puzzleslib.impl.event.core;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.impl.core.CommonFactories;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public final class EventInvokerImpl {

    private static final Map<Class<?>, EventInvokerImpl.EventInvokerLike<?>> EVENT_INVOKER_LOOKUP = Collections.synchronizedMap(Maps.newIdentityHashMap());

    private static final Queue<Runnable> DEFERRED_INVOKER_REGISTRATIONS = Queues.newConcurrentLinkedQueue();

    private static boolean initialized;

    private EventInvokerImpl() {
    }

    public static void initialize() {
        CommonFactories.INSTANCE.registerEventHandlers();
        initialized = true;
        while (!DEFERRED_INVOKER_REGISTRATIONS.isEmpty()) {
            ((Runnable) DEFERRED_INVOKER_REGISTRATIONS.poll()).run();
        }
    }

    public static <T> EventInvoker<T> softLookup(Class<T> clazz, @Nullable Object context) {
        Objects.requireNonNull(clazz, "type is null");
        Supplier<EventInvoker<T>> invoker = Suppliers.memoize(() -> lookup(clazz, context));
        return (phase, callback) -> {
            if (!initialized && !EVENT_INVOKER_LOOKUP.containsKey(clazz)) {
                DEFERRED_INVOKER_REGISTRATIONS.offer((Runnable) () -> ((EventInvoker) invoker.get()).register(phase, callback));
            } else {
                ((EventInvoker) invoker.get()).register(phase, callback);
            }
        };
    }

    private static <T> EventInvoker<T> lookup(Class<T> clazz, @Nullable Object context) {
        Objects.requireNonNull(clazz, "type is null");
        EventInvokerImpl.EventInvokerLike<T> invokerLike = (EventInvokerImpl.EventInvokerLike<T>) EVENT_INVOKER_LOOKUP.get(clazz);
        Objects.requireNonNull(invokerLike, "invoker for type %s is null".formatted(clazz));
        EventInvoker<T> invoker = invokerLike.asEventInvoker(context);
        Objects.requireNonNull(invoker, "invoker for type %s is null".formatted(clazz));
        return invoker;
    }

    public static <T> void register(Class<T> clazz, EventInvokerImpl.EventInvokerLike<T> invoker, boolean joinInvokers) {
        EventInvokerImpl.EventInvokerLike<T> other = (EventInvokerImpl.EventInvokerLike<T>) EVENT_INVOKER_LOOKUP.get(clazz);
        if (other != null) {
            if (!joinInvokers) {
                throw new IllegalArgumentException("duplicate event invoker for type %s".formatted(clazz));
            }
            invoker = join(invoker, other);
        }
        EVENT_INVOKER_LOOKUP.put(clazz, invoker);
    }

    private static <T> EventInvokerImpl.EventInvokerLike<T> join(EventInvokerImpl.EventInvokerLike<T> invoker, EventInvokerImpl.EventInvokerLike<T> other) {
        return context -> (phase, callback) -> {
            invoker.asEventInvoker(context).register(phase, callback);
            other.asEventInvoker(context).register(phase, callback);
        };
    }

    static {
        CommonFactories.INSTANCE.registerLoadingHandlers();
    }

    public interface EventInvokerLike<T> {

        EventInvoker<T> asEventInvoker(@Nullable Object var1);
    }
}
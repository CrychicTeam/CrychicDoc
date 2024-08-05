package fuzs.puzzleslib.api.event.v1.core;

import fuzs.puzzleslib.impl.event.ForgeEventInvokerRegistryImpl;
import java.util.function.BiConsumer;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public interface ForgeEventInvokerRegistry extends EventInvokerRegistry {

    ForgeEventInvokerRegistry INSTANCE = new ForgeEventInvokerRegistryImpl();

    default <T, E extends Event> void register(Class<T> clazz, Class<E> event, BiConsumer<T, E> converter) {
        this.register(clazz, event, (ForgeEventInvokerRegistry.ForgeEventContextConsumer<Object, Event>) ((callback, evt, context) -> converter.accept(callback, evt)), false);
    }

    default <T, E extends Event> void register(Class<T> clazz, Class<E> event, BiConsumer<T, E> converter, boolean joinInvokers) {
        this.register(clazz, event, (ForgeEventInvokerRegistry.ForgeEventContextConsumer<Object, Event>) ((callback, evt, context) -> converter.accept(callback, evt)), joinInvokers);
    }

    default <T, E extends Event> void register(Class<T> clazz, Class<E> event, ForgeEventInvokerRegistry.ForgeEventContextConsumer<T, E> converter) {
        this.register(clazz, event, converter, false);
    }

    <T, E extends Event> void register(Class<T> var1, Class<E> var2, ForgeEventInvokerRegistry.ForgeEventContextConsumer<T, E> var3, boolean var4);

    @FunctionalInterface
    public interface ForgeEventContextConsumer<T, E extends Event> {

        void accept(T var1, E var2, @Nullable Object var3);
    }
}
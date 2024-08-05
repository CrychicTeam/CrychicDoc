package org.violetmoon.zetaimplforge.event.load;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public class Test {

    private static final Map<Class<? extends IZetaLoadEvent>, Function<? extends Event, ? extends IZetaLoadEvent>> FORGE_TO_ZETA = Map.of(ForgeZAddReloadListener.class, ForgeZAddReloadListener::new);

    public static <T extends IZetaLoadEvent> Consumer<? extends Event> remap(Consumer<T> zetaEventConsumer, Class<T> cl) {
        Function<? extends Event, T> forgeToZeta = (Function<? extends Event, T>) FORGE_TO_ZETA.get(cl);
        return getEventConsumer(zetaEventConsumer, forgeToZeta);
    }

    @NotNull
    private static <T extends IZetaLoadEvent, E extends Event> Consumer<E> getEventConsumer(Consumer<T> zetaEventConsumer, Function<E, T> forgeToZeta) {
        return event -> zetaEventConsumer.accept((IZetaLoadEvent) forgeToZeta.apply(event));
    }

    public static class ExampleZetaBus {

        private EventBus forgeBus;

        public <T extends IZetaLoadEvent> void addListener(Consumer<T> zetaEventConsumer, Class<T> cl) {
            this.forgeBus.addListener(Test.remap(zetaEventConsumer, cl));
        }
    }
}
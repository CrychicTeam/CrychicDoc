package net.mehvahdjukaar.moonlight.api.events;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.events.forge.MoonlightEventsHelperImpl;

public class MoonlightEventsHelper {

    @ExpectPlatform
    @Transformed
    public static <T extends SimpleEvent> void addListener(Consumer<T> listener, Class<T> eventClass) {
        MoonlightEventsHelperImpl.addListener(listener, eventClass);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends SimpleEvent> void postEvent(T event, Class<T> eventClass) {
        MoonlightEventsHelperImpl.postEvent(event, eventClass);
    }
}
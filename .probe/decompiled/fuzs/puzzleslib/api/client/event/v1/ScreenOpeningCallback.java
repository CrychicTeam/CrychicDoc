package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ScreenOpeningCallback {

    EventInvoker<ScreenOpeningCallback> EVENT = EventInvoker.lookup(ScreenOpeningCallback.class);

    EventResult onScreenOpening(@Nullable Screen var1, DefaultedValue<Screen> var2);
}
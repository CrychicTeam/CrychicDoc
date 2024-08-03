package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

public final class ScreenEvents {

    @Deprecated(forRemoval = true)
    public static final EventInvoker<ScreenEvents.BeforeInit> BEFORE_INIT = EventInvoker.lookup(ScreenEvents.BeforeInit.class);

    @Deprecated(forRemoval = true)
    public static final EventInvoker<ScreenEvents.AfterInit> AFTER_INIT = EventInvoker.lookup(ScreenEvents.AfterInit.class);

    private ScreenEvents() {
    }

    public static <T extends Screen> EventInvoker<ScreenEvents.BeforeInitV2<T>> beforeInit(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenEvents.BeforeInitV2.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenEvents.AfterInitV2<T>> afterInit(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenEvents.AfterInitV2.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenEvents.Remove<T>> remove(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenEvents.Remove.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenEvents.BeforeRender<T>> beforeRender(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenEvents.BeforeRender.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenEvents.AfterRender<T>> afterRender(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenEvents.AfterRender.class, screen);
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface AfterInit {

        void onAfterInit(Minecraft var1, Screen var2, int var3, int var4, List<AbstractWidget> var5, Consumer<AbstractWidget> var6, Consumer<AbstractWidget> var7);
    }

    @FunctionalInterface
    public interface AfterInitV2<T extends Screen> {

        void onAfterInit(Minecraft var1, T var2, int var3, int var4, List<AbstractWidget> var5, ScreenEvents.ConsumingOperator<AbstractWidget> var6, ScreenEvents.ConsumingOperator<AbstractWidget> var7);
    }

    @FunctionalInterface
    public interface AfterRender<T extends Screen> {

        void onAfterRender(T var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface BeforeInit {

        void onBeforeInit(Minecraft var1, Screen var2, int var3, int var4, List<AbstractWidget> var5);
    }

    @FunctionalInterface
    public interface BeforeInitV2<T extends Screen> {

        void onBeforeInit(Minecraft var1, T var2, int var3, int var4, List<AbstractWidget> var5);
    }

    @FunctionalInterface
    public interface BeforeRender<T extends Screen> {

        void onBeforeRender(T var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    public static final class ConsumingOperator<T> {

        private final Consumer<T> consumer;

        public ConsumingOperator(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        public <S extends T> S apply(S s) {
            this.consumer.accept(s);
            return s;
        }
    }

    @FunctionalInterface
    public interface Remove<T extends Screen> {

        void onRemove(T var1);
    }
}
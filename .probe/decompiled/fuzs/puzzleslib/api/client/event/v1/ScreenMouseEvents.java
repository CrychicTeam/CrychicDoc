package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;

public final class ScreenMouseEvents {

    private ScreenMouseEvents() {
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.BeforeMouseClick<T>> beforeMouseClick(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.BeforeMouseClick.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.AfterMouseClick<T>> afterMouseClick(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.AfterMouseClick.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.BeforeMouseRelease<T>> beforeMouseRelease(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.BeforeMouseRelease.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.AfterMouseRelease<T>> afterMouseRelease(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.AfterMouseRelease.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.BeforeMouseScroll<T>> beforeMouseScroll(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.BeforeMouseScroll.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.AfterMouseScroll<T>> afterMouseScroll(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.AfterMouseScroll.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.BeforeMouseDrag<T>> beforeMouseDrag(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.BeforeMouseDrag.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenMouseEvents.AfterMouseDrag<T>> afterMouseDrag(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenMouseEvents.AfterMouseDrag.class, screen);
    }

    @FunctionalInterface
    public interface AfterMouseClick<T extends Screen> {

        void onAfterMouseClick(T var1, double var2, double var4, int var6);
    }

    @FunctionalInterface
    public interface AfterMouseDrag<T extends Screen> {

        void onAfterMouseDrag(T var1, double var2, double var4, int var6, double var7, double var9);
    }

    @FunctionalInterface
    public interface AfterMouseRelease<T extends Screen> {

        void onAfterMouseRelease(T var1, double var2, double var4, int var6);
    }

    @FunctionalInterface
    public interface AfterMouseScroll<T extends Screen> {

        void onAfterMouseScroll(T var1, double var2, double var4, double var6, double var8);
    }

    @FunctionalInterface
    public interface BeforeMouseClick<T extends Screen> {

        EventResult onBeforeMouseClick(T var1, double var2, double var4, int var6);
    }

    @FunctionalInterface
    public interface BeforeMouseDrag<T extends Screen> {

        EventResult onBeforeMouseDrag(T var1, double var2, double var4, int var6, double var7, double var9);
    }

    @FunctionalInterface
    public interface BeforeMouseRelease<T extends Screen> {

        EventResult onBeforeMouseRelease(T var1, double var2, double var4, int var6);
    }

    @FunctionalInterface
    public interface BeforeMouseScroll<T extends Screen> {

        EventResult onBeforeMouseScroll(T var1, double var2, double var4, double var6, double var8);
    }
}
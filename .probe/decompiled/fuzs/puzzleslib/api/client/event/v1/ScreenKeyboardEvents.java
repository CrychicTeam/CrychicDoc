package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;

public final class ScreenKeyboardEvents {

    private ScreenKeyboardEvents() {
    }

    public static <T extends Screen> EventInvoker<ScreenKeyboardEvents.BeforeKeyPress<T>> beforeKeyPress(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenKeyboardEvents.BeforeKeyPress.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenKeyboardEvents.AfterKeyPress<T>> afterKeyPress(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenKeyboardEvents.AfterKeyPress.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenKeyboardEvents.BeforeKeyRelease<T>> beforeKeyRelease(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenKeyboardEvents.BeforeKeyRelease.class, screen);
    }

    public static <T extends Screen> EventInvoker<ScreenKeyboardEvents.AfterKeyRelease<T>> afterKeyRelease(Class<T> screen) {
        Objects.requireNonNull(screen, "screen type is null");
        return EventInvoker.lookup(ScreenKeyboardEvents.AfterKeyRelease.class, screen);
    }

    @FunctionalInterface
    public interface AfterKeyPress<T extends Screen> {

        void onAfterKeyPress(T var1, int var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface AfterKeyRelease<T extends Screen> {

        void onAfterKeyRelease(T var1, int var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface BeforeKeyPress<T extends Screen> {

        EventResult onBeforeKeyPress(T var1, int var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface BeforeKeyRelease<T extends Screen> {

        EventResult onBeforeKeyRelease(T var1, int var2, int var3, int var4);
    }
}
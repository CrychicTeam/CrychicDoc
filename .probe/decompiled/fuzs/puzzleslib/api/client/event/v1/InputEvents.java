package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;

public final class InputEvents {

    public static final EventInvoker<InputEvents.BeforeMouseAction> BEFORE_MOUSE_ACTION = EventInvoker.lookup(InputEvents.BeforeMouseAction.class);

    public static final EventInvoker<InputEvents.AfterMouseAction> AFTER_MOUSE_ACTION = EventInvoker.lookup(InputEvents.AfterMouseAction.class);

    public static final EventInvoker<InputEvents.BeforeMouseScroll> BEFORE_MOUSE_SCROLL = EventInvoker.lookup(InputEvents.BeforeMouseScroll.class);

    public static final EventInvoker<InputEvents.AfterMouseScroll> AFTER_MOUSE_SCROLL = EventInvoker.lookup(InputEvents.AfterMouseScroll.class);

    public static final EventInvoker<InputEvents.BeforeKeyAction> BEFORE_KEY_ACTION = EventInvoker.lookup(InputEvents.BeforeKeyAction.class);

    public static final EventInvoker<InputEvents.AfterKeyAction> AFTER_KEY_ACTION = EventInvoker.lookup(InputEvents.AfterKeyAction.class);

    private InputEvents() {
    }

    @FunctionalInterface
    public interface AfterKeyAction {

        void onAfterKeyAction(int var1, int var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface AfterMouseAction {

        void onAfterMouseAction(int var1, int var2, int var3);
    }

    @FunctionalInterface
    public interface AfterMouseScroll {

        void onAfterMouseScroll(boolean var1, boolean var2, boolean var3, double var4, double var6);
    }

    @FunctionalInterface
    public interface BeforeKeyAction {

        EventResult onBeforeKeyAction(int var1, int var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface BeforeMouseAction {

        EventResult onBeforeMouseAction(int var1, int var2, int var3);
    }

    @FunctionalInterface
    public interface BeforeMouseScroll {

        EventResult onBeforeMouseScroll(boolean var1, boolean var2, boolean var3, double var4, double var6);
    }
}
package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import java.util.List;

public final class GatherDebugTextEvents {

    public static final EventInvoker<GatherDebugTextEvents.Left> LEFT = EventInvoker.lookup(GatherDebugTextEvents.Left.class);

    public static final EventInvoker<GatherDebugTextEvents.Right> RIGHT = EventInvoker.lookup(GatherDebugTextEvents.Right.class);

    private GatherDebugTextEvents() {
    }

    @FunctionalInterface
    public interface Left {

        void onGatherLeftDebugText(List<String> var1);
    }

    @FunctionalInterface
    public interface Right {

        void onGatherRightDebugText(List<String> var1);
    }
}
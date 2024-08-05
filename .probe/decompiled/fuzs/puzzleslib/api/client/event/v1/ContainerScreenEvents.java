package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class ContainerScreenEvents {

    public static final EventInvoker<ContainerScreenEvents.Background> BACKGROUND = EventInvoker.lookup(ContainerScreenEvents.Background.class);

    public static final EventInvoker<ContainerScreenEvents.Foreground> FOREGROUND = EventInvoker.lookup(ContainerScreenEvents.Foreground.class);

    private ContainerScreenEvents() {
    }

    @FunctionalInterface
    public interface Background {

        void onDrawBackground(AbstractContainerScreen<?> var1, GuiGraphics var2, int var3, int var4);
    }

    @FunctionalInterface
    public interface Foreground {

        void onDrawForeground(AbstractContainerScreen<?> var1, GuiGraphics var2, int var3, int var4);
    }
}
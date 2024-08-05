package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

public final class ScreenTooltipEvents {

    public static final EventInvoker<ScreenTooltipEvents.Render> RENDER = EventInvoker.lookup(ScreenTooltipEvents.Render.class);

    private ScreenTooltipEvents() {
    }

    @FunctionalInterface
    public interface Render {

        EventResult onRenderTooltip(GuiGraphics var1, int var2, int var3, int var4, int var5, Font var6, List<ClientTooltipComponent> var7, ClientTooltipPositioner var8);
    }
}
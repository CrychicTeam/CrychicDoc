package de.keksuccino.fancymenu.util.rendering.ui.tooltip;

import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenEvent;
import de.keksuccino.fancymenu.events.screen.RenderScreenEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.components.AbstractWidget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class TooltipHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final TooltipHandler INSTANCE = new TooltipHandler();

    private final List<TooltipHandler.HandledTooltip> tooltips = new ArrayList();

    private final Map<AbstractWidget, TooltipHandler.HandledTooltip> widgetTooltips = new HashMap();

    public TooltipHandler() {
        EventHandler.INSTANCE.registerListenersOf(this);
    }

    @EventListener(priority = -1000)
    public void onScreenRenderPost(RenderScreenEvent.Post e) {
        TooltipHandler.HandledTooltip renderTooltip = null;
        for (TooltipHandler.HandledTooltip t : new ArrayList(this.tooltips)) {
            if (t.shouldRender.getAsBoolean()) {
                renderTooltip = t;
            }
            if (t.removeAfterScreenRender) {
                t.remove();
            }
        }
        if (renderTooltip != null) {
            renderTooltip.tooltip.render(e.getGraphics(), e.getMouseX(), e.getMouseY(), e.getPartial());
        }
    }

    @EventListener(priority = 1000)
    public void onScreenInitResizePre(InitOrResizeScreenEvent.Pre e) {
        for (TooltipHandler.HandledTooltip t : new ArrayList(this.tooltips)) {
            if (t.removeOnScreenInitOrResize) {
                t.remove();
            }
        }
    }

    public TooltipHandler.HandledTooltip addWidgetTooltip(@NotNull AbstractWidget widget, @NotNull Tooltip tooltip, boolean removeOnScreenInitOrResize, boolean removeAfterScreenRender) {
        if (this.widgetTooltips.containsKey(widget)) {
            this.removeTooltip((TooltipHandler.HandledTooltip) this.widgetTooltips.get(widget));
        }
        TooltipHandler.HandledTooltip t = this.addTooltip(tooltip, () -> widget.isHovered() && widget.visible, removeOnScreenInitOrResize, removeAfterScreenRender);
        t.widget = widget;
        this.widgetTooltips.put(widget, t);
        return t;
    }

    public TooltipHandler.HandledTooltip addTooltip(@NotNull Tooltip tooltip, @NotNull BooleanSupplier shouldRender, boolean removeOnScreenInitOrResize, boolean removeAfterScreenRender) {
        TooltipHandler.HandledTooltip t = new TooltipHandler.HandledTooltip(this, tooltip, shouldRender, removeOnScreenInitOrResize, removeAfterScreenRender);
        this.tooltips.add(t);
        return t;
    }

    public void removeTooltip(TooltipHandler.HandledTooltip tooltip) {
        this.tooltips.remove(tooltip);
        if (tooltip.widget != null) {
            this.widgetTooltips.remove(tooltip.widget);
        }
    }

    public static class HandledTooltip {

        private final TooltipHandler parent;

        public final Tooltip tooltip;

        public final BooleanSupplier shouldRender;

        public final boolean removeOnScreenInitOrResize;

        public final boolean removeAfterScreenRender;

        protected AbstractWidget widget = null;

        private HandledTooltip(TooltipHandler parent, Tooltip tooltip, BooleanSupplier shouldRender, boolean removeOnScreenInitOrResize, boolean removeAfterScreenRender) {
            this.parent = parent;
            this.tooltip = tooltip;
            this.shouldRender = shouldRender;
            this.removeOnScreenInitOrResize = removeOnScreenInitOrResize;
            this.removeAfterScreenRender = removeAfterScreenRender;
        }

        public void remove() {
            this.parent.removeTooltip(this);
        }
    }
}
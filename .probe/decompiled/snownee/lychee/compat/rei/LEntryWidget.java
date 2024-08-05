package snownee.lychee.compat.rei;

import java.util.function.UnaryOperator;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.client.gui.ScreenElement;

public class LEntryWidget extends EntryWidget {

    private ScreenElement bg;

    private UnaryOperator<Tooltip> tooltipCallback;

    public LEntryWidget(Point point) {
        super(point);
    }

    public LEntryWidget(Rectangle bounds) {
        super(bounds);
    }

    protected void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.background) {
            super.drawBackground(graphics, mouseX, mouseY, delta);
        } else if (this.bg != null) {
            Rectangle rect = this.getBounds();
            this.bg.render(graphics, rect.x, rect.y);
        }
    }

    public void background(ScreenElement bg) {
        this.disableBackground();
        this.bg = bg;
    }

    @Nullable
    public Tooltip getCurrentTooltip(TooltipContext ctx) {
        Tooltip tooltip = super.getCurrentTooltip(ctx);
        if (this.tooltipCallback != null) {
            tooltip = (Tooltip) this.tooltipCallback.apply(tooltip);
        }
        return tooltip;
    }

    public void addTooltipCallback(UnaryOperator<Tooltip> tooltipCallback) {
        this.tooltipCallback = tooltipCallback;
    }
}
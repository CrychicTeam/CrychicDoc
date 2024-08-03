package snownee.lychee.compat.rei;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ReactiveWidget extends WidgetWithBounds {

    private boolean focused = false;

    private boolean focusable = true;

    private final Rectangle bounds;

    private Point point;

    @Nullable
    private Function<ReactiveWidget, Component[]> tooltip;

    @Nullable
    private BiConsumer<ReactiveWidget, Integer> onClick;

    public ReactiveWidget(Rectangle bounds) {
        this.bounds = bounds;
        this.point = new Point(bounds.getCenterX(), bounds.getMaxY());
    }

    public final Point getPoint() {
        return this.point;
    }

    public final void setPoint(Point point) {
        this.point = (Point) Objects.requireNonNull(point);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.isHovered(mouseX, mouseY)) {
            Component[] tooltip = this.getTooltipLines();
            if (tooltip != null) {
                if (!this.focused && this.containsMouse(mouseX, mouseY)) {
                    Tooltip.create(tooltip).queue();
                } else if (this.focused) {
                    Tooltip.create(this.point, tooltip).queue();
                }
            }
        }
    }

    @Nullable
    public final Component[] getTooltipLines() {
        return this.tooltip == null ? null : (Component[]) this.tooltip.apply(this);
    }

    public final void setTooltipFunction(@Nullable Function<ReactiveWidget, Component[]> tooltip) {
        this.tooltip = tooltip;
    }

    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isClickable() && this.containsMouse(mouseX, mouseY)) {
            Widgets.produceClickSound();
            this.onClick.accept(this, button);
            return true;
        } else {
            return false;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isClickable() || !this.isFocusable() || !this.focused) {
            return false;
        } else if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
            return false;
        } else {
            Widgets.produceClickSound();
            if (this.onClick != null) {
                this.onClick.accept(this, 0);
            }
            return true;
        }
    }

    public final boolean isClickable() {
        return this.getOnClick() != null;
    }

    @Nullable
    public final BiConsumer<ReactiveWidget, Integer> getOnClick() {
        return this.onClick;
    }

    public final void setOnClick(@Nullable BiConsumer<ReactiveWidget, Integer> onClick) {
        this.onClick = onClick;
    }

    public final boolean isFocusable() {
        return this.focusable;
    }

    public final void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return this.containsMouse(mouseX, mouseY) || this.focused;
    }
}
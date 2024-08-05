package snownee.kiwi.customization.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class PanelLayout {

    private final Vector2i pos = new Vector2i();

    private final Vector2i lastPos = new Vector2i();

    private final Rect2i bounds = new Rect2i(0, 0, 0, 0);

    private final int[] padding;

    private final List<AbstractWidget> widgets = Lists.newArrayList();

    public PanelLayout(int padding) {
        this(padding, padding, padding, padding);
    }

    public PanelLayout(int left, int top, int right, int bottom) {
        this.padding = new int[] { left, top, right, bottom };
    }

    public void addWidget(AbstractWidget widget) {
        this.widgets.add(widget);
    }

    public void bind(Screen screen, Vector2i pos, Vector2f anchor) {
        Preconditions.checkArgument(!this.widgets.isEmpty());
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (AbstractWidget widget : this.widgets) {
            screen.renderables.add(widget);
            screen.children.add(widget);
            screen.narratables.add(widget);
            minX = Math.min(minX, widget.getX());
            minY = Math.min(minY, widget.getY());
            maxX = Math.max(maxX, widget.getX() + widget.getWidth());
            maxY = Math.max(maxY, widget.getY() + widget.getHeight());
        }
        this.bounds.setWidth(maxX - minX + this.padding[0] + this.padding[2]);
        this.bounds.setHeight(maxY - minY + this.padding[1] + this.padding[3]);
        this.bounds.setX(this.bounds.getX() - this.padding[0]);
        this.bounds.setY(this.bounds.getY() - this.padding[1]);
        this.pos.set(pos);
        this.lastPos.set((int) ((float) this.bounds.getWidth() * anchor.x) - this.padding[0], (int) ((float) this.bounds.getHeight() * anchor.y) - this.padding[1]);
        this.update();
    }

    public Vector2i getAnchoredPos() {
        return this.pos;
    }

    public void update() {
        int deltaX = this.pos.x - this.lastPos.x;
        int deltaY = this.pos.y - this.lastPos.y;
        if (deltaX != 0 || deltaY != 0) {
            for (AbstractWidget widget : this.widgets) {
                widget.m_264152_(widget.getX() + deltaX, widget.getY() + deltaY);
            }
            this.lastPos.set(this.pos);
            this.bounds.setX(this.bounds.getX() + deltaX);
            this.bounds.setY(this.bounds.getY() + deltaY);
        }
    }

    public Rect2i bounds() {
        return this.bounds;
    }

    public List<AbstractWidget> widgets() {
        return this.widgets;
    }
}
package org.embeddedt.embeddium.gui.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphics;
import org.apache.commons.lang3.Validate;

public class BasicFrame extends AbstractFrame {

    protected List<Function<Dim2i, AbstractWidget>> functions;

    public BasicFrame(Dim2i dim, boolean renderOutline, List<Function<Dim2i, AbstractWidget>> functions) {
        super(dim, renderOutline);
        this.functions = functions;
        this.buildFrame();
    }

    public static BasicFrame.Builder createBuilder() {
        return new BasicFrame.Builder();
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();
        this.functions.forEach(function -> this.children.add((AbstractWidget) function.apply(this.dim)));
        super.buildFrame();
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
    }

    public static class Builder {

        private final List<Function<Dim2i, AbstractWidget>> functions = new ArrayList();

        private Dim2i dim;

        private boolean renderOutline;

        public BasicFrame.Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public BasicFrame.Builder shouldRenderOutline(boolean renderOutline) {
            this.renderOutline = renderOutline;
            return this;
        }

        public BasicFrame.Builder addChild(Function<Dim2i, AbstractWidget> function) {
            this.functions.add(function);
            return this;
        }

        public BasicFrame build() {
            Validate.notNull(this.dim, "Dimension must be specified", new Object[0]);
            return new BasicFrame(this.dim, this.renderOutline, this.functions);
        }
    }
}
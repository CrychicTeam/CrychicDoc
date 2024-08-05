package fr.frinn.custommachinery.client.screen.creation.component;

import fr.frinn.custommachinery.client.screen.widget.ListWidget;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class ComponentPropertyListWidget extends ListWidget<ComponentPropertyListWidget.ComponentPropertyEntry> {

    public ComponentPropertyListWidget(int x, int y, int width, int height, int itemHeight) {
        super(x, y, width, height, itemHeight, Component.empty());
    }

    public <T extends AbstractWidget> T add(Component title, T widget) {
        this.addEntry(new ComponentPropertyListWidget.ComponentPropertyEntry(title, widget));
        return widget;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        this.m_6702_().stream().map(entry -> entry.widget).filter(widget -> widget.getTooltip() != null && widget.isMouseOver((double) mouseX, (double) mouseY)).forEach(widget -> graphics.renderTooltip(Minecraft.getInstance().font, widget.getTooltip().toCharSequence(Minecraft.getInstance()), mouseX, mouseY));
    }

    public static class ComponentPropertyEntry extends ListWidget.Entry {

        private final Component title;

        private final AbstractWidget widget;

        public ComponentPropertyEntry(Component title, AbstractWidget widget) {
            this.title = title;
            this.widget = widget;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int x, int y, int width, int height, int mouseX, int mouseY, float partialTick) {
            graphics.drawString(Minecraft.getInstance().font, this.title, x, y + (height - 9) / 2, 0, false);
            this.widget.m_264152_(x + width - this.widget.getWidth() - 10, y + (height - this.widget.getHeight()) / 2);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.widget);
        }
    }
}
package de.keksuccino.fancymenu.events.widget;

import de.keksuccino.fancymenu.util.event.acara.EventBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;

public class RenderGuiListBackgroundEvent extends EventBase {

    protected AbstractSelectionList list;

    protected GuiGraphics graphics;

    public RenderGuiListBackgroundEvent(GuiGraphics graphics, AbstractSelectionList list) {
        this.list = list;
        this.graphics = graphics;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public AbstractSelectionList getList() {
        return this.list;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }

    public static class Post extends RenderGuiListBackgroundEvent {

        public Post(GuiGraphics graphics, AbstractSelectionList list) {
            super(graphics, list);
        }
    }

    public static class Pre extends RenderGuiListBackgroundEvent {

        public Pre(GuiGraphics graphics, AbstractSelectionList list) {
            super(graphics, list);
        }
    }
}
package de.keksuccino.fancymenu.events.screen;

import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinScreen;
import de.keksuccino.fancymenu.util.event.acara.EventBase;
import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

public class ScreenMouseScrollEvent extends EventBase {

    private final Screen screen;

    private final double scrollDelta;

    private final double mouseX;

    private final double mouseY;

    protected ScreenMouseScrollEvent(Screen screen, double mouseX, double mouseY, double scrollDelta) {
        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.scrollDelta = scrollDelta;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public double getScrollDelta() {
        return this.scrollDelta;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public <T extends GuiEventListener & NarratableEntry> void addWidget(T widget) {
        this.getWidgets().add(widget);
        this.getNarratables().add(widget);
    }

    public <T extends GuiEventListener & NarratableEntry & Renderable> void addRenderableWidget(T widget) {
        this.addWidget(widget);
        this.getRenderables().add(widget);
    }

    public List<GuiEventListener> getWidgets() {
        return ((IMixinScreen) this.getScreen()).getChildrenFancyMenu();
    }

    public List<Renderable> getRenderables() {
        return ((IMixinScreen) this.getScreen()).getRenderablesFancyMenu();
    }

    public List<NarratableEntry> getNarratables() {
        return ((IMixinScreen) this.getScreen()).getNarratablesFancyMenu();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static class Post extends ScreenMouseScrollEvent {

        public Post(Screen screen, double mouseX, double mouseY, double scrollDelta) {
            super(screen, mouseX, mouseY, scrollDelta);
        }
    }

    public static class Pre extends ScreenMouseScrollEvent {

        public Pre(Screen screen, double mouseX, double mouseY, double scrollDelta) {
            super(screen, mouseX, mouseY, scrollDelta);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}
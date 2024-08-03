package dev.architectury.impl;

import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.hooks.client.screen.ScreenHooks;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

public class ScreenAccessImpl implements ScreenAccess {

    private Screen screen;

    public ScreenAccessImpl(Screen screen) {
        this.screen = screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public List<NarratableEntry> getNarratables() {
        return ScreenHooks.getNarratables(this.screen);
    }

    @Override
    public List<Renderable> getRenderables() {
        return ScreenHooks.getRenderables(this.screen);
    }

    @Override
    public <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return ScreenHooks.addRenderableWidget(this.screen, widget);
    }

    @Override
    public <T extends Renderable> T addRenderableOnly(T listener) {
        return ScreenHooks.addRenderableOnly(this.screen, listener);
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
        return ScreenHooks.addWidget(this.screen, listener);
    }
}
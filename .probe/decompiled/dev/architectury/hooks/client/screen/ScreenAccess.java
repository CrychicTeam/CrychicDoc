package dev.architectury.hooks.client.screen;

import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

public interface ScreenAccess {

    Screen getScreen();

    List<NarratableEntry> getNarratables();

    List<Renderable> getRenderables();

    <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(T var1);

    <T extends Renderable> T addRenderableOnly(T var1);

    <T extends GuiEventListener & NarratableEntry> T addWidget(T var1);
}
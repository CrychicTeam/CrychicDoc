package dev.architectury.hooks.client.screen.forge;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ScreenHooksImpl {

    public static List<NarratableEntry> getNarratables(Screen screen) {
        return screen.narratables;
    }

    public static List<Renderable> getRenderables(Screen screen) {
        return screen.renderables;
    }

    public static <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "m_142416_", new Class[] { GuiEventListener.class }).invoke(screen, widget);
        } catch (InvocationTargetException | IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T extends Renderable> T addRenderableOnly(Screen screen, T listener) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "m_169394_", new Class[] { Renderable.class }).invoke(screen, listener);
        } catch (InvocationTargetException | IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T extends GuiEventListener & NarratableEntry> T addWidget(Screen screen, T listener) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "m_7787_", new Class[] { GuiEventListener.class }).invoke(screen, listener);
        } catch (InvocationTargetException | IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }
}
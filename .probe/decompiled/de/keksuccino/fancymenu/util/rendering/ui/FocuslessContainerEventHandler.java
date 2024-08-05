package de.keksuccino.fancymenu.util.rendering.ui;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.Nullable;

public interface FocuslessContainerEventHandler extends ContainerEventHandler {

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.m_7897_(false);
        for (GuiEventListener child : this.m_6702_()) {
            if (child.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double $$3, double $$4) {
        if (this.m_7282_() && button == 0) {
            for (GuiEventListener child : this.m_6702_()) {
                if (child.mouseDragged(mouseX, mouseY, button, $$3, $$4)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        for (GuiEventListener child : this.m_6702_()) {
            if (child.mouseScrolled(mouseX, mouseY, scrollDelta)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyPressed(int keycode, int scancode, int modifiers) {
        for (GuiEventListener child : this.m_6702_()) {
            if (child.keyPressed(keycode, scancode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyReleased(int keycode, int scancode, int modifiers) {
        for (GuiEventListener child : this.m_6702_()) {
            if (child.keyReleased(keycode, scancode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean charTyped(char c, int $$1) {
        for (GuiEventListener child : this.m_6702_()) {
            if (child.charTyped(c, $$1)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    default GuiEventListener getFocused() {
        return null;
    }

    @Override
    default void setFocused(@Nullable GuiEventListener var1) {
    }

    @Override
    default void setFocused(boolean $$0) {
    }
}
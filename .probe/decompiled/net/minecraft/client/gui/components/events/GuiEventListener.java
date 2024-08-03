package net.minecraft.client.gui.components.events;

import javax.annotation.Nullable;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.TabOrderedElement;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface GuiEventListener extends TabOrderedElement {

    long DOUBLE_CLICK_THRESHOLD_MS = 250L;

    default void mouseMoved(double double0, double double1) {
    }

    default boolean mouseClicked(double double0, double double1, int int2) {
        return false;
    }

    default boolean mouseReleased(double double0, double double1, int int2) {
        return false;
    }

    default boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        return false;
    }

    default boolean mouseScrolled(double double0, double double1, double double2) {
        return false;
    }

    default boolean keyPressed(int int0, int int1, int int2) {
        return false;
    }

    default boolean keyReleased(int int0, int int1, int int2) {
        return false;
    }

    default boolean charTyped(char char0, int int1) {
        return false;
    }

    @Nullable
    default ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        return null;
    }

    default boolean isMouseOver(double double0, double double1) {
        return false;
    }

    void setFocused(boolean var1);

    boolean isFocused();

    @Nullable
    default ComponentPath getCurrentFocusPath() {
        return this.isFocused() ? ComponentPath.leaf(this) : null;
    }

    default ScreenRectangle getRectangle() {
        return ScreenRectangle.empty();
    }
}
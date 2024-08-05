package fuzs.puzzleslib.api.client.screen.v2;

import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.Nullable;

public final class ScreenElementPositioner {

    private ScreenElementPositioner() {
    }

    public static boolean tryPositionElement(LayoutElement element, List<? extends GuiEventListener> widgets, String... translationKeys) {
        return tryPositionElement(element, widgets, false, translationKeys);
    }

    public static boolean tryPositionElement(LayoutElement element, List<? extends GuiEventListener> widgets, boolean tryPositionRightFirst, String... translationKeys) {
        return tryPositionElement(element, widgets, tryPositionRightFirst, 4, translationKeys);
    }

    public static boolean tryPositionElement(LayoutElement element, List<? extends GuiEventListener> widgets, boolean tryPositionRightFirst, int horizontalOffset, String... translationKeys) {
        int originalX = element.getX();
        int originalY = element.getY();
        for (String translationKey : translationKeys) {
            LayoutElement otherElement = findElement(widgets, translationKey);
            if (otherElement != null) {
                moveElementToOther(element, otherElement, tryPositionRightFirst, horizontalOffset);
                if (noOverlapWithExisting(widgets, element)) {
                    return true;
                }
                moveElementToOther(element, otherElement, !tryPositionRightFirst, horizontalOffset);
                if (noOverlapWithExisting(widgets, element)) {
                    return true;
                }
            }
        }
        element.setPosition(originalX, originalY);
        return false;
    }

    private static void moveElementToOther(LayoutElement element, LayoutElement otherElement, boolean tryPositionRightFirst, int horizontalOffset) {
        if (tryPositionRightFirst) {
            moveToRight(element, otherElement, horizontalOffset);
        } else {
            moveToLeft(element, otherElement, horizontalOffset);
        }
    }

    private static void moveToLeft(LayoutElement element, LayoutElement otherElement, int horizontalOffset) {
        element.setPosition(otherElement.getX() - element.getWidth() - horizontalOffset, otherElement.getY());
    }

    private static void moveToRight(LayoutElement element, LayoutElement otherElement, int horizontalOffset) {
        element.setPosition(otherElement.getX() + otherElement.getWidth() + horizontalOffset, otherElement.getY());
    }

    private static boolean noOverlapWithExisting(List<? extends GuiEventListener> widgets, LayoutElement element) {
        for (GuiEventListener widget : widgets) {
            if (widget instanceof LayoutElement otherElement && element.getRectangle().intersection(otherElement.getRectangle()) != null) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static LayoutElement findElement(List<? extends GuiEventListener> widgets, String translationKey) {
        for (GuiEventListener listener : widgets) {
            if (listener instanceof AbstractWidget widget && matchesTranslationKey(widget, translationKey)) {
                return widget;
            }
        }
        return null;
    }

    private static boolean matchesTranslationKey(AbstractWidget widget, String translationKey) {
        if (widget.getMessage().getContents() instanceof TranslatableContents contents && contents.getKey().equals(translationKey)) {
            return true;
        }
        return false;
    }
}
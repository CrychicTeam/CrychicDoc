package de.keksuccino.fancymenu.customization.element;

import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ElementStacker<E extends AbstractElement> {

    void stackElements(@NotNull E var1, @NotNull E var2);

    default void stackElementsSingleInternal(AbstractElement e, AbstractElement stack) {
        if (e.anchorPoint != null && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.anchorPoint = e.anchorPoint;
        }
        if (e.anchorPointElementIdentifier != null) {
            stack.anchorPointElementIdentifier = e.anchorPointElementIdentifier;
        }
        if (e.posOffsetX != 0 && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.posOffsetX = e.posOffsetX;
        }
        if (e.posOffsetY != 0 && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.posOffsetY = e.posOffsetY;
        }
        if (e.baseWidth != 0 && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.baseWidth = e.baseWidth;
        }
        if (e.baseHeight != 0 && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.baseHeight = e.baseHeight;
        }
        if (e.advancedX != null && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.advancedX = e.advancedX;
        }
        if (e.advancedY != null && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.advancedY = e.advancedY;
        }
        if (e.advancedWidth != null && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.advancedWidth = e.advancedWidth;
        }
        if (e.advancedHeight != null && e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.advancedHeight = e.advancedHeight;
        }
        if (e.stretchX) {
            stack.stretchX = true;
        }
        if (e.stretchY) {
            stack.stretchY = true;
        }
        if (e.appearanceDelay != AbstractElement.AppearanceDelay.NO_DELAY) {
            stack.appearanceDelay = e.appearanceDelay;
        }
        if (e.appearanceDelayInSeconds != 1.0F) {
            stack.appearanceDelayInSeconds = e.appearanceDelayInSeconds;
        }
        if (e.fadeIn) {
            stack.fadeIn = true;
        }
        if (e.fadeInSpeed != 1.0F) {
            stack.fadeInSpeed = e.fadeInSpeed;
        }
        this.stackElements((E) e, (E) stack);
    }

    @Nullable
    default E stackElementsInternal(AbstractElement stack, AbstractElement... elements) {
        try {
            List<LoadingRequirementContainer> containers = new ArrayList();
            for (AbstractElement e : elements) {
                this.stackElementsSingleInternal(e, stack);
                containers.add(e.loadingRequirementContainer);
            }
            stack.loadingRequirementContainer = LoadingRequirementContainer.stackContainers((LoadingRequirementContainer[]) containers.toArray(new LoadingRequirementContainer[0]));
            return (E) stack;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
}
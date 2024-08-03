package de.keksuccino.fancymenu.customization.deep;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDeepElement extends AbstractElement implements HideableElement {

    public boolean deepElementHidden = false;

    public AbstractDeepElement(DeepElementBuilder<?, ?, ?> builder) {
        super(builder);
        this.anchorPoint = ElementAnchorPoints.VANILLA;
    }

    @Override
    public boolean shouldRender() {
        return !this.isDeepElementVisible() ? false : super.shouldRender();
    }

    @NotNull
    @Override
    public String getInstanceIdentifier() {
        return "deep:" + this.builder.getIdentifier();
    }

    public boolean isDeepElementVisible() {
        if (!this.loadingRequirementsMet()) {
            return false;
        } else {
            return this.deepElementHidden ? false : this.visible;
        }
    }

    @Override
    public boolean isHidden() {
        return this.deepElementHidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.deepElementHidden = hidden;
    }
}
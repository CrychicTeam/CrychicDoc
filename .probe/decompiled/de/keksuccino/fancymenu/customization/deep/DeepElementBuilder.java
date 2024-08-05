package de.keksuccino.fancymenu.customization.deep;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.ElementStacker;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DeepElementBuilder<D extends DeepScreenCustomizationLayer, E extends AbstractDeepElement, L extends AbstractDeepEditorElement> extends ElementBuilder<E, L> implements ElementStacker<E> {

    public final D layer;

    public DeepElementBuilder(@NotNull String uniqueElementIdentifier, @NotNull D layer) {
        super(uniqueElementIdentifier);
        this.layer = layer;
    }

    @Nullable
    public E deserializeElementInternal(@NotNull SerializedElement serialized) {
        try {
            E element = super.deserializeElementInternal(serialized);
            if (element != null) {
                String hidden = serialized.getValue("is_hidden");
                if (hidden == null) {
                    hidden = serialized.getValue("hidden");
                }
                if (hidden != null && hidden.equals("true")) {
                    element.deepElementHidden = true;
                }
            }
            return element;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public SerializedElement serializeElementInternal(@NotNull AbstractElement elementAbstract) {
        try {
            AbstractDeepElement element = (AbstractDeepElement) elementAbstract;
            SerializedElement serialized = super.serializeElementInternal(element);
            if (serialized != null) {
                serialized.setType("deep_element");
                serialized.putProperty("is_hidden", element.deepElementHidden + "");
                return serialized;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return null;
    }

    public abstract void stackElements(E var1, E var2);

    @Override
    public void stackElementsSingleInternal(AbstractElement elementAbstract, AbstractElement stackAbstract) {
        ElementStacker.super.stackElementsSingleInternal(elementAbstract, stackAbstract);
        if (elementAbstract instanceof AbstractDeepElement e && stackAbstract instanceof AbstractDeepElement stack) {
            if (e.deepElementHidden) {
                stack.deepElementHidden = true;
            }
            if (e.anchorPoint != ElementAnchorPoints.VANILLA) {
                stack.anchorPoint = e.anchorPoint;
            }
        }
    }

    @Nullable
    public E stackElementsInternal(AbstractElement stack, AbstractElement... elements) {
        if (stack != null) {
            stack.anchorPoint = ElementAnchorPoints.VANILLA;
        }
        return ElementStacker.super.stackElementsInternal(stack, elements);
    }
}
package net.minecraft.client.gui.layouts;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;

public interface Layout extends LayoutElement {

    void visitChildren(Consumer<LayoutElement> var1);

    @Override
    default void visitWidgets(Consumer<AbstractWidget> consumerAbstractWidget0) {
        this.visitChildren(p_270634_ -> p_270634_.visitWidgets(consumerAbstractWidget0));
    }

    default void arrangeElements() {
        this.visitChildren(p_270565_ -> {
            if (p_270565_ instanceof Layout $$1) {
                $$1.arrangeElements();
            }
        });
    }
}
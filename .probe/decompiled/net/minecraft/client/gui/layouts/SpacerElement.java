package net.minecraft.client.gui.layouts;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;

public class SpacerElement implements LayoutElement {

    private int x;

    private int y;

    private final int width;

    private final int height;

    public SpacerElement(int int0, int int1) {
        this(0, 0, int0, int1);
    }

    public SpacerElement(int int0, int int1, int int2, int int3) {
        this.x = int0;
        this.y = int1;
        this.width = int2;
        this.height = int3;
    }

    public static SpacerElement width(int int0) {
        return new SpacerElement(int0, 0);
    }

    public static SpacerElement height(int int0) {
        return new SpacerElement(0, int0);
    }

    @Override
    public void setX(int int0) {
        this.x = int0;
    }

    @Override
    public void setY(int int0) {
        this.y = int0;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumerAbstractWidget0) {
    }
}
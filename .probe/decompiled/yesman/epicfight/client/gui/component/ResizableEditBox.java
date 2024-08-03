package yesman.epicfight.client.gui.component;

import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class ResizableEditBox extends EditBox implements ResizableComponent {

    private final int x1;

    private final int x2;

    private final int y1;

    private final int y2;

    private final ResizableComponent.HorizontalSizing horizontalSizingOption;

    private final ResizableComponent.VerticalSizing verticalSizingOption;

    public ResizableEditBox(Font font, int x, int y, int width, int height, Component title, ResizableComponent.HorizontalSizing horizontalSizingOption, ResizableComponent.VerticalSizing verticalSizingOption) {
        super(font, x, y, width, height, title);
        this.x1 = x;
        this.x2 = width;
        this.y1 = y;
        this.y2 = height;
        this.horizontalSizingOption = horizontalSizingOption;
        this.verticalSizingOption = verticalSizingOption;
    }

    public ResizableEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editbox, Component title, ResizableComponent.HorizontalSizing horizontalSizingOption, ResizableComponent.VerticalSizing verticalSizingOption) {
        super(font, x, y, width, height, editbox, title);
        this.x1 = x;
        this.x2 = width;
        this.y1 = y;
        this.y2 = height;
        this.horizontalSizingOption = horizontalSizingOption;
        this.verticalSizingOption = verticalSizingOption;
    }

    @Override
    public int getX1() {
        return this.x1;
    }

    @Override
    public int getX2() {
        return this.x2;
    }

    @Override
    public int getY1() {
        return this.y1;
    }

    @Override
    public int getY2() {
        return this.y2;
    }

    @Override
    public ResizableComponent.HorizontalSizing getHorizontalSizingOption() {
        return this.horizontalSizingOption;
    }

    @Override
    public ResizableComponent.VerticalSizing getVerticalSizingOption() {
        return this.verticalSizingOption;
    }
}
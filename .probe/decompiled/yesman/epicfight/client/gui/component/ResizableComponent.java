package yesman.epicfight.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ResizableComponent extends GuiEventListener, NarratableEntry {

    default void resize(ScreenRectangle screenRectangle) {
        if (this.getHorizontalSizingOption() != null) {
            this.getHorizontalSizingOption().resizeFunction.resize(this, screenRectangle, this.getX1(), this.getX2());
        }
        if (this.getVerticalSizingOption() != null) {
            this.getVerticalSizingOption().resizeFunction.resize(this, screenRectangle, this.getY1(), this.getY2());
        }
    }

    default void relocateX(int x) {
        this.setX(x);
    }

    default void relocateY(int y) {
        this.setY(y);
    }

    int getX1();

    int getX2();

    int getY1();

    int getY2();

    ResizableComponent.HorizontalSizing getHorizontalSizingOption();

    ResizableComponent.VerticalSizing getVerticalSizingOption();

    void render(GuiGraphics var1, int var2, int var3, float var4);

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setX(int var1);

    void setY(int var1);

    void setWidth(int var1);

    void setHeight(int var1);

    public static enum HorizontalSizing {

        LEFT_WIDTH((component, screenRectangle, v1, v2) -> {
            component.setX(v1);
            component.setWidth(v2);
        }), LEFT_RIGHT((component, screenRectangle, v1, v2) -> {
            int end = screenRectangle.right() - v2;
            int width = Math.max(end - v1, 0);
            component.setX(v1);
            component.setWidth(width);
        }), WIDTH_RIGHT((component, screenRectangle, v1, v2) -> {
            int end = screenRectangle.right() - v2;
            int start = Math.max(end - v1, 0);
            component.setX(start);
            component.setWidth(v1);
        });

        ResizableComponent.ResizeFunction resizeFunction;

        private HorizontalSizing(ResizableComponent.ResizeFunction resizeFunction) {
            this.resizeFunction = resizeFunction;
        }
    }

    @FunctionalInterface
    public interface ResizeFunction {

        void resize(ResizableComponent var1, ScreenRectangle var2, int var3, int var4);
    }

    public static enum VerticalSizing {

        TOP_HEIGHT((component, screenRectangle, v1, v2) -> {
            component.setY(v1);
            component.setHeight(v2);
        }), TOP_BOTTOM((component, screenRectangle, v1, v2) -> {
            int end = screenRectangle.bottom() - v2;
            int height = Math.max(end - v1, 0);
            component.setY(v1);
            component.setHeight(height);
        }), HEIGHT_BOTTOM((component, screenRectangle, v1, v2) -> {
            int end = screenRectangle.bottom() - v2;
            int start = Math.max(end - v1, 0);
            component.setY(start);
            component.setHeight(v1);
        });

        ResizableComponent.ResizeFunction resizeFunction;

        private VerticalSizing(ResizableComponent.ResizeFunction resizeFunction) {
            this.resizeFunction = resizeFunction;
        }
    }
}
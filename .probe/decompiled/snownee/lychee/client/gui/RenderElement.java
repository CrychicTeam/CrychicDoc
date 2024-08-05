package snownee.lychee.client.gui;

import net.minecraft.client.gui.GuiGraphics;

public abstract class RenderElement implements ScreenElement {

    public static final RenderElement EMPTY = new RenderElement() {

        @Override
        public void render(GuiGraphics graphics) {
        }
    };

    protected int width = 16;

    protected int height = 16;

    protected float x = 0.0F;

    protected float y = 0.0F;

    protected float z = 0.0F;

    protected float alpha = 1.0F;

    public static RenderElement of(ScreenElement renderable) {
        return new RenderElement.SimpleRenderElement(renderable);
    }

    public <T extends RenderElement> T at(float x, float y) {
        this.x = x;
        this.y = y;
        return (T) this;
    }

    public <T extends RenderElement> T at(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return (T) this;
    }

    public <T extends RenderElement> T withBounds(int width, int height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    public <T extends RenderElement> T withAlpha(float alpha) {
        this.alpha = alpha;
        return (T) this;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public abstract void render(GuiGraphics var1);

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        this.<RenderElement>at((float) x, (float) y).render(graphics);
    }

    public static class SimpleRenderElement extends RenderElement {

        private ScreenElement renderable;

        public SimpleRenderElement(ScreenElement renderable) {
            this.renderable = renderable;
        }

        @Override
        public void render(GuiGraphics graphics) {
            this.renderable.render(graphics, (int) this.x, (int) this.y);
        }
    }
}
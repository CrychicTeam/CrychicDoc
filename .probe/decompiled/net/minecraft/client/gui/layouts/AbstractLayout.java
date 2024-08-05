package net.minecraft.client.gui.layouts;

import net.minecraft.util.Mth;

public abstract class AbstractLayout implements Layout {

    private int x;

    private int y;

    protected int width;

    protected int height;

    public AbstractLayout(int int0, int int1, int int2, int int3) {
        this.x = int0;
        this.y = int1;
        this.width = int2;
        this.height = int3;
    }

    @Override
    public void setX(int int0) {
        this.m_264090_(p_265043_ -> {
            int $$2 = p_265043_.getX() + (int0 - this.getX());
            p_265043_.setX($$2);
        });
        this.x = int0;
    }

    @Override
    public void setY(int int0) {
        this.m_264090_(p_265586_ -> {
            int $$2 = p_265586_.getY() + (int0 - this.getY());
            p_265586_.setY($$2);
        });
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

    protected abstract static class AbstractChildWrapper {

        public final LayoutElement child;

        public final LayoutSettings.LayoutSettingsImpl layoutSettings;

        protected AbstractChildWrapper(LayoutElement layoutElement0, LayoutSettings layoutSettings1) {
            this.child = layoutElement0;
            this.layoutSettings = layoutSettings1.getExposed();
        }

        public int getHeight() {
            return this.child.getHeight() + this.layoutSettings.paddingTop + this.layoutSettings.paddingBottom;
        }

        public int getWidth() {
            return this.child.getWidth() + this.layoutSettings.paddingLeft + this.layoutSettings.paddingRight;
        }

        public void setX(int int0, int int1) {
            float $$2 = (float) this.layoutSettings.paddingLeft;
            float $$3 = (float) (int1 - this.child.getWidth() - this.layoutSettings.paddingRight);
            int $$4 = (int) Mth.lerp(this.layoutSettings.xAlignment, $$2, $$3);
            this.child.setX($$4 + int0);
        }

        public void setY(int int0, int int1) {
            float $$2 = (float) this.layoutSettings.paddingTop;
            float $$3 = (float) (int1 - this.child.getHeight() - this.layoutSettings.paddingBottom);
            int $$4 = Math.round(Mth.lerp(this.layoutSettings.yAlignment, $$2, $$3));
            this.child.setY($$4 + int0);
        }
    }
}
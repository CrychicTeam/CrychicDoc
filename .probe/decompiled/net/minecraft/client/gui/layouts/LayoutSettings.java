package net.minecraft.client.gui.layouts;

public interface LayoutSettings {

    LayoutSettings padding(int var1);

    LayoutSettings padding(int var1, int var2);

    LayoutSettings padding(int var1, int var2, int var3, int var4);

    LayoutSettings paddingLeft(int var1);

    LayoutSettings paddingTop(int var1);

    LayoutSettings paddingRight(int var1);

    LayoutSettings paddingBottom(int var1);

    LayoutSettings paddingHorizontal(int var1);

    LayoutSettings paddingVertical(int var1);

    LayoutSettings align(float var1, float var2);

    LayoutSettings alignHorizontally(float var1);

    LayoutSettings alignVertically(float var1);

    default LayoutSettings alignHorizontallyLeft() {
        return this.alignHorizontally(0.0F);
    }

    default LayoutSettings alignHorizontallyCenter() {
        return this.alignHorizontally(0.5F);
    }

    default LayoutSettings alignHorizontallyRight() {
        return this.alignHorizontally(1.0F);
    }

    default LayoutSettings alignVerticallyTop() {
        return this.alignVertically(0.0F);
    }

    default LayoutSettings alignVerticallyMiddle() {
        return this.alignVertically(0.5F);
    }

    default LayoutSettings alignVerticallyBottom() {
        return this.alignVertically(1.0F);
    }

    LayoutSettings copy();

    LayoutSettings.LayoutSettingsImpl getExposed();

    static LayoutSettings defaults() {
        return new LayoutSettings.LayoutSettingsImpl();
    }

    public static class LayoutSettingsImpl implements LayoutSettings {

        public int paddingLeft;

        public int paddingTop;

        public int paddingRight;

        public int paddingBottom;

        public float xAlignment;

        public float yAlignment;

        public LayoutSettingsImpl() {
        }

        public LayoutSettingsImpl(LayoutSettings.LayoutSettingsImpl layoutSettingsLayoutSettingsImpl0) {
            this.paddingLeft = layoutSettingsLayoutSettingsImpl0.paddingLeft;
            this.paddingTop = layoutSettingsLayoutSettingsImpl0.paddingTop;
            this.paddingRight = layoutSettingsLayoutSettingsImpl0.paddingRight;
            this.paddingBottom = layoutSettingsLayoutSettingsImpl0.paddingBottom;
            this.xAlignment = layoutSettingsLayoutSettingsImpl0.xAlignment;
            this.yAlignment = layoutSettingsLayoutSettingsImpl0.yAlignment;
        }

        public LayoutSettings.LayoutSettingsImpl padding(int int0) {
            return this.padding(int0, int0);
        }

        public LayoutSettings.LayoutSettingsImpl padding(int int0, int int1) {
            return this.paddingHorizontal(int0).paddingVertical(int1);
        }

        public LayoutSettings.LayoutSettingsImpl padding(int int0, int int1, int int2, int int3) {
            return this.paddingLeft(int0).paddingRight(int2).paddingTop(int1).paddingBottom(int3);
        }

        public LayoutSettings.LayoutSettingsImpl paddingLeft(int int0) {
            this.paddingLeft = int0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl paddingTop(int int0) {
            this.paddingTop = int0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl paddingRight(int int0) {
            this.paddingRight = int0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl paddingBottom(int int0) {
            this.paddingBottom = int0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl paddingHorizontal(int int0) {
            return this.paddingLeft(int0).paddingRight(int0);
        }

        public LayoutSettings.LayoutSettingsImpl paddingVertical(int int0) {
            return this.paddingTop(int0).paddingBottom(int0);
        }

        public LayoutSettings.LayoutSettingsImpl align(float float0, float float1) {
            this.xAlignment = float0;
            this.yAlignment = float1;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl alignHorizontally(float float0) {
            this.xAlignment = float0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl alignVertically(float float0) {
            this.yAlignment = float0;
            return this;
        }

        public LayoutSettings.LayoutSettingsImpl copy() {
            return new LayoutSettings.LayoutSettingsImpl(this);
        }

        @Override
        public LayoutSettings.LayoutSettingsImpl getExposed() {
            return this;
        }
    }
}
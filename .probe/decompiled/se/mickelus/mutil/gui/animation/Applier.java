package se.mickelus.mutil.gui.animation;

import se.mickelus.mutil.gui.GuiElement;

public abstract class Applier {

    protected GuiElement element;

    protected boolean relativeStart;

    protected boolean relativeTarget;

    protected float startOffset = 0.0F;

    protected float targetOffset = 0.0F;

    protected float startValue;

    protected float targetValue;

    protected float currentValue;

    public Applier(float targetValue) {
        this(0.0F, targetValue, true, false);
    }

    public Applier(float startValue, float targetValue) {
        this(startValue, targetValue, false, false);
    }

    public Applier(float startValue, float targetValue, boolean relativeStart, boolean relativeTarget) {
        this.targetValue = targetValue;
        this.startValue = startValue;
        this.relativeStart = relativeStart;
        this.relativeTarget = relativeTarget;
        this.startOffset = startValue;
        this.targetOffset = targetValue;
    }

    public void setElement(GuiElement element) {
        this.element = element;
    }

    public void start(int duration) {
        if (this.relativeStart) {
            this.startValue = this.getRelativeValue() + this.startOffset;
        }
        if (this.relativeTarget) {
            this.targetValue = this.getRelativeValue() + this.targetOffset;
        }
        this.currentValue = this.startValue;
    }

    protected abstract float getRelativeValue();

    public void preDraw(float progress) {
        this.currentValue = this.startValue + progress * (this.targetValue - this.startValue);
    }

    public static class Opacity extends Applier {

        public Opacity(float targetValue) {
            super(targetValue);
        }

        public Opacity(float startValue, float targetValue) {
            super(startValue, targetValue);
        }

        public Opacity(float startValue, float targetValue, boolean relative) {
            super(startValue, targetValue, relative, relative);
        }

        public Opacity(float startValue, float targetValue, boolean relativeStart, boolean relativeTarget) {
            super(startValue, targetValue, relativeStart, relativeTarget);
        }

        @Override
        public void start(int duration) {
            super.start(duration);
            if (!this.relativeStart) {
                this.element.setOpacity((float) ((int) this.startValue));
            }
        }

        @Override
        protected float getRelativeValue() {
            return this.element.getOpacity();
        }

        @Override
        public void preDraw(float progress) {
            super.preDraw(progress);
            this.element.setOpacity(this.currentValue);
        }
    }

    public static class TranslateX extends Applier {

        public TranslateX(float targetValue) {
            super(targetValue);
        }

        public TranslateX(float startValue, float targetValue) {
            super(startValue, targetValue);
        }

        public TranslateX(float startValue, float targetValue, boolean relative) {
            super(startValue, targetValue, relative, relative);
        }

        public TranslateX(float startValue, float targetValue, boolean relativeStart, boolean relativeTarget) {
            super(startValue, targetValue, relativeStart, relativeTarget);
        }

        @Override
        public void start(int duration) {
            super.start(duration);
            if (!this.relativeStart) {
                this.element.setX((int) this.startValue);
            }
        }

        @Override
        protected float getRelativeValue() {
            return (float) this.element.getX();
        }

        @Override
        public void preDraw(float progress) {
            super.preDraw(progress);
            this.element.setX((int) this.currentValue);
        }
    }

    public static class TranslateY extends Applier {

        public TranslateY(float targetValue) {
            super(targetValue);
        }

        public TranslateY(float startValue, float targetValue) {
            super(startValue, targetValue);
        }

        public TranslateY(float startValue, float targetValue, boolean relative) {
            super(startValue, targetValue, relative, relative);
        }

        public TranslateY(float startValue, float targetValue, boolean relativeStart, boolean relativeTarget) {
            super(startValue, targetValue, relativeStart, relativeTarget);
        }

        @Override
        public void start(int duration) {
            super.start(duration);
            if (!this.relativeStart) {
                this.element.setY((int) this.startValue);
            }
        }

        @Override
        protected float getRelativeValue() {
            return (float) this.element.getY();
        }

        @Override
        public void preDraw(float progress) {
            super.preDraw(progress);
            this.element.setY((int) this.currentValue);
        }
    }

    public static class Width extends Applier {

        public Width(float targetValue) {
            super(targetValue);
        }

        public Width(float startValue, float targetValue) {
            super(startValue, targetValue);
        }

        public Width(float startValue, float targetValue, boolean relative) {
            super(startValue, targetValue, relative, relative);
        }

        public Width(float startValue, float targetValue, boolean relativeStart, boolean relativeTarget) {
            super(startValue, targetValue, relativeStart, relativeTarget);
        }

        @Override
        public void start(int duration) {
            super.start(duration);
            if (!this.relativeStart) {
                this.element.setWidth((int) this.startValue);
            }
        }

        @Override
        protected float getRelativeValue() {
            return (float) this.element.getWidth();
        }

        @Override
        public void preDraw(float progress) {
            super.preDraw(progress);
            this.element.setWidth((int) this.currentValue);
        }
    }
}
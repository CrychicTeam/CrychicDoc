package net.minecraft.client.gui.navigation;

import javax.annotation.Nullable;

public record ScreenRectangle(ScreenPosition f_263846_, int f_263770_, int f_263800_) {

    private final ScreenPosition position;

    private final int width;

    private final int height;

    private static final ScreenRectangle EMPTY = new ScreenRectangle(0, 0, 0, 0);

    public ScreenRectangle(int p_265721_, int p_265116_, int p_265225_, int p_265493_) {
        this(new ScreenPosition(p_265721_, p_265116_), p_265225_, p_265493_);
    }

    public ScreenRectangle(ScreenPosition f_263846_, int f_263770_, int f_263800_) {
        this.position = f_263846_;
        this.width = f_263770_;
        this.height = f_263800_;
    }

    public static ScreenRectangle empty() {
        return EMPTY;
    }

    public static ScreenRectangle of(ScreenAxis p_265648_, int p_265317_, int p_265685_, int p_265218_, int p_265226_) {
        return switch(p_265648_) {
            case HORIZONTAL ->
                new ScreenRectangle(p_265317_, p_265685_, p_265218_, p_265226_);
            case VERTICAL ->
                new ScreenRectangle(p_265685_, p_265317_, p_265226_, p_265218_);
        };
    }

    public ScreenRectangle step(ScreenDirection p_265714_) {
        return new ScreenRectangle(this.position.step(p_265714_), this.width, this.height);
    }

    public int getLength(ScreenAxis p_265463_) {
        return switch(p_265463_) {
            case HORIZONTAL ->
                this.width;
            case VERTICAL ->
                this.height;
        };
    }

    public int getBoundInDirection(ScreenDirection p_265778_) {
        ScreenAxis $$1 = p_265778_.getAxis();
        return p_265778_.isPositive() ? this.position.getCoordinate($$1) + this.getLength($$1) - 1 : this.position.getCoordinate($$1);
    }

    public ScreenRectangle getBorder(ScreenDirection p_265704_) {
        int $$1 = this.getBoundInDirection(p_265704_);
        ScreenAxis $$2 = p_265704_.getAxis().orthogonal();
        int $$3 = this.getBoundInDirection($$2.getNegative());
        int $$4 = this.getLength($$2);
        return of(p_265704_.getAxis(), $$1, $$3, 1, $$4).step(p_265704_);
    }

    public boolean overlaps(ScreenRectangle p_265652_) {
        return this.overlapsInAxis(p_265652_, ScreenAxis.HORIZONTAL) && this.overlapsInAxis(p_265652_, ScreenAxis.VERTICAL);
    }

    public boolean overlapsInAxis(ScreenRectangle p_265306_, ScreenAxis p_265340_) {
        int $$2 = this.getBoundInDirection(p_265340_.getNegative());
        int $$3 = p_265306_.getBoundInDirection(p_265340_.getNegative());
        int $$4 = this.getBoundInDirection(p_265340_.getPositive());
        int $$5 = p_265306_.getBoundInDirection(p_265340_.getPositive());
        return Math.max($$2, $$3) <= Math.min($$4, $$5);
    }

    public int getCenterInAxis(ScreenAxis p_265694_) {
        return (this.getBoundInDirection(p_265694_.getPositive()) + this.getBoundInDirection(p_265694_.getNegative())) / 2;
    }

    @Nullable
    public ScreenRectangle intersection(ScreenRectangle p_276058_) {
        int $$1 = Math.max(this.left(), p_276058_.left());
        int $$2 = Math.max(this.top(), p_276058_.top());
        int $$3 = Math.min(this.right(), p_276058_.right());
        int $$4 = Math.min(this.bottom(), p_276058_.bottom());
        return $$1 < $$3 && $$2 < $$4 ? new ScreenRectangle($$1, $$2, $$3 - $$1, $$4 - $$2) : null;
    }

    public int top() {
        return this.position.y();
    }

    public int bottom() {
        return this.position.y() + this.height;
    }

    public int left() {
        return this.position.x();
    }

    public int right() {
        return this.position.x() + this.width;
    }
}
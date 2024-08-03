package me.jellysquid.mods.sodium.client.util;

public record Dim2i(int x, int y, int width, int height) implements Point2i {

    public int getLimitX() {
        return this.x + this.width;
    }

    public int getLimitY() {
        return this.y + this.height;
    }

    public boolean containsCursor(double x, double y) {
        return x >= (double) this.x && x < (double) this.getLimitX() && y >= (double) this.y && y < (double) this.getLimitY();
    }

    public int getCenterX() {
        return this.x + this.width / 2;
    }

    public int getCenterY() {
        return this.y + this.height / 2;
    }

    public Dim2i withHeight(int newHeight) {
        return new Dim2i(this.x, this.y, this.width, newHeight);
    }

    public Dim2i withWidth(int newWidth) {
        return new Dim2i(this.x, this.y, newWidth, this.height);
    }

    public Dim2i withX(int newX) {
        return new Dim2i(newX, this.y, this.width, this.height);
    }

    public Dim2i withY(int newY) {
        return new Dim2i(this.x, newY, this.width, this.height);
    }

    public boolean canFitDimension(Dim2i anotherDim) {
        return this.x() <= anotherDim.x() && this.y() <= anotherDim.y() && this.getLimitX() >= anotherDim.getLimitX() && this.getLimitY() >= anotherDim.getLimitY();
    }

    public boolean overlapsWith(Dim2i other) {
        return this.x() < other.getLimitX() && this.getLimitX() > other.x() && this.y() < other.getLimitY() && this.getLimitY() > other.y();
    }

    public Dim2i withParentOffset(Point2i parent) {
        return new Dim2i(parent.x() + this.x, parent.y() + this.y, this.width, this.height);
    }
}
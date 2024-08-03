package net.minecraft.client.renderer;

public class Rect2i {

    private int xPos;

    private int yPos;

    private int width;

    private int height;

    public Rect2i(int int0, int int1, int int2, int int3) {
        this.xPos = int0;
        this.yPos = int1;
        this.width = int2;
        this.height = int3;
    }

    public Rect2i intersect(Rect2i rectI0) {
        int $$1 = this.xPos;
        int $$2 = this.yPos;
        int $$3 = this.xPos + this.width;
        int $$4 = this.yPos + this.height;
        int $$5 = rectI0.getX();
        int $$6 = rectI0.getY();
        int $$7 = $$5 + rectI0.getWidth();
        int $$8 = $$6 + rectI0.getHeight();
        this.xPos = Math.max($$1, $$5);
        this.yPos = Math.max($$2, $$6);
        this.width = Math.max(0, Math.min($$3, $$7) - this.xPos);
        this.height = Math.max(0, Math.min($$4, $$8) - this.yPos);
        return this;
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    public void setX(int int0) {
        this.xPos = int0;
    }

    public void setY(int int0) {
        this.yPos = int0;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int int0) {
        this.width = int0;
    }

    public void setHeight(int int0) {
        this.height = int0;
    }

    public void setPosition(int int0, int int1) {
        this.xPos = int0;
        this.yPos = int1;
    }

    public boolean contains(int int0, int int1) {
        return int0 >= this.xPos && int0 <= this.xPos + this.width && int1 >= this.yPos && int1 <= this.yPos + this.height;
    }
}
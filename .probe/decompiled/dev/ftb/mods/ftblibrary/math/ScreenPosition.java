package dev.ftb.mods.ftblibrary.math;

import net.minecraft.util.StringRepresentable;

public enum ScreenPosition implements StringRepresentable {

    CENTER("center", 0, 0),
    TOP("top", 0, -1),
    BOTTOM("bottom", 0, 1),
    LEFT("left", -1, 0),
    RIGHT("right", 1, 0),
    TOP_LEFT("top_left", -1, -1),
    TOP_RIGHT("top_right", 1, -1),
    BOTTOM_LEFT("bottom_left", -1, 1),
    BOTTOM_RIGHT("bottom_right", 1, 1);

    private final String name;

    private final int offsetX;

    private final int offsetY;

    private ScreenPosition(String n, int ox, int oy) {
        this.name = n;
        this.offsetX = ox;
        this.offsetY = oy;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public int getX(int screenWidth, int width, int offset) {
        return switch(this.offsetX) {
            case -1 ->
                offset;
            case 1 ->
                (screenWidth - width) / 2;
            default ->
                screenWidth - width - offset;
        };
    }

    public int getY(int screenHeight, int height, int offset) {
        return switch(this.offsetY) {
            case -1 ->
                offset;
            case 1 ->
                (screenHeight - height) / 2;
            default ->
                screenHeight - height - offset;
        };
    }
}
package com.github.alexthe666.citadel.repack.jcodec.common.model;

public class Rect {

    private int x;

    private int y;

    private int width;

    private int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.height;
        result = 31 * result + this.width;
        result = 31 * result + this.x;
        return 31 * result + this.y;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Rect other = (Rect) obj;
            if (this.height != other.height) {
                return false;
            } else if (this.width != other.width) {
                return false;
            } else {
                return this.x != other.x ? false : this.y == other.y;
            }
        }
    }

    public String toString() {
        return "Rect [x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + "]";
    }
}
package com.github.alexthe666.citadel.repack.jcodec.common.model;

public class Size {

    private int width;

    private int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
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
        return 31 * result + this.width;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Size other = (Size) obj;
            return this.height != other.height ? false : this.width == other.width;
        }
    }
}
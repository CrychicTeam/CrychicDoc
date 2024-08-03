package com.github.alexthe666.citadel.repack.jcodec.common.model;

public class Plane {

    int[] data;

    Size size;

    public Plane(int[] data, Size size) {
        this.data = data;
        this.size = size;
    }

    public int[] getData() {
        return this.data;
    }

    public Size getSize() {
        return this.size;
    }
}
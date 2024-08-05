package com.mojang.blaze3d.vertex;

public abstract class DefaultedVertexConsumer implements VertexConsumer {

    protected boolean defaultColorSet;

    protected int defaultR = 255;

    protected int defaultG = 255;

    protected int defaultB = 255;

    protected int defaultA = 255;

    @Override
    public void defaultColor(int int0, int int1, int int2, int int3) {
        this.defaultR = int0;
        this.defaultG = int1;
        this.defaultB = int2;
        this.defaultA = int3;
        this.defaultColorSet = true;
    }

    @Override
    public void unsetDefaultColor() {
        this.defaultColorSet = false;
    }
}
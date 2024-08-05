package com.github.alexthe666.alexsmobs.entity.util;

public enum AnacondaPartIndex {

    HEAD(0.0F), NECK(0.5F), BODY(0.5F), TAIL(0.4F);

    private final float backOffset;

    private AnacondaPartIndex(float partOffset) {
        this.backOffset = partOffset;
    }

    public static AnacondaPartIndex fromOrdinal(int i) {
        return switch(i) {
            case 0 ->
                HEAD;
            case 1 ->
                NECK;
            default ->
                BODY;
            case 3 ->
                TAIL;
        };
    }

    public static AnacondaPartIndex sizeAt(int bodyindex) {
        return switch(bodyindex) {
            case 0 ->
                HEAD;
            case 1, 5, 6 ->
                NECK;
            default ->
                BODY;
            case 7 ->
                TAIL;
        };
    }

    public float getBackOffset() {
        return this.backOffset;
    }
}
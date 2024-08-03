package com.github.alexthe666.alexsmobs.entity.util;

public class Maths {

    public static final float QUARTER_PI = (float) (Math.PI / 4);

    public static final float STARTING_ANGLE = (float) (Math.PI / 180.0);

    public static final float THREE_STARTING_ANGLE = 0.05235988F;

    public static final float EIGHT_STARTING_ANGLE = 0.13962634F;

    public static float rad(double deg) {
        return (float) Math.toRadians(deg);
    }
}
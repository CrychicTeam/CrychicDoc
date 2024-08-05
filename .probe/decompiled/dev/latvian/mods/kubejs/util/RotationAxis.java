package dev.latvian.mods.kubejs.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum RotationAxis {

    XN(f -> new Quaternionf().rotationX(-f), new Vector3f(-1.0F, 0.0F, 0.0F)),
    XP(f -> new Quaternionf().rotationX(f), new Vector3f(1.0F, 0.0F, 0.0F)),
    YN(f -> new Quaternionf().rotationY(-f), new Vector3f(0.0F, -1.0F, 0.0F)),
    YP(f -> new Quaternionf().rotationY(f), new Vector3f(0.0F, 1.0F, 0.0F)),
    ZN(f -> new Quaternionf().rotationZ(-f), new Vector3f(0.0F, 0.0F, -1.0F)),
    ZP(f -> new Quaternionf().rotationZ(f), new Vector3f(0.0F, 0.0F, 1.0F));

    private final RotationAxis.Func func;

    public final Vector3f vec;

    private RotationAxis(RotationAxis.Func func, Vector3f vec) {
        this.func = func;
        this.vec = vec;
    }

    public Quaternionf rad(float f) {
        return this.func.rotation(f);
    }

    public Quaternionf deg(float f) {
        return this.func.rotation(f * (float) (Math.PI / 180.0));
    }

    private interface Func {

        Quaternionf rotation(float var1);
    }
}
package com.mojang.math;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Quaternionf;

public record GivensParameters(float f_276143_, float f_276137_) {

    private final float sinHalf;

    private final float cosHalf;

    public GivensParameters(float f_276143_, float f_276137_) {
        this.sinHalf = f_276143_;
        this.cosHalf = f_276137_;
    }

    public static GivensParameters fromUnnormalized(float p_276277_, float p_276305_) {
        float $$2 = Math.invsqrt(p_276277_ * p_276277_ + p_276305_ * p_276305_);
        return new GivensParameters($$2 * p_276277_, $$2 * p_276305_);
    }

    public static GivensParameters fromPositiveAngle(float p_276260_) {
        float $$1 = Math.sin(p_276260_ / 2.0F);
        float $$2 = Math.cosFromSin($$1, p_276260_ / 2.0F);
        return new GivensParameters($$1, $$2);
    }

    public GivensParameters inverse() {
        return new GivensParameters(-this.sinHalf, this.cosHalf);
    }

    public Quaternionf aroundX(Quaternionf p_276271_) {
        return p_276271_.set(this.sinHalf, 0.0F, 0.0F, this.cosHalf);
    }

    public Quaternionf aroundY(Quaternionf p_276323_) {
        return p_276323_.set(0.0F, this.sinHalf, 0.0F, this.cosHalf);
    }

    public Quaternionf aroundZ(Quaternionf p_276281_) {
        return p_276281_.set(0.0F, 0.0F, this.sinHalf, this.cosHalf);
    }

    public float cos() {
        return this.cosHalf * this.cosHalf - this.sinHalf * this.sinHalf;
    }

    public float sin() {
        return 2.0F * this.sinHalf * this.cosHalf;
    }

    public Matrix3f aroundX(Matrix3f p_276268_) {
        p_276268_.m01 = 0.0F;
        p_276268_.m02 = 0.0F;
        p_276268_.m10 = 0.0F;
        p_276268_.m20 = 0.0F;
        float $$1 = this.cos();
        float $$2 = this.sin();
        p_276268_.m11 = $$1;
        p_276268_.m22 = $$1;
        p_276268_.m12 = $$2;
        p_276268_.m21 = -$$2;
        p_276268_.m00 = 1.0F;
        return p_276268_;
    }

    public Matrix3f aroundY(Matrix3f p_276274_) {
        p_276274_.m01 = 0.0F;
        p_276274_.m10 = 0.0F;
        p_276274_.m12 = 0.0F;
        p_276274_.m21 = 0.0F;
        float $$1 = this.cos();
        float $$2 = this.sin();
        p_276274_.m00 = $$1;
        p_276274_.m22 = $$1;
        p_276274_.m02 = -$$2;
        p_276274_.m20 = $$2;
        p_276274_.m11 = 1.0F;
        return p_276274_;
    }

    public Matrix3f aroundZ(Matrix3f p_276317_) {
        p_276317_.m02 = 0.0F;
        p_276317_.m12 = 0.0F;
        p_276317_.m20 = 0.0F;
        p_276317_.m21 = 0.0F;
        float $$1 = this.cos();
        float $$2 = this.sin();
        p_276317_.m00 = $$1;
        p_276317_.m11 = $$1;
        p_276317_.m01 = $$2;
        p_276317_.m10 = -$$2;
        p_276317_.m22 = 1.0F;
        return p_276317_;
    }
}
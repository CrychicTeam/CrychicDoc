package com.simibubi.create.foundation.collision;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class Matrix3d {

    double m00;

    double m01;

    double m02;

    double m10;

    double m11;

    double m12;

    double m20;

    double m21;

    double m22;

    float[] conversionBuffer = new float[16];

    public Matrix3d asIdentity() {
        this.m00 = this.m11 = this.m22 = 1.0;
        this.m01 = this.m02 = this.m10 = this.m12 = this.m20 = this.m21 = 0.0;
        return this;
    }

    public Matrix3d asXRotation(float radians) {
        this.asIdentity();
        if (radians == 0.0F) {
            return this;
        } else {
            double s = (double) Mth.sin(radians);
            double c = (double) Mth.cos(radians);
            this.m22 = this.m11 = c;
            this.m21 = s;
            this.m12 = -s;
            return this;
        }
    }

    public Matrix3d asYRotation(float radians) {
        this.asIdentity();
        if (radians == 0.0F) {
            return this;
        } else {
            double s = (double) Mth.sin(radians);
            double c = (double) Mth.cos(radians);
            this.m00 = this.m22 = c;
            this.m02 = s;
            this.m20 = -s;
            return this;
        }
    }

    public Matrix3d asZRotation(float radians) {
        this.asIdentity();
        if (radians == 0.0F) {
            return this;
        } else {
            double s = (double) Mth.sin(radians);
            double c = (double) Mth.cos(radians);
            this.m00 = this.m11 = c;
            this.m01 = -s;
            this.m10 = s;
            return this;
        }
    }

    public Matrix3d transpose() {
        double d = this.m01;
        this.m01 = this.m10;
        this.m10 = d;
        d = this.m02;
        this.m02 = this.m20;
        this.m20 = d;
        d = this.m12;
        this.m12 = this.m21;
        this.m21 = d;
        return this;
    }

    public Matrix3d scale(double d) {
        this.m00 *= d;
        this.m11 *= d;
        this.m22 *= d;
        return this;
    }

    public Matrix3d add(Matrix3d matrix) {
        this.m00 = this.m00 + matrix.m00;
        this.m01 = this.m01 + matrix.m01;
        this.m02 = this.m02 + matrix.m02;
        this.m10 = this.m10 + matrix.m10;
        this.m11 = this.m11 + matrix.m11;
        this.m12 = this.m12 + matrix.m12;
        this.m20 = this.m20 + matrix.m20;
        this.m21 = this.m21 + matrix.m21;
        this.m22 = this.m22 + matrix.m22;
        return this;
    }

    public Matrix3d multiply(Matrix3d m) {
        double new00 = this.m00 * m.m00 + this.m01 * m.m10 + this.m02 * m.m20;
        double new01 = this.m00 * m.m01 + this.m01 * m.m11 + this.m02 * m.m21;
        double new02 = this.m00 * m.m02 + this.m01 * m.m12 + this.m02 * m.m22;
        double new10 = this.m10 * m.m00 + this.m11 * m.m10 + this.m12 * m.m20;
        double new11 = this.m10 * m.m01 + this.m11 * m.m11 + this.m12 * m.m21;
        double new12 = this.m10 * m.m02 + this.m11 * m.m12 + this.m12 * m.m22;
        double new20 = this.m20 * m.m00 + this.m21 * m.m10 + this.m22 * m.m20;
        double new21 = this.m20 * m.m01 + this.m21 * m.m11 + this.m22 * m.m21;
        double new22 = this.m20 * m.m02 + this.m21 * m.m12 + this.m22 * m.m22;
        this.m00 = new00;
        this.m01 = new01;
        this.m02 = new02;
        this.m10 = new10;
        this.m11 = new11;
        this.m12 = new12;
        this.m20 = new20;
        this.m21 = new21;
        this.m22 = new22;
        return this;
    }

    public Vec3 transform(Vec3 vec) {
        double x = vec.x * this.m00 + vec.y * this.m01 + vec.z * this.m02;
        double y = vec.x * this.m10 + vec.y * this.m11 + vec.z * this.m12;
        double z = vec.x * this.m20 + vec.y * this.m21 + vec.z * this.m22;
        return new Vec3(x, y, z);
    }

    public Matrix3d copy() {
        return new Matrix3d().add(this);
    }

    @OnlyIn(Dist.CLIENT)
    public Matrix4f getAsMatrix4f() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.conversionBuffer[j * 4 + i] = i == j ? 1.0F : 0.0F;
            }
        }
        this.conversionBuffer[0] = (float) this.m00;
        this.conversionBuffer[1] = (float) this.m01;
        this.conversionBuffer[2] = (float) this.m02;
        this.conversionBuffer[4] = (float) this.m10;
        this.conversionBuffer[5] = (float) this.m11;
        this.conversionBuffer[6] = (float) this.m12;
        this.conversionBuffer[8] = (float) this.m20;
        this.conversionBuffer[9] = (float) this.m21;
        this.conversionBuffer[10] = (float) this.m22;
        return new Matrix4f().setTransposed(this.conversionBuffer);
    }
}
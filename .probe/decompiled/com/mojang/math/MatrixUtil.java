package com.mojang.math;

import org.apache.commons.lang3.tuple.Triple;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixUtil {

    private static final float G = 3.0F + 2.0F * Math.sqrt(2.0F);

    private static final GivensParameters PI_4 = GivensParameters.fromPositiveAngle((float) (java.lang.Math.PI / 4));

    private MatrixUtil() {
    }

    public static Matrix4f mulComponentWise(Matrix4f matrixF0, float float1) {
        return matrixF0.set(matrixF0.m00() * float1, matrixF0.m01() * float1, matrixF0.m02() * float1, matrixF0.m03() * float1, matrixF0.m10() * float1, matrixF0.m11() * float1, matrixF0.m12() * float1, matrixF0.m13() * float1, matrixF0.m20() * float1, matrixF0.m21() * float1, matrixF0.m22() * float1, matrixF0.m23() * float1, matrixF0.m30() * float1, matrixF0.m31() * float1, matrixF0.m32() * float1, matrixF0.m33() * float1);
    }

    private static GivensParameters approxGivensQuat(float float0, float float1, float float2) {
        float $$3 = 2.0F * (float0 - float2);
        return G * float1 * float1 < $$3 * $$3 ? GivensParameters.fromUnnormalized(float1, $$3) : PI_4;
    }

    private static GivensParameters qrGivensQuat(float float0, float float1) {
        float $$2 = (float) java.lang.Math.hypot((double) float0, (double) float1);
        float $$3 = $$2 > 1.0E-6F ? float1 : 0.0F;
        float $$4 = Math.abs(float0) + Math.max($$2, 1.0E-6F);
        if (float0 < 0.0F) {
            float $$5 = $$3;
            $$3 = $$4;
            $$4 = $$5;
        }
        return GivensParameters.fromUnnormalized($$3, $$4);
    }

    private static void similarityTransform(Matrix3f matrixF0, Matrix3f matrixF1) {
        matrixF0.mul(matrixF1);
        matrixF1.transpose();
        matrixF1.mul(matrixF0);
        matrixF0.set(matrixF1);
    }

    private static void stepJacobi(Matrix3f matrixF0, Matrix3f matrixF1, Quaternionf quaternionf2, Quaternionf quaternionf3) {
        if (matrixF0.m01 * matrixF0.m01 + matrixF0.m10 * matrixF0.m10 > 1.0E-6F) {
            GivensParameters $$4 = approxGivensQuat(matrixF0.m00, 0.5F * (matrixF0.m01 + matrixF0.m10), matrixF0.m11);
            Quaternionf $$5 = $$4.aroundZ(quaternionf2);
            quaternionf3.mul($$5);
            $$4.aroundZ(matrixF1);
            similarityTransform(matrixF0, matrixF1);
        }
        if (matrixF0.m02 * matrixF0.m02 + matrixF0.m20 * matrixF0.m20 > 1.0E-6F) {
            GivensParameters $$6 = approxGivensQuat(matrixF0.m00, 0.5F * (matrixF0.m02 + matrixF0.m20), matrixF0.m22).inverse();
            Quaternionf $$7 = $$6.aroundY(quaternionf2);
            quaternionf3.mul($$7);
            $$6.aroundY(matrixF1);
            similarityTransform(matrixF0, matrixF1);
        }
        if (matrixF0.m12 * matrixF0.m12 + matrixF0.m21 * matrixF0.m21 > 1.0E-6F) {
            GivensParameters $$8 = approxGivensQuat(matrixF0.m11, 0.5F * (matrixF0.m12 + matrixF0.m21), matrixF0.m22);
            Quaternionf $$9 = $$8.aroundX(quaternionf2);
            quaternionf3.mul($$9);
            $$8.aroundX(matrixF1);
            similarityTransform(matrixF0, matrixF1);
        }
    }

    public static Quaternionf eigenvalueJacobi(Matrix3f matrixF0, int int1) {
        Quaternionf $$2 = new Quaternionf();
        Matrix3f $$3 = new Matrix3f();
        Quaternionf $$4 = new Quaternionf();
        for (int $$5 = 0; $$5 < int1; $$5++) {
            stepJacobi(matrixF0, $$3, $$4, $$2);
        }
        $$2.normalize();
        return $$2;
    }

    public static Triple<Quaternionf, Vector3f, Quaternionf> svdDecompose(Matrix3f matrixF0) {
        Matrix3f $$1 = new Matrix3f(matrixF0);
        $$1.transpose();
        $$1.mul(matrixF0);
        Quaternionf $$2 = eigenvalueJacobi($$1, 5);
        float $$3 = $$1.m00;
        float $$4 = $$1.m11;
        boolean $$5 = (double) $$3 < 1.0E-6;
        boolean $$6 = (double) $$4 < 1.0E-6;
        Matrix3f $$8 = matrixF0.rotate($$2);
        Quaternionf $$9 = new Quaternionf();
        Quaternionf $$10 = new Quaternionf();
        GivensParameters $$11;
        if ($$5) {
            $$11 = qrGivensQuat($$8.m11, -$$8.m10);
        } else {
            $$11 = qrGivensQuat($$8.m00, $$8.m01);
        }
        Quaternionf $$13 = $$11.aroundZ($$10);
        Matrix3f $$14 = $$11.aroundZ($$1);
        $$9.mul($$13);
        $$14.transpose().mul($$8);
        if ($$5) {
            $$11 = qrGivensQuat($$14.m22, -$$14.m20);
        } else {
            $$11 = qrGivensQuat($$14.m00, $$14.m02);
        }
        $$11 = $$11.inverse();
        Quaternionf $$15 = $$11.aroundY($$10);
        Matrix3f $$16 = $$11.aroundY($$8);
        $$9.mul($$15);
        $$16.transpose().mul($$14);
        if ($$6) {
            $$11 = qrGivensQuat($$16.m22, -$$16.m21);
        } else {
            $$11 = qrGivensQuat($$16.m11, $$16.m12);
        }
        Quaternionf $$17 = $$11.aroundX($$10);
        Matrix3f $$18 = $$11.aroundX($$14);
        $$9.mul($$17);
        $$18.transpose().mul($$16);
        Vector3f $$19 = new Vector3f($$18.m00, $$18.m11, $$18.m22);
        return Triple.of($$9, $$19, $$2.conjugate());
    }
}
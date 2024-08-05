package net.caffeinemc.mods.sodium.api.math;

import com.mojang.blaze3d.vertex.PoseStack;
import net.caffeinemc.mods.sodium.api.util.NormI8;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MatrixHelper {

    public static int transformNormal(Matrix3f mat, float x, float y, float z) {
        float nxt = transformNormalX(mat, x, y, z);
        float nyt = transformNormalY(mat, x, y, z);
        float nzt = transformNormalZ(mat, x, y, z);
        return NormI8.pack(nxt, nyt, nzt);
    }

    public static int transformNormal(Matrix3f mat, int norm) {
        float x = NormI8.unpackX(norm);
        float y = NormI8.unpackY(norm);
        float z = NormI8.unpackZ(norm);
        return transformNormal(mat, x, y, z);
    }

    public static float transformNormalX(Matrix3f mat, float x, float y, float z) {
        return mat.m00() * x + mat.m10() * y + mat.m20() * z;
    }

    public static float transformNormalY(Matrix3f mat, float x, float y, float z) {
        return mat.m01() * x + mat.m11() * y + mat.m21() * z;
    }

    public static float transformNormalZ(Matrix3f mat, float x, float y, float z) {
        return mat.m02() * x + mat.m12() * y + mat.m22() * z;
    }

    public static float transformPositionX(Matrix4f mat, float x, float y, float z) {
        return mat.m00() * x + mat.m10() * y + mat.m20() * z + mat.m30();
    }

    public static float transformPositionY(Matrix4f mat, float x, float y, float z) {
        return mat.m01() * x + mat.m11() * y + mat.m21() * z + mat.m31();
    }

    public static float transformPositionZ(Matrix4f mat, float x, float y, float z) {
        return mat.m02() * x + mat.m12() * y + mat.m22() * z + mat.m32();
    }

    public static void rotateZYX(PoseStack.Pose matrices, float angleZ, float angleY, float angleX) {
        matrices.pose().rotateZYX(angleZ, angleY, angleX);
        matrices.normal().rotateZYX(angleZ, angleY, angleX);
    }

    public static int transformNormal(Matrix3f matrix, Direction direction) {
        return switch(direction) {
            case DOWN ->
                NormI8.pack(-matrix.m10, -matrix.m11, -matrix.m12);
            case UP ->
                NormI8.pack(matrix.m10, matrix.m11, matrix.m12);
            case NORTH ->
                NormI8.pack(-matrix.m20, -matrix.m21, -matrix.m22);
            case SOUTH ->
                NormI8.pack(matrix.m20, matrix.m21, matrix.m22);
            case WEST ->
                NormI8.pack(-matrix.m00, -matrix.m01, -matrix.m02);
            case EAST ->
                NormI8.pack(matrix.m00, matrix.m01, matrix.m02);
        };
    }
}
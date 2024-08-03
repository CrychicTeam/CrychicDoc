package me.jellysquid.mods.sodium.client.util;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.util.NormI8;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class ModelQuadUtil {

    public static final int POSITION_INDEX = 0;

    public static final int COLOR_INDEX = 3;

    public static final int TEXTURE_INDEX = 4;

    public static final int LIGHT_INDEX = 6;

    public static final int NORMAL_INDEX = 7;

    public static final int VERTEX_SIZE = 8;

    public static int vertexOffset(int vertexIndex) {
        return vertexIndex * 8;
    }

    public static ModelQuadFacing findNormalFace(float x, float y, float z) {
        if (Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z)) {
            float maxDot = 0.0F;
            Direction closestFace = null;
            for (Direction face : DirectionUtil.ALL_DIRECTIONS) {
                float dot = x * (float) face.getStepX() + y * (float) face.getStepY() + z * (float) face.getStepZ();
                if (dot > maxDot) {
                    maxDot = dot;
                    closestFace = face;
                }
            }
            return closestFace != null && Mth.equal(maxDot, 1.0F) ? ModelQuadFacing.fromDirection(closestFace) : ModelQuadFacing.UNASSIGNED;
        } else {
            return ModelQuadFacing.UNASSIGNED;
        }
    }

    public static ModelQuadFacing findNormalFace(int normal) {
        return findNormalFace(NormI8.unpackX(normal), NormI8.unpackY(normal), NormI8.unpackZ(normal));
    }

    public static int calculateNormal(ModelQuadView quad) {
        float x0 = quad.getX(0);
        float y0 = quad.getY(0);
        float z0 = quad.getZ(0);
        float x1 = quad.getX(1);
        float y1 = quad.getY(1);
        float z1 = quad.getZ(1);
        float x2 = quad.getX(2);
        float y2 = quad.getY(2);
        float z2 = quad.getZ(2);
        float x3 = quad.getX(3);
        float y3 = quad.getY(3);
        float z3 = quad.getZ(3);
        float dx0 = x2 - x0;
        float dy0 = y2 - y0;
        float dz0 = z2 - z0;
        float dx1 = x3 - x1;
        float dy1 = y3 - y1;
        float dz1 = z3 - z1;
        float normX = dy0 * dz1 - dz0 * dy1;
        float normY = dz0 * dx1 - dx0 * dz1;
        float normZ = dx0 * dy1 - dy0 * dx1;
        float l = (float) Math.sqrt((double) (normX * normX + normY * normY + normZ * normZ));
        if (l != 0.0F) {
            normX /= l;
            normY /= l;
            normZ /= l;
        }
        return NormI8.pack(normX, normY, normZ);
    }

    public static int mergeNormal(int packedNormal, int calcNormal) {
        return (packedNormal & 16777215) == 0 ? calcNormal : packedNormal;
    }

    public static int mergeBakedLight(int packedLight, int calcLight) {
        if (packedLight == 0) {
            return calcLight;
        } else {
            int psl = packedLight >> 16 & 0xFF;
            int csl = calcLight >> 16 & 0xFF;
            int pbl = packedLight & 0xFF;
            int cbl = calcLight & 0xFF;
            int bl = Math.max(pbl, cbl);
            int sl = Math.max(psl, csl);
            return sl << 16 | bl;
        }
    }

    public static int mixARGBColors(int colorA, int colorB) {
        if (colorA == -1) {
            return colorB;
        } else if (colorB == -1) {
            return colorA;
        } else {
            int a = (int) ((float) ColorARGB.unpackAlpha(colorA) / 255.0F * ((float) ColorARGB.unpackAlpha(colorB) / 255.0F) * 255.0F);
            int b = (int) ((float) ColorARGB.unpackBlue(colorA) / 255.0F * ((float) ColorARGB.unpackBlue(colorB) / 255.0F) * 255.0F);
            int g = (int) ((float) ColorARGB.unpackGreen(colorA) / 255.0F * ((float) ColorARGB.unpackGreen(colorB) / 255.0F) * 255.0F);
            int r = (int) ((float) ColorARGB.unpackRed(colorA) / 255.0F * ((float) ColorARGB.unpackRed(colorB) / 255.0F) * 255.0F);
            return ColorARGB.pack(r, g, b, a);
        }
    }
}
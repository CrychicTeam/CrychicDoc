package com.mna.tools.math;

import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class MathUtils {

    public static float RandomBetween(float start, float end) {
        return (float) ((double) start + Math.random() * (double) (end - start));
    }

    public static Vector3[] GetHorizontalBlocksInFrontOfCharacter(LivingEntity entity, int numBlocks, int x, int y, int z) {
        float speed = 0.1F;
        float factor = (float) (Math.PI / 180.0);
        float sinYawRadians = Mth.sin(entity.m_146908_() * factor);
        float cosYawRadians = Mth.cos(entity.m_146908_() * factor);
        double motionZ = (double) (cosYawRadians * speed);
        double motionX = (double) (-sinYawRadians * speed);
        double curX = (double) x;
        double curY = (double) y;
        double curZ = (double) z;
        float minimum = 0.01F;
        if (Math.abs(motionX) < (double) minimum) {
            motionX = 0.0;
        }
        if (Math.abs(motionZ) < (double) minimum) {
            motionZ = 0.0;
        }
        int lastX = x;
        int lastY = y;
        int lastZ = z;
        Vector3[] list = new Vector3[numBlocks];
        list[0] = new Vector3((double) x, (double) y, (double) z);
        int count = 1;
        while (count < numBlocks) {
            curX += motionX;
            curZ += motionZ;
            if ((int) Math.round(curX) != lastX || (int) Math.round(curY) != lastY || (int) Math.round(curZ) != lastZ) {
                lastX = (int) Math.round(curX);
                lastY = (int) Math.round(curY);
                lastZ = (int) Math.round(curZ);
                list[count++] = new Vector3((double) lastX, (double) lastY, (double) lastZ);
            }
        }
        return list;
    }

    public static Vector3[] GetBlocksInFrontOfCharacter(LivingEntity entity, int numBlocks, int x, int y, int z) {
        float speed = 0.1F;
        float factor = (float) (Math.PI / 180.0);
        float sinYawRadians = Mth.sin(entity.m_146908_() * factor);
        float cosYawRadians = Mth.cos(entity.m_146908_() * factor);
        float sinPitchRadians = Mth.sin(entity.m_146909_() * factor);
        float cosPitchRadians = Mth.cos(entity.m_146909_() * factor);
        double motionZ = (double) (cosYawRadians * cosPitchRadians * speed);
        double motionX = (double) (-sinYawRadians * cosPitchRadians * speed);
        double motionY = (double) (-sinPitchRadians * speed);
        double curX = (double) x;
        double curY = (double) y;
        double curZ = (double) z;
        float minimum = 0.01F;
        if (Math.abs(motionX) < (double) minimum) {
            motionX = 0.0;
        }
        if (Math.abs(motionY) < (double) minimum) {
            motionY = 0.0;
        }
        if (Math.abs(motionZ) < (double) minimum) {
            motionZ = 0.0;
        }
        int lastX = x;
        int lastY = y;
        int lastZ = z;
        Vector3[] list = new Vector3[numBlocks];
        list[0] = new Vector3((double) x, (double) y, (double) z);
        int count = 1;
        while (count < numBlocks) {
            curX += motionX;
            curY += motionY;
            curZ += motionZ;
            if ((int) Math.round(curX) != lastX || (int) Math.round(curY) != lastY || (int) Math.round(curZ) != lastZ) {
                lastX = (int) Math.round(curX);
                lastY = (int) Math.round(curY);
                lastZ = (int) Math.round(curZ);
                list[count++] = new Vector3((double) lastX, (double) lastY, (double) lastZ);
            }
        }
        return list;
    }

    public static int getDistanceToGround(LivingEntity ent, Level world) {
        int yCoordOffset = 0;
        int distance;
        for (distance = 0; distance < 20 && !world.m_46859_(ent.m_20183_().offset(0, yCoordOffset, 0)); yCoordOffset--) {
            distance++;
        }
        return distance;
    }

    public static Vec3 reflect(Vec3 incident, Vec3 reflectionNormal) {
        Vec3 rn = reflectionNormal.normalize();
        Vec3 i = incident.normalize();
        double scale = 2.0 * rn.dot(i);
        return rn.subtract(i).scale(scale);
    }

    public static float[] colorIntToFloats(int color) {
        return new float[] { (float) (color >> 16 & 0xFF) / 255.0F, (float) (color >> 8 & 0xFF) / 255.0F, (float) (color & 0xFF) / 255.0F };
    }

    public static int colorFloatsToInt(float r, float g, float b) {
        return ((int) (r * 255.0F) << 16) + ((int) (g * 255.0F) << 8) + (int) (b * 255.0F);
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double clamp01(double val) {
        return clamp(val, 0.0, 1.0);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float clamp01(float val) {
        return clamp(val, 0.0F, 1.0F);
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float lerpf(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public static int lerpColor(int color_1, int color_2, float t) {
        float[] a = new float[] { (float) FastColor.ARGB32.alpha(color_1) / 255.0F, (float) FastColor.ARGB32.red(color_1) / 255.0F, (float) FastColor.ARGB32.green(color_1) / 255.0F, (float) FastColor.ARGB32.blue(color_1) / 255.0F };
        float[] b = new float[] { (float) FastColor.ARGB32.alpha(color_2) / 255.0F, (float) FastColor.ARGB32.red(color_2) / 255.0F, (float) FastColor.ARGB32.green(color_2) / 255.0F, (float) FastColor.ARGB32.blue(color_2) / 255.0F };
        return FastColor.ARGB32.color((int) (lerpf(a[0], b[0], t) * 255.0F), (int) (lerpf(a[1], b[1], t) * 255.0F), (int) (lerpf(a[2], b[2], t) * 255.0F), (int) (lerpf(a[3], b[3], t) * 255.0F));
    }

    public static Vec3 lerpVector3d(Vec3 a, Vec3 b, float t) {
        Vec3 diff = b.subtract(a);
        Vec3 scaled = diff.scale((double) t);
        return a.add(scaled);
    }

    public static Vec3 bezierVector3d(Vec3 start, Vec3 end, Vec3 control_1, Vec3 control_2, float time) {
        if (time < 0.0F) {
            time = 0.0F;
        } else if (time > 1.0F) {
            time = 1.0F;
        }
        float one_minus_t = 1.0F - time;
        Vec3 retValue = new Vec3(0.0, 0.0, 0.0);
        Vec3[] terms = new Vec3[] { start.scale((double) (one_minus_t * one_minus_t * one_minus_t)), control_1.scale((double) (3.0F * one_minus_t * one_minus_t * time)), control_2.scale((double) (3.0F * one_minus_t * time * time)), end.scale((double) (time * time * time)) };
        for (int i = 0; i < 4; i++) {
            retValue = retValue.add(terms[i]);
        }
        return retValue;
    }

    public static Vec3 rotateTowards(Vec3 current, Vec3 target, float maxAngleDegrees) {
        Vec3 fromDirection = current.normalize();
        Vec3 toDirection = target.normalize();
        double angleRadians = Math.acos(fromDirection.dot(toDirection));
        double angleDegrees = Math.min(angleRadians * 180.0 / Math.PI, (double) maxAngleDegrees);
        Vec3 axis = fromDirection.cross(toDirection);
        Vector3f vec = new Vector3f((float) current.x, (float) current.y, (float) current.z);
        vec = vec.rotateAxis((float) angleDegrees, (float) axis.x, (float) axis.y, (float) axis.z);
        return new Vec3((double) vec.x(), (double) vec.y(), (double) vec.z());
    }

    public static AABB createInclusiveBB(BlockPos a, BlockPos b) {
        return new AABB(a, b).expandTowards(1.0, 1.0, 1.0);
    }

    public static boolean rotateEntityLookTowards(EntityAnchorArgument.Anchor type, LivingEntity living, Vec3 position, float maxDelta) {
        double d0 = position.x() - living.m_20185_();
        double d2 = position.z() - living.m_20189_();
        double d1 = position.y - living.m_20188_();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
        float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
        Pair<Float, Boolean> xRot = rotlerp(living.m_146909_(), f1, maxDelta);
        Pair<Float, Boolean> yRot = rotlerp(living.m_146908_(), f, maxDelta);
        living.m_146926_((Float) xRot.getFirst());
        living.m_146922_((Float) yRot.getFirst());
        return !(Boolean) yRot.getSecond();
    }

    private static Pair<Float, Boolean> rotlerp(float pAngle, float pTargetAngle, float pMaxIncrease) {
        float f = Mth.wrapDegrees(pTargetAngle - pAngle);
        boolean clamped = false;
        if (f > pMaxIncrease) {
            f = pMaxIncrease;
            clamped = true;
        }
        if (f < -pMaxIncrease) {
            f = -pMaxIncrease;
            clamped = true;
        }
        return new Pair(pAngle + f, clamped);
    }

    public static int weightedRandomNumber(int[] weights) {
        int sum_of_weight = 0;
        for (int i = 0; i < weights.length; i++) {
            sum_of_weight += weights[i];
        }
        int target = (int) ((double) sum_of_weight * Math.random());
        for (int i = 0; i < weights.length; i++) {
            if (target <= weights[i]) {
                return i;
            }
            target -= weights[i];
        }
        return 0;
    }

    public static int weightedRandomNumber(int max) {
        int sum_of_weight = 0;
        for (int i = 0; i <= max; i++) {
            sum_of_weight += i;
        }
        int target = (int) ((double) sum_of_weight * Math.random());
        for (int i = 0; i < max; i++) {
            if (target <= i) {
                return i;
            }
            target -= i;
        }
        return 0;
    }
}
package com.mna.tools.math;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Vector3 {

    public float x;

    public float y;

    public float z;

    public Vector3(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public Vector3(BlockEntity tile) {
        this.x = (float) tile.getBlockPos().m_123341_();
        this.y = (float) tile.getBlockPos().m_123342_();
        this.z = (float) tile.getBlockPos().m_123343_();
    }

    public Vector3(BlockPos pos) {
        this.x = (float) pos.m_123341_();
        this.y = (float) pos.m_123342_();
        this.z = (float) pos.m_123343_();
    }

    public Vector3(Vec3 vec) {
        this.x = (float) vec.x;
        this.y = (float) vec.y;
        this.z = (float) vec.z;
    }

    public Vector3(Entity entity) {
        this(entity.position().x, entity.position().y, entity.position().z);
    }

    public Vector3 add(Vector3 vec) {
        return new Vector3((double) (this.x + vec.x), (double) (this.y + vec.y), (double) (this.z + vec.z));
    }

    public Vector3 sub(Vector3 vec) {
        return new Vector3((double) (this.x - vec.x), (double) (this.y - vec.y), (double) (this.z - vec.z));
    }

    public Vector3 scale(float scale) {
        return this.scale(scale, scale, scale);
    }

    public Vector3 scale(float scalex, float scaley, float scalez) {
        return new Vector3((double) (this.x * scalex), (double) (this.y * scaley), (double) (this.z * scalez));
    }

    public Vector3 modulo(float divisor) {
        return new Vector3((double) (this.x % divisor), (double) (this.y % divisor), (double) (this.z % divisor));
    }

    public Vector3 normalize() {
        float length = this.length();
        return new Vector3((double) (this.x / length), (double) (this.y / length), (double) (this.z / length));
    }

    public Vector3 rotatePitch(float pitch) {
        float f = Mth.cos(pitch);
        float f1 = Mth.sin(pitch);
        double d0 = (double) this.x;
        double d1 = (double) this.y * (double) f + (double) this.z * (double) f1;
        double d2 = (double) this.z * (double) f - (double) this.y * (double) f1;
        return new Vector3(d0, d1, d2);
    }

    public Vector3 rotateYaw(float yaw) {
        float f = Mth.cos(yaw);
        float f1 = Mth.sin(yaw);
        double d0 = (double) this.x * (double) f + (double) this.z * (double) f1;
        double d1 = (double) this.y;
        double d2 = (double) this.z * (double) f - (double) this.x * (double) f1;
        return new Vector3(d0, d1, d2);
    }

    public Vector3 rotate(float angle, Vector3 axis) {
        return Matrix4.eulerAngles((double) angle, axis).translate(this);
    }

    public float length() {
        return (float) Math.sqrt((double) (this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public float lengthPow2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3 copy() {
        return new Vector3((double) this.x, (double) this.y, (double) this.z);
    }

    public static Vector3 crossProduct(Vector3 vec1, Vector3 vec2) {
        return new Vector3((double) (vec1.y * vec2.z - vec1.z * vec2.y), (double) (vec1.z * vec2.x - vec1.x * vec2.z), (double) (vec1.x * vec2.y - vec1.y * vec2.x));
    }

    public static Vector3 xCrossProduct(Vector3 vec) {
        return new Vector3(0.0, (double) vec.z, (double) (-vec.y));
    }

    public static Vector3 zCrossProduct(Vector3 vec) {
        return new Vector3((double) (-vec.y), (double) vec.x, 0.0);
    }

    public static float dotProduct(Vector3 vec1, Vector3 vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }

    public static Quaternionf LookAt(Vector3 source, Vector3 dest) {
        Vector3 forwardVec = dest.sub(source).normalize();
        float dot = dotProduct(forward(), forwardVec);
        if (Math.abs(dot - -1.0F) < 1.0E-6F) {
            return new Quaternionf(0.0F, 1.0F, 0.0F, (float) Math.PI);
        } else if (Math.abs(dot - 1.0F) < 1.0E-6F) {
            return new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        } else {
            float rotationAngle = (float) Math.acos((double) dot);
            Vector3 rotAxis = crossProduct(forward(), forwardVec);
            rotAxis = rotAxis.normalize();
            Quaternionf out = new Quaternionf();
            return out.rotateAxis(rotationAngle, new Vector3f(rotAxis.x, rotAxis.y, rotAxis.z));
        }
    }

    public static float angle(Vector3 vec1, Vector3 vec2) {
        return anglePreNorm(vec1.copy().normalize(), vec2.copy().normalize());
    }

    public static float anglePreNorm(Vector3 vec1, Vector3 vec2) {
        return (float) Math.acos((double) dotProduct(vec1, vec2));
    }

    public static Vector3 zero() {
        return new Vector3(0.0, 0.0, 0.0);
    }

    public String toString() {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    public Vec3 toVec3D() {
        return new Vec3((double) this.x, (double) this.y, (double) this.z);
    }

    public Vector3f toVector3f() {
        return new Vector3f(this.x, this.y, this.z);
    }

    public static Vector3 getPerpendicular(Vector3 vec) {
        return vec.z == 0.0F ? zCrossProduct(vec) : xCrossProduct(vec);
    }

    public static Vector3 lerp(Vector3 a, Vector3 b, float t) {
        Vector3 diff = b.sub(a);
        Vector3 scaled = diff.scale(t);
        return a.add(scaled);
    }

    public static Vector3 bezier(Vector3 start, Vector3 end, Vector3 control_1, Vector3 control_2, float time) {
        if (time < 0.0F) {
            time = 0.0F;
        } else if (time > 1.0F) {
            time = 1.0F;
        }
        float one_minus_t = 1.0F - time;
        Vector3 retValue = new Vector3(0.0, 0.0, 0.0);
        Vector3[] terms = new Vector3[] { start.scale(one_minus_t * one_minus_t * one_minus_t), control_1.scale(3.0F * one_minus_t * one_minus_t * time), control_2.scale(3.0F * one_minus_t * time * time), end.scale(time * time * time) };
        for (int i = 0; i < 4; i++) {
            retValue = retValue.add(terms[i]);
        }
        return retValue;
    }

    public static Vector3 forward() {
        return new Vector3(1.0, 0.0, 0.0);
    }

    public static Vector3 up() {
        return new Vector3(0.0, 1.0, 0.0);
    }

    public static Vector3 right() {
        return new Vector3(0.0, 0.0, 1.0);
    }

    public boolean isZero() {
        return this.x == 0.0F && this.y == 0.0F && this.z == 0.0F;
    }

    public boolean isWithinRange(float min, float max) {
        return this.x >= min && this.x <= max && this.y >= min && this.y <= max && this.z >= min && this.z <= max;
    }

    public double distanceTo(Vector3 target) {
        double var2 = (double) (target.x - this.x);
        double var4 = (double) (target.y - this.y);
        double var6 = (double) (target.z - this.z);
        return (double) Mth.sqrt((float) (var2 * var2 + var4 * var4 + var6 * var6));
    }

    public double distanceSqTo(Vector3 target) {
        double var2 = (double) (target.x - this.x);
        double var4 = (double) (target.y - this.y);
        double var6 = (double) (target.z - this.z);
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public void floorToI() {
        this.x = (float) Math.floor((double) this.x);
        this.y = (float) Math.floor((double) this.y);
        this.z = (float) Math.floor((double) this.z);
    }

    public void roundToI() {
        this.x = (float) Math.round(this.x);
        this.y = (float) Math.round(this.y);
        this.z = (float) Math.round(this.z);
    }

    public void ceilToI() {
        this.x = (float) Math.ceil((double) this.x);
        this.y = (float) Math.ceil((double) this.y);
        this.z = (float) Math.ceil((double) this.z);
    }

    public void writeToNBT(CompoundTag compound) {
        compound.putFloat("Vec3_x", this.x);
        compound.putFloat("Vec3_y", this.y);
        compound.putFloat("Vec3_z", this.z);
    }

    public static Vector3 readFromNBT(CompoundTag compound) {
        return new Vector3((double) compound.getFloat("Vec3_x"), (double) compound.getFloat("Vec3_y"), (double) compound.getFloat("Vec3_z"));
    }

    public boolean equals(Object obj) {
        return !(obj instanceof Vector3 comp) ? false : comp.x == this.x && comp.y == this.y && comp.z == this.z;
    }

    public int hashCode() {
        return (int) (this.x + this.y + this.z);
    }
}
package net.minecraft.core;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;

public class Rotations {

    protected final float x;

    protected final float y;

    protected final float z;

    public Rotations(float float0, float float1, float float2) {
        this.x = !Float.isInfinite(float0) && !Float.isNaN(float0) ? float0 % 360.0F : 0.0F;
        this.y = !Float.isInfinite(float1) && !Float.isNaN(float1) ? float1 % 360.0F : 0.0F;
        this.z = !Float.isInfinite(float2) && !Float.isNaN(float2) ? float2 % 360.0F : 0.0F;
    }

    public Rotations(ListTag listTag0) {
        this(listTag0.getFloat(0), listTag0.getFloat(1), listTag0.getFloat(2));
    }

    public ListTag save() {
        ListTag $$0 = new ListTag();
        $$0.add(FloatTag.valueOf(this.x));
        $$0.add(FloatTag.valueOf(this.y));
        $$0.add(FloatTag.valueOf(this.z));
        return $$0;
    }

    public boolean equals(Object object0) {
        return !(object0 instanceof Rotations $$1) ? false : this.x == $$1.x && this.y == $$1.y && this.z == $$1.z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getWrappedX() {
        return Mth.wrapDegrees(this.x);
    }

    public float getWrappedY() {
        return Mth.wrapDegrees(this.y);
    }

    public float getWrappedZ() {
        return Mth.wrapDegrees(this.z);
    }
}
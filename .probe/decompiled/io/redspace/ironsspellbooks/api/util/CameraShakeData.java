package io.redspace.ironsspellbooks.api.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class CameraShakeData {

    final int duration;

    final float radius;

    int tickCount;

    final Vec3 origin;

    public CameraShakeData(int duration, Vec3 origin, float radius) {
        this.duration = duration;
        this.origin = origin;
        this.radius = radius;
    }

    public void serializeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.duration);
        buf.writeInt(this.tickCount);
        buf.writeInt((int) (this.origin.x * 10.0));
        buf.writeInt((int) (this.origin.y * 10.0));
        buf.writeInt((int) (this.origin.z * 10.0));
        buf.writeInt((int) (this.radius * 10.0F));
    }

    public static CameraShakeData deserializeFromBuffer(FriendlyByteBuf buf) {
        int duration = buf.readInt();
        int tickCount = buf.readInt();
        Vec3 origin = new Vec3((double) ((float) buf.readInt() / 10.0F), (double) ((float) buf.readInt() / 10.0F), (double) ((float) buf.readInt() / 10.0F));
        float radius = (float) buf.readInt() / 10.0F;
        CameraShakeData data = new CameraShakeData(duration, origin, radius);
        data.tickCount = tickCount;
        return data;
    }
}
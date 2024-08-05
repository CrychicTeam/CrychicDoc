package net.minecraft.world.level.portal;

import net.minecraft.world.phys.Vec3;

public class PortalInfo {

    public final Vec3 pos;

    public final Vec3 speed;

    public final float yRot;

    public final float xRot;

    public PortalInfo(Vec3 vec0, Vec3 vec1, float float2, float float3) {
        this.pos = vec0;
        this.speed = vec1;
        this.yRot = float2;
        this.xRot = float3;
    }
}
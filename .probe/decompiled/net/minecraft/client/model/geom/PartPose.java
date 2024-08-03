package net.minecraft.client.model.geom;

public class PartPose {

    public static final PartPose ZERO = offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

    public final float x;

    public final float y;

    public final float z;

    public final float xRot;

    public final float yRot;

    public final float zRot;

    private PartPose(float float0, float float1, float float2, float float3, float float4, float float5) {
        this.x = float0;
        this.y = float1;
        this.z = float2;
        this.xRot = float3;
        this.yRot = float4;
        this.zRot = float5;
    }

    public static PartPose offset(float float0, float float1, float float2) {
        return offsetAndRotation(float0, float1, float2, 0.0F, 0.0F, 0.0F);
    }

    public static PartPose rotation(float float0, float float1, float float2) {
        return offsetAndRotation(0.0F, 0.0F, 0.0F, float0, float1, float2);
    }

    public static PartPose offsetAndRotation(float float0, float float1, float float2, float float3, float float4, float float5) {
        return new PartPose(float0, float1, float2, float3, float4, float5);
    }
}
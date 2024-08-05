package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SquidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Squid;

public class SquidRenderer<T extends Squid> extends MobRenderer<T, SquidModel<T>> {

    private static final ResourceLocation SQUID_LOCATION = new ResourceLocation("textures/entity/squid/squid.png");

    public SquidRenderer(EntityRendererProvider.Context entityRendererProviderContext0, SquidModel<T> squidModelT1) {
        super(entityRendererProviderContext0, squidModelT1, 0.7F);
    }

    public ResourceLocation getTextureLocation(T t0) {
        return SQUID_LOCATION;
    }

    protected void setupRotations(T t0, PoseStack poseStack1, float float2, float float3, float float4) {
        float $$5 = Mth.lerp(float4, t0.xBodyRotO, t0.xBodyRot);
        float $$6 = Mth.lerp(float4, t0.zBodyRotO, t0.zBodyRot);
        poseStack1.translate(0.0F, 0.5F, 0.0F);
        poseStack1.mulPose(Axis.YP.rotationDegrees(180.0F - float3));
        poseStack1.mulPose(Axis.XP.rotationDegrees($$5));
        poseStack1.mulPose(Axis.YP.rotationDegrees($$6));
        poseStack1.translate(0.0F, -1.2F, 0.0F);
    }

    protected float getBob(T t0, float float1) {
        return Mth.lerp(float1, t0.oldTentacleAngle, t0.tentacleAngle);
    }
}
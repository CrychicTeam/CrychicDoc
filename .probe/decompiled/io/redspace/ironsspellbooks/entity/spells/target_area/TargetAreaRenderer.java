package io.redspace.ironsspellbooks.entity.spells.target_area;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TargetAreaRenderer extends EntityRenderer<TargetedAreaEntity> {

    public TargetAreaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public ResourceLocation getTextureLocation(TargetedAreaEntity pEntity) {
        return null;
    }

    public void render(TargetedAreaEntity entity, float pEntityYaw, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl(SpellRenderingHelper.SOLID, 0.0F, 0.0F));
        Vector3f color = entity.getColor();
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float radius = entity.getRadius();
        int segments = (int) (5.0F * radius + 9.0F);
        float angle = (float) (Math.PI * 2) / (float) segments;
        float entityY = (float) Mth.lerp((double) pPartialTick, entity.f_19791_, entity.m_20186_());
        float[] heights = new float[6];
        for (int i = 0; i < 6; i++) {
            int degrees = i * 60;
            float x = radius * Mth.cos((float) degrees * (float) (Math.PI / 180.0));
            float z = radius * Mth.sin((float) degrees * (float) (Math.PI / 180.0));
            float y = Utils.findRelativeGroundLevel(entity.f_19853_, entity.m_20182_().add((double) x, (double) entity.m_20206_(), (double) z), (int) (entity.m_20206_() * 4.0F));
            heights[i] = y - entityY;
            if (entity.f_19853_.m_186437_(null, AABB.ofSize(new Vec3((double) x, (double) y, (double) z), 0.1, 0.1, 0.1))) {
                heights[i] = 0.0F;
            }
        }
        for (int ix = 0; ix < segments; ix++) {
            float theta = angle * (float) ix;
            float theta2 = angle * (float) (ix + 1);
            float x1 = radius * Mth.cos(theta);
            float x2 = radius * Mth.cos(theta2);
            float z1 = radius * Mth.sin(theta);
            float z2 = radius * Mth.sin(theta2);
            int degrees = (int) (theta * (180.0F / (float) Math.PI));
            int degrees2 = (int) (theta2 * (180.0F / (float) Math.PI));
            int j = degrees / 60 % 6;
            float heightMin = heights[j];
            float heightMax = heights[(j + 1) % 6];
            float f = theta * (180.0F / (float) Math.PI) % 60.0F / 60.0F;
            float f2 = theta2 * (180.0F / (float) Math.PI) % 60.0F / 60.0F;
            float y1 = Mth.lerp(f, heightMin, heightMax);
            if (f2 < f) {
                heightMin = heightMax;
                heightMax = heights[(j + 2) % 6];
            }
            float y2 = Mth.lerp(f2, heightMin, heightMax);
            consumer.vertex(poseMatrix, x2, y2 - 0.6F, z2).color(color.x(), color.y(), color.z(), 1.0F).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, x2, y2 + 0.6F, z2).color(0, 0, 0, 1).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, x1, y1 + 0.6F, z1).color(0, 0, 0, 1).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, x1, y1 - 0.6F, z1).color(color.x(), color.y(), color.z(), 1.0F).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        }
        poseStack.popPose();
    }

    private ParticleOptions particle(int i) {
        return switch(i) {
            case 0 ->
                ParticleHelper.SNOWFLAKE;
            case 1 ->
                ParticleHelper.UNSTABLE_ENDER;
            case 2 ->
                ParticleHelper.ACID_BUBBLE;
            case 3 ->
                ParticleHelper.BLOOD;
            case 4 ->
                ParticleHelper.WISP;
            case 5 ->
                ParticleHelper.ELECTRIC_SPARKS;
            default ->
                throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
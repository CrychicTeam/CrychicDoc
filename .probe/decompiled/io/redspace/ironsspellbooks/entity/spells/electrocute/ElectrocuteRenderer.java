package io.redspace.ironsspellbooks.entity.spells.electrocute;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ElectrocuteRenderer extends EntityRenderer<ElectrocuteProjectile> {

    private static ResourceLocation[] TEXTURES = new ResourceLocation[] { IronsSpellbooks.id("textures/entity/electric_beams/beam_1.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_2.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_3.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_4.png") };

    private static ResourceLocation SOLID = IronsSpellbooks.id("textures/entity/electric_beams/solid.png");

    public ElectrocuteRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(ElectrocuteProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (entity.m_19749_() != null) {
            poseStack.pushPose();
            PoseStack.Pose pose = poseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            poseStack.translate(0.0F, entity.m_20192_() * 0.5F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-entity.m_19749_().getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.m_19749_().getXRot()));
            if (entity.getAge() % 2 == 0 && !Minecraft.getInstance().isPaused()) {
                entity.generateLightningBeams();
            }
            List<Vec3> segments = entity.getBeamCache();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(this.getTextureLocation(entity)));
            float width = 0.25F;
            float height = width;
            Vec3 start = Vec3.ZERO;
            for (int i = 0; i < segments.size() - 1; i += 2) {
                Vec3 from = ((Vec3) segments.get(i)).add(start);
                Vec3 to = ((Vec3) segments.get(i + 1)).add(start);
                this.drawHull(from, to, width, height, pose, consumer, 0, 156, 255, 30);
                this.drawHull(from, to, width * 0.55F, height * 0.55F, pose, consumer, 0, 226, 255, 30);
            }
            consumer = bufferSource.getBuffer(RenderType.energySwirl(this.getTextureLocation(entity), 0.0F, 0.0F));
            for (int i = 0; i < segments.size() - 1; i += 2) {
                Vec3 from = ((Vec3) segments.get(i)).add(start);
                Vec3 to = ((Vec3) segments.get(i + 1)).add(start);
                this.drawHull(from, to, width * 0.2F, height * 0.2F, pose, consumer, 255, 255, 255, 255);
            }
            poseStack.popPose();
            super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
        }
    }

    public void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        this.drawQuad(from.subtract(0.0, (double) (height * 0.5F), 0.0), to.subtract(0.0, (double) (height * 0.5F), 0.0), width, 0.0F, pose, consumer, r, g, b, a);
        this.drawQuad(from.add(0.0, (double) (height * 0.5F), 0.0), to.add(0.0, (double) (height * 0.5F), 0.0), width, 0.0F, pose, consumer, r, g, b, a);
        this.drawQuad(from.subtract((double) (width * 0.5F), 0.0, 0.0), to.subtract((double) (width * 0.5F), 0.0, 0.0), 0.0F, height, pose, consumer, r, g, b, a);
        this.drawQuad(from.add((double) (width * 0.5F), 0.0, 0.0), to.add((double) (width * 0.5F), 0.0, 0.0), 0.0F, height, pose, consumer, r, g, b, a);
    }

    public void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5F;
        float halfHeight = height * 0.5F;
        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(ElectrocuteProjectile electrocuteProjectile0) {
        return SOLID;
    }
}
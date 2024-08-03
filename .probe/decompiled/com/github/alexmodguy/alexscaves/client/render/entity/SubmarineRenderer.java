package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.SubmarineModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SubmarineRenderer extends EntityRenderer<SubmarineEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/submarine/submarine.png");

    private static final ResourceLocation TEXTURE_EXPOSED = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_exposed.png");

    private static final ResourceLocation TEXTURE_WEATHERED = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_weathered.png");

    private static final ResourceLocation TEXTURE_OXIDIZED = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_oxidized.png");

    private static final ResourceLocation TEXTURE_NEW = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_new.png");

    private static final ResourceLocation TEXTURE_LOW = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_low.png");

    private static final ResourceLocation TEXTURE_MEDIUM = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_medium.png");

    private static final ResourceLocation TEXTURE_HIGH = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_high.png");

    private static final ResourceLocation TEXTURE_CRITICAL = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_critical.png");

    private static final ResourceLocation TEXTURE_BUTTONS = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_buttons.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/submarine/submarine_glow.png");

    private static SubmarineModel MODEL = new SubmarineModel();

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public SubmarineRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 1.0F;
    }

    public void render(SubmarineEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn) {
        if (!isFirstPersonFloodlightsMode(entity)) {
            renderSubmarine(entity, partialTicks, poseStack, source, lightIn, true);
            super.render(entity, entityYaw, partialTicks, poseStack, source, lightIn);
        }
    }

    public static boolean isFirstPersonFloodlightsMode(SubmarineEntity entity) {
        Entity player = Minecraft.getInstance().getCameraEntity();
        return player.isPassengerOfSameVehicle(entity) && Minecraft.getInstance().options.getCameraType().isFirstPerson() && entity.areLightsOn();
    }

    public static void renderSubFirstPerson(SubmarineEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource source) {
        renderSubmarine(entity, partialTicks, poseStack, source, LevelRenderer.getLightColor(entity.m_9236_(), entity.m_20183_()), false);
    }

    public static void renderSubmarine(SubmarineEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn, boolean maskWater) {
        Player player = Minecraft.getInstance().player;
        float ageInTicks = (float) entity.f_19797_ + partialTicks;
        float submarineYaw = entity.m_5675_(partialTicks);
        float submarinePitch = entity.m_5686_(partialTicks);
        poseStack.pushPose();
        poseStack.translate(0.0, 1.5, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - submarineYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.XN.rotationDegrees(submarinePitch));
        poseStack.translate(0.0F, 0.0F, 0.0F);
        if (entity.getWaterHeight() > 0.0F && entity.getWaterHeight() < 1.6F) {
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.sin((double) (ageInTicks * 0.1F)) * 0.5)));
            poseStack.mulPose(Axis.XP.rotationDegrees((float) (Math.sin((double) (ageInTicks * 0.1F + 1.3F)) * 0.5)));
        }
        poseStack.pushPose();
        MODEL.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
        for (Entity passenger : entity.m_20197_()) {
            if (passenger != player || !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                AlexsCaves.PROXY.releaseRenderingEntity(passenger.getUUID());
                poseStack.pushPose();
                poseStack.translate(0.0F, 0.65F, -0.75F);
                poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.YN.rotationDegrees(360.0F - submarineYaw));
                renderPassenger(passenger, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, source, lightIn);
                poseStack.popPose();
                AlexsCaves.PROXY.blockRenderingEntity(passenger.getUUID());
            }
        }
        VertexConsumer textureBuffer = source.getBuffer(RenderType.entityCutoutNoCull(getSubmarineBaseTexture(entity)));
        MODEL.m_7695_(poseStack, textureBuffer, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer damageBuffer = source.getBuffer(RenderType.entityTranslucent(getSubmarineDamageTexture(entity)));
        MODEL.m_7695_(poseStack, damageBuffer, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (entity.getDamageLevel() <= 3) {
            VertexConsumer buttonsBuffer = source.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_BUTTONS));
            MODEL.m_7695_(poseStack, buttonsBuffer, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, entity.getSonarFlashAmount(partialTicks));
            if (entity.areLightsOn() && entity.m_20160_()) {
                VertexConsumer glowBuffer = source.getBuffer(RenderType.eyes(TEXTURE_GLOW));
                MODEL.m_7695_(poseStack, glowBuffer, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        if (maskWater) {
            VertexConsumer waterMask = source.getBuffer(ACRenderTypes.getSubmarineMask());
            MODEL.setupWaterMask(entity, partialTicks);
            MODEL.getWaterMask().render(poseStack, waterMask, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (!isFirstPersonFloodlightsMode(entity) && entity.areLightsOn() && entity.m_20160_()) {
            Entity first = entity.m_146895_();
            float xRot = 0.0F;
            float yRot = 0.0F;
            if (first instanceof Player firstPlayer) {
                float headYaw = firstPlayer.f_20886_ + (firstPlayer.m_6080_() - firstPlayer.f_20886_) * partialTicks;
                xRot = firstPlayer.m_5686_(partialTicks) + submarinePitch;
                yRot = Mth.approachDegrees(submarineYaw, headYaw, 60.0F);
            }
            float length = 4.5F;
            float width = 0.45F;
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.75F, -2.4F);
            poseStack.mulPose(Axis.YN.rotationDegrees(submarineYaw));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XN.rotationDegrees(90.0F - xRot));
            poseStack.scale(3.0F, 1.0F, 1.0F);
            poseStack.translate(0.0F, -1.0F, 0.0F);
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            Matrix3f matrix3f1 = posestack$pose.normal();
            VertexConsumer lightConsumer = source.getBuffer(ACRenderTypes.getSubmarineLights());
            shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            poseStack.popPose();
        }
        poseStack.popPose();
        poseStack.popPose();
    }

    private static ResourceLocation getSubmarineDamageTexture(SubmarineEntity entity) {
        switch(entity.getDamageLevel()) {
            case 0:
                return TEXTURE_NEW;
            case 1:
                return TEXTURE_LOW;
            case 2:
                return TEXTURE_MEDIUM;
            case 3:
                return TEXTURE_HIGH;
            case 4:
                return TEXTURE_CRITICAL;
            default:
                return TEXTURE_NEW;
        }
    }

    public static <E extends Entity> void renderPassenger(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable var18) {
                    throw new ReportedException(CrashReport.forThrowable(var18, "Rendering entity in world"));
                }
            }
        } catch (Throwable var19) {
            CrashReport crashreport = CrashReport.forThrowable(var19, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -HALF_SQRT_3 * float4, float3, 0.0F).color(200, 235, 255, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, HALF_SQRT_3 * float4, float3, 0.0F).color(200, 235, 255, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static ResourceLocation getSubmarineBaseTexture(SubmarineEntity entity) {
        switch(entity.getOxidizationLevel()) {
            case 0:
                return TEXTURE;
            case 1:
                return TEXTURE_EXPOSED;
            case 2:
                return TEXTURE_WEATHERED;
            case 3:
                return TEXTURE_OXIDIZED;
            default:
                return TEXTURE;
        }
    }

    public ResourceLocation getTextureLocation(SubmarineEntity entity) {
        return TEXTURE;
    }
}
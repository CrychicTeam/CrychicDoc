package io.redspace.ironsspellbooks.entity.spells.portal;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PortalRenderer extends EntityRenderer<PortalEntity> {

    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/portal.png");

    static int frameCount = 10;

    static int ticksPerFrame = 2;

    public PortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PortalEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.m_146908_()));
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(PortalRenderer.CustomerRenderType.crumbling(getTextureLocation()));
        int anim = entity.f_19797_ / ticksPerFrame % 9;
        float uvMin = (float) anim / (float) frameCount;
        float uvMax = (float) (anim + 1) / (float) frameCount;
        vertex(poseMatrix, normalMatrix, consumer, -8.0F, 0.0F, 0.0F, uvMin, 0.0F);
        vertex(poseMatrix, normalMatrix, consumer, 8.0F, 0.0F, 0.0F, uvMax, 0.0F);
        vertex(poseMatrix, normalMatrix, consumer, 8.0F, 32.0F, 0.0F, uvMax, 1.0F);
        vertex(poseMatrix, normalMatrix, consumer, -8.0F, 32.0F, 0.0F, uvMin, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public static void vertex(Matrix4f pMatrix, Matrix3f pNormals, VertexConsumer pVertexBuilder, float pOffsetX, float pOffsetY, float pOffsetZ, float pTextureX, float pTextureY) {
        pVertexBuilder.vertex(pMatrix, pOffsetX, pOffsetY, pOffsetZ).color(255, 255, 255, 100).uv(pTextureX, pTextureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pNormals, 0.0F, 0.0F, 1.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        return getTextureLocation();
    }

    public static ResourceLocation getTextureLocation() {
        return TEXTURE;
    }

    public static class CustomerRenderType extends RenderType {

        protected static final RenderStateShard.TransparencyStateShard ONE_MINUS = new RenderStateShard.TransparencyStateShard("one_minus", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });

        public CustomerRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
            super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
        }

        @NotNull
        public static RenderType crumbling(@NotNull ResourceLocation pLocation) {
            return m_173215_("crumbling", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173074_).setTextureState(new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(ONE_MINUS).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
        }
    }
}
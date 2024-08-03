package io.redspace.ironsspellbooks.entity.spells.eldritch_blast;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class EldritchBlastRenderer extends EntityRenderer<EldritchBlastVisualEntity> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("irons_spellbooks", "eldritch_blast_model"), "main");

    private static final ResourceLocation TEXTURE_CORE = IronsSpellbooks.id("textures/entity/eldritch_blast/core.png");

    private static final ResourceLocation TEXTURE_OVERLAY = IronsSpellbooks.id("textures/entity/eldritch_blast/overlay.png");

    private final ModelPart body;

    public EldritchBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public boolean shouldRender(EldritchBlastVisualEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    public void render(EldritchBlastVisualEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        float lifetime = 8.0F;
        float scalar = 0.25F;
        float length = 32.0F * scalar * scalar;
        float f = (float) entity.f_19797_ + partialTicks;
        poseStack.translate(0.0, entity.m_20191_().getYsize() * 0.5, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.m_146908_() - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.m_146909_() - 90.0F));
        poseStack.scale(scalar, scalar, scalar);
        float alpha = Mth.clamp(1.0F - f / lifetime, 0.0F, 1.0F);
        for (float i = 0.0F; i < entity.distance * 4.0F; i += length) {
            poseStack.translate(0.0F, length, 0.0F);
            VertexConsumer consumer = bufferSource.getBuffer(MagicArrowRenderer.CustomRenderType.magicNoCull(TEXTURE_OVERLAY));
            poseStack.pushPose();
            float expansion = Mth.clampedLerp(1.2F, 0.0F, f / lifetime);
            poseStack.mulPose(Axis.YP.rotationDegrees(f * 5.0F));
            poseStack.scale(expansion, 1.0F, expansion);
            poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
            this.body.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
            poseStack.popPose();
            consumer = bufferSource.getBuffer(EldritchBlastRenderer.CustomerRenderType.crumbling(TEXTURE_CORE));
            poseStack.pushPose();
            expansion = Mth.clampedLerp(1.0F, 0.0F, f / (lifetime - 5.0F));
            poseStack.scale(expansion, 1.0F, expansion);
            poseStack.mulPose(Axis.YP.rotationDegrees(f * -10.0F));
            this.body.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(EldritchBlastVisualEntity entity) {
        return TEXTURE_CORE;
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
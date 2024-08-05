package com.mna.entities.renderers.ritual;

import com.mna.api.tools.RLoc;
import com.mna.entities.models.AncientWizardModel;
import com.mna.entities.rituals.AncientCouncil;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class AncientCouncilRenderer extends EntityRenderer<AncientCouncil> {

    private static final ResourceLocation ANCIENT_WIZARD_TEXTURE = RLoc.create("textures/entity/ancient_wizard.png");

    protected final AncientWizardModel<AncientCouncil> modelWizard = new AncientWizardModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(AncientWizardModel.LAYER_LOCATION));

    public AncientCouncilRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(AncientCouncil entity) {
        return ANCIENT_WIZARD_TEXTURE;
    }

    public void render(AncientCouncil entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        int age = entityIn.f_19797_;
        matrixStackIn.translate(0.0F, -1.0F, 0.0F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        float angleDegrees = 0.0F;
        int numWizards = 6;
        for (int i = 0; i < numWizards; i++) {
            matrixStackIn.pushPose();
            float riseAmount = Math.min(((float) age + partialTicks) / 70.0F * 2.5F, 2.5F);
            matrixStackIn.translate(0.0F, -riseAmount, 0.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(angleDegrees));
            matrixStackIn.translate(-3.0F, 0.0F, 0.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(270.0F));
            this.modelWizard.setupAnim(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelWizard.m_103119_(this.getTextureLocation(entityIn)));
            this.modelWizard.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
            if (entityIn.getRadiantPct() > 0.0F) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.0F, -riseAmount, 0.0F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(angleDegrees));
                matrixStackIn.translate(-2.25F, 0.5F - 1.25F * entityIn.getRadiantLift(), 0.0F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(270.0F));
                WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, new int[] { 203, 8, 112 }, new int[] { 151, 25, 151 }, (int) Math.floor((double) (entityIn.getRadiantPct() * 255.0F)), 0.05F);
                matrixStackIn.popPose();
            }
            if (entityIn.getBeamPct() > 0.0F) {
                matrixStackIn.pushPose();
                Vec3 start = entityIn.m_20182_().add(new Vec3(-2.25, -3.2, 0.0));
                Vec3 end = entityIn.m_20182_().add(0.0, -5.0, 0.0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-angleDegrees));
                matrixStackIn.translate(-2.25F, -3.2F, 0.0F);
                WorldRenderUtils.renderBeam(entityIn.m_9236_(), partialTicks, matrixStackIn, bufferIn, packedLightIn, start, end, entityIn.getBeamPct(), new int[] { 255, 255, 255 }, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                matrixStackIn.popPose();
            }
            angleDegrees += 360.0F / (float) numWizards;
        }
        if (entityIn.getCenterPct() > 0.0F) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0F, -5.0F, 0.0F);
            WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, new int[] { 151, 25, 151 }, new int[] { 77, 133, 207 }, (int) Math.floor((double) (entityIn.getCenterPct() * 255.0F)), 0.15F);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected boolean shouldShowName(AncientCouncil entity) {
        return false;
    }
}
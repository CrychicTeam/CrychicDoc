package com.mna.entities.renderers.boss;

import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.entities.boss.FaerieQueen;
import com.mna.entities.models.boss.FaerieQueenModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.common.util.LazyOptional;

public class FaerieQueenRenderer extends MAGeckoRenderer<FaerieQueen> {

    public FaerieQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new FaerieQueenModel());
    }

    public void render(FaerieQueen entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        int[] innerColor = entity.isWinter() ? new int[] { 148, 242, 244 } : new int[] { 128, 102, 25 };
        int[] outerColor = entity.isWinter() ? new int[] { 236, 255, 253 } : new int[] { 253, 94, 83 };
        float size = 0.25F;
        if (entity.faerieDeathTime > 0) {
            size = (float) ((double) size - 0.01 * (double) entity.faerieDeathTime);
        }
        if (size > -0.035F) {
            stack.pushPose();
            stack.translate(0.0, 1.2, 0.0);
            WorldRenderUtils.renderRadiant(entity, stack, bufferIn, innerColor, outerColor, 64, size);
            stack.popPose();
        }
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    public boolean shouldRender(FaerieQueen pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        LazyOptional<MAPFX> pfxCap = pLivingEntity.getCapability(MAPFXProvider.MAPFX);
        return pfxCap.isPresent() && ((MAPFX) pfxCap.resolve().get()).getFlag(pLivingEntity, MAPFX.Flag.MIST_FORM) ? false : super.m_5523_(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
    }
}
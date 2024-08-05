package com.mna.entities.renderers.sorcery;

import com.mna.entities.sorcery.targeting.SpellBeam;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SpellBeamRenderer extends EntityRenderer<SpellBeam> {

    public SpellBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(SpellBeam entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.getLastTickImpact() != null) {
            Vec3 endPointOffset = entityIn.getLastTickImpact().subtract(entityIn.m_20182_());
            matrixStackIn.pushPose();
            matrixStackIn.translate(endPointOffset.x, endPointOffset.y, endPointOffset.z);
            WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, new int[] { 255, 255, 255 }, entityIn.getBeamColor(), 128, 0.05F);
            matrixStackIn.popPose();
            WorldRenderUtils.renderBeam(entityIn.m_9236_(), partialTicks, matrixStackIn, bufferIn, packedLightIn, entityIn.m_20182_(), entityIn.getLastTickImpact(), 1.0F, entityIn.getBeamColor(), 0.1F, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
        }
    }

    public ResourceLocation getTextureLocation(SpellBeam entity) {
        return null;
    }
}
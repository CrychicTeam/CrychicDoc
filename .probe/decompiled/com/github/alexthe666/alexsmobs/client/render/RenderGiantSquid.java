package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelGiantSquid;
import com.github.alexthe666.alexsmobs.entity.EntityGiantSquid;
import com.github.alexthe666.alexsmobs.entity.EntityGiantSquidPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderGiantSquid extends MobRenderer<EntityGiantSquid, ModelGiantSquid> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/giant_squid.png");

    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("alexsmobs:textures/entity/giant_squid_blue.png");

    private static final ResourceLocation TEXTURE_DEPRESSURIZED = new ResourceLocation("alexsmobs:textures/entity/giant_squid_depressurized.png");

    public RenderGiantSquid(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelGiantSquid(), 1.0F);
        this.m_115326_(new RenderGiantSquid.LayerDepressurization(this));
    }

    protected float getFlipDegrees(EntityGiantSquid squid) {
        return 0.0F;
    }

    public boolean shouldRender(EntityGiantSquid livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (livingEntityIn.isCaptured() && livingEntityIn.m_6084_()) {
            return false;
        } else if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            for (EntityGiantSquidPart part : livingEntityIn.allParts) {
                if (camera.isVisible(part.m_20191_())) {
                    return true;
                }
            }
            return false;
        }
    }

    protected void scale(EntityGiantSquid entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(EntityGiantSquid entity) {
        return entity.isBlue() ? TEXTURE_BLUE : TEXTURE;
    }

    static class LayerDepressurization extends RenderLayer<EntityGiantSquid, ModelGiantSquid> {

        public LayerDepressurization(RenderGiantSquid render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGiantSquid squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(RenderGiantSquid.TEXTURE_DEPRESSURIZED));
            float alpha = squid.prevDepressurization + (squid.getDepressurization() - squid.prevDepressurization) * partialTicks;
            ((ModelGiantSquid) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(squid, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerGrizzlyHoney;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerGrizzlyItem;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
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

public class RenderGrizzlyBear extends MobRenderer<EntityGrizzlyBear, ModelGrizzlyBear> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear.png");

    private static final ResourceLocation TEXTURE_SNOWY = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_snowy.png");

    public static final ResourceLocation TEXTURE_FREDDY = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_freddy.png");

    private static final ResourceLocation TEXTURE_FREDDY_EYES = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_freddy_eyes.png");

    public RenderGrizzlyBear(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelGrizzlyBear(), 0.8F);
        this.m_115326_(new RenderGrizzlyBear.LayerFreddyEyes());
        this.m_115326_(new LayerGrizzlyHoney(this));
        this.m_115326_(new RenderGrizzlyBear.LayerSnow());
        this.m_115326_(new LayerGrizzlyItem(this));
    }

    public boolean shouldRender(EntityGrizzlyBear livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return livingEntityIn.getAprilFoolsFlag() == 5 ? false : super.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    public ResourceLocation getTextureLocation(EntityGrizzlyBear entity) {
        return entity.isFreddy() ? TEXTURE_FREDDY : TEXTURE;
    }

    class LayerFreddyEyes extends RenderLayer<EntityGrizzlyBear, ModelGrizzlyBear> {

        public LayerFreddyEyes() {
            super(RenderGrizzlyBear.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGrizzlyBear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.getAprilFoolsFlag() == 4 && entitylivingbaseIn.f_19797_ % 6 <= 2) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(AMRenderTypes.getEyesNoFog(RenderGrizzlyBear.TEXTURE_FREDDY_EYES));
                ((ModelGrizzlyBear) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 0.1F);
            }
        }
    }

    class LayerSnow extends RenderLayer<EntityGrizzlyBear, ModelGrizzlyBear> {

        public LayerSnow() {
            super(RenderGrizzlyBear.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGrizzlyBear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.isSnowy()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(RenderGrizzlyBear.TEXTURE_SNOWY));
                ((ModelGrizzlyBear) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
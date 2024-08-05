package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.VallumraptorModel;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class VallumraptorRenderer extends MobRenderer<VallumraptorEntity, VallumraptorModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/vallumraptor.png");

    private static final ResourceLocation TEXTURE_ELDER = new ResourceLocation("alexscaves:textures/entity/vallumraptor_elder.png");

    private static final ResourceLocation TEXTURE_ALAN = new ResourceLocation("alexscaves:textures/entity/vallumraptor_alan.png");

    private static final ResourceLocation TEXTURE_ALAN_ELDER = new ResourceLocation("alexscaves:textures/entity/vallumraptor_alan_elder.png");

    private static final ResourceLocation TEXTURE_RETRO = new ResourceLocation("alexscaves:textures/entity/vallumraptor_retro.png");

    private static final ResourceLocation TEXTURE_RETRO_ELDER = new ResourceLocation("alexscaves:textures/entity/vallumraptor_retro_elder.png");

    private static final ResourceLocation TEXTURE_TECTONIC = new ResourceLocation("alexscaves:textures/entity/vallumraptor_tectonic.png");

    private static final ResourceLocation TEXTURE_TECTONIC_ELDER = new ResourceLocation("alexscaves:textures/entity/vallumraptor_tectonic_elder.png");

    public VallumraptorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new VallumraptorModel(), 0.3F);
        this.m_115326_(new VallumraptorRenderer.ItemLayer());
    }

    protected void scale(VallumraptorEntity mob, PoseStack matrixStackIn, float partialTicks) {
        if (mob.isElder()) {
            matrixStackIn.scale(1.1F, 1.1F, 1.1F);
        }
        float alpha = 1.0F - 0.9F * mob.getHideProgress(partialTicks);
        ((VallumraptorModel) this.f_115290_).setAlpha(alpha);
    }

    public ResourceLocation getTextureLocation(VallumraptorEntity entity) {
        if (entity.m_8077_() && "alan".equalsIgnoreCase(entity.m_7755_().getString())) {
            return entity.isElder() ? TEXTURE_ALAN_ELDER : TEXTURE_ALAN;
        } else if (entity.getAltSkin() == 1) {
            return entity.isElder() ? TEXTURE_RETRO_ELDER : TEXTURE_RETRO;
        } else if (entity.getAltSkin() == 2) {
            return entity.isElder() ? TEXTURE_TECTONIC_ELDER : TEXTURE_TECTONIC;
        } else {
            return entity.isElder() ? TEXTURE_ELDER : TEXTURE;
        }
    }

    @Nullable
    protected RenderType getRenderType(VallumraptorEntity entity, boolean defColor, boolean invis, boolean v) {
        if (entity.getHideProgress(1.0F) > 0.0F) {
            ResourceLocation resourcelocation = this.getTextureLocation(entity);
            return RenderType.entityTranslucent(resourcelocation);
        } else {
            return super.m_7225_(entity, defColor, invis, v);
        }
    }

    class ItemLayer extends RenderLayer<VallumraptorEntity, VallumraptorModel> {

        public ItemLayer() {
            super(VallumraptorRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, VallumraptorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.m_21205_();
            if (!itemstack.isEmpty()) {
                boolean left = entitylivingbaseIn.m_21526_();
                matrixStackIn.pushPose();
                if (entitylivingbaseIn.m_6162_()) {
                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                    matrixStackIn.translate(0.0, 1.5, 0.0);
                }
                matrixStackIn.pushPose();
                ((VallumraptorModel) this.m_117386_()).translateToHand(matrixStackIn, left);
                if (entitylivingbaseIn.m_6162_()) {
                    matrixStackIn.translate(0.0, 0.1F, -0.6);
                }
                matrixStackIn.translate(left ? -0.2F : 0.2F, 0.2F, -0.3F);
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-10.0F));
                ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
                renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.popPose();
                matrixStackIn.popPose();
            }
        }
    }
}
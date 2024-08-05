package com.mna.entities.renderers.sorcery;

import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BasicChanneledSpellRenderer extends EntityRenderer<ChanneledSpellEntity> {

    public BasicChanneledSpellRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(ChanneledSpellEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(ChanneledSpellEntity entity) {
        return null;
    }
}
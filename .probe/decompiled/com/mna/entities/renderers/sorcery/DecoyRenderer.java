package com.mna.entities.renderers.sorcery;

import com.mna.entities.sorcery.EntityDecoy;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DecoyRenderer extends LivingEntityRenderer<EntityDecoy, PlayerModel<EntityDecoy>> {

    private static final ResourceLocation DEFAULT = new ResourceLocation("");

    public DecoyRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
    }

    public ResourceLocation getTextureLocation(EntityDecoy entity) {
        return entity.getPlayer() != null && entity.getPlayer() instanceof AbstractClientPlayer ? ((AbstractClientPlayer) entity.getPlayer()).getSkinTextureLocation() : DEFAULT;
    }

    protected boolean shouldShowName(EntityDecoy entity) {
        return true;
    }

    protected void renderNameTag(EntityDecoy entityIn, Component displayNameIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.getPlayer() != null) {
            super.m_7649_(entityIn, entityIn.getPlayer().getDisplayName(), matrixStackIn, bufferIn, packedLightIn);
        }
    }
}
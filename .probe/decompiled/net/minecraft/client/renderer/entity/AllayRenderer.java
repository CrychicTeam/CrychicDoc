package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.AllayModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.allay.Allay;

public class AllayRenderer extends MobRenderer<Allay, AllayModel> {

    private static final ResourceLocation ALLAY_TEXTURE = new ResourceLocation("textures/entity/allay/allay.png");

    public AllayRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new AllayModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ALLAY)), 0.4F);
        this.m_115326_(new ItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(Allay allay0) {
        return ALLAY_TEXTURE;
    }

    protected int getBlockLightLevel(Allay allay0, BlockPos blockPos1) {
        return 15;
    }
}
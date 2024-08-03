package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.VexModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vex;

public class VexRenderer extends MobRenderer<Vex, VexModel> {

    private static final ResourceLocation VEX_LOCATION = new ResourceLocation("textures/entity/illager/vex.png");

    private static final ResourceLocation VEX_CHARGING_LOCATION = new ResourceLocation("textures/entity/illager/vex_charging.png");

    public VexRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new VexModel(entityRendererProviderContext0.bakeLayer(ModelLayers.VEX)), 0.3F);
        this.m_115326_(new ItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    protected int getBlockLightLevel(Vex vex0, BlockPos blockPos1) {
        return 15;
    }

    public ResourceLocation getTextureLocation(Vex vex0) {
        return vex0.isCharging() ? VEX_CHARGING_LOCATION : VEX_LOCATION;
    }
}
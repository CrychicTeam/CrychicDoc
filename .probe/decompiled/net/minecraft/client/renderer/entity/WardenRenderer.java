package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.WardenEmissiveLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.warden.Warden;

public class WardenRenderer extends MobRenderer<Warden, WardenModel<Warden>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/warden/warden.png");

    private static final ResourceLocation BIOLUMINESCENT_LAYER_TEXTURE = new ResourceLocation("textures/entity/warden/warden_bioluminescent_layer.png");

    private static final ResourceLocation HEART_TEXTURE = new ResourceLocation("textures/entity/warden/warden_heart.png");

    private static final ResourceLocation PULSATING_SPOTS_TEXTURE_1 = new ResourceLocation("textures/entity/warden/warden_pulsating_spots_1.png");

    private static final ResourceLocation PULSATING_SPOTS_TEXTURE_2 = new ResourceLocation("textures/entity/warden/warden_pulsating_spots_2.png");

    public WardenRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new WardenModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.WARDEN)), 0.9F);
        this.m_115326_(new WardenEmissiveLayer<>(this, BIOLUMINESCENT_LAYER_TEXTURE, (p_234809_, p_234810_, p_234811_) -> 1.0F, WardenModel::m_233543_));
        this.m_115326_(new WardenEmissiveLayer<>(this, PULSATING_SPOTS_TEXTURE_1, (p_234805_, p_234806_, p_234807_) -> Math.max(0.0F, Mth.cos(p_234807_ * 0.045F) * 0.25F), WardenModel::m_233544_));
        this.m_115326_(new WardenEmissiveLayer<>(this, PULSATING_SPOTS_TEXTURE_2, (p_234801_, p_234802_, p_234803_) -> Math.max(0.0F, Mth.cos(p_234803_ * 0.045F + (float) Math.PI) * 0.25F), WardenModel::m_233544_));
        this.m_115326_(new WardenEmissiveLayer<>(this, TEXTURE, (p_234797_, p_234798_, p_234799_) -> p_234797_.getTendrilAnimation(p_234798_), WardenModel::m_233541_));
        this.m_115326_(new WardenEmissiveLayer<>(this, HEART_TEXTURE, (p_234793_, p_234794_, p_234795_) -> p_234793_.getHeartAnimation(p_234794_), WardenModel::m_233542_));
    }

    public ResourceLocation getTextureLocation(Warden warden0) {
        return TEXTURE;
    }
}
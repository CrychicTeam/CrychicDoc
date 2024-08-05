package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

public class BeeRenderer extends MobRenderer<Bee, BeeModel<Bee>> {

    private static final ResourceLocation ANGRY_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry.png");

    private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry_nectar.png");

    private static final ResourceLocation BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee.png");

    private static final ResourceLocation NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_nectar.png");

    public BeeRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new BeeModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.BEE)), 0.4F);
    }

    public ResourceLocation getTextureLocation(Bee bee0) {
        if (bee0.m_21660_()) {
            return bee0.hasNectar() ? ANGRY_NECTAR_BEE_TEXTURE : ANGRY_BEE_TEXTURE;
        } else {
            return bee0.hasNectar() ? NECTAR_BEE_TEXTURE : BEE_TEXTURE;
        }
    }
}
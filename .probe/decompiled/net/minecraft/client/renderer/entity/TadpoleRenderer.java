package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.TadpoleModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Tadpole;

public class TadpoleRenderer extends MobRenderer<Tadpole, TadpoleModel<Tadpole>> {

    private static final ResourceLocation TADPOLE_TEXTURE = new ResourceLocation("textures/entity/tadpole/tadpole.png");

    public TadpoleRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new TadpoleModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.TADPOLE)), 0.14F);
    }

    public ResourceLocation getTextureLocation(Tadpole tadpole0) {
        return TADPOLE_TEXTURE;
    }
}
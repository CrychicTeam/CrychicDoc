package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VoidTentacleRenderer extends GeoLivingEntityRenderer<VoidTentacle> {

    public VoidTentacleRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidTentacleModel());
        this.addRenderLayer(new VoidTentacleEmissiveLayer(this));
        this.f_114477_ = 1.0F;
    }
}
package io.redspace.ironsspellbooks.entity.spells.ice_block;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceBlockRenderer extends GeoEntityRenderer<IceBlockProjectile> {

    public IceBlockRenderer(EntityRendererProvider.Context context) {
        super(context, new IceBlockModel());
        this.f_114477_ = 1.5F;
    }
}
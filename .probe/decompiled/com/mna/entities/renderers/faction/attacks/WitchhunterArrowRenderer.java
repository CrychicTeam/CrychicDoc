package com.mna.entities.renderers.faction.attacks;

import com.mna.entities.faction.util.WitchHunterArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

public class WitchhunterArrowRenderer extends ArrowRenderer<WitchHunterArrow> {

    public WitchhunterArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(WitchHunterArrow entity) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
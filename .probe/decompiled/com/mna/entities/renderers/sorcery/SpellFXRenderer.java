package com.mna.entities.renderers.sorcery;

import com.mna.entities.utility.SpellFX;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpellFXRenderer extends EntityRenderer<SpellFX> {

    public SpellFXRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(SpellFX entity) {
        return null;
    }
}
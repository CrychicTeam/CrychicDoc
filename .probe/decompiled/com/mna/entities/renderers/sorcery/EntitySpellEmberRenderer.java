package com.mna.entities.renderers.sorcery;

import com.mna.entities.sorcery.targeting.SpellEmber;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EntitySpellEmberRenderer extends EntityRenderer<SpellEmber> {

    public EntitySpellEmberRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(SpellEmber pEntity) {
        return null;
    }
}
package com.mna.entities.renderers.sorcery;

import com.mna.entities.utility.FillHole;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FillHoleRenderer extends EntityRenderer<FillHole> {

    public FillHoleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(FillHole entity) {
        return null;
    }
}
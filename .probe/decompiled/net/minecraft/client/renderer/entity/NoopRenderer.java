package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NoopRenderer<T extends Entity> extends EntityRenderer<T> {

    public NoopRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    @Override
    public ResourceLocation getTextureLocation(T t0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
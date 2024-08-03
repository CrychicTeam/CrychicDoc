package org.embeddedt.embeddium.render.fluid;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class EmbeddiumFluidSpriteCache {

    private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];

    private final Object2ObjectOpenHashMap<ResourceLocation, TextureAtlasSprite> spriteCache = new Object2ObjectOpenHashMap();

    private TextureAtlasSprite getTexture(ResourceLocation identifier) {
        TextureAtlasSprite sprite = (TextureAtlasSprite) this.spriteCache.get(identifier);
        if (sprite == null) {
            sprite = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(identifier);
            this.spriteCache.put(identifier, sprite);
        }
        return sprite;
    }

    public TextureAtlasSprite[] getSprites(BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(fluidState);
        this.sprites[0] = this.getTexture(fluidExt.getStillTexture(fluidState, world, pos));
        this.sprites[1] = this.getTexture(fluidExt.getFlowingTexture(fluidState, world, pos));
        ResourceLocation overlay = fluidExt.getOverlayTexture(fluidState, world, pos);
        this.sprites[2] = overlay != null ? this.getTexture(overlay) : null;
        return this.sprites;
    }
}
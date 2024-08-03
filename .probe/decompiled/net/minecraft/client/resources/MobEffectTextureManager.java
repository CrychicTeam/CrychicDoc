package net.minecraft.client.resources;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class MobEffectTextureManager extends TextureAtlasHolder {

    public MobEffectTextureManager(TextureManager textureManager0) {
        super(textureManager0, new ResourceLocation("textures/atlas/mob_effects.png"), new ResourceLocation("mob_effects"));
    }

    public TextureAtlasSprite get(MobEffect mobEffect0) {
        return this.m_118901_(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect0));
    }
}
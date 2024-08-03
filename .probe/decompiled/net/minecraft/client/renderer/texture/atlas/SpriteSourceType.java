package net.minecraft.client.renderer.texture.atlas;

import com.mojang.serialization.Codec;

public record SpriteSourceType(Codec<? extends SpriteSource> f_260449_) {

    private final Codec<? extends SpriteSource> codec;

    public SpriteSourceType(Codec<? extends SpriteSource> f_260449_) {
        this.codec = f_260449_;
    }
}
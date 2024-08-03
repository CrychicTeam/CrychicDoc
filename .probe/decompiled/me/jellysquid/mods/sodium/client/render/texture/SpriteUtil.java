package me.jellysquid.mods.sodium.client.render.texture;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.embeddedt.embeddium.impl.render.chunk.compile.GlobalChunkBuildContext;
import org.jetbrains.annotations.Nullable;

public class SpriteUtil {

    public static void markSpriteActive(@Nullable TextureAtlasSprite sprite) {
        if (sprite != null) {
            ((SpriteContentsExtended) sprite.contents()).sodium$setActive(true);
            if (hasAnimation(sprite)) {
                ChunkBuildContext context = GlobalChunkBuildContext.get();
                if (context != null) {
                    context.captureAdditionalSprite(sprite);
                }
            }
        }
    }

    public static boolean hasAnimation(TextureAtlasSprite sprite) {
        return ((SpriteContentsExtended) sprite.contents()).sodium$hasAnimation();
    }
}
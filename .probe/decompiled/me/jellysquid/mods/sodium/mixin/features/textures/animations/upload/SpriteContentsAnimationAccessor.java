package me.jellysquid.mods.sodium.mixin.features.textures.animations.upload;

import java.util.List;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ SpriteContents.AnimatedTexture.class })
public interface SpriteContentsAnimationAccessor {

    @Accessor
    List<SpriteContents.FrameInfo> getFrames();

    @Accessor
    int getFrameRowSize();
}
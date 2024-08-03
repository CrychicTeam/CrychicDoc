package me.jellysquid.mods.sodium.mixin.features.textures;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ SpriteContents.class })
public interface SpriteContentsInvoker {

    @Invoker
    void invokeUpload(int var1, int var2, int var3, int var4, NativeImage[] var5);
}
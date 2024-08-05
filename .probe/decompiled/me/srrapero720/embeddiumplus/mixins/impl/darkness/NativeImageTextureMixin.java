package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import com.mojang.blaze3d.platform.NativeImage;
import me.srrapero720.embeddiumplus.foundation.darkness.DarknessPlus;
import me.srrapero720.embeddiumplus.foundation.darkness.accessors.TextureAccess;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ DynamicTexture.class })
public class NativeImageTextureMixin implements TextureAccess {

    @Shadow
    private NativeImage pixels;

    @Unique
    private boolean embPlus$enableHook = false;

    @Inject(method = { "upload" }, at = { @At("HEAD") })
    private void inject$onUpload(CallbackInfo ci) {
        if (this.embPlus$enableHook && DarknessPlus.enabled) {
            NativeImage img = this.pixels;
            for (int b = 0; b < 16; b++) {
                for (int s = 0; s < 16; s++) {
                    int color = DarknessPlus.darken(img.getPixelRGBA(b, s), b, s);
                    img.setPixelRGBA(b, s, color);
                }
            }
        }
    }

    @Override
    public void embPlus$enableUploadHook() {
        this.embPlus$enableHook = true;
    }
}
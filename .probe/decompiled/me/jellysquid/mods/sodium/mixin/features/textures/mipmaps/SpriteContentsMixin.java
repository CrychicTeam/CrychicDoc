package me.jellysquid.mods.sodium.mixin.features.textures.mipmaps;

import com.mojang.blaze3d.platform.NativeImage;
import me.jellysquid.mods.sodium.client.util.NativeImageHelper;
import me.jellysquid.mods.sodium.client.util.color.ColorSRGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ SpriteContents.class })
public class SpriteContentsMixin {

    @Mutable
    @Shadow
    @Final
    private NativeImage originalImage;

    @Shadow
    @Final
    private ResourceLocation name;

    @Redirect(method = { "<init>(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/metadata/animation/FrameSize;Lcom/mojang/blaze3d/platform/NativeImage;Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;Lnet/minecraftforge/client/textures/ForgeTextureMetadata;)V" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/SpriteContents;originalImage:Lcom/mojang/blaze3d/platform/NativeImage;", opcode = 181))
    private void sodium$beforeGenerateMipLevels(SpriteContents instance, NativeImage nativeImage, ResourceLocation identifier) {
        if (Minecraft.getInstance().options.mipmapLevels().get() > 0 && !this.name.getPath().contains("leaves")) {
            sodium$fillInTransparentPixelColors(nativeImage);
        }
        this.originalImage = nativeImage;
    }

    @Unique
    private static void sodium$fillInTransparentPixelColors(NativeImage nativeImage) {
        long ppPixel = NativeImageHelper.getPointerRGBA(nativeImage);
        int pixelCount = nativeImage.getHeight() * nativeImage.getWidth();
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        float totalWeight = 0.0F;
        for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
            long pPixel = ppPixel + (long) (pixelIndex * 4);
            int color = MemoryUtil.memGetInt(pPixel);
            int alpha = FastColor.ABGR32.alpha(color);
            if (alpha != 0) {
                float weight = (float) alpha;
                r += ColorSRGB.srgbToLinear(FastColor.ABGR32.red(color)) * weight;
                g += ColorSRGB.srgbToLinear(FastColor.ABGR32.green(color)) * weight;
                b += ColorSRGB.srgbToLinear(FastColor.ABGR32.blue(color)) * weight;
                totalWeight += weight;
            }
        }
        if (totalWeight != 0.0F) {
            r /= totalWeight;
            g /= totalWeight;
            b /= totalWeight;
            int averageColor = ColorSRGB.linearToSrgb(r, g, b, 0);
            for (int pixelIndexx = 0; pixelIndexx < pixelCount; pixelIndexx++) {
                long pPixel = ppPixel + (long) (pixelIndexx * 4);
                int color = MemoryUtil.memGetInt(pPixel);
                int alpha = FastColor.ABGR32.alpha(color);
                if (alpha == 0) {
                    MemoryUtil.memPutInt(pPixel, averageColor);
                }
            }
        }
    }
}
package team.lodestar.lodestone.systems.textureloader;

import com.mojang.blaze3d.platform.NativeImage;
import java.awt.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RegisterTextureAtlasSpriteLoadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.lodestar.lodestone.helpers.ColorHelper;
import team.lodestar.lodestone.systems.easing.Easing;

public class LodestoneTextureLoader {

    protected static final LodestoneTextureLoader.ColorLerp GRADIENT = (image, x, y, luminosity, s) -> (float) (y % 16) / 16.0F;

    protected static final LodestoneTextureLoader.ColorLerp LUMINOUS_GRADIENT = (image, x, y, luminosity, s) -> ((float) (y % 16) / 16.0F + luminosity / s) / 2.0F;

    protected static final LodestoneTextureLoader.ColorLerp LUMINOUS = (image, x, y, luminosity, s) -> luminosity / s;

    public static void registerTextureLoader(String loaderName, ResourceLocation targetPath, ResourceLocation inputImage, LodestoneTextureLoader.TextureModifier textureModifier, RegisterTextureAtlasSpriteLoadersEvent event) {
        IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
        event.register(loaderName, new LodestoneTextureAtlasSpriteLoader(textureModifier));
    }

    public static NativeImage applyGrayscale(NativeImage nativeimage) {
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int L = (int) (0.299 * (double) (pixel & 0xFF) + 0.587 * (double) (pixel >> 8 & 0xFF) + 0.114 * (double) (pixel >> 16 & 0xFF));
                nativeimage.setPixelRGBA(x, y, FastColor.ABGR32.color(pixel >> 24 & 0xFF, L, L, L));
            }
        }
        return nativeimage;
    }

    public static NativeImage applyMultiColorGradient(Easing easing, NativeImage nativeimage, LodestoneTextureLoader.ColorLerp colorLerp, Color... colors) {
        int colorCount = colors.length - 1;
        int lowestLuminosity = 255;
        int highestLuminosity = 0;
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int alpha = pixel >> 24 & 0xFF;
                if (alpha != 0) {
                    int luminosity = (int) (0.299 * (double) (pixel & 0xFF) + 0.587 * (double) (pixel >> 8 & 0xFF) + 0.114 * (double) (pixel >> 16 & 0xFF));
                    if (luminosity < lowestLuminosity) {
                        lowestLuminosity = luminosity;
                    }
                    if (luminosity > highestLuminosity) {
                        highestLuminosity = luminosity;
                    }
                }
            }
        }
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int yx = 0; yx < nativeimage.getHeight(); yx++) {
                int pixel = nativeimage.getPixelRGBA(x, yx);
                int alpha = pixel >> 24 & 0xFF;
                if (alpha != 0) {
                    int luminosityx = (int) (0.299 * (double) (pixel & 0xFF) + 0.587 * (double) (pixel >> 8 & 0xFF) + 0.114 * (double) (pixel >> 16 & 0xFF));
                    float pct = (float) luminosityx / 255.0F;
                    float newLuminosity = Mth.lerp(pct, (float) lowestLuminosity, (float) highestLuminosity);
                    float lerp = 1.0F - colorLerp.lerp(pixel, x, yx, newLuminosity, (float) highestLuminosity);
                    float colorIndex = (float) (2 * colorCount) * lerp;
                    int index = (int) Mth.clamp(colorIndex, 0.0F, (float) colorCount);
                    Color color = colors[index];
                    Color nextColor = index == colorCount ? color : colors[index + 1];
                    Color transition = ColorHelper.colorLerp(easing, colorIndex - (float) ((int) colorIndex), color, nextColor);
                    nativeimage.setPixelRGBA(x, yx, FastColor.ABGR32.color(alpha, transition.getBlue(), transition.getGreen(), transition.getRed()));
                }
            }
        }
        return nativeimage;
    }

    public interface ColorLerp {

        float lerp(int var1, int var2, int var3, float var4, float var5);
    }

    public interface TextureModifier {

        NativeImage modifyTexture(NativeImage var1);
    }
}
package net.mehvahdjukaar.moonlight.api.fluids;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.client.TextureCache;
import net.mehvahdjukaar.moonlight.api.client.texture_renderer.RenderedTexturesManager;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidColorsImpl;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.BlockAndTintGetter;

public class SoftFluidColors implements ResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        RenderedTexturesManager.clearCache();
        TextureCache.clear();
        refreshParticleColors();
    }

    public static void refreshParticleColors() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            for (Entry<ResourceKey<SoftFluid>, SoftFluid> entry : SoftFluidRegistry.getRegistry(mc.level.m_9598_()).entrySet()) {
                SoftFluid fluid = (SoftFluid) entry.getValue();
                ResourceLocation location = fluid.getStillTexture();
                int averageColor = -1;
                int tint = fluid.getTintMethod().appliesToStill() ? fluid.getTintColor() : -1;
                TextureAtlas textureMap = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
                TextureAtlasSprite sprite = textureMap.getSprite(location);
                try {
                    averageColor = getAverageColor(sprite, tint);
                } catch (Exception var11) {
                    Moonlight.LOGGER.warn("Failed to load particle color for " + sprite + " using current resource pack. might be a broken png.mcmeta");
                }
                fluid.averageTextureTint = averageColor;
            }
        }
    }

    private static int getAverageColor(TextureAtlasSprite sprite, int tint) {
        SpriteContents c = sprite.contents();
        if (sprite != null && c.getFrameCount() != 0) {
            int tintR = tint >> 16 & 0xFF;
            int tintG = tint >> 8 & 0xFF;
            int tintB = tint & 0xFF;
            int total = 0;
            int totalR = 0;
            int totalB = 0;
            int totalG = 0;
            for (int tryFrame = 0; tryFrame < c.getFrameCount(); tryFrame++) {
                try {
                    for (int x = 0; x < c.width(); x++) {
                        for (int y = 0; y < c.height(); y++) {
                            int pixel = ClientHelper.getPixelRGBA(sprite, tryFrame, x, y);
                            int pixelB = pixel >> 16 & 0xFF;
                            int pixelG = pixel >> 8 & 0xFF;
                            int pixelR = pixel & 0xFF;
                            total++;
                            totalR += pixelR;
                            totalG += pixelG;
                            totalB += pixelB;
                        }
                    }
                    break;
                } catch (Exception var17) {
                    total = 0;
                    totalR = 0;
                    totalB = 0;
                    totalG = 0;
                }
            }
            return total <= 0 ? -1 : FastColor.ARGB32.color(255, totalR / total * tintR / 255, totalG / total * tintG / 255, totalB / total * tintB / 255);
        } else {
            return -1;
        }
    }

    @ExpectPlatform
    @Transformed
    public static int getSpecialColor(SoftFluidStack softFluidStack, BlockAndTintGetter world, BlockPos pos) {
        return SoftFluidColorsImpl.getSpecialColor(softFluidStack, world, pos);
    }
}
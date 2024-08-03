package sereneseasons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sereneseasons.api.season.ISeasonColorProvider;
import sereneseasons.api.season.Season;
import sereneseasons.config.ServerConfig;
import sereneseasons.init.ModTags;

public class SeasonColorUtil {

    public static int multiplyColours(int colour1, int colour2) {
        return (int) ((float) colour1 / 255.0F * ((float) colour2 / 255.0F) * 255.0F);
    }

    public static int overlayBlendChannel(int underColour, int overColour) {
        int retVal;
        if (underColour < 128) {
            retVal = multiplyColours(2 * underColour, overColour);
        } else {
            retVal = multiplyColours(2 * (255 - underColour), 255 - overColour);
            retVal = 255 - retVal;
        }
        return retVal;
    }

    public static int overlayBlend(int underColour, int overColour) {
        int r = overlayBlendChannel(underColour >> 16 & 0xFF, overColour >> 16 & 0xFF);
        int g = overlayBlendChannel(underColour >> 8 & 0xFF, overColour >> 8 & 0xFF);
        int b = overlayBlendChannel(underColour & 0xFF, overColour & 0xFF);
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static int mixColours(int a, int b, float ratio) {
        if (ratio > 1.0F) {
            ratio = 1.0F;
        } else if (ratio < 0.0F) {
            ratio = 0.0F;
        }
        float iRatio = 1.0F - ratio;
        int aA = a >> 24 & 0xFF;
        int aR = (a & 0xFF0000) >> 16;
        int aG = (a & 0xFF00) >> 8;
        int aB = a & 0xFF;
        int bA = b >> 24 & 0xFF;
        int bR = (b & 0xFF0000) >> 16;
        int bG = (b & 0xFF00) >> 8;
        int bB = b & 0xFF;
        int A = (int) ((float) aA * iRatio + (float) bA * ratio);
        int R = (int) ((float) aR * iRatio + (float) bR * ratio);
        int G = (int) ((float) aG * iRatio + (float) bG * ratio);
        int B = (int) ((float) aB * iRatio + (float) bB * ratio);
        return A << 24 | R << 16 | G << 8 | B;
    }

    public static int saturateColour(int colour, float saturationMultiplier) {
        Color newColor = new Color(colour);
        double[] hsv = newColor.toHSV();
        hsv[1] *= (double) saturationMultiplier;
        newColor = Color.convertHSVtoRGB(hsv[0], hsv[1], hsv[2]);
        return newColor.toInt();
    }

    public static int applySeasonalGrassColouring(ISeasonColorProvider colorProvider, Holder<Biome> biome, int originalColour) {
        ResourceKey<Level> dimension = Minecraft.getInstance().level.m_46472_();
        if (!biome.is(ModTags.Biomes.BLACKLISTED_BIOMES) && ServerConfig.isDimensionWhitelisted(dimension)) {
            int overlay = colorProvider.getGrassOverlay();
            float saturationMultiplier = colorProvider.getGrassSaturationMultiplier();
            if (!ServerConfig.changeGrassColor.get()) {
                overlay = Season.SubSeason.MID_SUMMER.getGrassOverlay();
                saturationMultiplier = Season.SubSeason.MID_SUMMER.getGrassSaturationMultiplier();
            }
            int newColour = overlay == 16777215 ? originalColour : overlayBlend(originalColour, overlay);
            int fixedColour = newColour;
            if (biome.is(ModTags.Biomes.LESSER_COLOR_CHANGE_BIOMES)) {
                fixedColour = mixColours(newColour, originalColour, 0.75F);
            }
            return saturationMultiplier != -1.0F ? saturateColour(fixedColour, saturationMultiplier) : fixedColour;
        } else {
            return originalColour;
        }
    }

    public static int applySeasonalFoliageColouring(ISeasonColorProvider colorProvider, Holder<Biome> biome, int originalColour) {
        ResourceKey<Level> dimension = Minecraft.getInstance().level.m_46472_();
        if (!biome.is(ModTags.Biomes.BLACKLISTED_BIOMES) && ServerConfig.isDimensionWhitelisted(dimension)) {
            int overlay = colorProvider.getFoliageOverlay();
            float saturationMultiplier = colorProvider.getFoliageSaturationMultiplier();
            if (!ServerConfig.changeFoliageColor.get()) {
                overlay = Season.SubSeason.MID_SUMMER.getFoliageOverlay();
                saturationMultiplier = Season.SubSeason.MID_SUMMER.getFoliageSaturationMultiplier();
            }
            int newColour = overlay == 16777215 ? originalColour : overlayBlend(originalColour, overlay);
            int fixedColour = newColour;
            if (biome.is(ModTags.Biomes.LESSER_COLOR_CHANGE_BIOMES)) {
                fixedColour = mixColours(newColour, originalColour, 0.75F);
            }
            return saturationMultiplier != -1.0F ? saturateColour(fixedColour, saturationMultiplier) : fixedColour;
        } else {
            return originalColour;
        }
    }
}
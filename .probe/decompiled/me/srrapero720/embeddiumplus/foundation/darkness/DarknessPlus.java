package me.srrapero720.embeddiumplus.foundation.darkness;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

public class DarknessPlus {

    public static final double MIN = 0.03;

    public static boolean enabled = false;

    private static final float[][] LUMINANCE = new float[16][16];

    public static Vec3 getDarkFogColor(Vec3 vanilla, double factor) {
        return factor == 1.0 ? vanilla : new Vec3(Math.max(0.03, vanilla.x * factor), Math.max(0.03, vanilla.y * factor), Math.max(0.03, vanilla.z * factor));
    }

    private static boolean isDark(Level world) {
        if (EmbyConfig.darknessMode.get() == EmbyConfig.DarknessMode.OFF) {
            return false;
        } else {
            ResourceKey<Level> dimType = world.dimension();
            if (dimType == Level.OVERWORLD) {
                return EmbyConfig.darknessOnOverworldCache;
            } else if (dimType == Level.NETHER) {
                return EmbyConfig.darknessOnNetherCache;
            } else if (dimType == Level.END) {
                return EmbyConfig.darknessOnEndCache;
            } else if (EmbyTools.isWhitelisted(dimType.location(), EmbyConfig.darknessDimensionWhiteList)) {
                return true;
            } else {
                return world.dimensionType().hasSkyLight() ? EmbyConfig.darknessByDefaultCache : EmbyConfig.darknessOnNoSkyLightCache;
            }
        }
    }

    private static float skyFactor(Level world) {
        if (EmbyConfig.darknessBlockLightOnlyCache || !isDark(world)) {
            return 1.0F;
        } else if (!world.dimensionType().hasSkyLight()) {
            return 0.0F;
        } else {
            float angle = world.m_46942_(0.0F);
            if (angle > 0.25F && angle < 0.75F) {
                float oldWeight = Math.max(0.0F, Math.abs(angle - 0.5F) - 0.2F) * 20.0F;
                float moon = EmbyConfig.darknessAffectedByMoonPhaseCache ? world.m_46940_() : 0.0F;
                float moonInterpolated = (float) Mth.lerp((double) moon, EmbyConfig.darknessNewMoonBrightCache, EmbyConfig.darknessFullMoonBrightCache);
                return Mth.lerp(oldWeight * oldWeight * oldWeight, moonInterpolated, 1.0F);
            } else {
                return 1.0F;
            }
        }
    }

    public static int darken(int c, int blockIndex, int skyIndex) {
        float lTarget = LUMINANCE[blockIndex][skyIndex];
        float r = (float) (c & 0xFF) / 255.0F;
        float g = (float) (c >> 8 & 0xFF) / 255.0F;
        float b = (float) (c >> 16 & 0xFF) / 255.0F;
        float l = luminance(r, g, b);
        float f = l > 0.0F ? Math.min(1.0F, lTarget / l) : 0.0F;
        return f == 1.0F ? c : 0xFF000000 | Math.round(f * r * 255.0F) | Math.round(f * g * 255.0F) << 8 | Math.round(f * b * 255.0F) << 16;
    }

    public static float luminance(float r, float g, float b) {
        return r * 0.2126F + g * 0.7152F + b * 0.0722F;
    }

    public static void updateLuminance(float tickDelta, Minecraft client, GameRenderer gameRenderer, float prevFlicker) {
        ClientLevel level = client.level;
        if (level != null) {
            boolean isDarkOnLevel = isDark(level);
            enabled = isDarkOnLevel && !client.player.m_21023_(MobEffects.NIGHT_VISION) && (!client.player.m_21023_(MobEffects.CONDUIT_POWER) || !(client.player.getWaterVision() > 0.0F)) && level.getSkyFlashTime() <= 0;
            if (enabled) {
                float dimSkyFactor = skyFactor(level);
                float ambient = level.getSkyDarken(1.0F);
                DimensionType dim = level.m_6042_();
                for (int skyIndex = 0; skyIndex < 16; skyIndex++) {
                    float skyFactor = 1.0F - (float) skyIndex / 15.0F;
                    skyFactor = 1.0F - skyFactor * skyFactor * skyFactor * skyFactor;
                    skyFactor *= dimSkyFactor;
                    float value = ((EmbyConfig.DarknessMode) EmbyConfig.darknessMode.get()).value;
                    if (value == -1.0F) {
                        throw new IllegalStateException("Darkness value can't be negative");
                    }
                    float min = Math.max(skyFactor * 0.05F, value);
                    float rawAmbient = ambient * skyFactor;
                    float minAmbient = rawAmbient * (1.0F - min) + min;
                    float skyBase = LightTexture.getBrightness(dim, skyIndex) * minAmbient;
                    min = Math.max(0.35F * skyFactor, value);
                    float v = skyBase * (rawAmbient * (1.0F - min) + min);
                    float skyRed = v;
                    float skyGreen = v;
                    float skyBlue = skyBase;
                    if (gameRenderer.getDarkenWorldAmount(tickDelta) > 0.0F) {
                        float skyDarkness = gameRenderer.getDarkenWorldAmount(tickDelta);
                        skyRed = v * (1.0F - skyDarkness) + v * 0.7F * skyDarkness;
                        skyGreen = v * (1.0F - skyDarkness) + v * 0.6F * skyDarkness;
                        skyBlue = skyBase * (1.0F - skyDarkness) + skyBase * 0.6F * skyDarkness;
                    }
                    for (int blockIndex = 0; blockIndex < 16; blockIndex++) {
                        float blockFactor = 1.0F - (float) blockIndex / 15.0F;
                        blockFactor = 1.0F - blockFactor * blockFactor * blockFactor * blockFactor;
                        float blockBase = blockFactor * LightTexture.getBrightness(dim, blockIndex) * (prevFlicker * 0.1F + 1.5F);
                        min = 0.4F * blockFactor;
                        float blockGreen = blockBase * ((blockBase * (1.0F - min) + min) * (1.0F - min) + min);
                        float blockBlue = blockBase * (blockBase * blockBase * (1.0F - min) + min);
                        float red = skyRed + blockBase;
                        float green = skyGreen + blockGreen;
                        float blue = skyBlue + blockBlue;
                        float f = Math.max(skyFactor, blockFactor);
                        min = 0.03F * f;
                        red = red * (0.99F - min) + min;
                        green = green * (0.99F - min) + min;
                        blue = blue * (0.99F - min) + min;
                        if (level.m_46472_() == Level.END) {
                            red = skyFactor * 0.22F + blockBase * 0.75F;
                            green = skyFactor * 0.28F + blockGreen * 0.75F;
                            blue = skyFactor * 0.25F + blockBlue * 0.75F;
                        }
                        if (red > 1.0F) {
                            red = 1.0F;
                        }
                        if (green > 1.0F) {
                            green = 1.0F;
                        }
                        if (blue > 1.0F) {
                            blue = 1.0F;
                        }
                        float gamma = client.options.gamma().get().floatValue() * f;
                        float invRed = 1.0F - red;
                        float invGreen = 1.0F - green;
                        float invBlue = 1.0F - blue;
                        invRed = 1.0F - invRed * invRed * invRed * invRed;
                        invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
                        invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
                        red = red * (1.0F - gamma) + invRed * gamma;
                        green = green * (1.0F - gamma) + invGreen * gamma;
                        blue = blue * (1.0F - gamma) + invBlue * gamma;
                        min = Math.max(0.03F * f, ((EmbyConfig.DarknessMode) EmbyConfig.darknessMode.get()).value);
                        red = red * (0.99F - min) + min;
                        green = green * (0.99F - min) + min;
                        blue = blue * (0.99F - min) + min;
                        red = Mth.clamp(red, 0.0F, 1.0F);
                        green = Mth.clamp(green, 0.0F, 1.0F);
                        blue = Mth.clamp(blue, 0.0F, 1.0F);
                        LUMINANCE[blockIndex][skyIndex] = luminance(red, green, blue);
                    }
                }
            }
        }
    }
}
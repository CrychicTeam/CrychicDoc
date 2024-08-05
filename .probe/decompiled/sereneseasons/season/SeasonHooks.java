package sereneseasons.season;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.config.ServerConfig;
import sereneseasons.init.ModTags;

public class SeasonHooks {

    public static boolean shouldSnowHook(Biome biome, LevelReader levelReader, BlockPos pos) {
        if ((!SeasonsConfig.generateSnowAndIce.get() || !warmEnoughToRainSeasonal(levelReader, pos)) && (SeasonsConfig.generateSnowAndIce.get() || !biome.warmEnoughToRain(pos))) {
            if (pos.m_123342_() >= levelReader.getMinBuildHeight() && pos.m_123342_() < levelReader.m_151558_() && levelReader.m_45517_(LightLayer.BLOCK, pos) < 10) {
                BlockState blockstate = levelReader.m_8055_(pos);
                if (blockstate.m_60795_() && Blocks.SNOW.defaultBlockState().m_60710_(levelReader, pos)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean shouldFreezeWarmEnoughToRainHook(Biome biome, BlockPos pos, LevelReader levelReader) {
        return SeasonsConfig.generateSnowAndIce.get() && warmEnoughToRainSeasonal(levelReader, pos) || !SeasonsConfig.generateSnowAndIce.get() && biome.warmEnoughToRain(pos);
    }

    public static boolean isRainingAtHook(Level level, BlockPos position) {
        if (!level.isRaining()) {
            return false;
        } else if (!level.m_45527_(position)) {
            return false;
        } else if (level.m_5452_(Heightmap.Types.MOTION_BLOCKING, position).m_123342_() > position.m_123342_()) {
            return false;
        } else {
            Holder<Biome> biome = level.m_204166_(position);
            if (!ServerConfig.isDimensionWhitelisted(level.dimension()) || biome.is(ModTags.Biomes.BLACKLISTED_BIOMES)) {
                return biome.value().getPrecipitationAt(position) == Biome.Precipitation.RAIN && biome.value().getTemperature(position) >= 0.15F;
            } else if (!shouldRainInBiomeInSeason(level, biome, position)) {
                return false;
            } else {
                return biome.is(ModTags.Biomes.TROPICAL_BIOMES) ? true : getBiomeTemperature(level, biome, position) >= 0.15F;
            }
        }
    }

    public static Biome.Precipitation getPrecipitationAtTickChunkHook(LevelReader level, Biome biome, BlockPos pos) {
        if (!biome.hasPrecipitation()) {
            return Biome.Precipitation.NONE;
        } else {
            boolean shouldSnow = SeasonsConfig.generateSnowAndIce.get() && coldEnoughToSnowSeasonal(level, pos) || !SeasonsConfig.generateSnowAndIce.get() && biome.coldEnoughToSnow(pos);
            return shouldSnow ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Biome.Precipitation getPrecipitationAtLevelRendererHook(Holder<Biome> biome, BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        if (!biome.value().hasPrecipitation()) {
            return Biome.Precipitation.NONE;
        } else {
            return coldEnoughToSnowSeasonal(level, biome, pos) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
        }
    }

    public static boolean coldEnoughToSnowSeasonal(LevelReader level, BlockPos pos) {
        return coldEnoughToSnowSeasonal(level, level.getBiome(pos), pos);
    }

    public static boolean coldEnoughToSnowSeasonal(LevelReader level, Holder<Biome> biome, BlockPos pos) {
        return !warmEnoughToRainSeasonal(level, biome, pos);
    }

    public static boolean warmEnoughToRainSeasonal(LevelReader level, BlockPos pos) {
        return warmEnoughToRainSeasonal(level, level.getBiome(pos), pos);
    }

    public static boolean warmEnoughToRainSeasonal(LevelReader level, Holder<Biome> biome, BlockPos pos) {
        return getBiomeTemperature(level, biome, pos) >= 0.15F;
    }

    public static float getBiomeTemperature(LevelReader level, Holder<Biome> biome, BlockPos pos) {
        return !(level instanceof Level) ? biome.value().getTemperature(pos) : getBiomeTemperature((Level) level, biome, pos);
    }

    public static float getBiomeTemperature(Level level, Holder<Biome> biome, BlockPos pos) {
        return ServerConfig.isDimensionWhitelisted(level.dimension()) && !biome.is(ModTags.Biomes.BLACKLISTED_BIOMES) ? getBiomeTemperatureInSeason(new SeasonTime(SeasonHelper.getSeasonState(level).getSeasonCycleTicks()).getSubSeason(), biome, pos) : biome.value().getTemperature(pos);
    }

    public static float getBiomeTemperatureInSeason(Season.SubSeason subSeason, Holder<Biome> biome, BlockPos pos) {
        boolean tropicalBiome = biome.is(ModTags.Biomes.TROPICAL_BIOMES);
        float biomeTemp = biome.value().getTemperature(pos);
        if (!tropicalBiome && biome.value().getBaseTemperature() <= 0.8F && !biome.is(ModTags.Biomes.BLACKLISTED_BIOMES)) {
            switch(subSeason) {
                case LATE_SPRING:
                case EARLY_AUTUMN:
                    biomeTemp = Mth.clamp(biomeTemp - 0.1F, -0.5F, 2.0F);
                    break;
                case MID_SPRING:
                case MID_AUTUMN:
                    biomeTemp = Mth.clamp(biomeTemp - 0.2F, -0.5F, 2.0F);
                    break;
                case EARLY_SPRING:
                case LATE_AUTUMN:
                    biomeTemp = Mth.clamp(biomeTemp - 0.4F, -0.5F, 2.0F);
                    break;
                case EARLY_WINTER:
                case MID_WINTER:
                case LATE_WINTER:
                    biomeTemp = Mth.clamp(biomeTemp - 0.8F, -0.5F, 2.0F);
            }
        }
        return biomeTemp;
    }

    public static boolean shouldRainInBiomeInSeason(Level level, Holder<Biome> biome, BlockPos pos) {
        if (biome.is(ModTags.Biomes.TROPICAL_BIOMES)) {
            Season.TropicalSeason tropicalSeason = SeasonHelper.getSeasonState(level).getTropicalSeason();
            switch(tropicalSeason) {
                case MID_DRY:
                    return false;
                case MID_WET:
                    return true;
            }
        }
        return biome.value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
    }
}
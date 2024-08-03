package sereneseasons.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.InMemoryFormat;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import sereneseasons.api.season.Season;

public class ServerConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static ForgeConfigSpec.IntValue dayDuration = BUILDER.comment("The duration of a Minecraft day in ticks.\nThis only adjusts the internal length of a day used by the season cycle.\nIt is intended to be used in conjunction with another mod which adjusts the actual length of a Minecraft day.").defineInRange("day_duration", 24000, 20, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue subSeasonDuration = BUILDER.comment("The duration of a sub season in days").defineInRange("sub_season_duration", 8, 1, Integer.MAX_VALUE);

    public static ForgeConfigSpec.IntValue startingSubSeason = BUILDER.comment("The starting sub season for new worlds.\n0 = Random, 1 - 3 = Early/Mid/Late Spring\n4 - 6 = Early/Mid/Late Summer\n7 - 9 = Early/Mid/Late Autumn\n10 - 12 = Early/Mid/Late Winter").defineInRange("starting_sub_season", 1, 0, 12);

    public static ForgeConfigSpec.BooleanValue progressSeasonWhileOffline = BUILDER.comment("If the season should progress on a server with no players online").define("progress_season_while_offline", true);

    public static ForgeConfigSpec.BooleanValue changeGrassColor = BUILDER.comment("Change the grass color based on the current season").define("change_grass_color", true);

    public static ForgeConfigSpec.BooleanValue changeFoliageColor = BUILDER.comment("Change the foliage colour based on the current season").define("change_foliage_color", true);

    public static ForgeConfigSpec.BooleanValue changeBirchColor = BUILDER.comment("Change the birch colour based on the current season").define("change_birch_color", true);

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedDimensions = BUILDER.comment("Seasons will only apply to dimensons listed here").defineList("whitelisted_dimensions", ServerConfig.defaultWhitelistedDimensions, ServerConfig.RESOURCE_LOCATION_VALIDATOR);

    private static List<String> defaultWhitelistedDimensions = Lists.newArrayList(new String[] { Level.OVERWORLD.location().toString() });

    private static ForgeConfigSpec.ConfigValue<List<Config>> meltChanceEntries = BUILDER.comment("The melting settings for snow and ice in each season. The game must be restarted for these to apply.\nmelt_percent is the 0-1 percentage chance a snow or ice block will melt when chosen. (e.g. 100.0 = 100%, 50.0 = 50%)\nrolls is the number of blocks randomly picked in each chunk, each tick. (High number rolls is not recommended on servers)\nrolls should be 0 if blocks should not melt in that season.").define("season_melt_chances", ServerConfig.defaultMeltChances, ServerConfig.MELT_INFO_VALIDATOR);

    private static List<Config> defaultMeltChances = (List<Config>) Lists.newArrayList(new ServerConfig.MeltChanceInfo[] { new ServerConfig.MeltChanceInfo(Season.SubSeason.EARLY_WINTER, 0.0F, 0), new ServerConfig.MeltChanceInfo(Season.SubSeason.MID_WINTER, 0.0F, 0), new ServerConfig.MeltChanceInfo(Season.SubSeason.LATE_WINTER, 0.0F, 0), new ServerConfig.MeltChanceInfo(Season.SubSeason.EARLY_SPRING, 6.25F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.MID_SPRING, 8.33F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.LATE_SPRING, 12.5F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.EARLY_SUMMER, 25.0F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.MID_SUMMER, 25.0F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.LATE_SUMMER, 25.0F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.EARLY_AUTUMN, 12.5F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.MID_AUTUMN, 8.33F, 1), new ServerConfig.MeltChanceInfo(Season.SubSeason.LATE_AUTUMN, 6.25F, 1) }).stream().map(ServerConfig::meltChanceInfoToConfig).collect(Collectors.toList());

    private static final Predicate<Object> RESOURCE_LOCATION_VALIDATOR = obj -> {
        if (!(obj instanceof String)) {
            return false;
        } else {
            try {
                new ResourceLocation((String) obj);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    };

    private static final Predicate<Object> MELT_INFO_VALIDATOR = obj -> {
        if (!(obj instanceof List)) {
            return false;
        } else {
            for (Object i : (List) obj) {
                if (!(i instanceof Config config)) {
                    return false;
                }
                if (!config.contains("season")) {
                    return false;
                }
                if (!config.contains("melt_percent")) {
                    return false;
                }
                if (!config.contains("rolls")) {
                    return false;
                }
                try {
                    config.getEnum("season", Season.SubSeason.class);
                    float meltChance = ((Number) config.get("melt_percent")).floatValue();
                    if (meltChance < 0.0F || meltChance > 100.0F) {
                        return false;
                    }
                    if (config.getInt("rolls") < 0) {
                        return false;
                    }
                } catch (Exception var5) {
                    return false;
                }
            }
            return true;
        }
    };

    private static ImmutableMap<Season.SubSeason, ServerConfig.MeltChanceInfo> meltInfoCache;

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        for (String whitelistedDimension : whitelistedDimensions.get()) {
            if (dimension.location().toString().equals(whitelistedDimension)) {
                return true;
            }
        }
        return false;
    }

    private static Config meltChanceInfoToConfig(ServerConfig.MeltChanceInfo meltChanceInfo) {
        Config config = Config.of(LinkedHashMap::new, InMemoryFormat.withUniversalSupport());
        config.add("season", meltChanceInfo.getSubSeason().toString());
        config.add("melt_percent", meltChanceInfo.getMeltChance());
        config.add("rolls", meltChanceInfo.getRolls());
        return config;
    }

    @Nullable
    public static ServerConfig.MeltChanceInfo getMeltInfo(Season.SubSeason season) {
        return (ServerConfig.MeltChanceInfo) getMeltInfos().get(season);
    }

    private static ImmutableMap<Season.SubSeason, ServerConfig.MeltChanceInfo> getMeltInfos() {
        if (meltInfoCache != null) {
            return meltInfoCache;
        } else {
            Map<Season.SubSeason, ServerConfig.MeltChanceInfo> tmp = Maps.newHashMap();
            for (Config config : meltChanceEntries.get()) {
                Season.SubSeason subSeason = (Season.SubSeason) config.getEnum("season", Season.SubSeason.class);
                float meltChance = ((Number) config.get("melt_percent")).floatValue();
                int rolls = config.getInt("rolls");
                tmp.put(subSeason, new ServerConfig.MeltChanceInfo(subSeason, meltChance, rolls));
            }
            meltInfoCache = ImmutableMap.copyOf(tmp);
            return meltInfoCache;
        }
    }

    static {
        BUILDER.push("time_settings");
        BUILDER.pop();
        BUILDER.push("aesthetic_settings");
        BUILDER.pop();
        BUILDER.push("dimension_settings");
        BUILDER.pop();
        BUILDER.push("melting_settings");
        BUILDER.pop();
    }

    public static class MeltChanceInfo {

        private final Season.SubSeason subSeason;

        private final float meltChance;

        private final int rolls;

        private MeltChanceInfo(Season.SubSeason subSeason, float meltChance, int rolls) {
            this.subSeason = subSeason;
            this.meltChance = meltChance;
            this.rolls = rolls;
        }

        public Season.SubSeason getSubSeason() {
            return this.subSeason;
        }

        public float getMeltChance() {
            return this.meltChance;
        }

        public int getRolls() {
            return this.rolls;
        }
    }
}
package sereneseasons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SeasonsConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static ForgeConfigSpec.BooleanValue generateSnowAndIce = BUILDER.comment("Generate snow and ice during the Winter season").define("generate_snow_ice", true);

    public static ForgeConfigSpec.BooleanValue changeWeatherFrequency = BUILDER.comment("Change the frequency of rain/snow/storms based on the season").define("change_weather_frequency", true);

    static {
        BUILDER.comment("Please be advised that certain season-related options are world-specific and are located in <Path to your world folder>/serverconfig/sereneseasons-server.toml.");
        BUILDER.push("weather_settings");
        BUILDER.pop();
    }
}
package dev.worldgen.tectonic.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record ConfigCodec(boolean modEnabled, ConfigCodec.Legacy legacyModule, ConfigCodec.Features featuresModule, ConfigCodec.Experimental experimentalModule) {

    public static final Codec<ConfigCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.fieldOf("mod_enabled").orElse(true).forGetter(ConfigCodec::modEnabled), ConfigCodec.Legacy.CODEC.fieldOf("legacy").orElse(ConfigCodec.Legacy.DEFAULT).forGetter(ConfigCodec::legacyModule), ConfigCodec.Features.CODEC.fieldOf("features").orElse(ConfigCodec.Features.DEFAULT).forGetter(ConfigCodec::featuresModule), ConfigCodec.Experimental.CODEC.fieldOf("experimental").orElse(ConfigCodec.Experimental.DEFAULT).forGetter(ConfigCodec::experimentalModule)).apply(instance, ConfigCodec::new));

    private List<String> getEnabledPacks(boolean terralithEnabled) {
        List<String> enabledPacks = new ArrayList();
        if (this.modEnabled()) {
            if (this.legacyModule().enabled()) {
                enabledPacks.add("legacy");
            }
            if (this.experimentalModule().increasedHeight()) {
                enabledPacks.add("increased_height");
            }
            enabledPacks.add(terralithEnabled ? "terratonic" : "tectonic");
        }
        return enabledPacks;
    }

    public void enablePacks(boolean terralithEnabled, Consumer<String> registerPack) {
        for (String packName : ConfigHandler.getConfig().getEnabledPacks(terralithEnabled)) {
            registerPack.accept(packName);
        }
    }

    public double getValue(String option) {
        return switch(option) {
            case "terrain_scale" ->
                this.experimentalModule().terrainScale();
            case "horizontal_mountain_scale" ->
                this.experimentalModule().horizontalMountainScale();
            case "deeper_oceans" ->
                this.featuresModule().deeperOceans() ? 1.0 : 0.0;
            case "desert_dunes" ->
                this.featuresModule().desertDunes() ? 1.0 : 0.0;
            case "lava_rivers" ->
                this.featuresModule().lavaRivers() ? 1.0 : 0.0;
            case "underground_rivers" ->
                this.featuresModule().undergroundRivers() ? 1.0 : 0.0;
            default ->
                0.0;
        };
    }

    public static record Experimental(String commentA, String commentB, String commentC, String commentD, boolean increasedHeight, double horizontalMountainScale, double terrainScale) {

        private static final String COMMENT_A = "The increased height setting will change the max Overworld build and generation height to y640.";

        private static final String COMMENT_B = "The horizontal mountain scale setting will change the thickness of mountain ranges and the spacing between them.";

        private static final String COMMENT_C = "Lower values = thicker mountain ranges and more space between ranges. 0.15-0.25 is the sweet spot.";

        private static final String COMMENT_D = "The terrain scale setting will vertically stretch/compress terrain. Higher values = more extreme terrain heights.";

        public static final Codec<ConfigCodec.Experimental> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("__A").orElse("The increased height setting will change the max Overworld build and generation height to y640.").forGetter(ConfigCodec.Experimental::commentA), Codec.STRING.fieldOf("__B").orElse("The horizontal mountain scale setting will change the thickness of mountain ranges and the spacing between them.").forGetter(ConfigCodec.Experimental::commentB), Codec.STRING.fieldOf("__C").orElse("Lower values = thicker mountain ranges and more space between ranges. 0.15-0.25 is the sweet spot.").forGetter(ConfigCodec.Experimental::commentC), Codec.STRING.fieldOf("__D").orElse("The terrain scale setting will vertically stretch/compress terrain. Higher values = more extreme terrain heights.").forGetter(ConfigCodec.Experimental::commentD), Codec.BOOL.fieldOf("increased_height").orElse(false).forGetter(ConfigCodec.Experimental::increasedHeight), Codec.DOUBLE.fieldOf("horizontal_mountain_scale").orElse(0.25).forGetter(ConfigCodec.Experimental::horizontalMountainScale), Codec.DOUBLE.fieldOf("terrain_scale").orElse(1.125).forGetter(ConfigCodec.Experimental::terrainScale)).apply(instance, ConfigCodec.Experimental::new));

        public static final ConfigCodec.Experimental DEFAULT = new ConfigCodec.Experimental("The increased height setting will change the max Overworld build and generation height to y640.", "The horizontal mountain scale setting will change the thickness of mountain ranges and the spacing between them.", "Lower values = thicker mountain ranges and more space between ranges. 0.15-0.25 is the sweet spot.", "The terrain scale setting will vertically stretch/compress terrain. Higher values = more extreme terrain heights.", false, 0.25, 1.125);
    }

    public static record Features(String commentA, String commentB, boolean deeperOceans, boolean desertDunes, boolean lavaRivers, boolean undergroundRivers) {

        private static final String COMMENT_A = "Enabling deeper oceans will lower vanilla ocean monuments to compensate for lower depth.";

        private static final String COMMENT_B = "This DOES NOT apply on Forge 1.18-1.20.1. Ocean monuments will remain at their vanilla levels on those versions.";

        public static final Codec<ConfigCodec.Features> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("__A").orElse("Enabling deeper oceans will lower vanilla ocean monuments to compensate for lower depth.").forGetter(ConfigCodec.Features::commentA), Codec.STRING.fieldOf("__B").orElse("This DOES NOT apply on Forge 1.18-1.20.1. Ocean monuments will remain at their vanilla levels on those versions.").forGetter(ConfigCodec.Features::commentB), Codec.BOOL.fieldOf("deeper_oceans").orElse(true).forGetter(ConfigCodec.Features::deeperOceans), Codec.BOOL.fieldOf("desert_dunes").orElse(true).forGetter(ConfigCodec.Features::desertDunes), Codec.BOOL.fieldOf("lava_rivers").orElse(true).forGetter(ConfigCodec.Features::lavaRivers), Codec.BOOL.fieldOf("underground_rivers").orElse(true).forGetter(ConfigCodec.Features::undergroundRivers)).apply(instance, ConfigCodec.Features::new));

        public static final ConfigCodec.Features DEFAULT = new ConfigCodec.Features("Enabling deeper oceans will lower vanilla ocean monuments to compensate for lower depth.", "This DOES NOT apply on Forge 1.18-1.20.1. Ocean monuments will remain at their vanilla levels on those versions.", true, true, true, true);
    }

    public static record Legacy(String commentA, String commentB, String commentC, boolean enabled) {

        private static final String COMMENT_A = "Tectonic v1 worlds have old biome data preventing them from being opened in Tectonic v2.1+.";

        private static final String COMMENT_B = "Enabling legacy mode will add back the biomes and upgrade worlds to the new format upon opening them.";

        private static final String COMMENT_C = "Once a world is upgraded by opening it, turn off legacy mode.";

        public static final Codec<ConfigCodec.Legacy> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("__A").orElse("Tectonic v1 worlds have old biome data preventing them from being opened in Tectonic v2.1+.").forGetter(ConfigCodec.Legacy::commentA), Codec.STRING.fieldOf("__B").orElse("Enabling legacy mode will add back the biomes and upgrade worlds to the new format upon opening them.").forGetter(ConfigCodec.Legacy::commentB), Codec.STRING.fieldOf("__C").orElse("Once a world is upgraded by opening it, turn off legacy mode.").forGetter(ConfigCodec.Legacy::commentC), Codec.BOOL.fieldOf("enabled").orElse(false).forGetter(ConfigCodec.Legacy::enabled)).apply(instance, ConfigCodec.Legacy::new));

        public static final ConfigCodec.Legacy DEFAULT = new ConfigCodec.Legacy("Tectonic v1 worlds have old biome data preventing them from being opened in Tectonic v2.1+.", "Enabling legacy mode will add back the biomes and upgrade worlds to the new format upon opening them.", "Once a world is upgraded by opening it, turn off legacy mode.", false);
    }
}
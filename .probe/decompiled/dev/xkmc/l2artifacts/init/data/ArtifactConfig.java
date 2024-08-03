package dev.xkmc.l2artifacts.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class ArtifactConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ArtifactConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ArtifactConfig.Common COMMON;

    public static String COMMON_PATH;

    public static void init() {
        register(Type.CLIENT, CLIENT_SPEC);
        COMMON_PATH = register(Type.COMMON, COMMON_SPEC);
    }

    private static String register(Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
        return path;
    }

    static {
        Pair<ArtifactConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(ArtifactConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (ArtifactConfig.Client) client.getLeft();
        Pair<ArtifactConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ArtifactConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) specPair.getRight();
        COMMON = (ArtifactConfig.Common) specPair.getLeft();
    }

    public static class Client {

        Client(ForgeConfigSpec.Builder builder) {
        }
    }

    public static class Common {

        public final ForgeConfigSpec.IntValue maxRank;

        public final ForgeConfigSpec.IntValue maxLevelPerRank;

        public final ForgeConfigSpec.IntValue levelPerSubStat;

        public final ForgeConfigSpec.IntValue storageSmall;

        public final ForgeConfigSpec.IntValue storageLarge;

        public final ForgeConfigSpec.DoubleValue expConsumptionRankFactor;

        public final ForgeConfigSpec.DoubleValue expLevelFactor;

        public final ForgeConfigSpec.IntValue baseExpConsumption;

        public final ForgeConfigSpec.DoubleValue expRetention;

        public final ForgeConfigSpec.IntValue baseExpConversion;

        public final ForgeConfigSpec.DoubleValue expConversionRankFactor;

        public final ForgeConfigSpec.BooleanValue enableArtifactRankUpRecipe;

        Common(ForgeConfigSpec.Builder builder) {
            this.maxRank = builder.comment("maximum available rank (Not implemented. Don't change.)").defineInRange("maxRank", 5, 5, 5);
            this.maxLevelPerRank = builder.comment("maximum level per rank (Not tested. Don't change)").defineInRange("maxLevelPerRank", 4, 1, 100);
            this.levelPerSubStat = builder.comment("level per sub stats granted (Not Tested. Don't change)").defineInRange("levelPerSubStat", 4, 1, 100);
            this.storageSmall = builder.comment("maximum available slots for artifact pocket").defineInRange("storageSmall", 256, 64, 1024);
            this.storageLarge = builder.comment("maximum available slots for upgraded artifact pocket").defineInRange("storageLarge", 512, 64, 1024);
            this.expConsumptionRankFactor = builder.comment("exponential experience requirement per rank").defineInRange("expConsumptionRankFactor", 2.0, 1.0, 10.0);
            this.expLevelFactor = builder.comment("exponential experience requirement per level").defineInRange("expLevelFactor", 1.05, 1.0, 10.0);
            this.baseExpConsumption = builder.comment("experience requirement for level 0 rank 1 artifact").defineInRange("baseExpConsumption", 100, 1, 10000);
            this.expRetention = builder.comment("experience retained for using upgraded artifact to upgrade").defineInRange("expRetention", 0.9, 0.0, 1.0);
            this.baseExpConversion = builder.comment("experience available for level 0 rank 1 artifact").defineInRange("baseExpConversion", 100, 1, 1000000);
            this.expConversionRankFactor = builder.comment("exponential experience available per rank").defineInRange("expConversionRankFactor", 2.0, 1.0, 10.0);
            this.enableArtifactRankUpRecipe = builder.comment("Enable Artifact Rank up recipe").define("enableArtifactRankUpRecipe", true);
        }
    }
}
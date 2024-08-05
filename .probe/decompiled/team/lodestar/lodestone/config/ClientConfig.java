package team.lodestar.lodestone.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import team.lodestar.lodestone.systems.config.LodestoneConfig;

public class ClientConfig extends LodestoneConfig {

    public static LodestoneConfig.ConfigValueHolder<Boolean> DELAYED_PARTICLE_RENDERING = new LodestoneConfig.ConfigValueHolder<>("lodestone", "client/graphics/particle", builder -> builder.comment("Should particles render on the delayed buffer? This means they will properly render after clouds & water do, but could cause issues with mods like sodium.").define("buffer_particles", true));

    public static LodestoneConfig.ConfigValueHolder<Boolean> EXPERIMENTAL_FABULOUS_LAYERING = new LodestoneConfig.ConfigValueHolder<>("lodestone", "client/graphics", builder -> builder.comment("Should lodestone use experimental fabulous graphics layering? You pretty much never wanna turn this on at the moment unless you're a developer.").define("experimental_fabulous_layering", false));

    public static LodestoneConfig.ConfigValueHolder<Double> FIRE_OVERLAY_OFFSET = new LodestoneConfig.ConfigValueHolder<>("lodestone", "client/graphics/fire", builder -> builder.comment("Downwards offset of Minecraft's first-person fire overlay. Higher numbers cause it to visually display lower and free up more screen space.").defineInRange("fire_overlay_offset", 0.0, 0.0, 1.0));

    public static LodestoneConfig.ConfigValueHolder<Double> SCREENSHAKE_INTENSITY = new LodestoneConfig.ConfigValueHolder<>("lodestone", "client/screenshake", builder -> builder.comment("Intensity of screenshake. Higher numbers increase amplitude. Disable to turn off screenshake.").defineInRange("screenshake_intensity", 1.0, 0.0, 5.0));

    public static LodestoneConfig.ConfigValueHolder<Boolean> ENABLE_SCREEN_PARTICLES = new LodestoneConfig.ConfigValueHolder<>("lodestone", "client/screen_particles", builder -> builder.comment("Are screen particles enabled?").define("enable_screen_particles", true));

    public static final ClientConfig INSTANCE;

    public static final ForgeConfigSpec SPEC;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        super("lodestone", "client", builder);
    }

    static {
        Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = (ForgeConfigSpec) specPair.getRight();
        INSTANCE = (ClientConfig) specPair.getLeft();
    }
}
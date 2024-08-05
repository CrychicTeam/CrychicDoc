package dev.xkmc.l2archery.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class ArcheryConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ArcheryConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ArcheryConfig.Common COMMON;

    public static void init() {
        register(Type.CLIENT, CLIENT_SPEC);
        register(Type.COMMON, COMMON_SPEC);
    }

    private static void register(Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
    }

    static {
        Pair<ArcheryConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(ArcheryConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (ArcheryConfig.Client) client.getLeft();
        Pair<ArcheryConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ArcheryConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) common.getRight();
        COMMON = (ArcheryConfig.Common) common.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.BooleanValue showInfo;

        public final ForgeConfigSpec.BooleanValue showArrow;

        Client(ForgeConfigSpec.Builder builder) {
            this.showInfo = builder.comment("Show combined bow arrow stats and features when holding bow").define("showInfo", true);
            this.showArrow = builder.comment("Show projectile selection").define("showArrow", true);
        }
    }

    public static class Common {

        Common(ForgeConfigSpec.Builder builder) {
        }
    }
}
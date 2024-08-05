package dev.xkmc.l2library.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class L2LibraryConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final L2LibraryConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final L2LibraryConfig.Common COMMON;

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
        Pair<L2LibraryConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(L2LibraryConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (L2LibraryConfig.Client) client.getLeft();
        Pair<L2LibraryConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(L2LibraryConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) common.getRight();
        COMMON = (L2LibraryConfig.Common) common.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.DoubleValue infoAlpha;

        public final ForgeConfigSpec.IntValue infoAnchor;

        public final ForgeConfigSpec.DoubleValue infoMaxWidth;

        public final ForgeConfigSpec.BooleanValue selectionDisplayRequireShift;

        public final ForgeConfigSpec.BooleanValue selectionScrollRequireShift;

        Client(ForgeConfigSpec.Builder builder) {
            this.infoAlpha = builder.comment("Info background transparency. 1 means opaque.").defineInRange("infoAlpha", 0.5, 0.0, 1.0);
            this.infoAnchor = builder.comment("Info alignment. 0 means top. 1 means middle. 2 means bottom.").defineInRange("infoAnchor", 1, 0, 2);
            this.infoMaxWidth = builder.comment("Info max width. 0.5 means half screen. default: 0.3").defineInRange("infoMaxWidth", 0.3, 0.0, 0.5);
            this.selectionDisplayRequireShift = builder.comment("Render Selection only when pressing shift").define("selectionDisplayRequireShift", false);
            this.selectionScrollRequireShift = builder.comment("Scroll for selection only when pressing shift").define("selectionScrollRequireShift", true);
        }
    }

    public static class Common {

        public final ForgeConfigSpec.BooleanValue restoreFullHealthOnRespawn;

        Common(ForgeConfigSpec.Builder builder) {
            this.restoreFullHealthOnRespawn = builder.comment("Restore full health on respawn").define("restoreFullHealthOnRespawn", true);
        }
    }
}
package dev.xkmc.l2backpack.init.data;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class BackpackConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final BackpackConfig.Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final BackpackConfig.Common COMMON;

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
        Pair<BackpackConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(BackpackConfig.Client::new);
        CLIENT_SPEC = (ForgeConfigSpec) client.getRight();
        CLIENT = (BackpackConfig.Client) client.getLeft();
        Pair<BackpackConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(BackpackConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) common.getRight();
        COMMON = (BackpackConfig.Common) common.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.BooleanValue previewOnCenter;

        public final ForgeConfigSpec.BooleanValue popupArrowOnSwitch;

        public final ForgeConfigSpec.BooleanValue popupToolOnSwitch;

        public final ForgeConfigSpec.BooleanValue popupArmorOnSwitch;

        public final ForgeConfigSpec.BooleanValue drawerAlwaysRenderFlat;

        public final ForgeConfigSpec.BooleanValue reverseScroll;

        public final ForgeConfigSpec.BooleanValue backpackInsertRequiresShift;

        public final ForgeConfigSpec.BooleanValue backpackEnableLeftClickInsert;

        public final ForgeConfigSpec.BooleanValue backpackEnableRightClickInsert;

        Client(ForgeConfigSpec.Builder builder) {
            this.previewOnCenter = builder.comment("Put quiver preview near the center of the screen, rather than edge of the screen").define("previewOnCenter", true);
            this.popupArrowOnSwitch = builder.comment("Show arrow quick swap when switching to a bow").define("popupArrowOnSwitch", true);
            this.popupToolOnSwitch = builder.comment("Show tool quick swap when switching to a tool").define("popupToolOnSwitch", false);
            this.popupArmorOnSwitch = builder.comment("Show armor quick swap when switching to empty hand").define("popupArmorOnSwitch", false);
            this.reverseScroll = builder.comment("Reverse scrolling direction for quick swap").define("reverseScroll", false);
            this.backpackInsertRequiresShift = builder.comment("Backpack inventory quick insert requires shift click").define("backpackInsertRequiresShift", false);
            this.backpackEnableLeftClickInsert = builder.comment("Backpack inventory quick insert allows left click insert").define("backpackEnableLeftClickInsert", true);
            this.backpackEnableRightClickInsert = builder.comment("Backpack inventory quick insert allows right click insert").define("backpackEnableRightClickInsert", true);
            this.drawerAlwaysRenderFlat = builder.comment("Draws Always render content directly").define("drawerAlwaysRenderFlat", false);
        }

        public boolean allowBackpackInsert(int button) {
            if (this.backpackInsertRequiresShift.get() && !Screen.hasShiftDown()) {
                return false;
            } else {
                boolean allow = false;
                if (this.backpackEnableLeftClickInsert.get() && button == 0) {
                    allow = true;
                }
                if (this.backpackEnableRightClickInsert.get() && button == 1) {
                    allow = true;
                }
                return allow;
            }
        }
    }

    public static class Common {

        public final ForgeConfigSpec.IntValue initialRows;

        public final ForgeConfigSpec.IntValue startupBackpackCondition;

        Common(ForgeConfigSpec.Builder builder) {
            this.initialRows = builder.comment("Initial Rows (x9 slots) for backpack").defineInRange("initialRows", 2, 1, 8);
            this.startupBackpackCondition = builder.comment("How many items do players need to spawn with to have the privilege of having them in a backpack").defineInRange("startupBackpackCondition", 6, 1, 36);
        }
    }
}
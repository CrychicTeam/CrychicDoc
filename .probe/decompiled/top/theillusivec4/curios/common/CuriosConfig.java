package top.theillusivec4.curios.common;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CuriosConfig {

    public static final ForgeConfigSpec SERVER_SPEC;

    public static final CuriosConfig.Server SERVER;

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final CuriosConfig.Common COMMON;

    private static final String CONFIG_PREFIX = "gui.curios.config.";

    static {
        Pair<CuriosConfig.Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CuriosConfig.Server::new);
        SERVER_SPEC = (ForgeConfigSpec) specPair.getRight();
        SERVER = (CuriosConfig.Server) specPair.getLeft();
        Pair<CuriosConfig.Common, ForgeConfigSpec> cspecPair = new ForgeConfigSpec.Builder().configure(CuriosConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) cspecPair.getRight();
        COMMON = (CuriosConfig.Common) cspecPair.getLeft();
    }

    public static class Common {

        public ForgeConfigSpec.ConfigValue<List<? extends String>> slots;

        public Common(ForgeConfigSpec.Builder builder) {
            this.slots = builder.comment("List of slots to create or modify.\nSee documentation for syntax: https://docs.illusivesoulworks.com/curios/configuration#slot-configuration\n").translation("gui.curios.config.slots").defineList("slots", List.of(), s -> s instanceof String);
            builder.build();
        }
    }

    public static enum KeepCurios {

        ON, DEFAULT, OFF
    }

    public static class Server {

        public ForgeConfigSpec.EnumValue<CuriosConfig.KeepCurios> keepCurios;

        public ForgeConfigSpec.BooleanValue enableLegacyMenu;

        public ForgeConfigSpec.IntValue minimumColumns;

        public ForgeConfigSpec.IntValue maxSlotsPerPage;

        public Server(ForgeConfigSpec.Builder builder) {
            this.keepCurios = builder.comment("Sets behavior for keeping Curios items on death.\nON - Curios items are kept on death\nDEFAULT - Curios items follow the keepInventory gamerule\nOFF - Curios items are dropped on death").translation("gui.curios.config.keepCurios").defineEnum("keepCurios", CuriosConfig.KeepCurios.DEFAULT);
            builder.push("menu");
            this.enableLegacyMenu = builder.comment("Enables the old legacy Curios menu for better backwards compatibility.").translation("gui.curios.config.enableLegacyMenu").define("enableLegacyMenu", false);
            builder.push("experimental");
            this.minimumColumns = builder.comment("The minimum number of columns for the Curios menu.").translation("gui.curios.config.minimumColumns").defineInRange("minimumColumns", 1, 1, 8);
            this.maxSlotsPerPage = builder.comment("The maximum number of slots per page of the Curios menu.").translation("gui.curios.config.maxSlotsPerPage").defineInRange("maxSlotsPerPage", 48, 1, 48);
            builder.pop();
            builder.pop();
            builder.build();
        }
    }
}
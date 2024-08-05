package fuzs.overflowingbars.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import net.minecraft.ChatFormatting;

public class ClientConfig implements ConfigCore {

    @Config
    public ClientConfig.IconRowConfig health = new ClientConfig.IconRowConfig();

    @Config
    public ClientConfig.ArmorRowConfig armor = new ClientConfig.ArmorRowConfig();

    @Config
    public ClientConfig.ToughnessRowConfig toughness = new ClientConfig.ToughnessRowConfig();

    @Config
    public ClientConfig.RowCountConfig rowCount = new ClientConfig.RowCountConfig();

    @Config(category = { "general" }, description = { "Move chat messages above armor/absorption bar." })
    public boolean moveChatAboveArmor = true;

    @Config(category = { "general" }, description = { "Move the experience level display above the experience bar." })
    public boolean moveExperienceAboveBar = true;

    public static class ArmorRowConfig extends ClientConfig.IconRowConfig {

        @Config(description = { "Don't draw empty armor points, this will make the armor bar potentially shorter." })
        public boolean skipEmptyArmorPoints = true;
    }

    public static class IconRowConfig implements ConfigCore {

        @Config(description = { "Add layers to this bar. When disabled any modifications to the bar from this mod will be turned off." })
        public boolean allowLayers = true;

        @Config(description = { "Render row count to indicate total amount of rows since not all may be visible at once due to the stacked rendering." })
        public boolean allowCount = true;

        @Config(description = { "Show colorful icons on the front row, not just on all subsequent rows." })
        public boolean colorizeFirstRow = false;

        @Config(description = { "Use vanilla's icons on all front rows, use custom colored icons on the background row." })
        public boolean inverseColoring = false;
    }

    public static class RowCountConfig implements ConfigCore {

        @Config(description = { "Color of row count, use any chat formatting color value." })
        @Config.AllowedValues(values = { "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE" })
        public ChatFormatting rowCountColor = ChatFormatting.WHITE;

        @Config(description = { "Force drawing row count using the font renderer, will make numbers display larger." })
        public boolean forceFontRenderer = false;

        @Config(description = { "Only include completely filled rows for the row count." })
        public boolean countFullRowsOnly = false;

        @Config(description = { "Show row count also when only one row is present." })
        public boolean alwaysRenderRowCount = false;

        @Config(description = { "Render an 'x' together with the row count number." })
        public boolean rowCountX = true;
    }

    public static class ToughnessRowConfig extends ClientConfig.ArmorRowConfig {

        @Config(description = { "Render a separate armor bar for the armor toughness attribute (from diamond and netherite armor).", "Having only this option active will make the toughness bar behave just like vanilla's armor bar without any colorful stacking or so." })
        public boolean armorToughnessBar = true;

        @Config(description = { "Shift toughness bar up or down by specified number of icon rows. Allows for better mod compat on Fabric, has no effect on Forge." })
        @Config.IntRange(min = -5, max = 5)
        public int toughnessBarRowShift = 0;

        @Config(description = { "Render the toughness bar on the left side above the hotbar (where health and armor is rendered)." })
        public boolean leftSide = false;
    }
}
package club.iananderson.seasonhud.impl.seasons;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.util.Rgb;
import java.util.EnumSet;
import java.util.HashMap;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

public enum SeasonList {

    SPRING(0, Component.translatable("desc.seasonhud.spring"), "spring", "\uea00", Config.springColor, Config.springColor.getDefault(), Config.getSpringColor(), Rgb.seasonMap(Config.getSpringColor())),
    SUMMER(1, Component.translatable("desc.seasonhud.summer"), "summer", "\uea01", Config.summerColor, Config.summerColor.getDefault(), Config.getSummerColor(), Rgb.seasonMap(Config.getSummerColor())),
    AUTUMN(2, Component.translatable("desc.seasonhud.autumn"), "autumn", "\uea02", Config.autumnColor, Config.autumnColor.getDefault(), Config.getAutumnColor(), Rgb.seasonMap(Config.getAutumnColor())),
    WINTER(3, Component.translatable("desc.seasonhud.winter"), "winter", "\uea03", Config.winterColor, Config.winterColor.getDefault(), Config.getWinterColor(), Rgb.seasonMap(Config.getWinterColor())),
    DRY(4, Component.translatable("desc.seasonhud.dry"), "dry", "\uea04", Config.dryColor, Config.dryColor.getDefault(), Config.getDryColor(), Rgb.seasonMap(Config.getDryColor())),
    WET(5, Component.translatable("desc.seasonhud.wet"), "wet", "\uea05", Config.wetColor, Config.wetColor.getDefault(), Config.getWetColor(), Rgb.seasonMap(Config.getWetColor()));

    public static EnumSet<SeasonList> seasons = EnumSet.allOf(SeasonList.class);

    private final int id;

    private final Component seasonName;

    private final String seasonFileName;

    private final String seasonIconChar;

    private final ForgeConfigSpec.ConfigValue<Integer> seasonColorConfig;

    private final int defaultColor;

    private final HashMap<String, Integer> rgbMap;

    private int seasonColor;

    private SeasonList(int id, Component seasonName, String fileName, String iconChar, ForgeConfigSpec.ConfigValue<Integer> seasonColorConfig, int defaultColor, int seasonColor, HashMap<String, Integer> rgbMap) {
        this.id = id;
        this.seasonName = seasonName;
        this.seasonFileName = fileName;
        this.seasonIconChar = iconChar;
        this.seasonColorConfig = seasonColorConfig;
        this.defaultColor = defaultColor;
        this.seasonColor = seasonColor;
        this.rgbMap = rgbMap;
    }

    public int getId() {
        return this.id;
    }

    public String getFileName() {
        return this.seasonFileName;
    }

    public String getIconChar() {
        return this.seasonIconChar;
    }

    public ForgeConfigSpec.ConfigValue<Integer> getSeasonColorConfig() {
        return this.seasonColorConfig;
    }

    public int getSeasonColor() {
        return this.seasonColor;
    }

    public int getDefaultColor() {
        return this.defaultColor;
    }

    public Component getSeasonName() {
        return this.seasonName;
    }

    public HashMap<String, Integer> getRgbMap() {
        return this.rgbMap;
    }

    public void setColor(int rgb) {
        this.seasonColor = rgb;
        this.seasonColorConfig.set(rgb);
        this.seasonColorConfig.save();
    }
}
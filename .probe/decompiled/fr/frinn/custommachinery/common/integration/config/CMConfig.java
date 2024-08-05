package fr.frinn.custommachinery.common.integration.config;

import com.google.common.collect.Lists;
import fr.frinn.custommachinery.common.util.LoggingLevel;
import java.util.List;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "custommachinery")
public class CMConfig implements ConfigData {

    @ConfigEntry.Category("Logs")
    @Comment("If true, all missing optional properties\nand their default values will be logged\nwhen parsing custom machines jsons.")
    public boolean logMissingOptional = false;

    @ConfigEntry.Category("Logs")
    @Comment("When parsing custom machines json files,\nsome properties can be read with 2 serializers.\nSet this to true to log when the first serializer throw an error,\neven if the second succeed.")
    public boolean logFirstEitherError = false;

    @ConfigEntry.Category("Logs")
    @Comment("Configure what logs will be printed in the custommachinery.log file.\nOnly logs with level higher or equal than selected will be printed.\nFATAL > ERROR > WARN > INFO > DEBUG > ALL")
    public LoggingLevel debugLevel = LoggingLevel.INFO;

    @ConfigEntry.Category("Rendering")
    @Comment("The time in milliseconds the block requirement\nworking box will be rendered around the machines\nwhen clicking on the icon in the jei recipe.")
    public int boxRenderTime = 10000;

    @ConfigEntry.Category("Rendering")
    @Comment("The time in milliseconds the structure requirement\nstructure will render in world when clicking\non the icon in the jei recipe.")
    public int structureRenderTime = 10000;

    @ConfigEntry.Category("Rendering")
    @Comment("The time in milliseconds each blocks will be shown\nwhen using a block tag in a structure.")
    public int blockTagCycleTime = 1000;

    @ConfigEntry.Category("Rendering")
    @Comment("The time in milliseconds the ghost item will be shown\nin a slot when a tag or more than 1 item is specified.")
    public int itemSlotCycleTime = 1000;

    @ConfigEntry.Category("Rendering")
    @Comment("If true the player will need to enable advanced item tooltips (F3 + H)\nto see the recipe name when hovering the progress arrow on a recipe in jei")
    public boolean needAdvancedInfoForRecipeID = true;

    @ConfigEntry.Category("Misc")
    @Comment("A list of folder names where CM will load models json.\nThese folders must be under the \"assets/namespace/models\" folder.")
    public List<String> modelFolders = Lists.newArrayList(new String[] { "machine", "machines" });

    public static CMConfig get() {
        return AutoConfig.<CMConfig>getConfigHolder(CMConfig.class).getConfig();
    }
}
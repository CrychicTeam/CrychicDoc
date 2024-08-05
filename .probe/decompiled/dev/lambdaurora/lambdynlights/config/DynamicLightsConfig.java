package dev.lambdaurora.lambdynlights.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.nio.file.Path;
import net.minecraftforge.common.ForgeConfigSpec;

public class DynamicLightsConfig {

    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.EnumValue<QualityMode> Quality;

    public static ForgeConfigSpec.ConfigValue<Boolean> EntityLighting;

    public static ForgeConfigSpec.ConfigValue<Boolean> TileEntityLighting;

    public static ForgeConfigSpec.ConfigValue<Boolean> OnlyUpdateOnPositionChange;

    public static void loadConfig(Path path) {
        CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        ConfigSpec.setConfig(configData);
    }

    static {
        ConfigBuilder builder = new ConfigBuilder("Dynamic Lights Settings");
        builder.Block("Lighting Settings", b -> {
            Quality = b.defineEnum("Quality Mode (OFF, SLOW, FAST, REALTIME)", QualityMode.REALTIME);
            EntityLighting = b.define("Dynamic Entity Lighting", true);
            TileEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            OnlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
        });
        ConfigSpec = builder.Save();
    }
}
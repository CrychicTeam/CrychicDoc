package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftblibrary.snbt.config.IntArrayValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;

public interface FTBLibraryClientConfig {

    SNBTConfig CONFIG = SNBTConfig.create("ftblibrary-client").comment(new String[] { "Client-specific configuration for FTB Library" });

    SNBTConfig TOOLTIPS = CONFIG.addGroup("tooltips");

    BooleanValue ITEM_MODNAME = TOOLTIPS.addBoolean("item_modname", false).comment(new String[] { "Add the name of the mod that items belong to in the item selection GUI.\nNote that several common mods also do this (modnametooltip,WTHIT,EMI...) so this is false by default" });

    BooleanValue FLUID_MODNAME = TOOLTIPS.addBoolean("fluid_modname", true).comment(new String[] { "Add the name of the mod that fluids belong to in the fluid selection GUI." });

    BooleanValue IMAGE_MODNAME = TOOLTIPS.addBoolean("image_modname", true).comment(new String[] { "Add the name of the mod that images belong to in the image selection GUI." });

    SNBTConfig COLOR = CONFIG.addGroup("colorselector");

    IntArrayValue RECENT = COLOR.addIntArray("recents", new int[0]).comment(new String[] { "Colors recently selected in the color selector" });

    static void load() {
        ConfigUtil.loadDefaulted(CONFIG, ConfigUtil.LOCAL_DIR, "ftblibrary");
    }

    static void save() {
        CONFIG.save(ConfigUtil.LOCAL_DIR.resolve("ftblibrary-client.snbt"));
    }

    static ConfigGroup getConfigGroup() {
        ConfigGroup group = new ConfigGroup("ftblibrary.client_settings", accepted -> {
            if (accepted) {
                save();
            }
        });
        CONFIG.createClientConfig(group);
        return group;
    }
}
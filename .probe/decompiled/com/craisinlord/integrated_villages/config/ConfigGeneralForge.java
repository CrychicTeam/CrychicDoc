package com.craisinlord.integrated_villages.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {

    public final ForgeConfigSpec.ConfigValue<Boolean> disableVanillaVillages;

    public final ForgeConfigSpec.ConfigValue<Boolean> activateCreateContraptions;

    public ConfigGeneralForge(ForgeConfigSpec.Builder BUILDER) {
        BUILDER.comment("##########################################################################################################\n# General settings.\n##########################################################################################################").push("General");
        this.disableVanillaVillages = BUILDER.comment("Whether or not vanilla villages should be disabled.\nDefault: true".indent(1)).worldRestart().define("Disable Vanilla Villages", true);
        this.activateCreateContraptions = BUILDER.comment("Whether or not create contraptions such as windmills will be activated on world generation. Turning this to false could prevent some lag.\nDefault: true".indent(1)).worldRestart().define("Activate Create Contraptions", true);
        BUILDER.pop();
    }
}
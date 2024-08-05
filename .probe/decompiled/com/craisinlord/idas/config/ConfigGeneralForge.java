package com.craisinlord.idas.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {

    public final ForgeConfigSpec.ConfigValue<Boolean> disableIaFStructures;

    public final ForgeConfigSpec.ConfigValue<Boolean> applyMiningFatigue;

    public ConfigGeneralForge(ForgeConfigSpec.Builder BUILDER) {
        BUILDER.comment("##########################################################################################################\n# General settings.\n##########################################################################################################").push("General");
        this.disableIaFStructures = BUILDER.comment("Whether or not Ice and Fire structures should be disabled.\nDefault: true".indent(1)).worldRestart().define("Disable Ice and Fire Structures", true);
        this.applyMiningFatigue = BUILDER.comment("Whether or not mining fatigue is applied to players in the Labyrinth if it has not yet been cleared.\nDefault: true".indent(1)).worldRestart().define("Apply Mining Fatigue", true);
        BUILDER.pop();
    }
}
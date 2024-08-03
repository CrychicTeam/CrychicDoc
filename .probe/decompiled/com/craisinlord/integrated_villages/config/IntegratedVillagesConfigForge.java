package com.craisinlord.integrated_villages.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IntegratedVillagesConfigForge {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final ConfigGeneralForge general = new ConfigGeneralForge(BUILDER);

    static {
        BUILDER.push("Integrated Villages");
        BUILDER.pop();
    }
}
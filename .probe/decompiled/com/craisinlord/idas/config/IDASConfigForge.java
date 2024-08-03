package com.craisinlord.idas.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IDASConfigForge {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final ConfigGeneralForge general = new ConfigGeneralForge(BUILDER);

    static {
        BUILDER.push("IDAS");
        BUILDER.pop();
    }
}
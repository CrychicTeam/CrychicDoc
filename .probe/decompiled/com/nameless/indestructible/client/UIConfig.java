package com.nameless.indestructible.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class UIConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Boolean> REPLACE_UI = BUILDER.define("replace_ui", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    static {
        BUILDER.push("replace entityindicator");
        BUILDER.comment("replace original entityindicator");
        BUILDER.pop();
    }
}
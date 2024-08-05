package com.nameless.impactful.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Double> SCREEN_SHAKE_AMPLITUDE_MULTIPLY;

    public static ForgeConfigSpec.ConfigValue<Boolean> DISABLE_SCREEN_SHAKE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("client screen shake setting");
        DISABLE_SCREEN_SHAKE = builder.define("disable_screen_shake", false);
        SCREEN_SHAKE_AMPLITUDE_MULTIPLY = builder.defineInRange("global_screen_shake_amplitude", 1.0, 0.0, 10.0);
        builder.pop();
        SPEC = builder.build();
    }
}
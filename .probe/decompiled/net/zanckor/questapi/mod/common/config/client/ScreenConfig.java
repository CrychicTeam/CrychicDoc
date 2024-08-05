package net.zanckor.questapi.mod.common.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScreenConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec SPEC = BUILDER.build();

    public static ForgeConfigSpec.ConfigValue<String> QUEST_TRACKED_SCREEN = BUILDER.comment("Which quest tracked design you want to see").define("Quest Tracked Screen", "questapi");

    public static ForgeConfigSpec.ConfigValue<String> QUEST_LOG_SCREEN = BUILDER.comment("Which design you want to see on quest log").define("Quest Log Screen", "questapi");

    static {
        BUILDER.push("Screen configuration");
        BUILDER.pop();
    }
}
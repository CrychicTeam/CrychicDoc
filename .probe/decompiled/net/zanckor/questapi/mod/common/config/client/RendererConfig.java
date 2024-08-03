package net.zanckor.questapi.mod.common.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class RendererConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec SPEC = BUILDER.build();

    public static ForgeConfigSpec.ConfigValue<Integer> QUEST_MARK_UPDATE_COOLDOWN = BUILDER.comment("How long ! mark takes to update on change entity data.").comment("Lower value = Smoothness + Lager").define("Quest Mark ! Update Cooldown", 5);

    static {
        BUILDER.push("Renderer configuration");
        BUILDER.pop();
    }
}
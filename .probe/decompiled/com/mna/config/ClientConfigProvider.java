package com.mna.config;

import java.util.Arrays;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE, value = { Dist.CLIENT })
public class ClientConfigProvider {

    public static ForgeConfigSpec.IntValue CODEX_BACK_STYLE;

    public static ForgeConfigSpec.IntValue SHOW_HUD_MODE;

    public static ForgeConfigSpec.IntValue HUD_POSITION;

    public static ForgeConfigSpec.IntValue PINNED_RECIPE_SCALE;

    public static ForgeConfigSpec.BooleanValue FANCY_MAGELIGHTS;

    public static ForgeConfigSpec.BooleanValue PARTICLE_BLUR;

    public static ForgeConfigSpec.BooleanValue HUD_AFFINITY;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> DID_YOU_KNOW_TIPS;

    public ClientConfigProvider(ForgeConfigSpec.Builder clientBuilder) {
        init_codex(clientBuilder);
        init_hud(clientBuilder);
        init_performance_options(clientBuilder);
        init_did_you_know(clientBuilder);
    }

    private static void init_codex(ForgeConfigSpec.Builder clientBuilder) {
        clientBuilder.comment("Mana and Artifice // Codex Settings").push("ma_codex_settings");
        CODEX_BACK_STYLE = clientBuilder.comment("Change the 'back' behaviour of the Codex (which buttons will cause it to go back, and which will close it entirely).").comment("0: UI buttons only.  Escape closes the codex entirely.", "1: UI buttons / escape.  Escape backs the codex out until the index, then closes it.", "2: UI buttons / right mouse.  Right mouse backs the codex out until the index. Escape closes the codex entirely.", "3: UI buttons / escape / right mouse.  Escape or right mouse backs the codex out until the index, then closes it.").defineInRange("codexBackStyle", 0, 0, 3);
        clientBuilder.pop();
    }

    private static void init_hud(ForgeConfigSpec.Builder clientBuilder) {
        clientBuilder.comment("Mana and Artifice // HUD Settings").push("ma_hud_settings");
        SHOW_HUD_MODE = clientBuilder.comment("Change the behaviour of the HUD.").comment("0: Always visible.", "1: Hidden unless holding a mana consuming/restoring item.", "2: Always hidden.").defineInRange("hudMode", 0, 0, 2);
        HUD_POSITION = clientBuilder.comment("Change the position of the HUD:").comment("0: Top Left", "1: Top Center", "2: Top Right", "3: Middle Right", "4: Bottom Right", "5: Bottom Center", "6: Bottom Left", "7: Middle Left").defineInRange("hudPosition", 0, 0, 7);
        PINNED_RECIPE_SCALE = clientBuilder.comment("Change the size of pinned recipes.").defineInRange("pinnedRecipeSize", 1, 1, 3);
        HUD_AFFINITY = clientBuilder.comment("Should affinity render on the HUD?").define("renderAffinityOnHUD", true);
        clientBuilder.pop();
    }

    private static void init_performance_options(ForgeConfigSpec.Builder clientBuilder) {
        clientBuilder.comment("Mana and Artifice // Performance Settings").push("ma_performance");
        FANCY_MAGELIGHTS = clientBuilder.comment("Enable fancy magelights (disable this if you're getting FPS issues)").define("ma_fancy_magelights", true);
        PARTICLE_BLUR = clientBuilder.comment("Should particles be blurred?  They'll be smoother, but shader packs may run into issues with artifact lines due to the blur math.").define("blurParticles", true);
        clientBuilder.pop();
    }

    private static void init_did_you_know(ForgeConfigSpec.Builder clientBuilder) {
        clientBuilder.comment("Mana and Artifice // Help Tips Settings").push("ma_helptips");
        DID_YOU_KNOW_TIPS = clientBuilder.defineListAllowEmpty("ma_ritual_tip", Arrays.asList(), e -> true);
        clientBuilder.pop();
    }
}
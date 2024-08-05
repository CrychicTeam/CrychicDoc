package com.craisinlord.idas.config;

import com.craisinlord.idas.IDAS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {

    public static void init() {
        initCustomFiles();
        ModLoadingContext.get().registerConfig(Type.COMMON, IDASConfigForge.SPEC, "idas.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
        loadJSON();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == IDASConfigForge.SPEC) {
            bakeConfig();
            loadJSON();
        }
    }

    private static void initCustomFiles() {
        loadJSON();
    }

    private static void loadJSON() {
    }

    private static void bakeConfig() {
        IDAS.CONFIG.general.disableIaFStructures = IDASConfigForge.general.disableIaFStructures.get();
        IDAS.CONFIG.general.applyMiningFatigue = IDASConfigForge.general.applyMiningFatigue.get();
    }
}
package com.craisinlord.integrated_villages.config;

import com.craisinlord.integrated_villages.IntegratedVillages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {

    public static void init() {
        initCustomFiles();
        ModLoadingContext.get().registerConfig(Type.COMMON, IntegratedVillagesConfigForge.SPEC, "integrated_villages-forge-1_20.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
        loadJSON();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == IntegratedVillagesConfigForge.SPEC) {
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
        IntegratedVillages.CONFIG.general.disableVanillaVillages = IntegratedVillagesConfigForge.general.disableVanillaVillages.get();
        IntegratedVillages.CONFIG.general.activateCreateContraptions = IntegratedVillagesConfigForge.general.activateCreateContraptions.get();
    }
}
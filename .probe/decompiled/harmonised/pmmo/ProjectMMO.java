package harmonised.pmmo;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.GlobalsConfig;
import harmonised.pmmo.config.PerksConfig;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.features.anticheese.AntiCheeseConfig;
import harmonised.pmmo.features.autovalues.AutoValueConfig;
import harmonised.pmmo.features.loot_modifiers.GLMRegistry;
import harmonised.pmmo.setup.CommonSetup;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pmmo")
public class ProjectMMO {

    public ProjectMMO() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(Type.SERVER, AntiCheeseConfig.SERVER_CONFIG, "pmmo-AntiCheese.toml");
        ModLoadingContext.get().registerConfig(Type.SERVER, AutoValueConfig.SERVER_CONFIG, "pmmo-AutoValues.toml");
        ModLoadingContext.get().registerConfig(Type.SERVER, GlobalsConfig.SERVER_CONFIG, "pmmo-Globals.toml");
        ModLoadingContext.get().registerConfig(Type.SERVER, SkillsConfig.SERVER_CONFIG, "pmmo-Skills.toml");
        ModLoadingContext.get().registerConfig(Type.SERVER, PerksConfig.SERVER_CONFIG, "pmmo-Perks.toml");
        GLMRegistry.CONDITIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLMRegistry.GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::onCapabilityRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::gatherData);
    }
}
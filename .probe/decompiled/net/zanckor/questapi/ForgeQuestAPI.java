package net.zanckor.questapi;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.zanckor.questapi.example.ModExample;
import net.zanckor.questapi.mod.common.config.client.RendererConfig;
import net.zanckor.questapi.mod.common.config.client.ScreenConfig;
import net.zanckor.questapi.mod.common.network.NetworkHandler;

@Mod("questapi")
public class ForgeQuestAPI {

    public ForgeQuestAPI() {
        CommonMain.init();
        CommonMain.Constants.LOG.info("Loading QuestAPI to Forge");
        new ModExample();
        MinecraftForge.EVENT_BUS.register(this);
        NetworkHandler.register();
        CommonMain.Constants.LOG.info("Registering config files");
        ModLoadingContext.get().registerConfig(Type.CLIENT, ScreenConfig.SPEC, "questapi-screen.toml");
        ModLoadingContext.get().registerConfig(Type.CLIENT, RendererConfig.SPEC, "questapi-renderer.toml");
    }
}
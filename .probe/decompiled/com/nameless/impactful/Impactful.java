package com.nameless.impactful;

import com.nameless.impactful.client.CameraEngine;
import com.nameless.impactful.config.ClientConfig;
import com.nameless.impactful.config.CommonConfig;
import com.nameless.impactful.network.NetWorkManger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("impactful")
public class Impactful {

    public static final String MOD_ID = "impactful";

    public Impactful() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.SPEC);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.SPEC);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        bus.addListener(this::SetupComplete);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetWorkManger.register();
    }

    private void SetupComplete(FMLLoadCompleteEvent event) {
        CommonConfig.load();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        new CameraEngine();
    }
}
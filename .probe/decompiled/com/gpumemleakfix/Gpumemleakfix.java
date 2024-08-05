package com.gpumemleakfix;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("gpumemleakfix")
public class Gpumemleakfix {

    public static final String MOD_ID = "gpumemleakfix";

    public static final Logger LOGGER = LogManager.getLogger();

    public Gpumemleakfix() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "", (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        GpumemleakfixClient.onInitializeClient(event);
    }

    private void setup(FMLCommonSetupEvent event) {
        LOGGER.info("gpumemleakfix mod initialized");
    }
}
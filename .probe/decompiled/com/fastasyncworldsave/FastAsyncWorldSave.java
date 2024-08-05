package com.fastasyncworldsave;

import com.fastasyncworldsave.event.EventHandler;
import java.util.Random;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fastasyncworldsave")
public class FastAsyncWorldSave {

    public static final String MOD_ID = "fastasyncworldsave";

    public static final Logger LOGGER = LogManager.getLogger();

    public static Random rand = new Random();

    public FastAsyncWorldSave() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "", (a, b) -> true));
        ((IEventBus) Bus.FORGE.bus().get()).register(EventHandler.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        FastAsyncWorldSaveClient.onInitializeClient(event);
    }
}
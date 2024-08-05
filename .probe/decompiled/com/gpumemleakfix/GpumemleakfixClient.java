package com.gpumemleakfix;

import com.gpumemleakfix.event.ClientEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class GpumemleakfixClient {

    public static void onInitializeClient(FMLClientSetupEvent event) {
        ((IEventBus) Bus.FORGE.bus().get()).register(ClientEventHandler.class);
    }
}
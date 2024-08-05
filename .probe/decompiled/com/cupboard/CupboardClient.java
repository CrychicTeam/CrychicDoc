package com.cupboard;

import com.cupboard.event.ClientEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CupboardClient {

    public static void onInitializeClient(FMLClientSetupEvent event) {
        ((IEventBus) Bus.FORGE.bus().get()).register(ClientEventHandler.class);
    }
}
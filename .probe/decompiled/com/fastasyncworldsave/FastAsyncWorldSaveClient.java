package com.fastasyncworldsave;

import com.fastasyncworldsave.event.ClientEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class FastAsyncWorldSaveClient {

    public static void onInitializeClient(FMLClientSetupEvent event) {
        ((IEventBus) Bus.FORGE.bus().get()).register(ClientEventHandler.class);
    }
}
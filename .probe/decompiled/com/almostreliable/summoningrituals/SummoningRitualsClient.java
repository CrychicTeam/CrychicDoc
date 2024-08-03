package com.almostreliable.summoningrituals;

import com.almostreliable.summoningrituals.platform.Platform;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SummoningRitualsClient {

    public void onInitializeClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Platform::registerBlockEntityRenderer);
    }
}
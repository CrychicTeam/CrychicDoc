package com.craisinlord.integrated_villages.forge;

import com.craisinlord.integrated_villages.IntegratedVillages;
import com.craisinlord.integrated_villages.config.ConfigModuleForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("integrated_villages")
public class IntegratedVillagesForge {

    public IntegratedVillagesForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        IntegratedVillages.init();
        ConfigModuleForge.init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}
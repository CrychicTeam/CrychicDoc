package com.craisinlord.integrated_stronghold.forge;

import com.craisinlord.integrated_stronghold.item.ISItems;
import com.craisinlord.integrated_stronghold.sound.ISSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("integrated_stronghold")
public class IntegratedStrongholdForge {

    public IntegratedStrongholdForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        ISSounds.SOUND_EVENTS.register(modEventBus);
        ISItems.ITEMS.register(modEventBus);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}
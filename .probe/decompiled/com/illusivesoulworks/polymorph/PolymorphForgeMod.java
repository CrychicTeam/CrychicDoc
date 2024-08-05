package com.illusivesoulworks.polymorph;

import com.illusivesoulworks.polymorph.client.ClientEventsListener;
import com.illusivesoulworks.polymorph.common.CommonEventsListener;
import com.illusivesoulworks.polymorph.common.PolymorphForgeNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("polymorph")
public class PolymorphForgeMod {

    public PolymorphForgeMod() {
        PolymorphCommonMod.init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
    }

    private void setup(FMLCommonSetupEvent evt) {
        PolymorphCommonMod.setup();
        PolymorphForgeNetwork.setup();
        MinecraftForge.EVENT_BUS.register(new CommonEventsListener());
    }

    private void clientSetup(FMLClientSetupEvent evt) {
        PolymorphCommonMod.clientSetup();
        MinecraftForge.EVENT_BUS.register(new ClientEventsListener());
    }
}
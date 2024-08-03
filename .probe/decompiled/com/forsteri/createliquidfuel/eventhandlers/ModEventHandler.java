package com.forsteri.createliquidfuel.eventhandlers;

import com.forsteri.createliquidfuel.core.DrainableFuelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = "createliquidfuel")
public class ModEventHandler {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(DrainableFuelLoader::load);
    }
}
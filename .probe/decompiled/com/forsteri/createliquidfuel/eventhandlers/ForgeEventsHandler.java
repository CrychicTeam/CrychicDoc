package com.forsteri.createliquidfuel.eventhandlers;

import com.forsteri.createliquidfuel.core.LiquidBurnerFuelJsonLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeEventsHandler {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(LiquidBurnerFuelJsonLoader.INSTANCE);
    }
}
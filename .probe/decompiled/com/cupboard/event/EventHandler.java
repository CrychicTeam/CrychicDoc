package com.cupboard.event;

import com.cupboard.config.CupboardConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CupboardConfig.pollConfigs();
        }
    }

    @SubscribeEvent
    public static void serverstart(ServerStartedEvent event) {
        CupboardConfig.initloadAll();
    }
}
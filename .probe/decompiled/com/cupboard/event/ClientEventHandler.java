package com.cupboard.event;

import com.cupboard.config.CupboardConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().hasSingleplayerServer()) {
            CupboardConfig.pollConfigs();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClientTick(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof TitleScreen) {
            CupboardConfig.initloadAll();
        }
    }
}
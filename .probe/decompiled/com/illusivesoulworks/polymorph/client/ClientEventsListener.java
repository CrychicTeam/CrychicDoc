package com.illusivesoulworks.polymorph.client;

import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventsListener {

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            PolymorphClientEvents.tick();
        }
    }

    @SubscribeEvent
    public void initScreen(ScreenEvent.Init.Post evt) {
        PolymorphClientEvents.initScreen(evt.getScreen());
    }

    @SubscribeEvent
    public void render(ScreenEvent.Render.Post evt) {
        PolymorphClientEvents.render(evt.getScreen(), evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY(), evt.getPartialTick());
    }

    @SubscribeEvent
    public void mouseClick(ScreenEvent.MouseButtonPressed.Pre evt) {
        if (PolymorphClientEvents.mouseClick(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton())) {
            evt.setCanceled(true);
        }
    }
}
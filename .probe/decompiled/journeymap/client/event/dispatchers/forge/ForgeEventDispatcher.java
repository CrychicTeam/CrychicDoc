package journeymap.client.event.dispatchers.forge;

import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.api.event.forge.FullscreenDisplayEvent;
import journeymap.client.api.event.forge.PopupMenuEvent;
import journeymap.client.event.dispatchers.EventDispatcher;
import net.minecraftforge.common.MinecraftForge;

public class ForgeEventDispatcher implements EventDispatcher {

    @Override
    public void getMapTypeToolbar(FullscreenDisplayEvent.MapTypeButtonDisplayEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void getAddonToolbar(FullscreenDisplayEvent.AddonButtonDisplayEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void getCustomToolBars(FullscreenDisplayEvent.CustomToolbarEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void popupWaypointMenuEvent(PopupMenuEvent.WaypointPopupMenuEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void popupMenuEvent(PopupMenuEvent.FullscreenPopupMenuEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void entityRadarUpdateEvent(EntityRadarUpdateEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }
}
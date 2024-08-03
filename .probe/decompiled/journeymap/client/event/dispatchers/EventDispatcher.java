package journeymap.client.event.dispatchers;

import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.api.event.forge.FullscreenDisplayEvent;
import journeymap.client.api.event.forge.PopupMenuEvent;

public interface EventDispatcher {

    void getMapTypeToolbar(FullscreenDisplayEvent.MapTypeButtonDisplayEvent var1);

    void getAddonToolbar(FullscreenDisplayEvent.AddonButtonDisplayEvent var1);

    void getCustomToolBars(FullscreenDisplayEvent.CustomToolbarEvent var1);

    void popupWaypointMenuEvent(PopupMenuEvent.WaypointPopupMenuEvent var1);

    void popupMenuEvent(PopupMenuEvent.FullscreenPopupMenuEvent var1);

    void entityRadarUpdateEvent(EntityRadarUpdateEvent var1);
}
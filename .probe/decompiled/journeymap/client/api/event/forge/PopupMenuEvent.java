package journeymap.client.api.event.forge;

import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.model.IFullscreen;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class PopupMenuEvent extends Event {

    private final ModPopupMenu popupMenu;

    private final PopupMenuEvent.Layer layer;

    private final IFullscreen fullscreen;

    public PopupMenuEvent(ModPopupMenu popupMenu, PopupMenuEvent.Layer layer, IFullscreen fullscreen) {
        this.popupMenu = popupMenu;
        this.layer = layer;
        this.fullscreen = fullscreen;
    }

    public ModPopupMenu getPopupMenu() {
        return this.popupMenu;
    }

    public PopupMenuEvent.Layer getLayer() {
        return this.layer;
    }

    public IFullscreen getFullscreen() {
        return this.fullscreen;
    }

    public static class FullscreenPopupMenuEvent extends PopupMenuEvent {

        public FullscreenPopupMenuEvent(ModPopupMenu popupMenu, IFullscreen fullscreen) {
            super(popupMenu, PopupMenuEvent.Layer.FULLSCREEN, fullscreen);
        }
    }

    public static enum Layer {

        WAYPOINT, FULLSCREEN
    }

    public static class WaypointPopupMenuEvent extends PopupMenuEvent {

        private final Waypoint waypoint;

        public WaypointPopupMenuEvent(ModPopupMenu popupMenu, IFullscreen fullscreen, Waypoint waypoint) {
            super(popupMenu, PopupMenuEvent.Layer.WAYPOINT, fullscreen);
            this.waypoint = waypoint;
        }

        public Waypoint getWaypoint() {
            return this.waypoint;
        }
    }
}
package journeymap.client.ui.waypoint;

import journeymap.client.waypoint.Waypoint;
import net.minecraft.client.gui.screens.ChatScreen;

public class WaypointChat extends ChatScreen {

    public WaypointChat(Waypoint waypoint) {
        this(waypoint.toChatString());
    }

    public WaypointChat(String text) {
        super(text);
    }

    @Override
    public void init() {
        super.init();
        this.f_95573_.moveCursorToStart();
    }
}
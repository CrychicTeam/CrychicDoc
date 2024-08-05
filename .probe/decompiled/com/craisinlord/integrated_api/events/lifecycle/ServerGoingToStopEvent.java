package com.craisinlord.integrated_api.events.lifecycle;

import com.craisinlord.integrated_api.events.base.EventHandler;

public class ServerGoingToStopEvent {

    public static final ServerGoingToStopEvent INSTANCE = new ServerGoingToStopEvent();

    public static final EventHandler<ServerGoingToStopEvent> EVENT = new EventHandler<>();

    private ServerGoingToStopEvent() {
    }
}
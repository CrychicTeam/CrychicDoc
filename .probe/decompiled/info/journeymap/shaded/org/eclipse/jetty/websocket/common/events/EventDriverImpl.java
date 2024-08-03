package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;

public interface EventDriverImpl {

    EventDriver create(Object var1, WebSocketPolicy var2) throws Throwable;

    String describeRule();

    boolean supports(Object var1);
}
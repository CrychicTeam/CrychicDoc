package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriver;
import java.net.URI;

public interface SessionFactory {

    boolean supports(EventDriver var1);

    WebSocketSession createSession(URI var1, EventDriver var2, LogicalConnection var3);
}
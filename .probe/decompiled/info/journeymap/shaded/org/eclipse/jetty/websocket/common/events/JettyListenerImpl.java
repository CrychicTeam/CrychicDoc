package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;

public class JettyListenerImpl implements EventDriverImpl {

    @Override
    public EventDriver create(Object websocket, WebSocketPolicy policy) {
        WebSocketConnectionListener listener = (WebSocketConnectionListener) websocket;
        return new JettyListenerEventDriver(policy, listener);
    }

    @Override
    public String describeRule() {
        return "class implements " + WebSocketListener.class.getName();
    }

    @Override
    public boolean supports(Object websocket) {
        return websocket instanceof WebSocketConnectionListener;
    }
}
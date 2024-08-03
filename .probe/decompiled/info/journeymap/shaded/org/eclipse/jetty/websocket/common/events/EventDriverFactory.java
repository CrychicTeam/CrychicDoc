package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import java.util.ArrayList;
import java.util.List;

public class EventDriverFactory {

    private static final Logger LOG = Log.getLogger(EventDriverFactory.class);

    private final WebSocketContainerScope containerScope;

    private final List<EventDriverImpl> implementations;

    public EventDriverFactory(WebSocketContainerScope containerScope) {
        this.containerScope = containerScope;
        this.implementations = new ArrayList();
        this.addImplementation(new JettyListenerImpl());
        this.addImplementation(new JettyAnnotatedImpl());
    }

    public void addImplementation(EventDriverImpl impl) {
        if (this.implementations.contains(impl)) {
            LOG.warn("Ignoring attempt to add duplicate EventDriverImpl: " + impl);
        } else {
            this.implementations.add(impl);
        }
    }

    public void clearImplementations() {
        this.implementations.clear();
    }

    protected String getClassName(Object websocket) {
        return websocket.getClass().getName();
    }

    public List<EventDriverImpl> getImplementations() {
        return this.implementations;
    }

    public boolean removeImplementation(EventDriverImpl impl) {
        return this.implementations.remove(impl);
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(this.getClass().getSimpleName());
        msg.append("[implementations=[");
        boolean delim = false;
        for (EventDriverImpl impl : this.implementations) {
            if (delim) {
                msg.append(',');
            }
            msg.append(impl.toString());
            delim = true;
        }
        msg.append("]");
        return msg.toString();
    }

    public EventDriver wrap(Object websocket) {
        if (websocket == null) {
            throw new InvalidWebSocketException("null websocket object");
        } else {
            for (EventDriverImpl impl : this.implementations) {
                if (impl.supports(websocket)) {
                    try {
                        return impl.create(websocket, this.containerScope.getPolicy().clonePolicy());
                    } catch (Throwable var6) {
                        throw new InvalidWebSocketException("Unable to create websocket", var6);
                    }
                }
            }
            StringBuilder err = new StringBuilder();
            err.append(this.getClassName(websocket));
            err.append(" is not a valid WebSocket object.");
            err.append("  Object must obey one of the following rules: ");
            int len = this.implementations.size();
            for (int i = 0; i < len; i++) {
                EventDriverImpl implx = (EventDriverImpl) this.implementations.get(i);
                if (i > 0) {
                    err.append(" or ");
                }
                err.append("\n(").append(i + 1).append(") ");
                err.append(implx.describeRule());
            }
            throw new InvalidWebSocketException(err.toString());
        }
    }
}
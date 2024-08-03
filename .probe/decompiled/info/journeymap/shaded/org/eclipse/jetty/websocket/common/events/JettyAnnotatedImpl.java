package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.util.concurrent.ConcurrentHashMap;

public class JettyAnnotatedImpl implements EventDriverImpl {

    private ConcurrentHashMap<Class<?>, JettyAnnotatedMetadata> cache = new ConcurrentHashMap();

    @Override
    public EventDriver create(Object websocket, WebSocketPolicy policy) {
        Class<?> websocketClass = websocket.getClass();
        synchronized (this) {
            JettyAnnotatedMetadata metadata = (JettyAnnotatedMetadata) this.cache.get(websocketClass);
            if (metadata == null) {
                JettyAnnotatedScanner scanner = new JettyAnnotatedScanner();
                metadata = scanner.scan(websocketClass);
                this.cache.put(websocketClass, metadata);
            }
            return new JettyAnnotatedEventDriver(policy, websocket, metadata);
        }
    }

    @Override
    public String describeRule() {
        return "class is annotated with @" + WebSocket.class.getName();
    }

    @Override
    public boolean supports(Object websocket) {
        WebSocket anno = (WebSocket) websocket.getClass().getAnnotation(WebSocket.class);
        return anno != null;
    }

    public String toString() {
        return String.format("%s [cache.count=%d]", this.getClass().getSimpleName(), this.cache.size());
    }
}
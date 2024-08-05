package info.journeymap.shaded.org.eclipse.jetty.io.ssl;

import java.util.EventListener;
import java.util.EventObject;
import javax.net.ssl.SSLEngine;

public interface SslHandshakeListener extends EventListener {

    default void handshakeSucceeded(SslHandshakeListener.Event event) {
    }

    default void handshakeFailed(SslHandshakeListener.Event event, Throwable failure) {
    }

    public static class Event extends EventObject {

        public Event(Object source) {
            super(source);
        }

        public SSLEngine getSSLEngine() {
            return (SSLEngine) this.getSource();
        }
    }
}
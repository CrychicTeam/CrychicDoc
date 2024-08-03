package info.journeymap.shaded.org.javax.servlet;

import java.util.EventListener;

public interface ServletRequestListener extends EventListener {

    default void requestDestroyed(ServletRequestEvent sre) {
    }

    default void requestInitialized(ServletRequestEvent sre) {
    }
}
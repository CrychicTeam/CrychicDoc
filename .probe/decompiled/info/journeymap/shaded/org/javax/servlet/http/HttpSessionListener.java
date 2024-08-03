package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionListener extends EventListener {

    default void sessionCreated(HttpSessionEvent se) {
    }

    default void sessionDestroyed(HttpSessionEvent se) {
    }
}
package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionActivationListener extends EventListener {

    default void sessionWillPassivate(HttpSessionEvent se) {
    }

    default void sessionDidActivate(HttpSessionEvent se) {
    }
}
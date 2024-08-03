package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionBindingListener extends EventListener {

    default void valueBound(HttpSessionBindingEvent event) {
    }

    default void valueUnbound(HttpSessionBindingEvent event) {
    }
}
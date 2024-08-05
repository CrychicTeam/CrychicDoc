package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionAttributeListener extends EventListener {

    default void attributeAdded(HttpSessionBindingEvent event) {
    }

    default void attributeRemoved(HttpSessionBindingEvent event) {
    }

    default void attributeReplaced(HttpSessionBindingEvent event) {
    }
}
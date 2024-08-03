package info.journeymap.shaded.org.javax.servlet;

import java.util.EventListener;

public interface ServletContextAttributeListener extends EventListener {

    default void attributeAdded(ServletContextAttributeEvent event) {
    }

    default void attributeRemoved(ServletContextAttributeEvent event) {
    }

    default void attributeReplaced(ServletContextAttributeEvent event) {
    }
}
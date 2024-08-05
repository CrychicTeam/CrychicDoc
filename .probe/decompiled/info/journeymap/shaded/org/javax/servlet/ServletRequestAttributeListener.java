package info.journeymap.shaded.org.javax.servlet;

import java.util.EventListener;

public interface ServletRequestAttributeListener extends EventListener {

    default void attributeAdded(ServletRequestAttributeEvent srae) {
    }

    default void attributeRemoved(ServletRequestAttributeEvent srae) {
    }

    default void attributeReplaced(ServletRequestAttributeEvent srae) {
    }
}
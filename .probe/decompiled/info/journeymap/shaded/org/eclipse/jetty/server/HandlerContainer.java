package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;

@ManagedObject("Handler of Multiple Handlers")
public interface HandlerContainer extends LifeCycle {

    @ManagedAttribute("handlers in this container")
    Handler[] getHandlers();

    @ManagedAttribute("all contained handlers")
    Handler[] getChildHandlers();

    Handler[] getChildHandlersByClass(Class<?> var1);

    <T extends Handler> T getChildHandlerByClass(Class<T> var1);
}
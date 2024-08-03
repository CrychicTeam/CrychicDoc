package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractHandlerContainer extends AbstractHandler implements HandlerContainer {

    @Override
    public Handler[] getChildHandlers() {
        List<Handler> list = new ArrayList();
        this.expandChildren(list, null);
        return (Handler[]) list.toArray(new Handler[list.size()]);
    }

    @Override
    public Handler[] getChildHandlersByClass(Class<?> byclass) {
        List<Handler> list = new ArrayList();
        this.expandChildren(list, byclass);
        return (Handler[]) list.toArray(new Handler[list.size()]);
    }

    @Override
    public <T extends Handler> T getChildHandlerByClass(Class<T> byclass) {
        List<Handler> list = new ArrayList();
        this.expandChildren(list, byclass);
        return (T) (list.isEmpty() ? null : list.get(0));
    }

    protected void expandChildren(List<Handler> list, Class<?> byClass) {
    }

    protected void expandHandler(Handler handler, List<Handler> list, Class<?> byClass) {
        if (handler != null) {
            if (byClass == null || byClass.isAssignableFrom(handler.getClass())) {
                list.add(handler);
            }
            if (handler instanceof AbstractHandlerContainer) {
                ((AbstractHandlerContainer) handler).expandChildren(list, byClass);
            } else if (handler instanceof HandlerContainer) {
                HandlerContainer container = (HandlerContainer) handler;
                Handler[] handlers = byClass == null ? container.getChildHandlers() : container.getChildHandlersByClass(byClass);
                list.addAll(Arrays.asList(handlers));
            }
        }
    }

    public static <T extends HandlerContainer> T findContainerOf(HandlerContainer root, Class<T> type, Handler handler) {
        if (root != null && handler != null) {
            Handler[] branches = root.getChildHandlersByClass(type);
            if (branches != null) {
                for (Handler h : branches) {
                    T container = (T) h;
                    Handler[] candidates = container.getChildHandlersByClass(handler.getClass());
                    if (candidates != null) {
                        for (Handler c : candidates) {
                            if (c == handler) {
                                return container;
                            }
                        }
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void setServer(Server server) {
        if (server != this.getServer()) {
            if (this.isStarted()) {
                throw new IllegalStateException("STARTED");
            } else {
                super.setServer(server);
                Handler[] handlers = this.getHandlers();
                if (handlers != null) {
                    for (Handler h : handlers) {
                        h.setServer(server);
                    }
                }
            }
        }
    }
}
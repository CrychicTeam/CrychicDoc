package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiException;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ManagedObject("Handler of multiple handlers")
public class HandlerCollection extends AbstractHandlerContainer {

    private final boolean _mutableWhenRunning;

    private volatile Handler[] _handlers;

    public HandlerCollection() {
        this(false);
    }

    public HandlerCollection(Handler... handlers) {
        this(false, handlers);
    }

    public HandlerCollection(boolean mutableWhenRunning, Handler... handlers) {
        this._mutableWhenRunning = mutableWhenRunning;
        if (handlers.length > 0) {
            this.setHandlers(handlers);
        }
    }

    @ManagedAttribute(value = "Wrapped handlers", readonly = true)
    @Override
    public Handler[] getHandlers() {
        return this._handlers;
    }

    public void setHandlers(Handler[] handlers) {
        if (!this._mutableWhenRunning && this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else {
            if (handlers != null) {
                for (Handler handler : handlers) {
                    if (handler == this || handler instanceof HandlerContainer && Arrays.asList(((HandlerContainer) handler).getChildHandlers()).contains(this)) {
                        throw new IllegalStateException("setHandler loop");
                    }
                }
                for (Handler handlerx : handlers) {
                    if (handlerx.getServer() != this.getServer()) {
                        handlerx.setServer(this.getServer());
                    }
                }
            }
            Handler[] old = this._handlers;
            this._handlers = handlers;
            this.updateBeans(old, handlers);
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (this._handlers != null && this.isStarted()) {
            MultiException mex = null;
            for (int i = 0; i < this._handlers.length; i++) {
                try {
                    this._handlers[i].handle(target, baseRequest, request, response);
                } catch (IOException var8) {
                    throw var8;
                } catch (RuntimeException var9) {
                    throw var9;
                } catch (Exception var10) {
                    if (mex == null) {
                        mex = new MultiException();
                    }
                    mex.add(var10);
                }
            }
            if (mex != null) {
                if (mex.size() == 1) {
                    throw new ServletException(mex.getThrowable(0));
                }
                throw new ServletException(mex);
            }
        }
    }

    public void addHandler(Handler handler) {
        this.setHandlers(ArrayUtil.addToArray(this.getHandlers(), handler, Handler.class));
    }

    public void prependHandler(Handler handler) {
        this.setHandlers(ArrayUtil.prependToArray(handler, this.getHandlers(), Handler.class));
    }

    public void removeHandler(Handler handler) {
        Handler[] handlers = this.getHandlers();
        if (handlers != null && handlers.length > 0) {
            this.setHandlers(ArrayUtil.removeFromArray(handlers, handler));
        }
    }

    @Override
    protected void expandChildren(List<Handler> list, Class<?> byClass) {
        if (this.getHandlers() != null) {
            for (Handler h : this.getHandlers()) {
                this.expandHandler(h, list, byClass);
            }
        }
    }

    @Override
    public void destroy() {
        if (!this.isStopped()) {
            throw new IllegalStateException("!STOPPED");
        } else {
            Handler[] children = this.getChildHandlers();
            this.setHandlers(null);
            for (Handler child : children) {
                child.destroy();
            }
            super.destroy();
        }
    }

    public String toString() {
        Handler[] handlers = this.getHandlers();
        return super.toString() + (handlers == null ? "[]" : Arrays.asList(this.getHandlers()).toString());
    }
}
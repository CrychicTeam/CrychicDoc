package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ManagedObject("Handler wrapping another Handler")
public class HandlerWrapper extends AbstractHandlerContainer {

    protected Handler _handler;

    @ManagedAttribute(value = "Wrapped Handler", readonly = true)
    public Handler getHandler() {
        return this._handler;
    }

    @Override
    public Handler[] getHandlers() {
        return this._handler == null ? new Handler[0] : new Handler[] { this._handler };
    }

    public void setHandler(Handler handler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else if (handler != this && (!(handler instanceof HandlerContainer) || !Arrays.asList(((HandlerContainer) handler).getChildHandlers()).contains(this))) {
            if (handler != null) {
                handler.setServer(this.getServer());
            }
            Handler old = this._handler;
            this._handler = handler;
            this.updateBean(old, this._handler, true);
        } else {
            throw new IllegalStateException("setHandler loop");
        }
    }

    public void insertHandler(HandlerWrapper wrapper) {
        if (wrapper == null) {
            throw new IllegalArgumentException();
        } else {
            HandlerWrapper tail = wrapper;
            while (tail.getHandler() instanceof HandlerWrapper) {
                tail = (HandlerWrapper) tail.getHandler();
            }
            if (tail.getHandler() != null) {
                throw new IllegalArgumentException("bad tail of inserted wrapper chain");
            } else {
                Handler next = this.getHandler();
                this.setHandler(wrapper);
                tail.setHandler(next);
            }
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Handler handler = this._handler;
        if (handler != null) {
            handler.handle(target, baseRequest, request, response);
        }
    }

    @Override
    protected void expandChildren(List<Handler> list, Class<?> byClass) {
        this.expandHandler(this._handler, list, byClass);
    }

    @Override
    public void destroy() {
        if (!this.isStopped()) {
            throw new IllegalStateException("!STOPPED");
        } else {
            Handler child = this.getHandler();
            if (child != null) {
                this.setHandler(null);
                child.destroy();
            }
            super.destroy();
        }
    }
}
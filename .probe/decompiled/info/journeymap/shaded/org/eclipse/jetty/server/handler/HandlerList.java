package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HandlerList extends HandlerCollection {

    public HandlerList() {
    }

    public HandlerList(Handler... handlers) {
        super(handlers);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Handler[] handlers = this.getHandlers();
        if (handlers != null && this.isStarted()) {
            for (int i = 0; i < handlers.length; i++) {
                handlers[i].handle(target, baseRequest, request, response);
                if (baseRequest.isHandled()) {
                    return;
                }
            }
        }
    }
}
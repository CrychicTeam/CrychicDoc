package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.session.SessionHandler;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyHandler extends SessionHandler {

    private Filter filter;

    public JettyHandler(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
        this.filter.doFilter(wrapper, response, null);
        if (wrapper.notConsumed()) {
            baseRequest.setHandled(false);
        } else {
            baseRequest.setHandled(true);
        }
    }
}
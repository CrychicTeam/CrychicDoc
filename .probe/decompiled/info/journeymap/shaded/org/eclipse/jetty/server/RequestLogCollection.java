package info.journeymap.shaded.org.eclipse.jetty.server;

import java.util.ArrayList;
import java.util.Arrays;

class RequestLogCollection implements RequestLog {

    private final ArrayList<RequestLog> delegates;

    public RequestLogCollection(RequestLog... requestLogs) {
        this.delegates = new ArrayList(Arrays.asList(requestLogs));
    }

    public void add(RequestLog requestLog) {
        this.delegates.add(requestLog);
    }

    @Override
    public void log(Request request, Response response) {
        for (RequestLog delegate : this.delegates) {
            delegate.log(request, response);
        }
    }
}
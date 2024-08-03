package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventObject;

public class HttpSessionEvent extends EventObject {

    private static final long serialVersionUID = -7622791603672342895L;

    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }
}
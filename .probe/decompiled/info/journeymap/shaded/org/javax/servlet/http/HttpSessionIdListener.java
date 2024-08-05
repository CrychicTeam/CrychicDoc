package info.journeymap.shaded.org.javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionIdListener extends EventListener {

    void sessionIdChanged(HttpSessionEvent var1, String var2);
}
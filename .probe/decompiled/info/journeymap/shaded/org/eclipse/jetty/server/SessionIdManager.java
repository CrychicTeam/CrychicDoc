package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.server.session.HouseKeeper;
import info.journeymap.shaded.org.eclipse.jetty.server.session.SessionHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface SessionIdManager extends LifeCycle {

    boolean isIdInUse(String var1);

    void expireAll(String var1);

    void invalidateAll(String var1);

    String newSessionId(HttpServletRequest var1, long var2);

    String getWorkerName();

    String getId(String var1);

    String getExtendedId(String var1, HttpServletRequest var2);

    String renewSessionId(String var1, String var2, HttpServletRequest var3);

    Set<SessionHandler> getSessionHandlers();

    void setSessionHouseKeeper(HouseKeeper var1);

    HouseKeeper getSessionHouseKeeper();
}
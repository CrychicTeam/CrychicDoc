package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;

public interface SessionDataMap extends LifeCycle {

    void initialize(SessionContext var1) throws Exception;

    SessionData load(String var1) throws Exception;

    void store(String var1, SessionData var2) throws Exception;

    boolean delete(String var1) throws Exception;
}
package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Destroyable;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ManagedObject("Jetty Handler")
public interface Handler extends LifeCycle, Destroyable {

    void handle(String var1, Request var2, HttpServletRequest var3, HttpServletResponse var4) throws IOException, ServletException;

    void setServer(Server var1);

    @ManagedAttribute(value = "the jetty server for this handler", readonly = true)
    Server getServer();

    @ManagedOperation(value = "destroy associated resources", impact = "ACTION")
    @Override
    void destroy();
}
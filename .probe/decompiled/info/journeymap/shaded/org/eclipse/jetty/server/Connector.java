package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Graceful;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

@ManagedObject("Connector Interface")
public interface Connector extends LifeCycle, Graceful {

    Server getServer();

    Executor getExecutor();

    Scheduler getScheduler();

    ByteBufferPool getByteBufferPool();

    ConnectionFactory getConnectionFactory(String var1);

    <T> T getConnectionFactory(Class<T> var1);

    ConnectionFactory getDefaultConnectionFactory();

    Collection<ConnectionFactory> getConnectionFactories();

    List<String> getProtocols();

    @ManagedAttribute("maximum time a connection can be idle before being closed (in ms)")
    long getIdleTimeout();

    Object getTransport();

    Collection<EndPoint> getConnectedEndPoints();

    String getName();
}
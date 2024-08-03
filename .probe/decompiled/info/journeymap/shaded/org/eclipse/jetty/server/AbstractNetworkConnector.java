package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@ManagedObject("AbstractNetworkConnector")
public abstract class AbstractNetworkConnector extends AbstractConnector implements NetworkConnector {

    private volatile String _host;

    private volatile int _port = 0;

    public AbstractNetworkConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, ConnectionFactory... factories) {
        super(server, executor, scheduler, pool, acceptors, factories);
    }

    public void setHost(String host) {
        this._host = host;
    }

    @ManagedAttribute("The network interface this connector binds to as an IP address or a hostname.  If null or 0.0.0.0, then bind to all interfaces.")
    @Override
    public String getHost() {
        return this._host;
    }

    public void setPort(int port) {
        this._port = port;
    }

    @ManagedAttribute("Port this connector listens on. If set the 0 a random port is assigned which may be obtained with getLocalPort()")
    @Override
    public int getPort() {
        return this._port;
    }

    @Override
    public int getLocalPort() {
        return -1;
    }

    @Override
    protected void doStart() throws Exception {
        this.open();
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        this.close();
        super.doStop();
    }

    @Override
    public void open() throws IOException {
    }

    @Override
    public void close() {
        this.interruptAcceptors();
    }

    @Override
    public Future<Void> shutdown() {
        this.close();
        return super.shutdown();
    }

    @Override
    protected boolean isAccepting() {
        return super.isAccepting() && this.isOpen();
    }

    @Override
    public String toString() {
        return String.format("%s{%s:%d}", super.toString(), this.getHost() == null ? "0.0.0.0" : this.getHost(), this.getLocalPort() <= 0 ? this.getPort() : this.getLocalPort());
    }
}
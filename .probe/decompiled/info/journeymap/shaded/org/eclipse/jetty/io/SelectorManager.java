package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

public abstract class SelectorManager extends ContainerLifeCycle implements Dumpable {

    public static final int DEFAULT_CONNECT_TIMEOUT = 15000;

    protected static final Logger LOG = Log.getLogger(SelectorManager.class);

    private final Executor executor;

    private final Scheduler scheduler;

    private final ManagedSelector[] _selectors;

    private long _connectTimeout = 15000L;

    private long _selectorIndex;

    protected SelectorManager(Executor executor, Scheduler scheduler) {
        this(executor, scheduler, (Runtime.getRuntime().availableProcessors() + 1) / 2);
    }

    protected SelectorManager(Executor executor, Scheduler scheduler, int selectors) {
        if (selectors <= 0) {
            throw new IllegalArgumentException("No selectors");
        } else {
            this.executor = executor;
            this.scheduler = scheduler;
            this._selectors = new ManagedSelector[selectors];
        }
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public long getConnectTimeout() {
        return this._connectTimeout;
    }

    public void setConnectTimeout(long milliseconds) {
        this._connectTimeout = milliseconds;
    }

    protected void execute(Runnable task) {
        this.executor.execute(task);
    }

    public int getSelectorCount() {
        return this._selectors.length;
    }

    private ManagedSelector chooseSelector(SelectableChannel channel) {
        ManagedSelector candidate1 = null;
        if (channel != null) {
            try {
                if (channel instanceof SocketChannel) {
                    SocketAddress remote = ((SocketChannel) channel).getRemoteAddress();
                    if (remote instanceof InetSocketAddress) {
                        byte[] addr = ((InetSocketAddress) remote).getAddress().getAddress();
                        if (addr != null) {
                            int s = addr[addr.length - 1] & 255;
                            candidate1 = this._selectors[s % this.getSelectorCount()];
                        }
                    }
                }
            } catch (IOException var7) {
                LOG.ignore(var7);
            }
        }
        long s = this._selectorIndex++;
        int index = (int) (s % (long) this.getSelectorCount());
        ManagedSelector candidate2 = this._selectors[index];
        return candidate1 != null && candidate1.size() < candidate2.size() * 2 ? candidate1 : candidate2;
    }

    public void connect(SelectableChannel channel, Object attachment) {
        ManagedSelector set = this.chooseSelector(channel);
        set.submit(set.new Connect(channel, attachment));
    }

    public void accept(SelectableChannel channel) {
        this.accept(channel, null);
    }

    public void accept(SelectableChannel channel, Object attachment) {
        ManagedSelector selector = this.chooseSelector(channel);
        selector.submit(selector.new Accept(channel, attachment));
    }

    public void acceptor(SelectableChannel server) {
        ManagedSelector selector = this.chooseSelector(null);
        selector.submit(selector.new Acceptor(server));
    }

    protected void accepted(SelectableChannel channel) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doStart() throws Exception {
        for (int i = 0; i < this._selectors.length; i++) {
            ManagedSelector selector = this.newSelector(i);
            this._selectors[i] = selector;
            this.addBean(selector);
        }
        super.doStart();
    }

    protected ManagedSelector newSelector(int id) {
        return new ManagedSelector(this, id);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        for (ManagedSelector selector : this._selectors) {
            this.removeBean(selector);
        }
    }

    protected void endPointOpened(EndPoint endpoint) {
    }

    protected void endPointClosed(EndPoint endpoint) {
    }

    public void connectionOpened(Connection connection) {
        try {
            connection.onOpen();
        } catch (Throwable var3) {
            if (this.isRunning()) {
                LOG.warn("Exception while notifying connection " + connection, var3);
            } else {
                LOG.debug("Exception while notifying connection " + connection, var3);
            }
            throw var3;
        }
    }

    public void connectionClosed(Connection connection) {
        try {
            connection.onClose();
        } catch (Throwable var3) {
            LOG.debug("Exception while notifying connection " + connection, var3);
        }
    }

    protected boolean doFinishConnect(SelectableChannel channel) throws IOException {
        return ((SocketChannel) channel).finishConnect();
    }

    protected boolean isConnectionPending(SelectableChannel channel) {
        return ((SocketChannel) channel).isConnectionPending();
    }

    protected SelectableChannel doAccept(SelectableChannel server) throws IOException {
        return ((ServerSocketChannel) server).accept();
    }

    protected void connectionFailed(SelectableChannel channel, Throwable ex, Object attachment) {
        LOG.warn(String.format("%s - %s", channel, attachment), ex);
    }

    protected Selector newSelector() throws IOException {
        return Selector.open();
    }

    protected abstract EndPoint newEndPoint(SelectableChannel var1, ManagedSelector var2, SelectionKey var3) throws IOException;

    public abstract Connection newConnection(SelectableChannel var1, EndPoint var2, Object var3) throws IOException;
}
package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.ChannelEndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.ManagedSelector;
import info.journeymap.shaded.org.eclipse.jetty.io.SelectorManager;
import info.journeymap.shaded.org.eclipse.jetty.io.SocketChannelEndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.Name;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.SslContextFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@ManagedObject("HTTP connector using NIO ByteChannels and Selectors")
public class ServerConnector extends AbstractNetworkConnector {

    private final SelectorManager _manager;

    private volatile ServerSocketChannel _acceptChannel;

    private volatile boolean _inheritChannel = false;

    private volatile int _localPort = -1;

    private volatile int _acceptQueueSize = 0;

    private volatile boolean _reuseAddress = true;

    private volatile int _lingerTime = -1;

    public ServerConnector(@Name("server") Server server) {
        this(server, null, null, null, -1, -1, new HttpConnectionFactory());
    }

    public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors) {
        this(server, null, null, null, acceptors, selectors, new HttpConnectionFactory());
    }

    public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("factories") ConnectionFactory... factories) {
        this(server, null, null, null, acceptors, selectors, factories);
    }

    public ServerConnector(@Name("server") Server server, @Name("factories") ConnectionFactory... factories) {
        this(server, null, null, null, -1, -1, factories);
    }

    public ServerConnector(@Name("server") Server server, @Name("sslContextFactory") SslContextFactory sslContextFactory) {
        this(server, null, null, null, -1, -1, AbstractConnectionFactory.getFactories(sslContextFactory, new HttpConnectionFactory()));
    }

    public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("sslContextFactory") SslContextFactory sslContextFactory) {
        this(server, null, null, null, acceptors, selectors, AbstractConnectionFactory.getFactories(sslContextFactory, new HttpConnectionFactory()));
    }

    public ServerConnector(@Name("server") Server server, @Name("sslContextFactory") SslContextFactory sslContextFactory, @Name("factories") ConnectionFactory... factories) {
        this(server, null, null, null, -1, -1, AbstractConnectionFactory.getFactories(sslContextFactory, factories));
    }

    public ServerConnector(@Name("server") Server server, @Name("executor") Executor executor, @Name("scheduler") Scheduler scheduler, @Name("bufferPool") ByteBufferPool bufferPool, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("factories") ConnectionFactory... factories) {
        super(server, executor, scheduler, bufferPool, acceptors, factories);
        this._manager = this.newSelectorManager(this.getExecutor(), this.getScheduler(), selectors > 0 ? selectors : Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors() / 2)));
        this.addBean(this._manager, true);
        this.setAcceptorPriorityDelta(-2);
    }

    protected SelectorManager newSelectorManager(Executor executor, Scheduler scheduler, int selectors) {
        return new ServerConnector.ServerConnectorManager(executor, scheduler, selectors);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (this.getAcceptors() == 0) {
            this._acceptChannel.configureBlocking(false);
            this._manager.acceptor(this._acceptChannel);
        }
    }

    @Override
    public boolean isOpen() {
        ServerSocketChannel channel = this._acceptChannel;
        return channel != null && channel.isOpen();
    }

    public boolean isInheritChannel() {
        return this._inheritChannel;
    }

    public void setInheritChannel(boolean inheritChannel) {
        this._inheritChannel = inheritChannel;
    }

    @Override
    public void open() throws IOException {
        if (this._acceptChannel == null) {
            ServerSocketChannel serverChannel = null;
            if (this.isInheritChannel()) {
                Channel channel = System.inheritedChannel();
                if (channel instanceof ServerSocketChannel) {
                    serverChannel = (ServerSocketChannel) channel;
                } else {
                    this.LOG.warn("Unable to use System.inheritedChannel() [{}]. Trying a new ServerSocketChannel at {}:{}", channel, this.getHost(), this.getPort());
                }
            }
            if (serverChannel == null) {
                serverChannel = ServerSocketChannel.open();
                InetSocketAddress bindAddress = this.getHost() == null ? new InetSocketAddress(this.getPort()) : new InetSocketAddress(this.getHost(), this.getPort());
                serverChannel.socket().setReuseAddress(this.getReuseAddress());
                serverChannel.socket().bind(bindAddress, this.getAcceptQueueSize());
                this._localPort = serverChannel.socket().getLocalPort();
                if (this._localPort <= 0) {
                    throw new IOException("Server channel not bound");
                }
            }
            serverChannel.configureBlocking(true);
            this.addBean(serverChannel);
            this._acceptChannel = serverChannel;
        }
    }

    @Override
    public Future<Void> shutdown() {
        return super.shutdown();
    }

    @Override
    public void close() {
        ServerSocketChannel serverChannel = this._acceptChannel;
        this._acceptChannel = null;
        if (serverChannel != null) {
            this.removeBean(serverChannel);
            if (serverChannel.isOpen()) {
                try {
                    serverChannel.close();
                } catch (IOException var3) {
                    this.LOG.warn(var3);
                }
            }
        }
        this._localPort = -2;
    }

    @Override
    public void accept(int acceptorID) throws IOException {
        ServerSocketChannel serverChannel = this._acceptChannel;
        if (serverChannel != null && serverChannel.isOpen()) {
            SocketChannel channel = serverChannel.accept();
            this.accepted(channel);
        }
    }

    private void accepted(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        this.configure(socket);
        this._manager.accept(channel);
    }

    protected void configure(Socket socket) {
        try {
            socket.setTcpNoDelay(true);
            if (this._lingerTime >= 0) {
                socket.setSoLinger(true, this._lingerTime / 1000);
            } else {
                socket.setSoLinger(false, 0);
            }
        } catch (SocketException var3) {
            this.LOG.ignore(var3);
        }
    }

    public SelectorManager getSelectorManager() {
        return this._manager;
    }

    @Override
    public Object getTransport() {
        return this._acceptChannel;
    }

    @ManagedAttribute("local port")
    @Override
    public int getLocalPort() {
        return this._localPort;
    }

    protected ChannelEndPoint newEndPoint(SocketChannel channel, ManagedSelector selectSet, SelectionKey key) throws IOException {
        SocketChannelEndPoint endpoint = new SocketChannelEndPoint(channel, selectSet, key, this.getScheduler());
        endpoint.setIdleTimeout(this.getIdleTimeout());
        return endpoint;
    }

    @ManagedAttribute("TCP/IP solinger time or -1 to disable")
    public int getSoLingerTime() {
        return this._lingerTime;
    }

    public void setSoLingerTime(int lingerTime) {
        this._lingerTime = lingerTime;
    }

    @ManagedAttribute("Accept Queue size")
    public int getAcceptQueueSize() {
        return this._acceptQueueSize;
    }

    public void setAcceptQueueSize(int acceptQueueSize) {
        this._acceptQueueSize = acceptQueueSize;
    }

    public boolean getReuseAddress() {
        return this._reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this._reuseAddress = reuseAddress;
    }

    protected class ServerConnectorManager extends SelectorManager {

        public ServerConnectorManager(Executor executor, Scheduler scheduler, int selectors) {
            super(executor, scheduler, selectors);
        }

        @Override
        protected void accepted(SelectableChannel channel) throws IOException {
            ServerConnector.this.accepted((SocketChannel) channel);
        }

        protected ChannelEndPoint newEndPoint(SelectableChannel channel, ManagedSelector selectSet, SelectionKey selectionKey) throws IOException {
            return ServerConnector.this.newEndPoint((SocketChannel) channel, selectSet, selectionKey);
        }

        @Override
        public Connection newConnection(SelectableChannel channel, EndPoint endpoint, Object attachment) throws IOException {
            return ServerConnector.this.getDefaultConnectionFactory().newConnection(ServerConnector.this, endpoint);
        }

        @Override
        protected void endPointOpened(EndPoint endpoint) {
            super.endPointOpened(endpoint);
            ServerConnector.this.onEndPointOpened(endpoint);
        }

        @Override
        protected void endPointClosed(EndPoint endpoint) {
            ServerConnector.this.onEndPointClosed(endpoint);
            super.endPointClosed(endpoint);
        }
    }
}
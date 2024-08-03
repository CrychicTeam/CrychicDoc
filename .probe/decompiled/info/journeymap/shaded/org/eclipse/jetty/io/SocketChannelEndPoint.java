package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SocketChannelEndPoint extends ChannelEndPoint {

    private static final Logger LOG = Log.getLogger(SocketChannelEndPoint.class);

    private final Socket _socket;

    private final InetSocketAddress _local;

    private final InetSocketAddress _remote;

    public SocketChannelEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler) {
        this((SocketChannel) channel, selector, key, scheduler);
    }

    public SocketChannelEndPoint(SocketChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler) {
        super(channel, selector, key, scheduler);
        this._socket = channel.socket();
        this._local = (InetSocketAddress) this._socket.getLocalSocketAddress();
        this._remote = (InetSocketAddress) this._socket.getRemoteSocketAddress();
    }

    public Socket getSocket() {
        return this._socket;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this._local;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this._remote;
    }

    @Override
    protected void doShutdownOutput() {
        try {
            if (!this._socket.isOutputShutdown()) {
                this._socket.shutdownOutput();
            }
        } catch (IOException var2) {
            LOG.debug(var2);
        }
    }
}
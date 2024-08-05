package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.AbstractConnection;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.AttributesMap;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ProxyConnectionFactory extends AbstractConnectionFactory {

    public static final String TLS_VERSION = "TLS_VERSION";

    private static final Logger LOG = Log.getLogger(ProxyConnectionFactory.class);

    private final String _next;

    private int _maxProxyHeader = 1024;

    private static final byte[] MAGIC = new byte[] { 13, 10, 13, 10, 0, 13, 10, 81, 85, 73, 84, 10 };

    public ProxyConnectionFactory() {
        super("proxy");
        this._next = null;
    }

    public ProxyConnectionFactory(String nextProtocol) {
        super("proxy");
        this._next = nextProtocol;
    }

    public int getMaxProxyHeader() {
        return this._maxProxyHeader;
    }

    public void setMaxProxyHeader(int maxProxyHeader) {
        this._maxProxyHeader = maxProxyHeader;
    }

    @Override
    public Connection newConnection(Connector connector, EndPoint endp) {
        String next = this._next;
        if (next == null) {
            Iterator<String> i = connector.getProtocols().iterator();
            while (i.hasNext()) {
                String p = (String) i.next();
                if (this.getProtocol().equalsIgnoreCase(p)) {
                    next = (String) i.next();
                    break;
                }
            }
        }
        return new ProxyConnectionFactory.ProxyProtocolV1orV2Connection(endp, connector, next);
    }

    static enum Family {

        UNSPEC, INET, INET6, UNIX
    }

    public static class ProxyEndPoint extends AttributesMap implements EndPoint {

        private final EndPoint _endp;

        private final InetSocketAddress _remote;

        private final InetSocketAddress _local;

        public ProxyEndPoint(EndPoint endp, InetSocketAddress remote, InetSocketAddress local) {
            this._endp = endp;
            this._remote = remote;
            this._local = local;
        }

        @Override
        public boolean isOptimizedForDirectBuffers() {
            return this._endp.isOptimizedForDirectBuffers();
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
        public boolean isOpen() {
            return this._endp.isOpen();
        }

        @Override
        public long getCreatedTimeStamp() {
            return this._endp.getCreatedTimeStamp();
        }

        @Override
        public void shutdownOutput() {
            this._endp.shutdownOutput();
        }

        @Override
        public boolean isOutputShutdown() {
            return this._endp.isOutputShutdown();
        }

        @Override
        public boolean isInputShutdown() {
            return this._endp.isInputShutdown();
        }

        @Override
        public void close() {
            this._endp.close();
        }

        @Override
        public int fill(ByteBuffer buffer) throws IOException {
            return this._endp.fill(buffer);
        }

        @Override
        public boolean flush(ByteBuffer... buffer) throws IOException {
            return this._endp.flush(buffer);
        }

        @Override
        public Object getTransport() {
            return this._endp.getTransport();
        }

        @Override
        public long getIdleTimeout() {
            return this._endp.getIdleTimeout();
        }

        @Override
        public void setIdleTimeout(long idleTimeout) {
            this._endp.setIdleTimeout(idleTimeout);
        }

        @Override
        public void fillInterested(Callback callback) throws ReadPendingException {
            this._endp.fillInterested(callback);
        }

        @Override
        public boolean tryFillInterested(Callback callback) {
            return this._endp.tryFillInterested(callback);
        }

        @Override
        public boolean isFillInterested() {
            return this._endp.isFillInterested();
        }

        @Override
        public void write(Callback callback, ByteBuffer... buffers) throws WritePendingException {
            this._endp.write(callback, buffers);
        }

        @Override
        public Connection getConnection() {
            return this._endp.getConnection();
        }

        @Override
        public void setConnection(Connection connection) {
            this._endp.setConnection(connection);
        }

        @Override
        public void onOpen() {
            this._endp.onOpen();
        }

        @Override
        public void onClose() {
            this._endp.onClose();
        }

        @Override
        public void upgrade(Connection newConnection) {
            this._endp.upgrade(newConnection);
        }
    }

    public static class ProxyProtocolV1Connection extends AbstractConnection {

        private final int[] __size = new int[] { 29, 23, 21, 13, 5, 3, 1 };

        private final Connector _connector;

        private final String _next;

        private final StringBuilder _builder = new StringBuilder();

        private final String[] _field = new String[6];

        private int _fields;

        private int _length;

        protected ProxyProtocolV1Connection(EndPoint endp, Connector connector, String next, ByteBuffer buffer) {
            super(endp, connector.getExecutor());
            this._connector = connector;
            this._next = next;
            this._length = buffer.remaining();
            this.parse(buffer);
        }

        @Override
        public void onOpen() {
            super.onOpen();
            this.fillInterested();
        }

        private boolean parse(ByteBuffer buffer) {
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                if (this._fields >= 6) {
                    if (b == 10) {
                        this._fields = 7;
                        return true;
                    }
                    ProxyConnectionFactory.LOG.warn("Bad CRLF for {}", this.getEndPoint());
                    this.close();
                    return false;
                }
                if (b != 32 && (b != 13 || this._fields != 5)) {
                    if (b < 32) {
                        ProxyConnectionFactory.LOG.warn("Bad character {} for {}", b & 255, this.getEndPoint());
                        this.close();
                        return false;
                    }
                    this._builder.append((char) b);
                } else {
                    this._field[this._fields++] = this._builder.toString();
                    this._builder.setLength(0);
                }
            }
            return true;
        }

        @Override
        public void onFillable() {
            try {
                ByteBuffer buffer = null;
                while (this._fields < 7) {
                    int size = Math.max(1, this.__size[this._fields] - this._builder.length());
                    if (buffer != null && buffer.capacity() == size) {
                        BufferUtil.clear(buffer);
                    } else {
                        buffer = BufferUtil.allocate(size);
                    }
                    int fill = this.getEndPoint().fill(buffer);
                    if (fill < 0) {
                        this.getEndPoint().shutdownOutput();
                        return;
                    }
                    if (fill == 0) {
                        this.fillInterested();
                        return;
                    }
                    this._length += fill;
                    if (this._length >= 108) {
                        ProxyConnectionFactory.LOG.warn("PROXY line too long {} for {}", this._length, this.getEndPoint());
                        this.close();
                        return;
                    }
                    if (!this.parse(buffer)) {
                        return;
                    }
                }
                if (!"PROXY".equals(this._field[0])) {
                    ProxyConnectionFactory.LOG.warn("Not PROXY protocol for {}", this.getEndPoint());
                    this.close();
                    return;
                }
                InetSocketAddress remote = new InetSocketAddress(this._field[2], Integer.parseInt(this._field[4]));
                InetSocketAddress local = new InetSocketAddress(this._field[3], Integer.parseInt(this._field[5]));
                ConnectionFactory connectionFactory = this._connector.getConnectionFactory(this._next);
                if (connectionFactory == null) {
                    ProxyConnectionFactory.LOG.warn("No Next protocol '{}' for {}", this._next, this.getEndPoint());
                    this.close();
                    return;
                }
                if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
                    ProxyConnectionFactory.LOG.warn("Next protocol '{}' for {} r={} l={}", this._next, this.getEndPoint(), remote, local);
                }
                EndPoint endPoint = new ProxyConnectionFactory.ProxyEndPoint(this.getEndPoint(), remote, local);
                Connection newConnection = connectionFactory.newConnection(this._connector, endPoint);
                endPoint.upgrade(newConnection);
            } catch (Throwable var7) {
                ProxyConnectionFactory.LOG.warn("PROXY error for " + this.getEndPoint(), var7);
                this.close();
            }
        }
    }

    public class ProxyProtocolV1orV2Connection extends AbstractConnection {

        private final Connector _connector;

        private final String _next;

        private ByteBuffer _buffer = BufferUtil.allocate(16);

        protected ProxyProtocolV1orV2Connection(EndPoint endp, Connector connector, String next) {
            super(endp, connector.getExecutor());
            this._connector = connector;
            this._next = next;
        }

        @Override
        public void onOpen() {
            super.onOpen();
            this.fillInterested();
        }

        @Override
        public void onFillable() {
            try {
                while (true) {
                    if (BufferUtil.space(this._buffer) > 0) {
                        int fill = this.getEndPoint().fill(this._buffer);
                        if (fill < 0) {
                            this.getEndPoint().shutdownOutput();
                            return;
                        }
                        if (fill == 0) {
                            this.fillInterested();
                            return;
                        }
                    } else {
                        switch(this._buffer.get(0)) {
                            case 13:
                                ProxyConnectionFactory.ProxyProtocolV2Connection v2 = ProxyConnectionFactory.this.new ProxyProtocolV2Connection(this.getEndPoint(), this._connector, this._next, this._buffer);
                                this.getEndPoint().upgrade(v2);
                                return;
                            case 80:
                                ProxyConnectionFactory.ProxyProtocolV1Connection v1 = new ProxyConnectionFactory.ProxyProtocolV1Connection(this.getEndPoint(), this._connector, this._next, this._buffer);
                                this.getEndPoint().upgrade(v1);
                                return;
                            default:
                                ProxyConnectionFactory.LOG.warn("Not PROXY protocol for {}", this.getEndPoint());
                                this.close();
                                return;
                        }
                    }
                }
            } catch (Throwable var2) {
                ProxyConnectionFactory.LOG.warn("PROXY error for " + this.getEndPoint(), var2);
                this.close();
            }
        }
    }

    public class ProxyProtocolV2Connection extends AbstractConnection {

        private final Connector _connector;

        private final String _next;

        private final boolean _local;

        private final ProxyConnectionFactory.Family _family;

        private final ProxyConnectionFactory.Transport _transport;

        private final int _length;

        private final ByteBuffer _buffer;

        protected ProxyProtocolV2Connection(EndPoint endp, Connector connector, String next, ByteBuffer buffer) throws IOException {
            super(endp, connector.getExecutor());
            this._connector = connector;
            this._next = next;
            if (buffer.remaining() != 16) {
                throw new IllegalStateException();
            } else {
                if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
                    ProxyConnectionFactory.LOG.debug("PROXYv2 header {} for {}", BufferUtil.toHexSummary(buffer), this);
                }
                for (int i = 0; i < ProxyConnectionFactory.MAGIC.length; i++) {
                    if (buffer.get() != ProxyConnectionFactory.MAGIC[i]) {
                        throw new IOException("Bad PROXY protocol v2 signature");
                    }
                }
                int versionAndCommand = 255 & buffer.get();
                if ((versionAndCommand & 240) != 32) {
                    throw new IOException("Bad PROXY protocol v2 version");
                } else {
                    this._local = (versionAndCommand & 15) == 0;
                    int transportAndFamily = 255 & buffer.get();
                    switch(transportAndFamily >> 4) {
                        case 0:
                            this._family = ProxyConnectionFactory.Family.UNSPEC;
                            break;
                        case 1:
                            this._family = ProxyConnectionFactory.Family.INET;
                            break;
                        case 2:
                            this._family = ProxyConnectionFactory.Family.INET6;
                            break;
                        case 3:
                            this._family = ProxyConnectionFactory.Family.UNIX;
                            break;
                        default:
                            throw new IOException("Bad PROXY protocol v2 family");
                    }
                    switch(15 & transportAndFamily) {
                        case 0:
                            this._transport = ProxyConnectionFactory.Transport.UNSPEC;
                            break;
                        case 1:
                            this._transport = ProxyConnectionFactory.Transport.STREAM;
                            break;
                        case 2:
                            this._transport = ProxyConnectionFactory.Transport.DGRAM;
                            break;
                        default:
                            throw new IOException("Bad PROXY protocol v2 family");
                    }
                    this._length = buffer.getChar();
                    if (this._local || this._family != ProxyConnectionFactory.Family.UNSPEC && this._family != ProxyConnectionFactory.Family.UNIX && this._transport == ProxyConnectionFactory.Transport.STREAM) {
                        if (this._length > ProxyConnectionFactory.this._maxProxyHeader) {
                            throw new IOException(String.format("Unsupported PROXY protocol v2 mode 0x%x,0x%x,0x%x", versionAndCommand, transportAndFamily, this._length));
                        } else {
                            this._buffer = this._length > 0 ? BufferUtil.allocate(this._length) : BufferUtil.EMPTY_BUFFER;
                        }
                    } else {
                        throw new IOException(String.format("Unsupported PROXY protocol v2 mode 0x%x,0x%x", versionAndCommand, transportAndFamily));
                    }
                }
            }
        }

        @Override
        public void onOpen() {
            super.onOpen();
            if (this._buffer.remaining() == this._length) {
                this.next();
            } else {
                this.fillInterested();
            }
        }

        @Override
        public void onFillable() {
            try {
                while (this._buffer.remaining() < this._length) {
                    int fill = this.getEndPoint().fill(this._buffer);
                    if (fill < 0) {
                        this.getEndPoint().shutdownOutput();
                        return;
                    }
                    if (fill == 0) {
                        this.fillInterested();
                        return;
                    }
                }
            } catch (Throwable var2) {
                ProxyConnectionFactory.LOG.warn("PROXY error for " + this.getEndPoint(), var2);
                this.close();
                return;
            }
            this.next();
        }

        private void next() {
            if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
                ProxyConnectionFactory.LOG.debug("PROXYv2 next {} from {} for {}", this._next, BufferUtil.toHexSummary(this._buffer), this);
            }
            ConnectionFactory connectionFactory = this._connector.getConnectionFactory(this._next);
            if (connectionFactory == null) {
                ProxyConnectionFactory.LOG.info("Next protocol '{}' for {}", this._next, this.getEndPoint());
                this.close();
            } else {
                EndPoint endPoint = this.getEndPoint();
                if (!this._local) {
                    try {
                        InetAddress src;
                        InetAddress dst;
                        int sp;
                        int dp;
                        switch(this._family) {
                            case INET:
                                {
                                    byte[] addr = new byte[4];
                                    this._buffer.get(addr);
                                    src = Inet4Address.getByAddress(addr);
                                    this._buffer.get(addr);
                                    dst = Inet4Address.getByAddress(addr);
                                    sp = this._buffer.getChar();
                                    dp = this._buffer.getChar();
                                    break;
                                }
                            case INET6:
                                {
                                    byte[] addr = new byte[16];
                                    this._buffer.get(addr);
                                    src = Inet6Address.getByAddress(addr);
                                    this._buffer.get(addr);
                                    dst = Inet6Address.getByAddress(addr);
                                    sp = this._buffer.getChar();
                                    dp = this._buffer.getChar();
                                    break;
                                }
                            default:
                                throw new IllegalStateException();
                        }
                        InetSocketAddress remote = new InetSocketAddress(src, sp);
                        InetSocketAddress local = new InetSocketAddress(dst, dp);
                        ProxyConnectionFactory.ProxyEndPoint proxyEndPoint = new ProxyConnectionFactory.ProxyEndPoint(endPoint, remote, local);
                        endPoint = proxyEndPoint;
                        while (this._buffer.hasRemaining()) {
                            int type = 255 & this._buffer.get();
                            int length = this._buffer.getShort();
                            byte[] value = new byte[length];
                            this._buffer.get(value);
                            if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
                                ProxyConnectionFactory.LOG.debug(String.format("T=%x L=%d V=%s for %s", type, length, TypeUtil.toHexString(value), this));
                            }
                            switch(type) {
                                case 1:
                                case 2:
                                case 33:
                                case 34:
                                case 48:
                                default:
                                    continue;
                                case 32:
                            }
                            int i = 0;
                            int client = 255 & value[i++];
                            int verify = (255 & value[i++]) << 24 + (255 & value[i++]) << 16 + (255 & value[i++]) << 8 + (255 & value[i++]);
                            while (i < value.length) {
                                int ssl_type = 255 & value[i++];
                                int ssl_length = (255 & value[i++]) * 256 + (255 & value[i++]);
                                byte[] ssl_val = new byte[ssl_length];
                                System.arraycopy(value, i, ssl_val, 0, ssl_length);
                                i += ssl_length;
                                switch(ssl_type) {
                                    case 33:
                                        String version = new String(ssl_val, 0, ssl_length, StandardCharsets.ISO_8859_1);
                                        if (client == 1) {
                                            proxyEndPoint.setAttribute("TLS_VERSION", version);
                                        }
                                }
                            }
                        }
                        if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
                            ProxyConnectionFactory.LOG.debug("{} {}", this.getEndPoint(), proxyEndPoint.toString());
                        }
                    } catch (Exception var20) {
                        ProxyConnectionFactory.LOG.warn(var20);
                    }
                }
                Connection newConnection = connectionFactory.newConnection(this._connector, endPoint);
                endPoint.upgrade(newConnection);
            }
        }
    }

    static enum Transport {

        UNSPEC, STREAM, DGRAM
    }
}
package me.lucko.spark.lib.bytesocks.ws;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import me.lucko.spark.lib.bytesocks.ws.interfaces.ISSLChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel, ISSLChannel {

    protected static ByteBuffer emptybuffer = ByteBuffer.allocate(0);

    private final Logger log = LoggerFactory.getLogger(SSLSocketChannel2.class);

    protected ExecutorService exec;

    protected List<Future<?>> tasks;

    protected ByteBuffer inData;

    protected ByteBuffer outCrypt;

    protected ByteBuffer inCrypt;

    protected SocketChannel socketChannel;

    protected SelectionKey selectionKey;

    protected SSLEngine sslEngine;

    protected SSLEngineResult readEngineResult;

    protected SSLEngineResult writeEngineResult;

    protected int bufferallocations = 0;

    private byte[] saveCryptData = null;

    public SSLSocketChannel2(SocketChannel channel, SSLEngine sslEngine, ExecutorService exec, SelectionKey key) throws IOException {
        if (channel != null && sslEngine != null && exec != null) {
            this.socketChannel = channel;
            this.sslEngine = sslEngine;
            this.exec = exec;
            this.readEngineResult = this.writeEngineResult = new SSLEngineResult(Status.BUFFER_UNDERFLOW, sslEngine.getHandshakeStatus(), 0, 0);
            this.tasks = new ArrayList(3);
            if (key != null) {
                key.interestOps(key.interestOps() | 4);
                this.selectionKey = key;
            }
            this.createBuffers(sslEngine.getSession());
            this.socketChannel.write(this.wrap(emptybuffer));
            this.processHandshake();
        } else {
            throw new IllegalArgumentException("parameter must not be null");
        }
    }

    private void consumeFutureUninterruptible(Future<?> f) {
        try {
            while (true) {
                try {
                    f.get();
                    return;
                } catch (InterruptedException var3) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (ExecutionException var4) {
            throw new RuntimeException(var4);
        }
    }

    private synchronized void processHandshake() throws IOException {
        if (this.sslEngine.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING) {
            if (!this.tasks.isEmpty()) {
                Iterator<Future<?>> it = this.tasks.iterator();
                while (it.hasNext()) {
                    Future<?> f = (Future<?>) it.next();
                    if (!f.isDone()) {
                        if (this.isBlocking()) {
                            this.consumeFutureUninterruptible(f);
                        }
                        return;
                    }
                    it.remove();
                }
            }
            if (this.sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP) {
                if (!this.isBlocking() || this.readEngineResult.getStatus() == Status.BUFFER_UNDERFLOW) {
                    this.inCrypt.compact();
                    int read = this.socketChannel.read(this.inCrypt);
                    if (read == -1) {
                        throw new IOException("connection closed unexpectedly by peer");
                    }
                    this.inCrypt.flip();
                }
                this.inData.compact();
                this.unwrap();
                if (this.readEngineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    this.createBuffers(this.sslEngine.getSession());
                    return;
                }
            }
            this.consumeDelegatedTasks();
            if (this.tasks.isEmpty() || this.sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_WRAP) {
                this.socketChannel.write(this.wrap(emptybuffer));
                if (this.writeEngineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    this.createBuffers(this.sslEngine.getSession());
                    return;
                }
            }
            assert this.sslEngine.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING;
            this.bufferallocations = 1;
        }
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        this.outCrypt.compact();
        this.writeEngineResult = this.sslEngine.wrap(b, this.outCrypt);
        this.outCrypt.flip();
        return this.outCrypt;
    }

    private synchronized ByteBuffer unwrap() throws SSLException {
        if (this.readEngineResult.getStatus() == Status.CLOSED && this.sslEngine.getHandshakeStatus() == HandshakeStatus.NOT_HANDSHAKING) {
            try {
                this.close();
            } catch (IOException var3) {
            }
        }
        int rem;
        do {
            rem = this.inData.remaining();
            this.readEngineResult = this.sslEngine.unwrap(this.inCrypt, this.inData);
        } while (this.readEngineResult.getStatus() == Status.OK && (rem != this.inData.remaining() || this.sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP));
        this.inData.flip();
        return this.inData;
    }

    protected void consumeDelegatedTasks() {
        Runnable task;
        while ((task = this.sslEngine.getDelegatedTask()) != null) {
            this.tasks.add(this.exec.submit(task));
        }
    }

    protected void createBuffers(SSLSession session) {
        this.saveCryptedData();
        int netBufferMax = session.getPacketBufferSize();
        int appBufferMax = Math.max(session.getApplicationBufferSize(), netBufferMax);
        if (this.inData == null) {
            this.inData = ByteBuffer.allocate(appBufferMax);
            this.outCrypt = ByteBuffer.allocate(netBufferMax);
            this.inCrypt = ByteBuffer.allocate(netBufferMax);
        } else {
            if (this.inData.capacity() != appBufferMax) {
                this.inData = ByteBuffer.allocate(appBufferMax);
            }
            if (this.outCrypt.capacity() != netBufferMax) {
                this.outCrypt = ByteBuffer.allocate(netBufferMax);
            }
            if (this.inCrypt.capacity() != netBufferMax) {
                this.inCrypt = ByteBuffer.allocate(netBufferMax);
            }
        }
        if (this.inData.remaining() != 0 && this.log.isTraceEnabled()) {
            this.log.trace(new String(this.inData.array(), this.inData.position(), this.inData.remaining()));
        }
        this.inData.rewind();
        this.inData.flip();
        if (this.inCrypt.remaining() != 0 && this.log.isTraceEnabled()) {
            this.log.trace(new String(this.inCrypt.array(), this.inCrypt.position(), this.inCrypt.remaining()));
        }
        this.inCrypt.rewind();
        this.inCrypt.flip();
        this.outCrypt.rewind();
        this.outCrypt.flip();
        this.bufferallocations++;
    }

    public int write(ByteBuffer src) throws IOException {
        if (!this.isHandShakeComplete()) {
            this.processHandshake();
            return 0;
        } else {
            int num = this.socketChannel.write(this.wrap(src));
            if (this.writeEngineResult.getStatus() == Status.CLOSED) {
                throw new EOFException("Connection is closed");
            } else {
                return num;
            }
        }
    }

    public int read(ByteBuffer dst) throws IOException {
        this.tryRestoreCryptedData();
        while (dst.hasRemaining()) {
            if (!this.isHandShakeComplete()) {
                if (this.isBlocking()) {
                    while (!this.isHandShakeComplete()) {
                        this.processHandshake();
                    }
                } else {
                    this.processHandshake();
                    if (!this.isHandShakeComplete()) {
                        return 0;
                    }
                }
            }
            int purged = this.readRemaining(dst);
            if (purged != 0) {
                return purged;
            }
            assert this.inData.position() == 0;
            this.inData.clear();
            if (!this.inCrypt.hasRemaining()) {
                this.inCrypt.clear();
            } else {
                this.inCrypt.compact();
            }
            if ((this.isBlocking() || this.readEngineResult.getStatus() == Status.BUFFER_UNDERFLOW) && this.socketChannel.read(this.inCrypt) == -1) {
                return -1;
            }
            this.inCrypt.flip();
            this.unwrap();
            int transferred = this.transfereTo(this.inData, dst);
            if (transferred != 0 || !this.isBlocking()) {
                return transferred;
            }
        }
        return 0;
    }

    private int readRemaining(ByteBuffer dst) throws SSLException {
        if (this.inData.hasRemaining()) {
            return this.transfereTo(this.inData, dst);
        } else {
            if (!this.inData.hasRemaining()) {
                this.inData.clear();
            }
            this.tryRestoreCryptedData();
            if (this.inCrypt.hasRemaining()) {
                this.unwrap();
                int amount = this.transfereTo(this.inData, dst);
                if (this.readEngineResult.getStatus() == Status.CLOSED) {
                    return -1;
                }
                if (amount > 0) {
                    return amount;
                }
            }
            return 0;
        }
    }

    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }

    public void close() throws IOException {
        this.sslEngine.closeOutbound();
        this.sslEngine.getSession().invalidate();
        try {
            if (this.socketChannel.isOpen()) {
                this.socketChannel.write(this.wrap(emptybuffer));
            }
        } finally {
            this.socketChannel.close();
        }
    }

    private boolean isHandShakeComplete() {
        HandshakeStatus status = this.sslEngine.getHandshakeStatus();
        return status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING;
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return this.socketChannel.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return this.socketChannel.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return this.socketChannel.finishConnect();
    }

    public Socket socket() {
        return this.socketChannel.socket();
    }

    public boolean isInboundDone() {
        return this.sslEngine.isInboundDone();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen();
    }

    @Override
    public boolean isNeedWrite() {
        return this.outCrypt.hasRemaining() || !this.isHandShakeComplete();
    }

    @Override
    public void writeMore() throws IOException {
        this.write(this.outCrypt);
    }

    @Override
    public boolean isNeedRead() {
        return this.saveCryptData != null || this.inData.hasRemaining() || this.inCrypt.hasRemaining() && this.readEngineResult.getStatus() != Status.BUFFER_UNDERFLOW && this.readEngineResult.getStatus() != Status.CLOSED;
    }

    @Override
    public int readMore(ByteBuffer dst) throws SSLException {
        return this.readRemaining(dst);
    }

    private int transfereTo(ByteBuffer from, ByteBuffer to) {
        int fremain = from.remaining();
        int toremain = to.remaining();
        if (fremain <= toremain) {
            to.put(from);
            return fremain;
        } else {
            int limit = Math.min(fremain, toremain);
            for (int i = 0; i < limit; i++) {
                to.put(from.get());
            }
            return limit;
        }
    }

    @Override
    public boolean isBlocking() {
        return this.socketChannel.isBlocking();
    }

    @Override
    public SSLEngine getSSLEngine() {
        return this.sslEngine;
    }

    private void saveCryptedData() {
        if (this.inCrypt != null && this.inCrypt.remaining() > 0) {
            int saveCryptSize = this.inCrypt.remaining();
            this.saveCryptData = new byte[saveCryptSize];
            this.inCrypt.get(this.saveCryptData);
        }
    }

    private void tryRestoreCryptedData() {
        if (this.saveCryptData != null) {
            this.inCrypt.clear();
            this.inCrypt.put(this.saveCryptData);
            this.inCrypt.flip();
            this.saveCryptData = null;
        }
    }
}
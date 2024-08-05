package me.lucko.spark.lib.bytesocks.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import me.lucko.spark.lib.bytesocks.ws.interfaces.ISSLChannel;
import me.lucko.spark.lib.bytesocks.ws.util.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLSocketChannel implements WrappedByteChannel, ByteChannel, ISSLChannel {

    private final Logger log = LoggerFactory.getLogger(SSLSocketChannel.class);

    private final SocketChannel socketChannel;

    private final SSLEngine engine;

    private ByteBuffer myAppData;

    private ByteBuffer myNetData;

    private ByteBuffer peerAppData;

    private ByteBuffer peerNetData;

    private ExecutorService executor;

    public SSLSocketChannel(SocketChannel inputSocketChannel, SSLEngine inputEngine, ExecutorService inputExecutor, SelectionKey key) throws IOException {
        if (inputSocketChannel != null && inputEngine != null && this.executor != inputExecutor) {
            this.socketChannel = inputSocketChannel;
            this.engine = inputEngine;
            this.executor = inputExecutor;
            this.myNetData = ByteBuffer.allocate(this.engine.getSession().getPacketBufferSize());
            this.peerNetData = ByteBuffer.allocate(this.engine.getSession().getPacketBufferSize());
            this.engine.beginHandshake();
            if (this.doHandshake()) {
                if (key != null) {
                    key.interestOps(key.interestOps() | 4);
                }
            } else {
                try {
                    this.socketChannel.close();
                } catch (IOException var6) {
                    this.log.error("Exception during the closing of the channel", var6);
                }
            }
        } else {
            throw new IllegalArgumentException("parameter must not be null");
        }
    }

    public synchronized int read(ByteBuffer dst) throws IOException {
        if (!dst.hasRemaining()) {
            return 0;
        } else if (this.peerAppData.hasRemaining()) {
            this.peerAppData.flip();
            return ByteBufferUtils.transferByteBuffer(this.peerAppData, dst);
        } else {
            this.peerNetData.compact();
            int bytesRead = this.socketChannel.read(this.peerNetData);
            if (bytesRead > 0 || this.peerNetData.hasRemaining()) {
                this.peerNetData.flip();
                if (this.peerNetData.hasRemaining()) {
                    this.peerAppData.compact();
                    SSLEngineResult result;
                    try {
                        result = this.engine.unwrap(this.peerNetData, this.peerAppData);
                    } catch (SSLException var5) {
                        this.log.error("SSLException during unwrap", var5);
                        throw var5;
                    }
                    switch(result.getStatus()) {
                        case OK:
                            this.peerAppData.flip();
                            return ByteBufferUtils.transferByteBuffer(this.peerAppData, dst);
                        case BUFFER_UNDERFLOW:
                            this.peerAppData.flip();
                            return ByteBufferUtils.transferByteBuffer(this.peerAppData, dst);
                        case BUFFER_OVERFLOW:
                            this.peerAppData = this.enlargeApplicationBuffer(this.peerAppData);
                            return this.read(dst);
                        case CLOSED:
                            this.closeConnection();
                            dst.clear();
                            return -1;
                        default:
                            throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                    }
                }
            } else if (bytesRead < 0) {
                this.handleEndOfStream();
            }
            ByteBufferUtils.transferByteBuffer(this.peerAppData, dst);
            return bytesRead;
        }
    }

    public synchronized int write(ByteBuffer output) throws IOException {
        int num = 0;
        while (output.hasRemaining()) {
            this.myNetData.clear();
            SSLEngineResult result = this.engine.wrap(output, this.myNetData);
            switch(result.getStatus()) {
                case OK:
                    this.myNetData.flip();
                    while (this.myNetData.hasRemaining()) {
                        num += this.socketChannel.write(this.myNetData);
                    }
                    break;
                case BUFFER_UNDERFLOW:
                    throw new SSLException("Buffer underflow occurred after a wrap. I don't think we should ever get here.");
                case BUFFER_OVERFLOW:
                    this.myNetData = this.enlargePacketBuffer(this.myNetData);
                    break;
                case CLOSED:
                    this.closeConnection();
                    return 0;
                default:
                    throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
            }
        }
        return num;
    }

    private boolean doHandshake() throws IOException {
        int appBufferSize = this.engine.getSession().getApplicationBufferSize();
        this.myAppData = ByteBuffer.allocate(appBufferSize);
        this.peerAppData = ByteBuffer.allocate(appBufferSize);
        this.myNetData.clear();
        this.peerNetData.clear();
        HandshakeStatus handshakeStatus = this.engine.getHandshakeStatus();
        boolean handshakeComplete = false;
        label102: while (!handshakeComplete) {
            switch(handshakeStatus) {
                case FINISHED:
                    handshakeComplete = !this.peerNetData.hasRemaining();
                    if (handshakeComplete) {
                        return true;
                    }
                    this.socketChannel.write(this.peerNetData);
                    break;
                case NEED_UNWRAP:
                    if (this.socketChannel.read(this.peerNetData) < 0) {
                        if (this.engine.isInboundDone() && this.engine.isOutboundDone()) {
                            return false;
                        }
                        try {
                            this.engine.closeInbound();
                        } catch (SSLException var6) {
                        }
                        this.engine.closeOutbound();
                        handshakeStatus = this.engine.getHandshakeStatus();
                        break;
                    } else {
                        this.peerNetData.flip();
                        SSLEngineResult result;
                        try {
                            result = this.engine.unwrap(this.peerNetData, this.peerAppData);
                            this.peerNetData.compact();
                            handshakeStatus = result.getHandshakeStatus();
                        } catch (SSLException var8) {
                            this.engine.closeOutbound();
                            handshakeStatus = this.engine.getHandshakeStatus();
                            break;
                        }
                        switch(result.getStatus()) {
                            case OK:
                                continue;
                            case BUFFER_UNDERFLOW:
                                this.peerNetData = this.handleBufferUnderflow(this.peerNetData);
                                continue;
                            case BUFFER_OVERFLOW:
                                this.peerAppData = this.enlargeApplicationBuffer(this.peerAppData);
                                continue;
                            case CLOSED:
                                if (this.engine.isOutboundDone()) {
                                    return false;
                                }
                                this.engine.closeOutbound();
                                handshakeStatus = this.engine.getHandshakeStatus();
                                continue;
                            default:
                                throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                        }
                    }
                case NEED_WRAP:
                    this.myNetData.clear();
                    SSLEngineResult result;
                    try {
                        result = this.engine.wrap(this.myAppData, this.myNetData);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException var7) {
                        this.engine.closeOutbound();
                        handshakeStatus = this.engine.getHandshakeStatus();
                        break;
                    }
                    switch(result.getStatus()) {
                        case OK:
                            this.myNetData.flip();
                            while (true) {
                                if (!this.myNetData.hasRemaining()) {
                                    continue label102;
                                }
                                this.socketChannel.write(this.myNetData);
                            }
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("Buffer underflow occurred after a wrap. I don't think we should ever get here.");
                        case BUFFER_OVERFLOW:
                            this.myNetData = this.enlargePacketBuffer(this.myNetData);
                            continue;
                        case CLOSED:
                            try {
                                this.myNetData.flip();
                                while (this.myNetData.hasRemaining()) {
                                    this.socketChannel.write(this.myNetData);
                                }
                                this.peerNetData.clear();
                            } catch (Exception var9) {
                                handshakeStatus = this.engine.getHandshakeStatus();
                            }
                            continue;
                        default:
                            throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                    }
                case NEED_TASK:
                    Runnable task;
                    while ((task = this.engine.getDelegatedTask()) != null) {
                        this.executor.execute(task);
                    }
                    handshakeStatus = this.engine.getHandshakeStatus();
                case NOT_HANDSHAKING:
                    break;
                default:
                    throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
            }
        }
        return true;
    }

    private ByteBuffer enlargePacketBuffer(ByteBuffer buffer) {
        return this.enlargeBuffer(buffer, this.engine.getSession().getPacketBufferSize());
    }

    private ByteBuffer enlargeApplicationBuffer(ByteBuffer buffer) {
        return this.enlargeBuffer(buffer, this.engine.getSession().getApplicationBufferSize());
    }

    private ByteBuffer enlargeBuffer(ByteBuffer buffer, int sessionProposedCapacity) {
        if (sessionProposedCapacity > buffer.capacity()) {
            buffer = ByteBuffer.allocate(sessionProposedCapacity);
        } else {
            buffer = ByteBuffer.allocate(buffer.capacity() * 2);
        }
        return buffer;
    }

    private ByteBuffer handleBufferUnderflow(ByteBuffer buffer) {
        if (this.engine.getSession().getPacketBufferSize() < buffer.limit()) {
            return buffer;
        } else {
            ByteBuffer replaceBuffer = this.enlargePacketBuffer(buffer);
            buffer.flip();
            replaceBuffer.put(buffer);
            return replaceBuffer;
        }
    }

    private void closeConnection() throws IOException {
        this.engine.closeOutbound();
        try {
            this.doHandshake();
        } catch (IOException var2) {
        }
        this.socketChannel.close();
    }

    private void handleEndOfStream() throws IOException {
        try {
            this.engine.closeInbound();
        } catch (Exception var2) {
            this.log.error("This engine was forced to close inbound, without having received the proper SSL/TLS close notification message from the peer, due to end of stream.");
        }
        this.closeConnection();
    }

    @Override
    public boolean isNeedWrite() {
        return false;
    }

    @Override
    public void writeMore() throws IOException {
    }

    @Override
    public boolean isNeedRead() {
        return this.peerNetData.hasRemaining() || this.peerAppData.hasRemaining();
    }

    @Override
    public int readMore(ByteBuffer dst) throws IOException {
        return this.read(dst);
    }

    @Override
    public boolean isBlocking() {
        return this.socketChannel.isBlocking();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen();
    }

    public void close() throws IOException {
        this.closeConnection();
    }

    @Override
    public SSLEngine getSSLEngine() {
        return this.engine;
    }
}
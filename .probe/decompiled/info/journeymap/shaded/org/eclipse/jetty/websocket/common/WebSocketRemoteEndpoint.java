package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.RemoteEndpoint;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.PingFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.PongFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.TextFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.FrameFlusher;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.FutureWriteCallback;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketRemoteEndpoint implements RemoteEndpoint {

    private static final WriteCallback NOOP_CALLBACK = new WriteCallback() {

        @Override
        public void writeSuccess() {
        }

        @Override
        public void writeFailed(Throwable x) {
        }
    };

    private static final Logger LOG = Log.getLogger(WebSocketRemoteEndpoint.class);

    private static final int ASYNC_MASK = 65535;

    private static final int BLOCK_MASK = 65536;

    private static final int STREAM_MASK = 131072;

    private static final int PARTIAL_TEXT_MASK = 262144;

    private static final int PARTIAL_BINARY_MASK = 524288;

    private final LogicalConnection connection;

    private final OutgoingFrames outgoing;

    private final AtomicInteger msgState = new AtomicInteger();

    private final BlockingWriteCallback blocker = new BlockingWriteCallback();

    private volatile BatchMode batchMode;

    public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing) {
        this(connection, outgoing, BatchMode.AUTO);
    }

    public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing, BatchMode batchMode) {
        if (connection == null) {
            throw new IllegalArgumentException("LogicalConnection cannot be null");
        } else {
            this.connection = connection;
            this.outgoing = outgoing;
            this.batchMode = batchMode;
        }
    }

    private void blockingWrite(WebSocketFrame frame) throws IOException {
        try (BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker()) {
            this.uncheckedSendFrame(frame, b);
            b.block();
        }
    }

    private boolean lockMsg(WebSocketRemoteEndpoint.MsgType type) {
        while (true) {
            int state = this.msgState.get();
            switch(type) {
                case BLOCKING:
                    if ((state & 786432) != 0) {
                        throw new IllegalStateException(String.format("Partial message pending %x for %s", state, type));
                    }
                    if ((state & 65536) != 0) {
                        throw new IllegalStateException(String.format("Blocking message pending %x for %s", state, type));
                    }
                    if (!this.msgState.compareAndSet(state, state | 65536)) {
                        break;
                    }
                    return state == 0;
                case ASYNC:
                    if ((state & 786432) != 0) {
                        throw new IllegalStateException(String.format("Partial message pending %x for %s", state, type));
                    }
                    if ((state & 65535) == 65535) {
                        throw new IllegalStateException(String.format("Too many async sends: %x", state));
                    }
                    if (!this.msgState.compareAndSet(state, state + 1)) {
                        break;
                    }
                    return state == 0;
                case STREAMING:
                    if ((state & 786432) != 0) {
                        throw new IllegalStateException(String.format("Partial message pending %x for %s", state, type));
                    }
                    if ((state & 131072) != 0) {
                        throw new IllegalStateException(String.format("Already streaming %x for %s", state, type));
                    }
                    if (!this.msgState.compareAndSet(state, state | 131072)) {
                        break;
                    }
                    return state == 0;
                case PARTIAL_BINARY:
                    if (state == 524288) {
                        return false;
                    }
                    if (state == 0 && this.msgState.compareAndSet(0, state | 524288)) {
                        return true;
                    }
                    throw new IllegalStateException(String.format("Cannot send %s in state %x", type, state));
                case PARTIAL_TEXT:
                    if (state == 262144) {
                        return false;
                    }
                    if (state == 0 && this.msgState.compareAndSet(0, state | 262144)) {
                        return true;
                    }
                    throw new IllegalStateException(String.format("Cannot send %s in state %x", type, state));
            }
        }
    }

    private void unlockMsg(WebSocketRemoteEndpoint.MsgType type) {
        while (true) {
            int state = this.msgState.get();
            switch(type) {
                case BLOCKING:
                    if ((state & 65536) == 0) {
                        throw new IllegalStateException(String.format("Not Blocking in state %x", state));
                    }
                    if (!this.msgState.compareAndSet(state, state & -65537)) {
                        break;
                    }
                    return;
                case ASYNC:
                    if ((state & 65535) == 0) {
                        throw new IllegalStateException(String.format("Not Async in %x", state));
                    }
                    if (!this.msgState.compareAndSet(state, state - 1)) {
                        break;
                    }
                    return;
                case STREAMING:
                    if ((state & 131072) == 0) {
                        throw new IllegalStateException(String.format("Not Streaming in state %x", state));
                    }
                    if (!this.msgState.compareAndSet(state, state & -131073)) {
                        break;
                    }
                    return;
                case PARTIAL_BINARY:
                    if (this.msgState.compareAndSet(524288, 0)) {
                        return;
                    }
                    throw new IllegalStateException(String.format("Not Partial Binary in state %x", state));
                case PARTIAL_TEXT:
                    if (this.msgState.compareAndSet(262144, 0)) {
                        return;
                    }
                    throw new IllegalStateException(String.format("Not Partial Text in state %x", state));
            }
        }
    }

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return this.connection == null ? null : this.connection.getRemoteAddress();
    }

    private Future<Void> sendAsyncFrame(WebSocketFrame frame) {
        FutureWriteCallback future = new FutureWriteCallback();
        this.uncheckedSendFrame(frame, future);
        return future;
    }

    @Override
    public void sendBytes(ByteBuffer data) throws IOException {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.BLOCKING);
        try {
            this.connection.getIOState().assertOutputOpen();
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendBytes with {}", BufferUtil.toDetailString(data));
            }
            this.blockingWrite(new BinaryFrame().setPayload(data));
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.BLOCKING);
        }
    }

    @Override
    public Future<Void> sendBytesByFuture(ByteBuffer data) {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        Future var2;
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendBytesByFuture with {}", BufferUtil.toDetailString(data));
            }
            var2 = this.sendAsyncFrame(new BinaryFrame().setPayload(data));
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        }
        return var2;
    }

    @Override
    public void sendBytes(ByteBuffer data, WriteCallback callback) {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendBytes({}, {})", BufferUtil.toDetailString(data), callback);
            }
            this.uncheckedSendFrame(new BinaryFrame().setPayload(data), callback == null ? NOOP_CALLBACK : callback);
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        }
    }

    public void uncheckedSendFrame(WebSocketFrame frame, WriteCallback callback) {
        try {
            BatchMode batchMode = BatchMode.OFF;
            if (frame.isDataFrame()) {
                batchMode = this.getBatchMode();
            }
            this.connection.getIOState().assertOutputOpen();
            this.outgoing.outgoingFrame(frame, callback, batchMode);
        } catch (IOException var4) {
            callback.writeFailed(var4);
        }
    }

    @Override
    public void sendPartialBytes(ByteBuffer fragment, boolean isLast) throws IOException {
        boolean first = this.lockMsg(WebSocketRemoteEndpoint.MsgType.PARTIAL_BINARY);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendPartialBytes({}, {})", BufferUtil.toDetailString(fragment), isLast);
            }
            DataFrame frame = (DataFrame) (first ? new BinaryFrame() : new ContinuationFrame());
            frame.setPayload(fragment);
            frame.setFin(isLast);
            this.blockingWrite(frame);
        } finally {
            if (isLast) {
                this.unlockMsg(WebSocketRemoteEndpoint.MsgType.PARTIAL_BINARY);
            }
        }
    }

    @Override
    public void sendPartialString(String fragment, boolean isLast) throws IOException {
        boolean first = this.lockMsg(WebSocketRemoteEndpoint.MsgType.PARTIAL_TEXT);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendPartialString({}, {})", fragment, isLast);
            }
            DataFrame frame = (DataFrame) (first ? new TextFrame() : new ContinuationFrame());
            frame.setPayload(BufferUtil.toBuffer(fragment, StandardCharsets.UTF_8));
            frame.setFin(isLast);
            this.blockingWrite(frame);
        } finally {
            if (isLast) {
                this.unlockMsg(WebSocketRemoteEndpoint.MsgType.PARTIAL_TEXT);
            }
        }
    }

    @Override
    public void sendPing(ByteBuffer applicationData) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendPing with {}", BufferUtil.toDetailString(applicationData));
        }
        this.sendAsyncFrame(new PingFrame().setPayload(applicationData));
    }

    @Override
    public void sendPong(ByteBuffer applicationData) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendPong with {}", BufferUtil.toDetailString(applicationData));
        }
        this.sendAsyncFrame(new PongFrame().setPayload(applicationData));
    }

    @Override
    public void sendString(String text) throws IOException {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.BLOCKING);
        try {
            WebSocketFrame frame = new TextFrame().setPayload(text);
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendString with {}", BufferUtil.toDetailString(frame.getPayload()));
            }
            this.blockingWrite(frame);
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.BLOCKING);
        }
    }

    @Override
    public Future<Void> sendStringByFuture(String text) {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        Future var3;
        try {
            TextFrame frame = new TextFrame().setPayload(text);
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendStringByFuture with {}", BufferUtil.toDetailString(frame.getPayload()));
            }
            var3 = this.sendAsyncFrame(frame);
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        }
        return var3;
    }

    @Override
    public void sendString(String text, WriteCallback callback) {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        try {
            TextFrame frame = new TextFrame().setPayload(text);
            if (LOG.isDebugEnabled()) {
                LOG.debug("sendString({},{})", BufferUtil.toDetailString(frame.getPayload()), callback);
            }
            this.uncheckedSendFrame(frame, callback == null ? NOOP_CALLBACK : callback);
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        }
    }

    @Override
    public BatchMode getBatchMode() {
        return this.batchMode;
    }

    @Override
    public void setBatchMode(BatchMode batchMode) {
        this.batchMode = batchMode;
    }

    @Override
    public void flush() throws IOException {
        this.lockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        try (BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker()) {
            this.uncheckedSendFrame(FrameFlusher.FLUSH_FRAME, b);
            b.block();
        } finally {
            this.unlockMsg(WebSocketRemoteEndpoint.MsgType.ASYNC);
        }
    }

    public String toString() {
        return String.format("%s@%x[batching=%b]", this.getClass().getSimpleName(), this.hashCode(), this.getBatchMode());
    }

    private static enum MsgType {

        BLOCKING, ASYNC, STREAMING, PARTIAL_TEXT, PARTIAL_BINARY
    }
}
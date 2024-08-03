package info.journeymap.shaded.org.eclipse.jetty.websocket.common.io;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.IteratingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.Generator;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FrameFlusher {

    public static final BinaryFrame FLUSH_FRAME = new BinaryFrame();

    private static final Logger LOG = Log.getLogger(FrameFlusher.class);

    private final ByteBufferPool bufferPool;

    private final EndPoint endpoint;

    private final int bufferSize;

    private final Generator generator;

    private final int maxGather;

    private final Object lock = new Object();

    private final Deque<FrameFlusher.FrameEntry> queue = new ArrayDeque();

    private final FrameFlusher.Flusher flusher;

    private final AtomicBoolean closed = new AtomicBoolean();

    private volatile Throwable failure;

    public FrameFlusher(ByteBufferPool bufferPool, Generator generator, EndPoint endpoint, int bufferSize, int maxGather) {
        this.bufferPool = bufferPool;
        this.endpoint = endpoint;
        this.bufferSize = bufferSize;
        this.generator = (Generator) Objects.requireNonNull(generator);
        this.maxGather = maxGather;
        this.flusher = new FrameFlusher.Flusher(maxGather);
    }

    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            LOG.debug("{} closing {}", this);
            EOFException eof = new EOFException("Connection has been closed locally");
            this.flusher.failed(eof);
            List<FrameFlusher.FrameEntry> entries = new ArrayList();
            synchronized (this.lock) {
                entries.addAll(this.queue);
                this.queue.clear();
            }
            for (FrameFlusher.FrameEntry entry : entries) {
                this.notifyCallbackFailure(entry.callback, eof);
            }
        }
    }

    public void enqueue(Frame frame, WriteCallback callback, BatchMode batchMode) {
        if (this.closed.get()) {
            this.notifyCallbackFailure(callback, new EOFException("Connection has been closed locally"));
        } else if (this.flusher.isFailed()) {
            this.notifyCallbackFailure(callback, this.failure);
        } else {
            FrameFlusher.FrameEntry entry = new FrameFlusher.FrameEntry(frame, callback, batchMode);
            synchronized (this.lock) {
                switch(frame.getOpCode()) {
                    case 8:
                        this.closed.set(true);
                        this.queue.offer(entry);
                        break;
                    case 9:
                        this.queue.offerFirst(entry);
                        break;
                    default:
                        this.queue.offer(entry);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} queued {}", this, entry);
            }
            this.flusher.iterate();
        }
    }

    protected void notifyCallbackFailure(WriteCallback callback, Throwable failure) {
        try {
            if (callback != null) {
                callback.writeFailed(failure);
            }
        } catch (Throwable var4) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception while notifying failure of callback " + callback, var4);
            }
        }
    }

    protected void notifyCallbackSuccess(WriteCallback callback) {
        try {
            if (callback != null) {
                callback.writeSuccess();
            }
        } catch (Throwable var3) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception while notifying success of callback " + callback, var3);
            }
        }
    }

    protected void onFailure(Throwable x) {
        LOG.warn(x);
    }

    public String toString() {
        ByteBuffer aggregate = this.flusher.aggregate;
        return String.format("%s[queueSize=%d,aggregateSize=%d,failure=%s]", this.getClass().getSimpleName(), this.queue.size(), aggregate == null ? 0 : aggregate.position(), this.failure);
    }

    private class Flusher extends IteratingCallback {

        private final List<FrameFlusher.FrameEntry> entries;

        private final List<ByteBuffer> buffers;

        private ByteBuffer aggregate;

        private BatchMode batchMode;

        public Flusher(int maxGather) {
            this.entries = new ArrayList(maxGather);
            this.buffers = new ArrayList(maxGather * 2 + 1);
        }

        private IteratingCallback.Action batch() {
            if (this.aggregate == null) {
                this.aggregate = FrameFlusher.this.bufferPool.acquire(FrameFlusher.this.bufferSize, true);
                if (FrameFlusher.LOG.isDebugEnabled()) {
                    FrameFlusher.LOG.debug("{} acquired aggregate buffer {}", FrameFlusher.this, this.aggregate);
                }
            }
            for (int i = 0; i < this.entries.size(); i++) {
                FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry) this.entries.get(i);
                entry.generateHeaderBytes(this.aggregate);
                ByteBuffer payload = entry.frame.getPayload();
                if (BufferUtil.hasContent(payload)) {
                    BufferUtil.append(this.aggregate, payload);
                }
            }
            if (FrameFlusher.LOG.isDebugEnabled()) {
                FrameFlusher.LOG.debug("{} aggregated {} frames: {}", FrameFlusher.this, this.entries.size(), this.entries);
            }
            this.succeeded();
            return IteratingCallback.Action.SCHEDULED;
        }

        @Override
        protected void onCompleteSuccess() {
        }

        @Override
        public void onCompleteFailure(Throwable x) {
            for (FrameFlusher.FrameEntry entry : this.entries) {
                FrameFlusher.this.notifyCallbackFailure(entry.callback, x);
                entry.release();
            }
            this.entries.clear();
            FrameFlusher.this.failure = x;
            FrameFlusher.this.onFailure(x);
        }

        private IteratingCallback.Action flush() {
            if (!BufferUtil.isEmpty(this.aggregate)) {
                this.buffers.add(this.aggregate);
                if (FrameFlusher.LOG.isDebugEnabled()) {
                    FrameFlusher.LOG.debug("{} flushing aggregate {}", FrameFlusher.this, this.aggregate);
                }
            }
            for (int i = 0; i < this.entries.size(); i++) {
                FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry) this.entries.get(i);
                if (entry.frame != FrameFlusher.FLUSH_FRAME) {
                    this.buffers.add(entry.generateHeaderBytes());
                    ByteBuffer payload = entry.frame.getPayload();
                    if (BufferUtil.hasContent(payload)) {
                        this.buffers.add(payload);
                    }
                }
            }
            if (FrameFlusher.LOG.isDebugEnabled()) {
                FrameFlusher.LOG.debug("{} flushing {} frames: {}", FrameFlusher.this, this.entries.size(), this.entries);
            }
            if (this.buffers.isEmpty()) {
                this.releaseAggregate();
                this.succeedEntries();
                return IteratingCallback.Action.IDLE;
            } else {
                FrameFlusher.this.endpoint.write(this, (ByteBuffer[]) this.buffers.toArray(new ByteBuffer[this.buffers.size()]));
                this.buffers.clear();
                return IteratingCallback.Action.SCHEDULED;
            }
        }

        @Override
        protected IteratingCallback.Action process() throws Exception {
            int space = this.aggregate == null ? FrameFlusher.this.bufferSize : BufferUtil.space(this.aggregate);
            BatchMode currentBatchMode = BatchMode.AUTO;
            synchronized (FrameFlusher.this.lock) {
                while (this.entries.size() <= FrameFlusher.this.maxGather && !FrameFlusher.this.queue.isEmpty()) {
                    FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry) FrameFlusher.this.queue.poll();
                    currentBatchMode = BatchMode.max(currentBatchMode, entry.batchMode);
                    if (entry.frame == FrameFlusher.FLUSH_FRAME) {
                        currentBatchMode = BatchMode.OFF;
                    }
                    int payloadLength = BufferUtil.length(entry.frame.getPayload());
                    int approxFrameLength = 28 + payloadLength;
                    if (approxFrameLength > FrameFlusher.this.bufferSize >> 2) {
                        currentBatchMode = BatchMode.OFF;
                    }
                    space -= approxFrameLength;
                    if (space <= 0) {
                        currentBatchMode = BatchMode.OFF;
                    }
                    this.entries.add(entry);
                }
            }
            if (FrameFlusher.LOG.isDebugEnabled()) {
                FrameFlusher.LOG.debug("{} processing {} entries: {}", FrameFlusher.this, this.entries.size(), this.entries);
            }
            if (this.entries.isEmpty()) {
                if (this.batchMode != BatchMode.AUTO) {
                    this.releaseAggregate();
                    return IteratingCallback.Action.IDLE;
                } else {
                    FrameFlusher.LOG.debug("{} auto flushing", FrameFlusher.this);
                    return this.flush();
                }
            } else {
                this.batchMode = currentBatchMode;
                return currentBatchMode == BatchMode.OFF ? this.flush() : this.batch();
            }
        }

        private void releaseAggregate() {
            if (this.aggregate != null && BufferUtil.isEmpty(this.aggregate)) {
                FrameFlusher.this.bufferPool.release(this.aggregate);
                this.aggregate = null;
            }
        }

        @Override
        public void succeeded() {
            this.succeedEntries();
            super.succeeded();
        }

        private void succeedEntries() {
            for (int i = 0; i < this.entries.size(); i++) {
                FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry) this.entries.get(i);
                FrameFlusher.this.notifyCallbackSuccess(entry.callback);
                entry.release();
            }
            this.entries.clear();
        }
    }

    private class FrameEntry {

        private final Frame frame;

        private final WriteCallback callback;

        private final BatchMode batchMode;

        private ByteBuffer headerBuffer;

        private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode) {
            this.frame = (Frame) Objects.requireNonNull(frame);
            this.callback = callback;
            this.batchMode = batchMode;
        }

        private ByteBuffer generateHeaderBytes() {
            return this.headerBuffer = FrameFlusher.this.generator.generateHeaderBytes(this.frame);
        }

        private void generateHeaderBytes(ByteBuffer buffer) {
            FrameFlusher.this.generator.generateHeaderBytes(this.frame, buffer);
        }

        private void release() {
            if (this.headerBuffer != null) {
                FrameFlusher.this.generator.getBufferPool().release(this.headerBuffer);
                this.headerBuffer = null;
            }
        }

        public String toString() {
            return String.format("%s[%s,%s,%s,%s]", this.getClass().getSimpleName(), this.frame, this.callback, this.batchMode, FrameFlusher.this.failure);
        }
    }
}
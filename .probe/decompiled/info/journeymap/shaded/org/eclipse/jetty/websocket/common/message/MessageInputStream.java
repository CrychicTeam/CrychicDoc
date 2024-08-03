package info.journeymap.shaded.org.eclipse.jetty.websocket.common.message;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageInputStream extends InputStream implements MessageAppender {

    private static final Logger LOG = Log.getLogger(MessageInputStream.class);

    private static final ByteBuffer EOF = ByteBuffer.allocate(0).asReadOnlyBuffer();

    private final BlockingDeque<ByteBuffer> buffers = new LinkedBlockingDeque();

    private AtomicBoolean closed = new AtomicBoolean(false);

    private final long timeoutMs;

    private ByteBuffer activeBuffer = null;

    public MessageInputStream() {
        this(-1);
    }

    public MessageInputStream(int timeoutMs) {
        this.timeoutMs = (long) timeoutMs;
    }

    @Override
    public void appendFrame(ByteBuffer framePayload, boolean fin) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Appending {} chunk: {}", fin ? "final" : "non-final", BufferUtil.toDetailString(framePayload));
        }
        if (!this.closed.get()) {
            try {
                if (framePayload == null) {
                    return;
                }
                int capacity = framePayload.remaining();
                if (capacity > 0) {
                    ByteBuffer copy = framePayload.isDirect() ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
                    copy.put(framePayload).flip();
                    this.buffers.put(copy);
                    return;
                }
            } catch (InterruptedException var8) {
                throw new IOException(var8);
            } finally {
                if (fin) {
                    this.buffers.offer(EOF);
                }
            }
        }
    }

    public void close() throws IOException {
        if (this.closed.compareAndSet(false, true)) {
            this.buffers.offer(EOF);
            super.close();
        }
    }

    public void mark(int readlimit) {
    }

    public boolean markSupported() {
        return false;
    }

    @Override
    public void messageComplete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Message completed");
        }
        this.buffers.offer(EOF);
    }

    public int read() throws IOException {
        try {
            if (this.closed.get()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Stream closed");
                }
                return -1;
            } else {
                while (this.activeBuffer == null || !this.activeBuffer.hasRemaining()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Waiting {} ms to read", this.timeoutMs);
                    }
                    if (this.timeoutMs < 0L) {
                        this.activeBuffer = (ByteBuffer) this.buffers.take();
                    } else {
                        this.activeBuffer = (ByteBuffer) this.buffers.poll(this.timeoutMs, TimeUnit.MILLISECONDS);
                        if (this.activeBuffer == null) {
                            throw new IOException(String.format("Read timeout: %,dms expired", this.timeoutMs));
                        }
                    }
                    if (this.activeBuffer == EOF) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Reached EOF");
                        }
                        this.closed.set(true);
                        this.buffers.clear();
                        return -1;
                    }
                }
                return this.activeBuffer.get() & 0xFF;
            }
        } catch (InterruptedException var2) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Interrupted while waiting to read", var2);
            }
            this.closed.set(true);
            return -1;
        }
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }
}
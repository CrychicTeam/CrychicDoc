package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.SelectionKey;

public abstract class ChannelEndPoint extends AbstractEndPoint implements ManagedSelector.Selectable {

    private static final Logger LOG = Log.getLogger(ChannelEndPoint.class);

    private final Locker _locker = new Locker();

    private final ByteChannel _channel;

    private final GatheringByteChannel _gather;

    protected final ManagedSelector _selector;

    protected final SelectionKey _key;

    private boolean _updatePending;

    protected int _currentInterestOps;

    protected int _desiredInterestOps;

    private final Runnable _runUpdateKey = new ChannelEndPoint.RunnableTask("runUpdateKey") {

        @Override
        public Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }

        public void run() {
            ChannelEndPoint.this.updateKey();
        }
    };

    private final Runnable _runFillable = new ChannelEndPoint.RunnableCloseable("runFillable") {

        @Override
        public Invocable.InvocationType getInvocationType() {
            return ChannelEndPoint.this.getFillInterest().getCallbackInvocationType();
        }

        public void run() {
            ChannelEndPoint.this.getFillInterest().fillable();
        }
    };

    private final Runnable _runCompleteWrite = new ChannelEndPoint.RunnableCloseable("runCompleteWrite") {

        @Override
        public Invocable.InvocationType getInvocationType() {
            return ChannelEndPoint.this.getWriteFlusher().getCallbackInvocationType();
        }

        public void run() {
            ChannelEndPoint.this.getWriteFlusher().completeWrite();
        }

        @Override
        public String toString() {
            return String.format("CEP:%s:%s:%s->%s", ChannelEndPoint.this, this._operation, this.getInvocationType(), ChannelEndPoint.this.getWriteFlusher());
        }
    };

    private final Runnable _runCompleteWriteFillable = new ChannelEndPoint.RunnableCloseable("runCompleteWriteFillable") {

        @Override
        public Invocable.InvocationType getInvocationType() {
            Invocable.InvocationType fillT = ChannelEndPoint.this.getFillInterest().getCallbackInvocationType();
            Invocable.InvocationType flushT = ChannelEndPoint.this.getWriteFlusher().getCallbackInvocationType();
            if (fillT == flushT) {
                return fillT;
            } else if (fillT == Invocable.InvocationType.EITHER && flushT == Invocable.InvocationType.NON_BLOCKING) {
                return Invocable.InvocationType.EITHER;
            } else {
                return fillT == Invocable.InvocationType.NON_BLOCKING && flushT == Invocable.InvocationType.EITHER ? Invocable.InvocationType.EITHER : Invocable.InvocationType.BLOCKING;
            }
        }

        public void run() {
            ChannelEndPoint.this.getWriteFlusher().completeWrite();
            ChannelEndPoint.this.getFillInterest().fillable();
        }
    };

    public ChannelEndPoint(ByteChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler) {
        super(scheduler);
        this._channel = channel;
        this._selector = selector;
        this._key = key;
        this._gather = channel instanceof GatheringByteChannel ? (GatheringByteChannel) channel : null;
    }

    @Override
    public boolean isOptimizedForDirectBuffers() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return this._channel.isOpen();
    }

    @Override
    public void doClose() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("doClose {}", this);
        }
        try {
            this._channel.close();
        } catch (IOException var5) {
            LOG.debug(var5);
        } finally {
            super.doClose();
        }
    }

    @Override
    public void onClose() {
        try {
            super.onClose();
        } finally {
            if (this._selector != null) {
                this._selector.destroyEndPoint(this);
            }
        }
    }

    @Override
    public int fill(ByteBuffer buffer) throws IOException {
        if (this.isInputShutdown()) {
            return -1;
        } else {
            int pos = BufferUtil.flipToFill(buffer);
            byte var4;
            try {
                int filled = this._channel.read(buffer);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("filled {} {}", filled, this);
                }
                if (filled > 0) {
                    this.notIdle();
                } else if (filled == -1) {
                    this.shutdownInput();
                }
                return filled;
            } catch (IOException var8) {
                LOG.debug(var8);
                this.shutdownInput();
                var4 = -1;
            } finally {
                BufferUtil.flipToFlush(buffer, pos);
            }
            return var4;
        }
    }

    @Override
    public boolean flush(ByteBuffer... buffers) throws IOException {
        long flushed = 0L;
        try {
            if (buffers.length == 1) {
                flushed = (long) this._channel.write(buffers[0]);
            } else if (this._gather != null && buffers.length > 1) {
                flushed = this._gather.write(buffers, 0, buffers.length);
            } else {
                for (ByteBuffer b : buffers) {
                    if (b.hasRemaining()) {
                        int l = this._channel.write(b);
                        if (l > 0) {
                            flushed += (long) l;
                        }
                        if (b.hasRemaining()) {
                            break;
                        }
                    }
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("flushed {} {}", flushed, this);
            }
        } catch (IOException var9) {
            throw new EofException(var9);
        }
        if (flushed > 0L) {
            this.notIdle();
        }
        for (ByteBuffer bx : buffers) {
            if (!BufferUtil.isEmpty(bx)) {
                return false;
            }
        }
        return true;
    }

    public ByteChannel getChannel() {
        return this._channel;
    }

    @Override
    public Object getTransport() {
        return this._channel;
    }

    @Override
    protected void needsFillInterest() {
        this.changeInterests(1);
    }

    @Override
    protected void onIncompleteFlush() {
        this.changeInterests(4);
    }

    @Override
    public Runnable onSelected() {
        int readyOps = this._key.readyOps();
        int oldInterestOps;
        int newInterestOps;
        try (Locker.Lock lock = this._locker.lock()) {
            this._updatePending = true;
            oldInterestOps = this._desiredInterestOps;
            newInterestOps = oldInterestOps & ~readyOps;
            this._desiredInterestOps = newInterestOps;
        }
        boolean fillable = (readyOps & 1) != 0;
        boolean flushable = (readyOps & 4) != 0;
        if (LOG.isDebugEnabled()) {
            LOG.debug("onSelected {}->{} r={} w={} for {}", oldInterestOps, newInterestOps, fillable, flushable, this);
        }
        Runnable task = fillable ? (flushable ? this._runCompleteWriteFillable : this._runFillable) : (flushable ? this._runCompleteWrite : null);
        if (LOG.isDebugEnabled()) {
            LOG.debug("task {}", task);
        }
        return task;
    }

    @Override
    public void updateKey() {
        try (Locker.Lock lock = this._locker.lock()) {
            this._updatePending = false;
            int oldInterestOps = this._currentInterestOps;
            int newInterestOps = this._desiredInterestOps;
            if (oldInterestOps != newInterestOps) {
                this._currentInterestOps = newInterestOps;
                this._key.interestOps(newInterestOps);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Key interests updated {} -> {} on {}", oldInterestOps, newInterestOps, this);
            }
        } catch (CancelledKeyException var17) {
            LOG.debug("Ignoring key update for concurrently closed channel {}", this);
            this.close();
        } catch (Throwable var18) {
            LOG.warn("Ignoring key update for " + this, var18);
            this.close();
        }
    }

    private void changeInterests(int operation) {
        int oldInterestOps;
        int newInterestOps;
        boolean pending;
        try (Locker.Lock lock = this._locker.lock()) {
            pending = this._updatePending;
            oldInterestOps = this._desiredInterestOps;
            newInterestOps = oldInterestOps | operation;
            if (newInterestOps != oldInterestOps) {
                this._desiredInterestOps = newInterestOps;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("changeInterests p={} {}->{} for {}", pending, oldInterestOps, newInterestOps, this);
        }
        if (!pending && this._selector != null) {
            this._selector.submit(this._runUpdateKey);
        }
    }

    @Override
    public String toEndPointString() {
        try {
            boolean valid = this._key != null && this._key.isValid();
            int keyInterests = valid ? this._key.interestOps() : -1;
            int keyReadiness = valid ? this._key.readyOps() : -1;
            return String.format("%s{io=%d/%d,kio=%d,kro=%d}", super.toEndPointString(), this._currentInterestOps, this._desiredInterestOps, keyInterests, keyReadiness);
        } catch (Throwable var4) {
            return String.format("%s{io=%s,kio=-2,kro=-2}", super.toString(), this._desiredInterestOps);
        }
    }

    private abstract class RunnableCloseable extends ChannelEndPoint.RunnableTask implements Closeable {

        protected RunnableCloseable(String op) {
            super(op);
        }

        public void close() {
            try {
                ChannelEndPoint.this.close();
            } catch (Throwable var2) {
                ChannelEndPoint.LOG.warn(var2);
            }
        }
    }

    private abstract class RunnableTask implements Runnable, Invocable {

        final String _operation;

        protected RunnableTask(String op) {
            this._operation = op;
        }

        public String toString() {
            return String.format("CEP:%s:%s:%s", ChannelEndPoint.this, this._operation, this.getInvocationType());
        }
    }
}
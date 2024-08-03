package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractConnection implements Connection {

    private static final Logger LOG = Log.getLogger(AbstractConnection.class);

    private final List<Connection.Listener> listeners = new CopyOnWriteArrayList();

    private final long _created = System.currentTimeMillis();

    private final EndPoint _endPoint;

    private final Executor _executor;

    private final Callback _readCallback;

    private int _inputBufferSize = 2048;

    protected AbstractConnection(EndPoint endp, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not be null!");
        } else {
            this._endPoint = endp;
            this._executor = executor;
            this._readCallback = new AbstractConnection.ReadCallback();
        }
    }

    @Override
    public void addListener(Connection.Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(Connection.Listener listener) {
        this.listeners.remove(listener);
    }

    public int getInputBufferSize() {
        return this._inputBufferSize;
    }

    public void setInputBufferSize(int inputBufferSize) {
        this._inputBufferSize = inputBufferSize;
    }

    protected Executor getExecutor() {
        return this._executor;
    }

    protected void failedCallback(Callback callback, Throwable x) {
        Runnable failCallback = () -> {
            try {
                callback.failed(x);
            } catch (Exception var3x) {
                LOG.warn(var3x);
            }
        };
        switch(Invocable.getInvocationType(callback)) {
            case BLOCKING:
                try {
                    this.getExecutor().execute(failCallback);
                } catch (RejectedExecutionException var5) {
                    LOG.debug(var5);
                    callback.failed(x);
                }
                break;
            case NON_BLOCKING:
                failCallback.run();
                break;
            case EITHER:
                Invocable.invokeNonBlocking(failCallback);
        }
    }

    public void fillInterested() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fillInterested {}", this);
        }
        this.getEndPoint().fillInterested(this._readCallback);
    }

    public void tryFillInterested() {
        this.tryFillInterested(this._readCallback);
    }

    public void tryFillInterested(Callback callback) {
        this.getEndPoint().tryFillInterested(callback);
    }

    public boolean isFillInterested() {
        return this.getEndPoint().isFillInterested();
    }

    public abstract void onFillable();

    protected void onFillInterestedFailed(Throwable cause) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onFillInterestedFailed {}", this, cause);
        }
        if (this._endPoint.isOpen()) {
            boolean close = true;
            if (cause instanceof TimeoutException) {
                close = this.onReadTimeout();
            }
            if (close) {
                if (this._endPoint.isOutputShutdown()) {
                    this._endPoint.close();
                } else {
                    this._endPoint.shutdownOutput();
                    this.fillInterested();
                }
            }
        }
    }

    protected boolean onReadTimeout() {
        return true;
    }

    @Override
    public void onOpen() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onOpen {}", this);
        }
        for (Connection.Listener listener : this.listeners) {
            listener.onOpened(this);
        }
    }

    @Override
    public void onClose() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onClose {}", this);
        }
        for (Connection.Listener listener : this.listeners) {
            listener.onClosed(this);
        }
    }

    @Override
    public EndPoint getEndPoint() {
        return this._endPoint;
    }

    @Override
    public void close() {
        this.getEndPoint().close();
    }

    @Override
    public boolean onIdleExpired() {
        return true;
    }

    @Override
    public long getMessagesIn() {
        return -1L;
    }

    @Override
    public long getMessagesOut() {
        return -1L;
    }

    @Override
    public long getBytesIn() {
        return -1L;
    }

    @Override
    public long getBytesOut() {
        return -1L;
    }

    @Override
    public long getCreatedTimeStamp() {
        return this._created;
    }

    public final String toString() {
        return String.format("%s<-%s", this.toConnectionString(), this.getEndPoint());
    }

    public String toConnectionString() {
        return String.format("%s@%h", this.getClass().getSimpleName(), this);
    }

    private class ReadCallback implements Callback {

        private ReadCallback() {
        }

        @Override
        public void succeeded() {
            AbstractConnection.this.onFillable();
        }

        @Override
        public void failed(Throwable x) {
            AbstractConnection.this.onFillInterestedFailed(x);
        }

        public String toString() {
            return String.format("AC.ReadCB@%h{%s}", AbstractConnection.this, AbstractConnection.this);
        }
    }
}
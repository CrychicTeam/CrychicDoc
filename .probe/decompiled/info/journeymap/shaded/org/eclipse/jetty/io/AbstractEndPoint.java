package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractEndPoint extends IdleTimeout implements EndPoint {

    private static final Logger LOG = Log.getLogger(AbstractEndPoint.class);

    private final AtomicReference<AbstractEndPoint.State> _state = new AtomicReference(AbstractEndPoint.State.OPEN);

    private final long _created = System.currentTimeMillis();

    private volatile Connection _connection;

    private final FillInterest _fillInterest = new FillInterest() {

        @Override
        protected void needsFillInterest() throws IOException {
            AbstractEndPoint.this.needsFillInterest();
        }
    };

    private final WriteFlusher _writeFlusher = new WriteFlusher(this) {

        @Override
        protected void onIncompleteFlush() {
            AbstractEndPoint.this.onIncompleteFlush();
        }
    };

    protected AbstractEndPoint(Scheduler scheduler) {
        super(scheduler);
    }

    protected final void shutdownInput() {
        while (true) {
            AbstractEndPoint.State s = (AbstractEndPoint.State) this._state.get();
            switch(s) {
                case OPEN:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.ISHUTTING)) {
                        break;
                    }
                    try {
                        this.doShutdownInput();
                    } finally {
                        if (!this._state.compareAndSet(AbstractEndPoint.State.ISHUTTING, AbstractEndPoint.State.ISHUT)) {
                            if (this._state.get() != AbstractEndPoint.State.CLOSED) {
                                throw new IllegalStateException();
                            }
                            this.doOnClose(null);
                        }
                    }
                    return;
                case ISHUTTING:
                case ISHUT:
                    return;
                case OSHUTTING:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    return;
                case OSHUT:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    this.doOnClose(null);
                    return;
                case CLOSED:
                    return;
            }
        }
    }

    @Override
    public final void shutdownOutput() {
        while (true) {
            AbstractEndPoint.State s = (AbstractEndPoint.State) this._state.get();
            switch(s) {
                case OPEN:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.OSHUTTING)) {
                        break;
                    }
                    try {
                        this.doShutdownOutput();
                    } finally {
                        if (!this._state.compareAndSet(AbstractEndPoint.State.OSHUTTING, AbstractEndPoint.State.OSHUT)) {
                            if (this._state.get() != AbstractEndPoint.State.CLOSED) {
                                throw new IllegalStateException();
                            }
                            this.doOnClose(null);
                        }
                    }
                    return;
                case ISHUTTING:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    return;
                case ISHUT:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    this.doOnClose(null);
                    return;
                case OSHUTTING:
                case OSHUT:
                    return;
                case CLOSED:
                    return;
            }
        }
    }

    @Override
    public final void close() {
        this.close(null);
    }

    protected final void close(Throwable failure) {
        while (true) {
            AbstractEndPoint.State s = (AbstractEndPoint.State) this._state.get();
            switch(s) {
                case OPEN:
                case ISHUT:
                case OSHUT:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    this.doOnClose(failure);
                    return;
                case ISHUTTING:
                case OSHUTTING:
                    if (!this._state.compareAndSet(s, AbstractEndPoint.State.CLOSED)) {
                        break;
                    }
                    return;
                case CLOSED:
                    return;
            }
        }
    }

    protected void doShutdownInput() {
    }

    protected void doShutdownOutput() {
    }

    private void doOnClose(Throwable failure) {
        try {
            this.doClose();
        } finally {
            if (failure == null) {
                this.onClose();
            } else {
                this.onClose(failure);
            }
        }
    }

    protected void doClose() {
    }

    protected void onClose(Throwable failure) {
        super.onClose();
        this._writeFlusher.onFail(failure);
        this._fillInterest.onFail(failure);
    }

    @Override
    public boolean isOutputShutdown() {
        switch((AbstractEndPoint.State) this._state.get()) {
            case OSHUTTING:
            case OSHUT:
            case CLOSED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isInputShutdown() {
        switch((AbstractEndPoint.State) this._state.get()) {
            case ISHUTTING:
            case ISHUT:
            case CLOSED:
                return true;
            case OSHUTTING:
            case OSHUT:
            default:
                return false;
        }
    }

    @Override
    public boolean isOpen() {
        switch((AbstractEndPoint.State) this._state.get()) {
            case CLOSED:
                return false;
            default:
                return true;
        }
    }

    public void checkFlush() throws IOException {
        AbstractEndPoint.State s = (AbstractEndPoint.State) this._state.get();
        switch(s) {
            case OSHUTTING:
            case OSHUT:
            case CLOSED:
                throw new IOException(s.toString());
        }
    }

    public void checkFill() throws IOException {
        AbstractEndPoint.State s = (AbstractEndPoint.State) this._state.get();
        switch(s) {
            case ISHUTTING:
            case ISHUT:
            case CLOSED:
                throw new IOException(s.toString());
            case OSHUTTING:
            case OSHUT:
        }
    }

    @Override
    public long getCreatedTimeStamp() {
        return this._created;
    }

    @Override
    public Connection getConnection() {
        return this._connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this._connection = connection;
    }

    @Override
    public boolean isOptimizedForDirectBuffers() {
        return false;
    }

    protected void reset() {
        this._state.set(AbstractEndPoint.State.OPEN);
        this._writeFlusher.onClose();
        this._fillInterest.onClose();
    }

    @Override
    public void onOpen() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onOpen {}", this);
        }
        if (this._state.get() != AbstractEndPoint.State.OPEN) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        this._writeFlusher.onClose();
        this._fillInterest.onClose();
    }

    @Override
    public void fillInterested(Callback callback) {
        this.notIdle();
        this._fillInterest.register(callback);
    }

    @Override
    public boolean tryFillInterested(Callback callback) {
        this.notIdle();
        return this._fillInterest.tryRegister(callback);
    }

    @Override
    public boolean isFillInterested() {
        return this._fillInterest.isInterested();
    }

    @Override
    public void write(Callback callback, ByteBuffer... buffers) throws IllegalStateException {
        this._writeFlusher.write(callback, buffers);
    }

    protected abstract void onIncompleteFlush();

    protected abstract void needsFillInterest() throws IOException;

    public FillInterest getFillInterest() {
        return this._fillInterest;
    }

    protected WriteFlusher getWriteFlusher() {
        return this._writeFlusher;
    }

    @Override
    protected void onIdleExpired(TimeoutException timeout) {
        Connection connection = this._connection;
        if (connection == null || connection.onIdleExpired()) {
            boolean output_shutdown = this.isOutputShutdown();
            boolean input_shutdown = this.isInputShutdown();
            boolean fillFailed = this._fillInterest.onFail(timeout);
            boolean writeFailed = this._writeFlusher.onFail(timeout);
            if (this.isOpen() && (output_shutdown || input_shutdown) && !fillFailed && !writeFailed) {
                this.close();
            } else {
                LOG.debug("Ignored idle endpoint {}", this);
            }
        }
    }

    @Override
    public void upgrade(Connection newConnection) {
        Connection old_connection = this.getConnection();
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} upgrading from {} to {}", this, old_connection, newConnection);
        }
        ByteBuffer prefilled = old_connection instanceof Connection.UpgradeFrom ? ((Connection.UpgradeFrom) old_connection).onUpgradeFrom() : null;
        old_connection.onClose();
        old_connection.getEndPoint().setConnection(newConnection);
        if (newConnection instanceof Connection.UpgradeTo) {
            ((Connection.UpgradeTo) newConnection).onUpgradeTo(prefilled);
        } else if (BufferUtil.hasContent(prefilled)) {
            throw new IllegalStateException();
        }
        newConnection.onOpen();
    }

    public String toString() {
        return String.format("%s->%s", this.toEndPointString(), this.toConnectionString());
    }

    public String toEndPointString() {
        Class<?> c = this.getClass();
        String name;
        for (name = c.getSimpleName(); name.length() == 0 && c.getSuperclass() != null; name = c.getSimpleName()) {
            c = c.getSuperclass();
        }
        return String.format("%s@%h{%s<->%s,%s,fill=%s,flush=%s,to=%d/%d}", name, this, this.getRemoteAddress(), this.getLocalAddress(), this._state.get(), this._fillInterest.toStateString(), this._writeFlusher.toStateString(), this.getIdleFor(), this.getIdleTimeout());
    }

    public String toConnectionString() {
        Connection connection = this.getConnection();
        if (connection == null) {
            return "<null>";
        } else {
            return connection instanceof AbstractConnection ? ((AbstractConnection) connection).toConnectionString() : String.format("%s@%x", connection.getClass().getSimpleName(), connection.hashCode());
        }
    }

    private static enum State {

        OPEN,
        ISHUTTING,
        ISHUT,
        OSHUTTING,
        OSHUT,
        CLOSED
    }
}
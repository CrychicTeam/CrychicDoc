package info.journeymap.shaded.org.eclipse.jetty.io.ssl;

import info.journeymap.shaded.org.eclipse.jetty.io.AbstractConnection;
import info.journeymap.shaded.org.eclipse.jetty.io.AbstractEndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.eclipse.jetty.io.WriteFlusher;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;

public class SslConnection extends AbstractConnection {

    private static final Logger LOG = Log.getLogger(SslConnection.class);

    private final List<SslHandshakeListener> handshakeListeners = new ArrayList();

    private final ByteBufferPool _bufferPool;

    private final SSLEngine _sslEngine;

    private final SslConnection.DecryptedEndPoint _decryptedEndPoint;

    private ByteBuffer _decryptedInput;

    private ByteBuffer _encryptedInput;

    private ByteBuffer _encryptedOutput;

    private final boolean _encryptedDirectBuffers = true;

    private final boolean _decryptedDirectBuffers = false;

    private boolean _renegotiationAllowed;

    private int _renegotiationLimit = -1;

    private boolean _closedOutbound;

    private final Runnable _runCompleteWrite = new SslConnection.RunnableTask("runCompleteWrite") {

        public void run() {
            SslConnection.this._decryptedEndPoint.getWriteFlusher().completeWrite();
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return SslConnection.this.getDecryptedEndPoint().getWriteFlusher().getCallbackInvocationType();
        }
    };

    private final Runnable _runFillable = new SslConnection.RunnableTask("runFillable") {

        public void run() {
            SslConnection.this._decryptedEndPoint.getFillInterest().fillable();
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return SslConnection.this.getDecryptedEndPoint().getFillInterest().getCallbackInvocationType();
        }
    };

    private final Callback _sslReadCallback = new Callback() {

        @Override
        public void succeeded() {
            SslConnection.this.onFillable();
        }

        @Override
        public void failed(Throwable x) {
            SslConnection.this.onFillInterestedFailed(x);
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return SslConnection.this.getDecryptedEndPoint().getFillInterest().getCallbackInvocationType();
        }

        public String toString() {
            return String.format("SSLC.NBReadCB@%x{%s}", SslConnection.this.hashCode(), SslConnection.this);
        }
    };

    public SslConnection(ByteBufferPool byteBufferPool, Executor executor, EndPoint endPoint, SSLEngine sslEngine) {
        super(endPoint, executor);
        this._bufferPool = byteBufferPool;
        this._sslEngine = sslEngine;
        this._decryptedEndPoint = this.newDecryptedEndPoint();
    }

    public void addHandshakeListener(SslHandshakeListener listener) {
        this.handshakeListeners.add(listener);
    }

    public boolean removeHandshakeListener(SslHandshakeListener listener) {
        return this.handshakeListeners.remove(listener);
    }

    protected SslConnection.DecryptedEndPoint newDecryptedEndPoint() {
        return new SslConnection.DecryptedEndPoint();
    }

    public SSLEngine getSSLEngine() {
        return this._sslEngine;
    }

    public SslConnection.DecryptedEndPoint getDecryptedEndPoint() {
        return this._decryptedEndPoint;
    }

    public boolean isRenegotiationAllowed() {
        return this._renegotiationAllowed;
    }

    public void setRenegotiationAllowed(boolean renegotiationAllowed) {
        this._renegotiationAllowed = renegotiationAllowed;
    }

    public int getRenegotiationLimit() {
        return this._renegotiationLimit;
    }

    public void setRenegotiationLimit(int renegotiationLimit) {
        this._renegotiationLimit = renegotiationLimit;
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.getDecryptedEndPoint().getConnection().onOpen();
    }

    @Override
    public void onClose() {
        this._decryptedEndPoint.getConnection().onClose();
        super.onClose();
    }

    @Override
    public void close() {
        this.getDecryptedEndPoint().getConnection().close();
    }

    @Override
    public boolean onIdleExpired() {
        return this.getDecryptedEndPoint().getConnection().onIdleExpired();
    }

    @Override
    public void onFillable() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onFillable enter {}", this._decryptedEndPoint);
        }
        if (this._decryptedEndPoint.isInputShutdown()) {
            this._decryptedEndPoint.close();
        }
        this._decryptedEndPoint.getFillInterest().fillable();
        boolean runComplete = false;
        synchronized (this._decryptedEndPoint) {
            if (this._decryptedEndPoint._flushRequiresFillToProgress) {
                this._decryptedEndPoint._flushRequiresFillToProgress = false;
                runComplete = true;
            }
        }
        if (runComplete) {
            this._runCompleteWrite.run();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("onFillable exit {}", this._decryptedEndPoint);
        }
    }

    @Override
    public void onFillInterestedFailed(Throwable cause) {
        this._decryptedEndPoint.getFillInterest().onFail(cause);
        boolean failFlusher = false;
        synchronized (this._decryptedEndPoint) {
            if (this._decryptedEndPoint._flushRequiresFillToProgress) {
                this._decryptedEndPoint._flushRequiresFillToProgress = false;
                failFlusher = true;
            }
        }
        if (failFlusher) {
            this._decryptedEndPoint.getWriteFlusher().onFail(cause);
        }
    }

    @Override
    public String toConnectionString() {
        ByteBuffer b = this._encryptedInput;
        int ei = b == null ? -1 : b.remaining();
        b = this._encryptedOutput;
        int eo = b == null ? -1 : b.remaining();
        b = this._decryptedInput;
        int di = b == null ? -1 : b.remaining();
        Connection connection = this._decryptedEndPoint.getConnection();
        return String.format("%s@%x{%s,eio=%d/%d,di=%d}=>%s", this.getClass().getSimpleName(), this.hashCode(), this._sslEngine.getHandshakeStatus(), ei, eo, di, connection instanceof AbstractConnection ? ((AbstractConnection) connection).toConnectionString() : connection);
    }

    public class DecryptedEndPoint extends AbstractEndPoint {

        private boolean _fillRequiresFlushToProgress;

        private boolean _flushRequiresFillToProgress;

        private boolean _cannotAcceptMoreAppDataToFlush;

        private boolean _handshaken;

        private boolean _underFlown;

        private final Callback _writeCallback = new SslConnection.DecryptedEndPoint.WriteCallBack();

        public DecryptedEndPoint() {
            super(null);
            super.setIdleTimeout(-1L);
        }

        @Override
        public long getIdleTimeout() {
            return SslConnection.this.getEndPoint().getIdleTimeout();
        }

        @Override
        public void setIdleTimeout(long idleTimeout) {
            SslConnection.this.getEndPoint().setIdleTimeout(idleTimeout);
        }

        @Override
        public boolean isOpen() {
            return SslConnection.this.getEndPoint().isOpen();
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return SslConnection.this.getEndPoint().getLocalAddress();
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return SslConnection.this.getEndPoint().getRemoteAddress();
        }

        @Override
        protected WriteFlusher getWriteFlusher() {
            return super.getWriteFlusher();
        }

        @Override
        protected void onIncompleteFlush() {
            boolean try_again = false;
            boolean write = false;
            boolean need_fill_interest = false;
            synchronized (this) {
                if (SslConnection.LOG.isDebugEnabled()) {
                    SslConnection.LOG.debug("onIncompleteFlush {}", SslConnection.this);
                }
                if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                    this._cannotAcceptMoreAppDataToFlush = true;
                    write = true;
                } else if (SslConnection.this._sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP) {
                    this._flushRequiresFillToProgress = true;
                    need_fill_interest = !SslConnection.this.isFillInterested();
                } else {
                    try_again = true;
                }
            }
            if (write) {
                SslConnection.this.getEndPoint().write(this._writeCallback, SslConnection.this._encryptedOutput);
            } else if (need_fill_interest) {
                this.ensureFillInterested();
            } else if (try_again) {
                if (this.isOutputShutdown()) {
                    this.getWriteFlusher().onClose();
                } else {
                    SslConnection.this.getExecutor().execute(SslConnection.this._runCompleteWrite);
                }
            }
        }

        @Override
        protected void needsFillInterest() throws IOException {
            boolean write = false;
            boolean fillable;
            synchronized (this) {
                fillable = BufferUtil.hasContent(SslConnection.this._decryptedInput) || BufferUtil.hasContent(SslConnection.this._encryptedInput) && !this._underFlown;
                if (!fillable && this._fillRequiresFlushToProgress) {
                    if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                        this._cannotAcceptMoreAppDataToFlush = true;
                        write = true;
                    } else {
                        this._fillRequiresFlushToProgress = false;
                        fillable = true;
                    }
                }
            }
            if (write) {
                SslConnection.this.getEndPoint().write(this._writeCallback, SslConnection.this._encryptedOutput);
            } else if (fillable) {
                SslConnection.this.getExecutor().execute(SslConnection.this._runFillable);
            } else {
                this.ensureFillInterested();
            }
        }

        @Override
        public void setConnection(Connection connection) {
            if (connection instanceof AbstractConnection) {
                AbstractConnection a = (AbstractConnection) connection;
                if (a.getInputBufferSize() < SslConnection.this._sslEngine.getSession().getApplicationBufferSize()) {
                    a.setInputBufferSize(SslConnection.this._sslEngine.getSession().getApplicationBufferSize());
                }
            }
            super.setConnection(connection);
        }

        public SslConnection getSslConnection() {
            return SslConnection.this;
        }

        @Override
        public int fill(ByteBuffer buffer) throws IOException {
            try {
                synchronized (this) {
                    Throwable failure = null;
                    try {
                        if (BufferUtil.hasContent(SslConnection.this._decryptedInput)) {
                            return BufferUtil.append(buffer, SslConnection.this._decryptedInput);
                        } else {
                            if (SslConnection.this._encryptedInput == null) {
                                SslConnection.this._encryptedInput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getPacketBufferSize(), true);
                            } else {
                                BufferUtil.compact(SslConnection.this._encryptedInput);
                            }
                            ByteBuffer app_in;
                            if (BufferUtil.space(buffer) > SslConnection.this._sslEngine.getSession().getApplicationBufferSize()) {
                                app_in = buffer;
                            } else if (SslConnection.this._decryptedInput == null) {
                                app_in = SslConnection.this._decryptedInput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getApplicationBufferSize(), false);
                            } else {
                                app_in = SslConnection.this._decryptedInput;
                            }
                            label709: while (true) {
                                int net_filled = SslConnection.this.getEndPoint().fill(SslConnection.this._encryptedInput);
                                if (net_filled > 0 && !this._handshaken && SslConnection.this._sslEngine.isOutboundDone()) {
                                    throw new SSLHandshakeException("Closed during handshake");
                                }
                                while (true) {
                                    int pos = BufferUtil.flipToFill(app_in);
                                    SSLEngineResult unwrapResult;
                                    try {
                                        unwrapResult = SslConnection.this._sslEngine.unwrap(SslConnection.this._encryptedInput, app_in);
                                    } finally {
                                        BufferUtil.flipToFlush(app_in, pos);
                                    }
                                    if (SslConnection.LOG.isDebugEnabled()) {
                                        SslConnection.LOG.debug("net={} unwrap {} {}", net_filled, unwrapResult.toString().replace('\n', ' '), SslConnection.this);
                                        SslConnection.LOG.debug("filled {} {}", BufferUtil.toHexSummary(buffer), SslConnection.this);
                                    }
                                    HandshakeStatus handshakeStatus = SslConnection.this._sslEngine.getHandshakeStatus();
                                    HandshakeStatus unwrapHandshakeStatus = unwrapResult.getHandshakeStatus();
                                    Status unwrapResultStatus = unwrapResult.getStatus();
                                    this._underFlown = unwrapResultStatus == Status.BUFFER_UNDERFLOW || unwrapResultStatus == Status.OK && unwrapResult.bytesConsumed() == 0 && unwrapResult.bytesProduced() == 0;
                                    if (this._underFlown) {
                                        if (net_filled < 0) {
                                            this.closeInbound();
                                        }
                                        if (net_filled <= 0) {
                                            return net_filled;
                                        }
                                    }
                                    switch(unwrapResultStatus) {
                                        case CLOSED:
                                            switch(handshakeStatus) {
                                                case NOT_HANDSHAKING:
                                                    return -1;
                                                case NEED_TASK:
                                                    SslConnection.this._sslEngine.getDelegatedTask().run();
                                                    continue;
                                                case NEED_WRAP:
                                                    return -1;
                                                case NEED_UNWRAP:
                                                    return -1;
                                                default:
                                                    throw new IllegalStateException();
                                            }
                                        case BUFFER_UNDERFLOW:
                                        case OK:
                                            if (unwrapHandshakeStatus == HandshakeStatus.FINISHED) {
                                                this.handshakeFinished();
                                            }
                                            if (!this.allowRenegotiate(handshakeStatus)) {
                                                return -1;
                                            }
                                            if (unwrapResult.bytesProduced() > 0) {
                                                if (app_in == buffer) {
                                                    return unwrapResult.bytesProduced();
                                                }
                                                return BufferUtil.append(buffer, SslConnection.this._decryptedInput);
                                            }
                                            switch(handshakeStatus) {
                                                case NOT_HANDSHAKING:
                                                    if (this._underFlown) {
                                                        continue label709;
                                                    }
                                                    continue;
                                                case NEED_TASK:
                                                    SslConnection.this._sslEngine.getDelegatedTask().run();
                                                    continue;
                                                case NEED_WRAP:
                                                    if (this._flushRequiresFillToProgress) {
                                                        return 0;
                                                    }
                                                    this._fillRequiresFlushToProgress = true;
                                                    this.flush(BufferUtil.EMPTY_BUFFER);
                                                    if (!BufferUtil.isEmpty(SslConnection.this._encryptedOutput)) {
                                                        return 0;
                                                    }
                                                    this._fillRequiresFlushToProgress = false;
                                                    if (this._underFlown) {
                                                        continue label709;
                                                    }
                                                    continue;
                                                case NEED_UNWRAP:
                                                    if (this._underFlown) {
                                                        continue label709;
                                                    }
                                                    continue;
                                                default:
                                                    throw new IllegalStateException();
                                            }
                                        default:
                                            throw new IllegalStateException();
                                    }
                                }
                            }
                        }
                    } catch (SSLHandshakeException var29) {
                        this.notifyHandshakeFailed(SslConnection.this._sslEngine, var29);
                        failure = var29;
                        throw var29;
                    } catch (SSLException var30) {
                        SSLException x = var30;
                        if (!this._handshaken) {
                            x = (SSLException) new SSLHandshakeException(var30.getMessage()).initCause(var30);
                            this.notifyHandshakeFailed(SslConnection.this._sslEngine, x);
                        }
                        failure = x;
                        throw x;
                    } catch (Throwable var31) {
                        failure = var31;
                        throw var31;
                    } finally {
                        if (this._flushRequiresFillToProgress) {
                            this._flushRequiresFillToProgress = false;
                            SslConnection.this.getExecutor().execute((Runnable) (failure == null ? SslConnection.this._runCompleteWrite : new SslConnection.DecryptedEndPoint.FailWrite(failure)));
                        }
                        if (SslConnection.this._encryptedInput != null && !SslConnection.this._encryptedInput.hasRemaining()) {
                            SslConnection.this._bufferPool.release(SslConnection.this._encryptedInput);
                            SslConnection.this._encryptedInput = null;
                        }
                        if (SslConnection.this._decryptedInput != null && !SslConnection.this._decryptedInput.hasRemaining()) {
                            SslConnection.this._bufferPool.release(SslConnection.this._decryptedInput);
                            SslConnection.this._decryptedInput = null;
                        }
                    }
                }
            } catch (Throwable var34) {
                this.close(var34);
                throw var34;
            }
        }

        private void handshakeFinished() {
            if (this._handshaken) {
                if (SslConnection.LOG.isDebugEnabled()) {
                    SslConnection.LOG.debug("Renegotiated {}", SslConnection.this);
                }
                if (SslConnection.this._renegotiationLimit > 0) {
                    SslConnection.this._renegotiationLimit--;
                }
            } else {
                this._handshaken = true;
                if (SslConnection.LOG.isDebugEnabled()) {
                    SslConnection.LOG.debug("{} handshake succeeded {}/{} {}", SslConnection.this._sslEngine.getUseClientMode() ? "client" : "resumed server", SslConnection.this._sslEngine.getSession().getProtocol(), SslConnection.this._sslEngine.getSession().getCipherSuite(), SslConnection.this);
                }
                this.notifyHandshakeSucceeded(SslConnection.this._sslEngine);
            }
        }

        private boolean allowRenegotiate(HandshakeStatus handshakeStatus) {
            if (!this._handshaken || handshakeStatus == HandshakeStatus.NOT_HANDSHAKING) {
                return true;
            } else if (!SslConnection.this.isRenegotiationAllowed()) {
                if (SslConnection.LOG.isDebugEnabled()) {
                    SslConnection.LOG.debug("Renegotiation denied {}", SslConnection.this);
                }
                this.closeInbound();
                return false;
            } else if (SslConnection.this._renegotiationLimit == 0) {
                if (SslConnection.LOG.isDebugEnabled()) {
                    SslConnection.LOG.debug("Renegotiation limit exceeded {}", SslConnection.this);
                }
                this.closeInbound();
                return false;
            } else {
                return true;
            }
        }

        private void closeInbound() {
            try {
                SslConnection.this._sslEngine.closeInbound();
            } catch (SSLException var2) {
                SslConnection.LOG.ignore(var2);
            }
        }

        @Override
        public boolean flush(ByteBuffer... appOuts) throws IOException {
            if (SslConnection.LOG.isDebugEnabled()) {
                for (ByteBuffer b : appOuts) {
                    SslConnection.LOG.debug("flush {} {}", BufferUtil.toHexSummary(b), SslConnection.this);
                }
            }
            try {
                synchronized (this) {
                    try {
                        if (this._cannotAcceptMoreAppDataToFlush) {
                            if (SslConnection.this._sslEngine.isOutboundDone()) {
                                throw new EofException(new ClosedChannelException());
                            } else {
                                return false;
                            }
                        } else {
                            if (SslConnection.this._encryptedOutput == null) {
                                SslConnection.this._encryptedOutput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getPacketBufferSize(), true);
                            }
                            while (true) {
                                BufferUtil.compact(SslConnection.this._encryptedOutput);
                                int pos = BufferUtil.flipToFill(SslConnection.this._encryptedOutput);
                                SSLEngineResult wrapResult;
                                try {
                                    wrapResult = SslConnection.this._sslEngine.wrap(appOuts, SslConnection.this._encryptedOutput);
                                } finally {
                                    BufferUtil.flipToFlush(SslConnection.this._encryptedOutput, pos);
                                }
                                if (SslConnection.LOG.isDebugEnabled()) {
                                    SslConnection.LOG.debug("wrap {} {}", wrapResult.toString().replace('\n', ' '), SslConnection.this);
                                }
                                Status wrapResultStatus = wrapResult.getStatus();
                                boolean allConsumed = true;
                                for (ByteBuffer b : appOuts) {
                                    if (BufferUtil.hasContent(b)) {
                                        allConsumed = false;
                                    }
                                }
                                switch(wrapResultStatus) {
                                    case CLOSED:
                                        if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                                            this._cannotAcceptMoreAppDataToFlush = true;
                                            SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput);
                                            SslConnection.this.getEndPoint().shutdownOutput();
                                            if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                                                return false;
                                            }
                                        } else {
                                            SslConnection.this.getEndPoint().shutdownOutput();
                                        }
                                        return allConsumed;
                                    case BUFFER_UNDERFLOW:
                                        throw new IllegalStateException();
                                }
                                if (SslConnection.LOG.isDebugEnabled()) {
                                    SslConnection.LOG.debug("wrap {} {} {}", wrapResultStatus, BufferUtil.toHexSummary(SslConnection.this._encryptedOutput), SslConnection.this);
                                }
                                if (wrapResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                                    this.handshakeFinished();
                                }
                                HandshakeStatus handshakeStatus = SslConnection.this._sslEngine.getHandshakeStatus();
                                if (!this.allowRenegotiate(handshakeStatus)) {
                                    SslConnection.this.getEndPoint().shutdownOutput();
                                    return allConsumed;
                                }
                                if (BufferUtil.hasContent(SslConnection.this._encryptedOutput) && !SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput)) {
                                    SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput);
                                }
                                switch(handshakeStatus) {
                                    case NOT_HANDSHAKING:
                                        if (allConsumed || wrapResult.getHandshakeStatus() != HandshakeStatus.FINISHED || !BufferUtil.isEmpty(SslConnection.this._encryptedOutput)) {
                                            return allConsumed && BufferUtil.isEmpty(SslConnection.this._encryptedOutput);
                                        }
                                        break;
                                    case NEED_TASK:
                                        SslConnection.this._sslEngine.getDelegatedTask().run();
                                    case NEED_WRAP:
                                    default:
                                        break;
                                    case NEED_UNWRAP:
                                        if (this._fillRequiresFlushToProgress || this.getFillInterest().isInterested()) {
                                            return allConsumed && BufferUtil.isEmpty(SslConnection.this._encryptedOutput);
                                        }
                                        this._flushRequiresFillToProgress = true;
                                        this.fill(BufferUtil.EMPTY_BUFFER);
                                        if (SslConnection.this._sslEngine.getHandshakeStatus() != HandshakeStatus.NEED_WRAP) {
                                            return allConsumed && BufferUtil.isEmpty(SslConnection.this._encryptedOutput);
                                        }
                                        break;
                                    case FINISHED:
                                        throw new IllegalStateException();
                                }
                            }
                        }
                    } catch (SSLHandshakeException var24) {
                        this.notifyHandshakeFailed(SslConnection.this._sslEngine, var24);
                        throw var24;
                    } finally {
                        this.releaseEncryptedOutputBuffer();
                    }
                }
            } catch (Throwable var27) {
                this.close(var27);
                throw var27;
            }
        }

        private void releaseEncryptedOutputBuffer() {
            if (!Thread.holdsLock(this)) {
                throw new IllegalStateException();
            } else {
                if (SslConnection.this._encryptedOutput != null && !SslConnection.this._encryptedOutput.hasRemaining()) {
                    SslConnection.this._bufferPool.release(SslConnection.this._encryptedOutput);
                    SslConnection.this._encryptedOutput = null;
                }
            }
        }

        @Override
        public void doShutdownOutput() {
            try {
                boolean flush = false;
                boolean close = false;
                synchronized (SslConnection.this._decryptedEndPoint) {
                    boolean ishut = this.isInputShutdown();
                    boolean oshut = this.isOutputShutdown();
                    if (SslConnection.LOG.isDebugEnabled()) {
                        SslConnection.LOG.debug("shutdownOutput: oshut={}, ishut={} {}", oshut, ishut, SslConnection.this);
                    }
                    if (oshut) {
                        return;
                    }
                    if (!SslConnection.this._closedOutbound) {
                        SslConnection.this._closedOutbound = true;
                        SslConnection.this._sslEngine.closeOutbound();
                        flush = true;
                    }
                    if (ishut) {
                        close = true;
                    }
                }
                if (flush) {
                    this.flush(BufferUtil.EMPTY_BUFFER);
                }
                if (close) {
                    SslConnection.this.getEndPoint().close();
                } else {
                    this.ensureFillInterested();
                }
            } catch (Throwable var8) {
                SslConnection.LOG.ignore(var8);
                SslConnection.this.getEndPoint().close();
            }
        }

        private void ensureFillInterested() {
            if (SslConnection.LOG.isDebugEnabled()) {
                SslConnection.LOG.debug("fillInterested SSL NB {}", SslConnection.this);
            }
            SslConnection.this.tryFillInterested(SslConnection.this._sslReadCallback);
        }

        @Override
        public boolean isOutputShutdown() {
            return SslConnection.this._sslEngine.isOutboundDone() || SslConnection.this.getEndPoint().isOutputShutdown();
        }

        @Override
        public void doClose() {
            this.doShutdownOutput();
            SslConnection.this.getEndPoint().close();
            super.doClose();
        }

        @Override
        public Object getTransport() {
            return SslConnection.this.getEndPoint();
        }

        @Override
        public boolean isInputShutdown() {
            return SslConnection.this._sslEngine.isInboundDone();
        }

        private void notifyHandshakeSucceeded(SSLEngine sslEngine) {
            SslHandshakeListener.Event event = null;
            for (SslHandshakeListener listener : SslConnection.this.handshakeListeners) {
                if (event == null) {
                    event = new SslHandshakeListener.Event(sslEngine);
                }
                try {
                    listener.handshakeSucceeded(event);
                } catch (Throwable var6) {
                    SslConnection.LOG.info("Exception while notifying listener " + listener, var6);
                }
            }
        }

        private void notifyHandshakeFailed(SSLEngine sslEngine, Throwable failure) {
            SslHandshakeListener.Event event = null;
            for (SslHandshakeListener listener : SslConnection.this.handshakeListeners) {
                if (event == null) {
                    event = new SslHandshakeListener.Event(sslEngine);
                }
                try {
                    listener.handshakeFailed(event, failure);
                } catch (Throwable var7) {
                    SslConnection.LOG.info("Exception while notifying listener " + listener, var7);
                }
            }
        }

        @Override
        public String toString() {
            return super.toString() + "->" + SslConnection.this.getEndPoint().toString();
        }

        private class FailWrite extends SslConnection.RunnableTask {

            private final Throwable failure;

            private FailWrite(Throwable failure) {
                super("runFailWrite");
                this.failure = failure;
            }

            public void run() {
                DecryptedEndPoint.this.getWriteFlusher().onFail(this.failure);
            }

            @Override
            public Invocable.InvocationType getInvocationType() {
                return DecryptedEndPoint.this.getWriteFlusher().getCallbackInvocationType();
            }
        }

        private final class WriteCallBack implements Callback, Invocable {

            private WriteCallBack() {
            }

            @Override
            public void succeeded() {
                boolean fillable = false;
                synchronized (DecryptedEndPoint.this) {
                    if (SslConnection.LOG.isDebugEnabled()) {
                        SslConnection.LOG.debug("write.complete {}", SslConnection.this.getEndPoint());
                    }
                    DecryptedEndPoint.this.releaseEncryptedOutputBuffer();
                    DecryptedEndPoint.this._cannotAcceptMoreAppDataToFlush = false;
                    if (DecryptedEndPoint.this._fillRequiresFlushToProgress) {
                        DecryptedEndPoint.this._fillRequiresFlushToProgress = false;
                        fillable = true;
                    }
                }
                if (fillable) {
                    DecryptedEndPoint.this.getFillInterest().fillable();
                }
                SslConnection.this._runCompleteWrite.run();
            }

            @Override
            public void failed(Throwable x) {
                final boolean fail_filler;
                synchronized (DecryptedEndPoint.this) {
                    if (SslConnection.LOG.isDebugEnabled()) {
                        SslConnection.LOG.debug("write failed {}", SslConnection.this, x);
                    }
                    BufferUtil.clear(SslConnection.this._encryptedOutput);
                    DecryptedEndPoint.this.releaseEncryptedOutputBuffer();
                    DecryptedEndPoint.this._cannotAcceptMoreAppDataToFlush = false;
                    fail_filler = DecryptedEndPoint.this._fillRequiresFlushToProgress;
                    if (DecryptedEndPoint.this._fillRequiresFlushToProgress) {
                        DecryptedEndPoint.this._fillRequiresFlushToProgress = false;
                    }
                }
                SslConnection.this.failedCallback(new Callback() {

                    @Override
                    public void failed(Throwable x) {
                        if (fail_filler) {
                            DecryptedEndPoint.this.getFillInterest().onFail(x);
                        }
                        DecryptedEndPoint.this.getWriteFlusher().onFail(x);
                    }
                }, x);
            }

            @Override
            public Invocable.InvocationType getInvocationType() {
                return DecryptedEndPoint.this.getWriteFlusher().getCallbackInvocationType();
            }

            public String toString() {
                return String.format("SSL@%h.DEP.writeCallback", SslConnection.this);
            }
        }
    }

    private abstract class RunnableTask implements Runnable, Invocable {

        private final String _operation;

        protected RunnableTask(String op) {
            this._operation = op;
        }

        public String toString() {
            return String.format("SSL:%s:%s:%s", SslConnection.this, this._operation, this.getInvocationType());
        }
    }
}
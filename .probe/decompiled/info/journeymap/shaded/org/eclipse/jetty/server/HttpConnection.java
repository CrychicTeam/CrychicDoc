package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpCompliance;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeaderValue;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpParser;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.http.PreEncodedHttpField;
import info.journeymap.shaded.org.eclipse.jetty.io.AbstractConnection;
import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.IteratingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpConnection extends AbstractConnection implements Runnable, HttpTransport, Connection.UpgradeFrom {

    private static final Logger LOG = Log.getLogger(HttpConnection.class);

    public static final HttpField CONNECTION_CLOSE = new PreEncodedHttpField(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString());

    public static final String UPGRADE_CONNECTION_ATTRIBUTE = "info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection.UPGRADE";

    private static final boolean REQUEST_BUFFER_DIRECT = false;

    private static final boolean HEADER_BUFFER_DIRECT = false;

    private static final boolean CHUNK_BUFFER_DIRECT = false;

    private static final ThreadLocal<HttpConnection> __currentConnection = new ThreadLocal();

    private final HttpConfiguration _config;

    private final Connector _connector;

    private final ByteBufferPool _bufferPool;

    private final HttpInput _input;

    private final HttpGenerator _generator;

    private final HttpChannelOverHttp _channel;

    private final HttpParser _parser;

    private final AtomicInteger _contentBufferReferences = new AtomicInteger();

    private volatile ByteBuffer _requestBuffer = null;

    private volatile ByteBuffer _chunk = null;

    private final HttpConnection.BlockingReadCallback _blockingReadCallback = new HttpConnection.BlockingReadCallback();

    private final HttpConnection.AsyncReadCallback _asyncReadCallback = new HttpConnection.AsyncReadCallback();

    private final HttpConnection.SendCallback _sendCallback = new HttpConnection.SendCallback();

    private final boolean _recordHttpComplianceViolations;

    public static HttpConnection getCurrentConnection() {
        return (HttpConnection) __currentConnection.get();
    }

    protected static HttpConnection setCurrentConnection(HttpConnection connection) {
        HttpConnection last = (HttpConnection) __currentConnection.get();
        __currentConnection.set(connection);
        return last;
    }

    public HttpConnection(HttpConfiguration config, Connector connector, EndPoint endPoint, HttpCompliance compliance, boolean recordComplianceViolations) {
        super(endPoint, connector.getExecutor());
        this._config = config;
        this._connector = connector;
        this._bufferPool = this._connector.getByteBufferPool();
        this._generator = this.newHttpGenerator();
        this._channel = this.newHttpChannel();
        this._input = this._channel.getRequest().getHttpInput();
        this._parser = this.newHttpParser(compliance);
        this._recordHttpComplianceViolations = recordComplianceViolations;
        if (LOG.isDebugEnabled()) {
            LOG.debug("New HTTP Connection {}", this);
        }
    }

    public HttpConfiguration getHttpConfiguration() {
        return this._config;
    }

    public boolean isRecordHttpComplianceViolations() {
        return this._recordHttpComplianceViolations;
    }

    protected HttpGenerator newHttpGenerator() {
        return new HttpGenerator(this._config.getSendServerVersion(), this._config.getSendXPoweredBy());
    }

    protected HttpChannelOverHttp newHttpChannel() {
        return new HttpChannelOverHttp(this, this._connector, this._config, this.getEndPoint(), this);
    }

    protected HttpParser newHttpParser(HttpCompliance compliance) {
        return new HttpParser(this.newRequestHandler(), this.getHttpConfiguration().getRequestHeaderSize(), compliance);
    }

    protected HttpParser.RequestHandler newRequestHandler() {
        return this._channel;
    }

    public Server getServer() {
        return this._connector.getServer();
    }

    public Connector getConnector() {
        return this._connector;
    }

    public HttpChannel getHttpChannel() {
        return this._channel;
    }

    public HttpParser getParser() {
        return this._parser;
    }

    public HttpGenerator getGenerator() {
        return this._generator;
    }

    @Override
    public boolean isOptimizedForDirectBuffers() {
        return this.getEndPoint().isOptimizedForDirectBuffers();
    }

    @Override
    public long getMessagesIn() {
        return this.getHttpChannel().getRequests();
    }

    @Override
    public long getMessagesOut() {
        return this.getHttpChannel().getRequests();
    }

    @Override
    public ByteBuffer onUpgradeFrom() {
        if (BufferUtil.hasContent(this._requestBuffer)) {
            ByteBuffer buffer = this._requestBuffer;
            this._requestBuffer = null;
            return buffer;
        } else {
            return null;
        }
    }

    void releaseRequestBuffer() {
        if (this._requestBuffer != null && !this._requestBuffer.hasRemaining()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("releaseRequestBuffer {}", this);
            }
            ByteBuffer buffer = this._requestBuffer;
            this._requestBuffer = null;
            this._bufferPool.release(buffer);
        }
    }

    public ByteBuffer getRequestBuffer() {
        if (this._requestBuffer == null) {
            this._requestBuffer = this._bufferPool.acquire(this.getInputBufferSize(), false);
        }
        return this._requestBuffer;
    }

    public boolean isRequestBufferEmpty() {
        return BufferUtil.isEmpty(this._requestBuffer);
    }

    @Override
    public void onFillable() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onFillable enter {} {}", this, this._channel.getState(), BufferUtil.toDetailString(this._requestBuffer));
        }
        HttpConnection last = setCurrentConnection(this);
        try {
            while (this.getEndPoint().isOpen()) {
                int filled = this.fillRequestBuffer();
                boolean handle = this.parseRequestBuffer();
                if (this.getEndPoint().getConnection() != this) {
                    break;
                }
                if (!this._parser.isClose() && !this._parser.isClosed()) {
                    if (handle) {
                        boolean suspended = !this._channel.handle();
                        if (suspended || this.getEndPoint().getConnection() != this) {
                            break;
                        }
                        continue;
                    }
                    if (filled > 0) {
                        continue;
                    }
                    if (filled == 0) {
                        this.fillInterested();
                    }
                    break;
                }
                this.close();
                break;
            }
        } finally {
            setCurrentConnection(last);
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} onFillable exit {} {}", this, this._channel.getState(), BufferUtil.toDetailString(this._requestBuffer));
            }
        }
    }

    protected boolean fillAndParseForContent() {
        boolean handled = false;
        while (this._parser.inContentState()) {
            int filled = this.fillRequestBuffer();
            handled = this.parseRequestBuffer();
            if (handled || filled <= 0 || this._input.hasContent()) {
                break;
            }
        }
        return handled;
    }

    private int fillRequestBuffer() {
        if (this._contentBufferReferences.get() > 0) {
            LOG.warn("{} fill with unconsumed content!", this);
            return 0;
        } else if (BufferUtil.isEmpty(this._requestBuffer)) {
            if (this.getEndPoint().isInputShutdown()) {
                this._parser.atEOF();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("{} filled -1 {}", this, BufferUtil.toDetailString(this._requestBuffer));
                }
                return -1;
            } else {
                this._requestBuffer = this.getRequestBuffer();
                try {
                    int filled = this.getEndPoint().fill(this._requestBuffer);
                    if (filled == 0) {
                        filled = this.getEndPoint().fill(this._requestBuffer);
                    }
                    if (filled < 0) {
                        this._parser.atEOF();
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{} filled {} {}", this, filled, BufferUtil.toDetailString(this._requestBuffer));
                    }
                    return filled;
                } catch (IOException var2) {
                    LOG.debug(var2);
                    return -1;
                }
            }
        } else {
            return 0;
        }
    }

    private boolean parseRequestBuffer() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} parse {} {}", this, BufferUtil.toDetailString(this._requestBuffer));
        }
        boolean handle = this._parser.parseNext(this._requestBuffer == null ? BufferUtil.EMPTY_BUFFER : this._requestBuffer);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} parsed {} {}", this, handle, this._parser);
        }
        if (this._contentBufferReferences.get() == 0) {
            this.releaseRequestBuffer();
        }
        return handle;
    }

    @Override
    public void onCompleted() {
        if (this._channel.getResponse().getStatus() == 101) {
            Connection connection = (Connection) this._channel.getRequest().getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection.UPGRADE");
            if (connection != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Upgrade from {} to {}", this, connection);
                }
                this._channel.getState().upgrade();
                this.getEndPoint().upgrade(connection);
                this._channel.recycle();
                this._parser.reset();
                this._generator.reset();
                if (this._contentBufferReferences.get() == 0) {
                    this.releaseRequestBuffer();
                } else {
                    LOG.warn("{} lingering content references?!?!", this);
                    this._requestBuffer = null;
                    this._contentBufferReferences.set(0);
                }
                return;
            }
        }
        if (this._channel.isExpecting100Continue()) {
            this._parser.close();
        } else if (this._parser.inContentState() && this._generator.isPersistent()) {
            if (this._input.isAsync()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("unconsumed async input {}", this);
                }
                this._channel.abort(new IOException("unconsumed input"));
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("unconsumed input {}", this);
                }
                if (!this._input.consumeAll()) {
                    this._channel.abort(new IOException("unconsumed input"));
                }
            }
        }
        this._channel.recycle();
        if (!this._parser.isClosed()) {
            if (this._generator.isPersistent()) {
                this._parser.reset();
            } else {
                this._parser.close();
            }
        }
        if (this._chunk != null) {
            this._bufferPool.release(this._chunk);
        }
        this._chunk = null;
        this._generator.reset();
        if (getCurrentConnection() != this) {
            if (this._parser.isStart()) {
                if (BufferUtil.isEmpty(this._requestBuffer)) {
                    this.fillInterested();
                } else if (this.getConnector().isRunning()) {
                    try {
                        this.getExecutor().execute(this);
                    } catch (RejectedExecutionException var2) {
                        if (this.getConnector().isRunning()) {
                            LOG.warn(var2);
                        } else {
                            LOG.ignore(var2);
                        }
                        this.getEndPoint().close();
                    }
                } else {
                    this.getEndPoint().close();
                }
            } else if (this.getEndPoint().isOpen()) {
                this.fillInterested();
            }
        }
    }

    @Override
    protected void onFillInterestedFailed(Throwable cause) {
        this._parser.close();
        super.onFillInterestedFailed(cause);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.fillInterested();
    }

    @Override
    public void onClose() {
        this._sendCallback.close();
        super.onClose();
    }

    public void run() {
        this.onFillable();
    }

    @Override
    public void send(MetaData.Response info, boolean head, ByteBuffer content, boolean lastContent, Callback callback) {
        if (info == null) {
            if (!lastContent && BufferUtil.isEmpty(content)) {
                callback.succeeded();
                return;
            }
        } else if (this._channel.isExpecting100Continue()) {
            this._generator.setPersistent(false);
        }
        if (this._sendCallback.reset(info, head, content, lastContent, callback)) {
            this._sendCallback.iterate();
        }
    }

    HttpInput.Content newContent(ByteBuffer c) {
        return new HttpConnection.Content(c);
    }

    @Override
    public void abort(Throwable failure) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("abort {} {}", this, failure);
        }
        this.getEndPoint().close();
    }

    @Override
    public boolean isPushSupported() {
        return false;
    }

    @Override
    public void push(MetaData.Request request) {
        LOG.debug("ignore push in {}", this);
    }

    public void asyncReadFillInterested() {
        this.getEndPoint().fillInterested(this._asyncReadCallback);
    }

    public void blockingReadFillInterested() {
        this.getEndPoint().fillInterested(this._blockingReadCallback);
    }

    public void blockingReadException(Throwable e) {
        this._blockingReadCallback.failed(e);
    }

    @Override
    public String toConnectionString() {
        return String.format("%s@%x[p=%s,g=%s]=>%s", this.getClass().getSimpleName(), this.hashCode(), this._parser, this._generator, this._channel);
    }

    private class AsyncReadCallback implements Callback {

        private AsyncReadCallback() {
        }

        @Override
        public void succeeded() {
            if (HttpConnection.this._channel.getState().onReadPossible()) {
                HttpConnection.this._channel.handle();
            }
        }

        @Override
        public void failed(Throwable x) {
            if (HttpConnection.this._input.failed(x)) {
                HttpConnection.this._channel.handle();
            }
        }
    }

    private class BlockingReadCallback implements Callback {

        private BlockingReadCallback() {
        }

        @Override
        public void succeeded() {
            HttpConnection.this._input.unblock();
        }

        @Override
        public void failed(Throwable x) {
            HttpConnection.this._input.failed(x);
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }
    }

    private class Content extends HttpInput.Content {

        public Content(ByteBuffer content) {
            super(content);
            HttpConnection.this._contentBufferReferences.incrementAndGet();
        }

        @Override
        public void succeeded() {
            if (HttpConnection.this._contentBufferReferences.decrementAndGet() == 0) {
                HttpConnection.this.releaseRequestBuffer();
            }
        }

        @Override
        public void failed(Throwable x) {
            this.succeeded();
        }
    }

    private class SendCallback extends IteratingCallback {

        private MetaData.Response _info;

        private boolean _head;

        private ByteBuffer _content;

        private boolean _lastContent;

        private Callback _callback;

        private ByteBuffer _header;

        private boolean _shutdownOut;

        private SendCallback() {
            super(true);
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return this._callback.getInvocationType();
        }

        private boolean reset(MetaData.Response info, boolean head, ByteBuffer content, boolean last, Callback callback) {
            if (this.reset()) {
                this._info = info;
                this._head = head;
                this._content = content;
                this._lastContent = last;
                this._callback = callback;
                this._header = null;
                this._shutdownOut = false;
                return true;
            } else {
                if (this.isClosed()) {
                    callback.failed(new EofException());
                } else {
                    callback.failed(new WritePendingException());
                }
                return false;
            }
        }

        @Override
        public IteratingCallback.Action process() throws Exception {
            if (this._callback == null) {
                throw new IllegalStateException();
            } else {
                ByteBuffer chunk = HttpConnection.this._chunk;
                while (true) {
                    HttpGenerator.Result result = HttpConnection.this._generator.generateResponse(this._info, this._head, this._header, chunk, this._content, this._lastContent);
                    if (HttpConnection.LOG.isDebugEnabled()) {
                        HttpConnection.LOG.debug("{} generate: {} ({},{},{})@{}", this, result, BufferUtil.toSummaryString(this._header), BufferUtil.toSummaryString(this._content), this._lastContent, HttpConnection.this._generator.getState());
                    }
                    switch(result) {
                        case NEED_INFO:
                            throw new EofException("request lifecycle violation");
                        case NEED_HEADER:
                            this._header = HttpConnection.this._bufferPool.acquire(HttpConnection.this._config.getResponseHeaderSize(), false);
                            break;
                        case NEED_CHUNK:
                            chunk = HttpConnection.this._chunk = HttpConnection.this._bufferPool.acquire(12, false);
                            break;
                        case NEED_CHUNK_TRAILER:
                            if (HttpConnection.this._chunk != null) {
                                HttpConnection.this._bufferPool.release(HttpConnection.this._chunk);
                            }
                            chunk = HttpConnection.this._chunk = HttpConnection.this._bufferPool.acquire(HttpConnection.this._config.getResponseHeaderSize(), false);
                            break;
                        case FLUSH:
                            if (this._head || HttpConnection.this._generator.isNoContent()) {
                                BufferUtil.clear(chunk);
                                BufferUtil.clear(this._content);
                            }
                            if (BufferUtil.hasContent(this._header)) {
                                if (BufferUtil.hasContent(this._content)) {
                                    if (BufferUtil.hasContent(chunk)) {
                                        HttpConnection.this.getEndPoint().write(this, this._header, chunk, this._content);
                                    } else {
                                        HttpConnection.this.getEndPoint().write(this, this._header, this._content);
                                    }
                                } else {
                                    HttpConnection.this.getEndPoint().write(this, this._header);
                                }
                            } else if (BufferUtil.hasContent(chunk)) {
                                if (BufferUtil.hasContent(this._content)) {
                                    HttpConnection.this.getEndPoint().write(this, chunk, this._content);
                                } else {
                                    HttpConnection.this.getEndPoint().write(this, chunk);
                                }
                            } else if (BufferUtil.hasContent(this._content)) {
                                HttpConnection.this.getEndPoint().write(this, this._content);
                            } else {
                                this.succeeded();
                            }
                            return IteratingCallback.Action.SCHEDULED;
                        case SHUTDOWN_OUT:
                            this._shutdownOut = true;
                            break;
                        case DONE:
                            return IteratingCallback.Action.SUCCEEDED;
                        case CONTINUE:
                            break;
                        default:
                            throw new IllegalStateException("generateResponse=" + result);
                    }
                }
            }
        }

        private void releaseHeader() {
            ByteBuffer h = this._header;
            this._header = null;
            if (h != null) {
                HttpConnection.this._bufferPool.release(h);
            }
        }

        @Override
        protected void onCompleteSuccess() {
            this.releaseHeader();
            this._callback.succeeded();
            if (this._shutdownOut) {
                HttpConnection.this.getEndPoint().shutdownOutput();
            }
        }

        @Override
        public void onCompleteFailure(Throwable x) {
            this.releaseHeader();
            HttpConnection.this.failedCallback(this._callback, x);
            if (this._shutdownOut) {
                HttpConnection.this.getEndPoint().shutdownOutput();
            }
        }

        @Override
        public String toString() {
            return String.format("%s[i=%s,cb=%s]", super.toString(), this._info, this._callback);
        }
    }
}
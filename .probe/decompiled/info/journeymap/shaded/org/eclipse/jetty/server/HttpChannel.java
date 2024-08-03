package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpStatus;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.ChannelEndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.QuietException;
import info.journeymap.shaded.org.eclipse.jetty.io.RuntimeIOException;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ErrorHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.SharedBlockingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class HttpChannel implements Runnable, HttpOutput.Interceptor {

    private static final Logger LOG = Log.getLogger(HttpChannel.class);

    private final AtomicBoolean _committed = new AtomicBoolean();

    private final AtomicLong _requests = new AtomicLong();

    private final Connector _connector;

    private final Executor _executor;

    private final HttpConfiguration _configuration;

    private final EndPoint _endPoint;

    private final HttpTransport _transport;

    private final HttpChannelState _state;

    private final Request _request;

    private final Response _response;

    private MetaData.Response _committedMetaData;

    private RequestLog _requestLog;

    private long _oldIdleTimeout;

    private long _written;

    public HttpChannel(Connector connector, HttpConfiguration configuration, EndPoint endPoint, HttpTransport transport) {
        this._connector = connector;
        this._configuration = configuration;
        this._endPoint = endPoint;
        this._transport = transport;
        this._state = new HttpChannelState(this);
        this._request = new Request(this, this.newHttpInput(this._state));
        this._response = new Response(this, this.newHttpOutput());
        this._executor = connector == null ? null : connector.getServer().getThreadPool();
        this._requestLog = connector == null ? null : connector.getServer().getRequestLog();
        if (LOG.isDebugEnabled()) {
            LOG.debug("new {} -> {},{},{}", this, this._endPoint, this._endPoint.getConnection(), this._state);
        }
    }

    protected HttpInput newHttpInput(HttpChannelState state) {
        return new HttpInput(state);
    }

    protected HttpOutput newHttpOutput() {
        return new HttpOutput(this);
    }

    public HttpChannelState getState() {
        return this._state;
    }

    public long getBytesWritten() {
        return this._written;
    }

    public long getRequests() {
        return this._requests.get();
    }

    public Connector getConnector() {
        return this._connector;
    }

    public HttpTransport getHttpTransport() {
        return this._transport;
    }

    public RequestLog getRequestLog() {
        return this._requestLog;
    }

    public void setRequestLog(RequestLog requestLog) {
        this._requestLog = requestLog;
    }

    public void addRequestLog(RequestLog requestLog) {
        if (this._requestLog == null) {
            this._requestLog = requestLog;
        } else if (this._requestLog instanceof RequestLogCollection) {
            ((RequestLogCollection) this._requestLog).add(requestLog);
        } else {
            this._requestLog = new RequestLogCollection(this._requestLog, requestLog);
        }
    }

    public MetaData.Response getCommittedMetaData() {
        return this._committedMetaData;
    }

    public long getIdleTimeout() {
        return this._endPoint.getIdleTimeout();
    }

    public void setIdleTimeout(long timeoutMs) {
        this._endPoint.setIdleTimeout(timeoutMs);
    }

    public ByteBufferPool getByteBufferPool() {
        return this._connector.getByteBufferPool();
    }

    public HttpConfiguration getHttpConfiguration() {
        return this._configuration;
    }

    @Override
    public boolean isOptimizedForDirectBuffers() {
        return this.getHttpTransport().isOptimizedForDirectBuffers();
    }

    public Server getServer() {
        return this._connector.getServer();
    }

    public Request getRequest() {
        return this._request;
    }

    public Response getResponse() {
        return this._response;
    }

    public EndPoint getEndPoint() {
        return this._endPoint;
    }

    public InetSocketAddress getLocalAddress() {
        return this._endPoint.getLocalAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return this._endPoint.getRemoteAddress();
    }

    public void continue100(int available) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void recycle() {
        this._committed.set(false);
        this._request.recycle();
        this._response.recycle();
        this._committedMetaData = null;
        this._requestLog = this._connector == null ? null : this._connector.getServer().getRequestLog();
        this._written = 0L;
    }

    public void asyncReadFillInterested() {
    }

    public void run() {
        this.handle();
    }

    public boolean handle() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} handle {} ", this, this._request.getHttpURI());
        }
        HttpChannelState.Action action = this._state.handling();
        while (true) {
            label442: {
                if (!this.getServer().isStopped()) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("{} action {}", this, action);
                        }
                        switch(action) {
                            case TERMINATED:
                            case WAIT:
                                break;
                            case DISPATCH:
                                if (!this._request.hasMetaData()) {
                                    throw new IllegalStateException("state=" + this._state);
                                }
                                this._request.setHandled(false);
                                this._response.getHttpOutput().reopen();
                                try {
                                    this._request.setDispatcherType(DispatcherType.REQUEST);
                                    List<HttpConfiguration.Customizer> customizers = this._configuration.getCustomizers();
                                    if (!customizers.isEmpty()) {
                                        for (HttpConfiguration.Customizer customizer : customizers) {
                                            customizer.customize(this.getConnector(), this._configuration, this._request);
                                            if (this._request.isHandled()) {
                                                break;
                                            }
                                        }
                                    }
                                    if (!this._request.isHandled()) {
                                        this.getServer().handle(this);
                                    }
                                    break label442;
                                } finally {
                                    this._request.setDispatcherType(null);
                                }
                            case ASYNC_DISPATCH:
                                this._request.setHandled(false);
                                this._response.getHttpOutput().reopen();
                                try {
                                    this._request.setDispatcherType(DispatcherType.ASYNC);
                                    this.getServer().handleAsync(this);
                                    break label442;
                                } finally {
                                    this._request.setDispatcherType(null);
                                }
                            case ERROR_DISPATCH:
                                try {
                                    this._response.reset();
                                    Integer icode = (Integer) this._request.getAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code");
                                    int code = icode != null ? icode : 500;
                                    this._response.setStatus(code);
                                    this._request.setAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code", code);
                                    this._request.setHandled(false);
                                    this._response.getHttpOutput().reopen();
                                    try {
                                        this._request.setDispatcherType(DispatcherType.ERROR);
                                        this.getServer().handle(this);
                                    } finally {
                                        this._request.setDispatcherType(null);
                                    }
                                } catch (Throwable var26) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Could not perform ERROR dispatch, aborting", var26);
                                    }
                                    Throwable failure = (Throwable) this._request.getAttribute("info.journeymap.shaded.org.javax.servlet.error.exception");
                                    failure.addSuppressed(var26);
                                    this.minimalErrorResponse(failure);
                                }
                                break label442;
                            case ASYNC_ERROR:
                                throw this._state.getAsyncContextEvent().getThrowable();
                            case READ_PRODUCE:
                                this._request.getHttpInput().produceContent();
                                break label442;
                            case READ_CALLBACK:
                                ContextHandler handlerx = this._state.getContextHandler();
                                if (handlerx != null) {
                                    handlerx.handle(this._request, this._request.getHttpInput());
                                } else {
                                    this._request.getHttpInput().run();
                                }
                                break label442;
                            case WRITE_CALLBACK:
                                ContextHandler handler = this._state.getContextHandler();
                                if (handler != null) {
                                    handler.handle(this._request, this._response.getHttpOutput());
                                } else {
                                    this._response.getHttpOutput().run();
                                }
                                break label442;
                            case COMPLETE:
                                if (!this._response.isCommitted() && !this._request.isHandled()) {
                                    this._response.sendError(404);
                                } else {
                                    int status = this._response.getStatus();
                                    boolean hasContent = !this._request.isHead() && (!HttpMethod.CONNECT.is(this._request.getMethod()) || status != 200) && !HttpStatus.isInformational(status) && status != 204 && status != 304;
                                    if (hasContent && !this._response.isContentComplete(this._response.getHttpOutput().getWritten())) {
                                        if (this.isCommitted()) {
                                            this._transport.abort(new IOException("insufficient content written"));
                                        } else {
                                            this._response.sendError(500, "insufficient content written");
                                        }
                                    }
                                }
                                this._response.closeOutput();
                                this._request.setHandled(true);
                                this._state.onComplete();
                                this.onCompleted();
                                break;
                            default:
                                throw new IllegalStateException("state=" + this._state);
                        }
                    } catch (Throwable var27) {
                        if ("info.journeymap.shaded.org.eclipse.jetty.continuation.ContinuationThrowable".equals(var27.getClass().getName())) {
                            LOG.ignore(var27);
                        } else {
                            this.handleException(var27);
                        }
                        break label442;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("{} handle exit, result {}", this, action);
                }
                boolean suspended = action == HttpChannelState.Action.WAIT;
                return !suspended;
            }
            action = this._state.unhandle();
        }
    }

    protected void sendError(int code, String reason) {
        try {
            this._response.sendError(code, reason);
        } catch (Throwable var7) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not send error " + code + " " + reason, var7);
            }
        } finally {
            this._state.errorComplete();
        }
    }

    protected void handleException(Throwable failure) {
        if (failure instanceof RuntimeIOException) {
            failure = failure.getCause();
        }
        if (!(failure instanceof QuietException) && this.getServer().isRunning()) {
            if (failure instanceof BadMessageException) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(this._request.getRequestURI(), failure);
                } else {
                    LOG.warn("{} {}", this._request.getRequestURI(), failure);
                }
            } else {
                LOG.warn(this._request.getRequestURI(), failure);
            }
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(this._request.getRequestURI(), failure);
        }
        try {
            this._state.onError(failure);
        } catch (Throwable var3) {
            failure.addSuppressed(var3);
            LOG.warn("ERROR dispatch failed", failure);
            this.minimalErrorResponse(failure);
        }
    }

    private void minimalErrorResponse(Throwable failure) {
        try {
            Integer code = (Integer) this._request.getAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code");
            this._response.reset();
            this._response.setStatus(code == null ? 500 : code);
            this._response.flushBuffer();
        } catch (Throwable var3) {
            failure.addSuppressed(var3);
            this._transport.abort(failure);
        }
    }

    public boolean isExpecting100Continue() {
        return false;
    }

    public boolean isExpecting102Processing() {
        return false;
    }

    public String toString() {
        return String.format("%s@%x{r=%s,c=%b,a=%s,uri=%s}", this.getClass().getSimpleName(), this.hashCode(), this._requests, this._committed.get(), this._state.getState(), this._request.getHttpURI());
    }

    public void onRequest(MetaData.Request request) {
        this._requests.incrementAndGet();
        this._request.setTimeStamp(System.currentTimeMillis());
        HttpFields fields = this._response.getHttpFields();
        if (this._configuration.getSendDateHeader() && !fields.contains(HttpHeader.DATE)) {
            fields.put(this._connector.getServer().getDateField());
        }
        long idleTO = this._configuration.getIdleTimeout();
        this._oldIdleTimeout = this.getIdleTimeout();
        if (idleTO >= 0L && this._oldIdleTimeout != idleTO) {
            this.setIdleTimeout(idleTO);
        }
        this._request.setMetaData(request);
        if (LOG.isDebugEnabled()) {
            LOG.debug("REQUEST for {} on {}{}{} {} {}{}{}", request.getURIString(), this, System.lineSeparator(), request.getMethod(), request.getURIString(), request.getHttpVersion(), System.lineSeparator(), request.getFields());
        }
    }

    public boolean onContent(HttpInput.Content content) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onContent {}", this, content);
        }
        return this._request.getHttpInput().addContent(content);
    }

    public boolean onContentComplete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onContentComplete", this);
        }
        return false;
    }

    public void onTrailers(HttpFields trailers) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onTrailers {}", this, trailers);
        }
        this._request.setTrailers(trailers);
    }

    public boolean onRequestComplete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onRequestComplete", this);
        }
        return this._request.getHttpInput().eof();
    }

    public void onCompleted() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("COMPLETE for {} written={}", this.getRequest().getRequestURI(), this.getBytesWritten());
        }
        if (this._requestLog != null) {
            this._requestLog.log(this._request, this._response);
        }
        long idleTO = this._configuration.getIdleTimeout();
        if (idleTO >= 0L && this.getIdleTimeout() != this._oldIdleTimeout) {
            this.setIdleTimeout(this._oldIdleTimeout);
        }
        this._transport.onCompleted();
    }

    public boolean onEarlyEOF() {
        return this._request.getHttpInput().earlyEOF();
    }

    public void onBadMessage(int status, String reason) {
        if (status < 400 || status > 599) {
            status = 400;
        }
        HttpChannelState.Action action;
        try {
            action = this._state.handling();
        } catch (IllegalStateException var12) {
            this.abort(var12);
            throw new BadMessageException(status, reason);
        }
        try {
            if (action == HttpChannelState.Action.DISPATCH) {
                ByteBuffer content = null;
                HttpFields fields = new HttpFields();
                ErrorHandler handler = this.getServer().getBean(ErrorHandler.class);
                if (handler != null) {
                    content = handler.badMessageError(status, reason, fields);
                }
                this.sendResponse(new MetaData.Response(HttpVersion.HTTP_1_1, status, reason, fields, (long) BufferUtil.length(content)), content, true);
            }
        } catch (IOException var11) {
            LOG.debug(var11);
        } finally {
            if (this._state.unhandle() != HttpChannelState.Action.COMPLETE) {
                throw new IllegalStateException();
            }
            this._state.onComplete();
            this.onCompleted();
        }
    }

    protected boolean sendResponse(MetaData.Response info, ByteBuffer content, boolean complete, Callback callback) {
        boolean committing = this._committed.compareAndSet(false, true);
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendResponse info={} content={} complete={} committing={} callback={}", info, BufferUtil.toDetailString(content), complete, committing, callback);
        }
        if (committing) {
            if (info == null) {
                info = this._response.newResponseMetaData();
            }
            this.commit(info);
            int status = info.getStatus();
            Callback committed = (Callback) (status < 200 && status >= 100 ? new HttpChannel.Commit100Callback(callback) : new HttpChannel.CommitCallback(callback));
            this._transport.send(info, this._request.isHead(), content, complete, committed);
        } else if (info == null) {
            this._transport.send(null, this._request.isHead(), content, complete, callback);
        } else {
            callback.failed(new IllegalStateException("committed"));
        }
        return committing;
    }

    protected boolean sendResponse(MetaData.Response info, ByteBuffer content, boolean complete) throws IOException {
        try {
            SharedBlockingCallback.Blocker blocker = this._response.getHttpOutput().acquireWriteBlockingCallback();
            Throwable var5 = null;
            boolean var7;
            try {
                boolean committing = this.sendResponse(info, content, complete, blocker);
                blocker.block();
                var7 = committing;
            } catch (Throwable var17) {
                var5 = var17;
                throw var17;
            } finally {
                if (blocker != null) {
                    if (var5 != null) {
                        try {
                            blocker.close();
                        } catch (Throwable var16) {
                            var5.addSuppressed(var16);
                        }
                    } else {
                        blocker.close();
                    }
                }
            }
            return var7;
        } catch (Throwable var19) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(var19);
            }
            this.abort(var19);
            throw var19;
        }
    }

    protected void commit(MetaData.Response info) {
        this._committedMetaData = info;
        if (LOG.isDebugEnabled()) {
            LOG.debug("COMMIT for {} on {}{}{} {} {}{}{}", this.getRequest().getRequestURI(), this, System.lineSeparator(), info.getStatus(), info.getReason(), info.getHttpVersion(), System.lineSeparator(), info.getFields());
        }
    }

    public boolean isCommitted() {
        return this._committed.get();
    }

    @Override
    public void write(ByteBuffer content, boolean complete, Callback callback) {
        this._written = this._written + (long) BufferUtil.length(content);
        this.sendResponse(null, content, complete, callback);
    }

    @Override
    public void resetBuffer() {
        if (this.isCommitted()) {
            throw new IllegalStateException("Committed");
        }
    }

    @Override
    public HttpOutput.Interceptor getNextInterceptor() {
        return null;
    }

    protected void execute(Runnable task) {
        this._executor.execute(task);
    }

    public Scheduler getScheduler() {
        return this._connector.getScheduler();
    }

    public boolean useDirectBuffers() {
        return this.getEndPoint() instanceof ChannelEndPoint;
    }

    public void abort(Throwable failure) {
        this._transport.abort(failure);
    }

    private class Commit100Callback extends HttpChannel.CommitCallback {

        private Commit100Callback(Callback callback) {
            super(callback);
        }

        @Override
        public void succeeded() {
            if (HttpChannel.this._committed.compareAndSet(true, false)) {
                super.succeeded();
            } else {
                super.failed(new IllegalStateException());
            }
        }
    }

    private class CommitCallback extends Callback.Nested {

        private CommitCallback(Callback callback) {
            super(callback);
        }

        @Override
        public void failed(final Throwable x) {
            if (HttpChannel.LOG.isDebugEnabled()) {
                HttpChannel.LOG.debug("Commit failed", x);
            }
            if (x instanceof BadMessageException) {
                HttpChannel.this._transport.send(HttpGenerator.RESPONSE_500_INFO, false, null, true, new Callback.Nested(this) {

                    @Override
                    public void succeeded() {
                        super.failed(x);
                        HttpChannel.this._response.getHttpOutput().closed();
                    }

                    @Override
                    public void failed(Throwable th) {
                        HttpChannel.this._transport.abort(x);
                        super.failed(x);
                    }
                });
            } else {
                HttpChannel.this._transport.abort(x);
                super.failed(x);
            }
        }
    }
}
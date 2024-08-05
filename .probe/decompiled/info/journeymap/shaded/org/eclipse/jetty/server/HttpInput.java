package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.eclipse.jetty.io.RuntimeIOException;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Destroyable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import info.journeymap.shaded.org.javax.servlet.ReadListener;
import info.journeymap.shaded.org.javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpInput extends ServletInputStream implements Runnable {

    private static final Logger LOG = Log.getLogger(HttpInput.class);

    static final HttpInput.Content EOF_CONTENT = new HttpInput.EofContent("EOF");

    static final HttpInput.Content EARLY_EOF_CONTENT = new HttpInput.EofContent("EARLY_EOF");

    private final byte[] _oneByteBuffer = new byte[1];

    private HttpInput.Content _content;

    private HttpInput.Content _intercepted;

    private final Deque<HttpInput.Content> _inputQ = new ArrayDeque();

    private final HttpChannelState _channelState;

    private ReadListener _listener;

    private HttpInput.State _state;

    private long _firstByteTimeStamp;

    private long _contentArrived;

    private long _contentConsumed;

    private long _blockUntil;

    private HttpInput.Interceptor _interceptor;

    protected static final HttpInput.State STREAM = new HttpInput.State() {

        @Override
        public boolean blockForContent(HttpInput input) throws IOException {
            input.blockForContent();
            return true;
        }

        public String toString() {
            return "STREAM";
        }
    };

    protected static final HttpInput.State ASYNC = new HttpInput.State() {

        @Override
        public int noContent() throws IOException {
            return 0;
        }

        public String toString() {
            return "ASYNC";
        }
    };

    protected static final HttpInput.State EARLY_EOF = new HttpInput.EOFState() {

        @Override
        public int noContent() throws IOException {
            throw this.getError();
        }

        public String toString() {
            return "EARLY_EOF";
        }

        public IOException getError() {
            return new EofException("Early EOF");
        }
    };

    protected static final HttpInput.State EOF = new HttpInput.EOFState() {

        public String toString() {
            return "EOF";
        }
    };

    protected static final HttpInput.State AEOF = new HttpInput.EOFState() {

        public String toString() {
            return "AEOF";
        }
    };

    public HttpInput(HttpChannelState state) {
        this._state = STREAM;
        this._firstByteTimeStamp = -1L;
        this._channelState = state;
    }

    protected HttpChannelState getHttpChannelState() {
        return this._channelState;
    }

    public void recycle() {
        synchronized (this._inputQ) {
            if (this._content != null) {
                this._content.failed(null);
            }
            this._content = null;
            for (HttpInput.Content item = (HttpInput.Content) this._inputQ.poll(); item != null; item = (HttpInput.Content) this._inputQ.poll()) {
                item.failed(null);
            }
            this._listener = null;
            this._state = STREAM;
            this._contentArrived = 0L;
            this._contentConsumed = 0L;
            this._firstByteTimeStamp = -1L;
            this._blockUntil = 0L;
            if (this._interceptor instanceof Destroyable) {
                ((Destroyable) this._interceptor).destroy();
            }
            this._interceptor = null;
        }
    }

    public HttpInput.Interceptor getInterceptor() {
        return this._interceptor;
    }

    public void setInterceptor(HttpInput.Interceptor interceptor) {
        this._interceptor = interceptor;
    }

    public void addInterceptor(HttpInput.Interceptor interceptor) {
        if (this._interceptor == null) {
            this._interceptor = interceptor;
        } else {
            this._interceptor = new HttpInput.ChainedInterceptor(this._interceptor, interceptor);
        }
    }

    public int available() {
        int available = 0;
        boolean woken = false;
        synchronized (this._inputQ) {
            if (this._content == null) {
                this._content = (HttpInput.Content) this._inputQ.poll();
            }
            if (this._content == null) {
                try {
                    this.produceContent();
                } catch (IOException var6) {
                    woken = this.failed(var6);
                }
                if (this._content == null) {
                    this._content = (HttpInput.Content) this._inputQ.poll();
                }
            }
            if (this._content != null) {
                available = this._content.remaining();
            }
        }
        if (woken) {
            this.wake();
        }
        return available;
    }

    protected void wake() {
        HttpChannel channel = this._channelState.getHttpChannel();
        Executor executor = channel.getConnector().getServer().getThreadPool();
        executor.execute(channel);
    }

    private long getBlockingTimeout() {
        return this.getHttpChannelState().getHttpChannel().getHttpConfiguration().getBlockingTimeout();
    }

    public int read() throws IOException {
        int read = this.read(this._oneByteBuffer, 0, 1);
        if (read == 0) {
            throw new IllegalStateException("unready read=0");
        } else {
            return read < 0 ? -1 : this._oneByteBuffer[0] & 0xFF;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        boolean wake = false;
        int l;
        synchronized (this._inputQ) {
            if (!this.isAsync() && this._blockUntil == 0L) {
                long blockingTimeout = this.getBlockingTimeout();
                if (blockingTimeout > 0L) {
                    this._blockUntil = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(blockingTimeout);
                }
            }
            long minRequestDataRate = this._channelState.getHttpChannel().getHttpConfiguration().getMinRequestDataRate();
            if (minRequestDataRate > 0L && this._firstByteTimeStamp != -1L) {
                long period = System.nanoTime() - this._firstByteTimeStamp;
                if (period > 0L) {
                    long minimum_data = minRequestDataRate * TimeUnit.NANOSECONDS.toMillis(period) / TimeUnit.SECONDS.toMillis(1L);
                    if (this._contentArrived < minimum_data) {
                        throw new BadMessageException(408, String.format("Request data rate < %d B/s", minRequestDataRate));
                    }
                }
            }
            while (true) {
                HttpInput.Content item = this.nextContent();
                if (item != null) {
                    l = this.get(item, b, off, len);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{} read {} from {}", this, l, item);
                    }
                    if (item.isEmpty()) {
                        this.nextInterceptedContent();
                    }
                    break;
                }
                if (!this._state.blockForContent(this)) {
                    l = this._state.noContent();
                    if (l < 0) {
                        wake = this._channelState.onReadEof();
                    }
                    break;
                }
            }
        }
        if (wake) {
            this.wake();
        }
        return l;
    }

    protected void produceContent() throws IOException {
    }

    protected HttpInput.Content nextContent() throws IOException {
        HttpInput.Content content = this.nextNonSentinelContent();
        if (content == null && !this.isFinished()) {
            this.produceContent();
            content = this.nextNonSentinelContent();
        }
        return content;
    }

    protected HttpInput.Content nextNonSentinelContent() {
        while (true) {
            HttpInput.Content content = this.nextInterceptedContent();
            if (!(content instanceof HttpInput.SentinelContent)) {
                return content;
            }
            this.consume(content);
        }
    }

    protected HttpInput.Content produceNextContext() throws IOException {
        HttpInput.Content content = this.nextInterceptedContent();
        if (content == null && !this.isFinished()) {
            this.produceContent();
            content = this.nextInterceptedContent();
        }
        return content;
    }

    protected HttpInput.Content nextInterceptedContent() {
        if (this._intercepted != null) {
            if (this._intercepted.hasContent()) {
                return this._intercepted;
            }
            this._intercepted.succeeded();
            this._intercepted = null;
        }
        if (this._content == null) {
            this._content = (HttpInput.Content) this._inputQ.poll();
        }
        while (this._content != null) {
            if (this._interceptor != null) {
                this._intercepted = this._interceptor.readFrom(this._content);
                if (this._intercepted != null && this._intercepted != this._content) {
                    if (this._intercepted.hasContent()) {
                        return this._intercepted;
                    }
                    this._intercepted.succeeded();
                }
                this._intercepted = null;
            }
            if (this._content.hasContent() || this._content instanceof HttpInput.SentinelContent) {
                return this._content;
            }
            this._content.succeeded();
            this._content = (HttpInput.Content) this._inputQ.poll();
        }
        return null;
    }

    private void consume(HttpInput.Content content) {
        if (content instanceof HttpInput.EofContent) {
            if (content == EARLY_EOF_CONTENT) {
                this._state = EARLY_EOF;
            } else if (this._listener == null) {
                this._state = EOF;
            } else {
                this._state = AEOF;
            }
        }
        content.succeeded();
        if (this._content == content) {
            this._content = null;
        } else if (this._intercepted == content) {
            this._intercepted = null;
        }
    }

    protected int get(HttpInput.Content content, byte[] buffer, int offset, int length) {
        int l = content.get(buffer, offset, length);
        this._contentConsumed += (long) l;
        return l;
    }

    protected void skip(HttpInput.Content content, int length) {
        int l = content.skip(length);
        this._contentConsumed += (long) l;
        if (l > 0 && content.isEmpty()) {
            this.nextNonSentinelContent();
        }
    }

    protected void blockForContent() throws IOException {
        try {
            long timeout = 0L;
            if (this._blockUntil != 0L) {
                timeout = TimeUnit.NANOSECONDS.toMillis(this._blockUntil - System.nanoTime());
                if (timeout <= 0L) {
                    throw new TimeoutException();
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} blocking for content timeout={}", this, timeout);
            }
            if (timeout > 0L) {
                this._inputQ.wait(timeout);
            } else {
                this._inputQ.wait();
            }
            if (this._blockUntil != 0L && TimeUnit.NANOSECONDS.toMillis(this._blockUntil - System.nanoTime()) <= 0L) {
                throw new TimeoutException(String.format("Blocking timeout %d ms", this.getBlockingTimeout()));
            }
        } catch (Throwable var3) {
            throw (IOException) new InterruptedIOException().initCause(var3);
        }
    }

    public boolean prependContent(HttpInput.Content item) {
        boolean woken = false;
        synchronized (this._inputQ) {
            if (this._content != null) {
                this._inputQ.push(this._content);
            }
            this._content = item;
            this._contentConsumed = this._contentConsumed - (long) item.remaining();
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} prependContent {}", this, item);
            }
            if (this._listener == null) {
                this._inputQ.notify();
            } else {
                woken = this._channelState.onContentAdded();
            }
            return woken;
        }
    }

    public boolean addContent(HttpInput.Content content) {
        boolean woken = false;
        synchronized (this._inputQ) {
            if (this._firstByteTimeStamp == -1L) {
                this._firstByteTimeStamp = System.nanoTime();
            }
            this._contentArrived = this._contentArrived + (long) content.remaining();
            if (this._content == null && this._inputQ.isEmpty()) {
                this._content = content;
            } else {
                this._inputQ.offer(content);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} addContent {}", this, content);
            }
            if (this.nextInterceptedContent() != null) {
                if (this._listener == null) {
                    this._inputQ.notify();
                } else {
                    woken = this._channelState.onContentAdded();
                }
            }
            return woken;
        }
    }

    public boolean hasContent() {
        synchronized (this._inputQ) {
            return this._content != null || this._inputQ.size() > 0;
        }
    }

    public void unblock() {
        synchronized (this._inputQ) {
            this._inputQ.notify();
        }
    }

    public long getContentConsumed() {
        synchronized (this._inputQ) {
            return this._contentConsumed;
        }
    }

    public boolean earlyEOF() {
        return this.addContent(EARLY_EOF_CONTENT);
    }

    public boolean eof() {
        return this.addContent(EOF_CONTENT);
    }

    public boolean consumeAll() {
        synchronized (this._inputQ) {
            boolean var10000;
            try {
                while (!this.isFinished()) {
                    HttpInput.Content item = this.nextContent();
                    if (item == null) {
                        break;
                    }
                    this.skip(item, item.remaining());
                }
                var10000 = this.isFinished() && !this.isError();
            } catch (IOException var4) {
                LOG.debug(var4);
                return false;
            }
            return var10000;
        }
    }

    public boolean isError() {
        synchronized (this._inputQ) {
            return this._state instanceof HttpInput.ErrorState;
        }
    }

    public boolean isAsync() {
        synchronized (this._inputQ) {
            return this._state == ASYNC;
        }
    }

    @Override
    public boolean isFinished() {
        synchronized (this._inputQ) {
            return this._state instanceof HttpInput.EOFState;
        }
    }

    public boolean isAsyncEOF() {
        synchronized (this._inputQ) {
            return this._state == AEOF;
        }
    }

    @Override
    public boolean isReady() {
        try {
            synchronized (this._inputQ) {
                if (this._listener == null) {
                    return true;
                }
                if (this._state instanceof HttpInput.EOFState) {
                    return true;
                }
                if (this.produceNextContext() != null) {
                    return true;
                }
                this._channelState.onReadUnready();
            }
            return false;
        } catch (IOException var4) {
            LOG.ignore(var4);
            return true;
        }
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        readListener = (ReadListener) Objects.requireNonNull(readListener);
        boolean woken = false;
        try {
            synchronized (this._inputQ) {
                if (this._listener != null) {
                    throw new IllegalStateException("ReadListener already set");
                }
                this._listener = readListener;
                HttpInput.Content content = this.produceNextContext();
                if (content != null) {
                    this._state = ASYNC;
                    woken = this._channelState.onReadReady();
                } else if (this._state == EOF) {
                    this._state = AEOF;
                    woken = this._channelState.onReadEof();
                } else {
                    this._state = ASYNC;
                    this._channelState.onReadUnready();
                }
            }
        } catch (IOException var7) {
            throw new RuntimeIOException(var7);
        }
        if (woken) {
            this.wake();
        }
    }

    public boolean failed(Throwable x) {
        boolean woken = false;
        synchronized (this._inputQ) {
            if (this._state instanceof HttpInput.ErrorState) {
                LOG.warn(x);
            } else {
                this._state = new HttpInput.ErrorState(x);
            }
            if (this._listener == null) {
                this._inputQ.notify();
            } else {
                woken = this._channelState.onContentAdded();
            }
            return woken;
        }
    }

    public void run() {
        boolean aeof = false;
        ReadListener listener;
        Throwable error;
        synchronized (this._inputQ) {
            listener = this._listener;
            if (this._state == EOF) {
                return;
            }
            if (this._state == AEOF) {
                this._state = EOF;
                aeof = true;
            }
            error = this._state.getError();
            if (!aeof && error == null) {
                HttpInput.Content content = this.nextInterceptedContent();
                if (content == null) {
                    return;
                }
                if (content instanceof HttpInput.EofContent) {
                    this.consume(content);
                    if (this._state == EARLY_EOF) {
                        error = this._state.getError();
                    } else if (this._state == AEOF) {
                        aeof = true;
                        this._state = EOF;
                    }
                }
            }
        }
        try {
            if (error != null) {
                this._channelState.getHttpChannel().getResponse().getHttpFields().add(HttpConnection.CONNECTION_CLOSE);
                listener.onError(error);
            } else if (aeof) {
                listener.onAllDataRead();
            } else {
                listener.onDataAvailable();
            }
        } catch (Throwable var8) {
            Throwable e = var8;
            LOG.warn(var8.toString());
            LOG.debug(var8);
            try {
                if (aeof || error == null) {
                    this._channelState.getHttpChannel().getResponse().getHttpFields().add(HttpConnection.CONNECTION_CLOSE);
                    listener.onError(e);
                }
            } catch (Throwable var7) {
                LOG.warn(var7.toString());
                LOG.debug(var7);
                throw new RuntimeIOException(var7);
            }
        }
    }

    public String toString() {
        HttpInput.State state;
        long consumed;
        int q;
        HttpInput.Content content;
        synchronized (this._inputQ) {
            state = this._state;
            consumed = this._contentConsumed;
            q = this._inputQ.size();
            content = (HttpInput.Content) this._inputQ.peekFirst();
        }
        return String.format("%s@%x[c=%d,q=%d,[0]=%s,s=%s]", this.getClass().getSimpleName(), this.hashCode(), consumed, q, content, state);
    }

    public static class ChainedInterceptor implements HttpInput.Interceptor, Destroyable {

        private final HttpInput.Interceptor _prev;

        private final HttpInput.Interceptor _next;

        public ChainedInterceptor(HttpInput.Interceptor prev, HttpInput.Interceptor next) {
            this._prev = prev;
            this._next = next;
        }

        public HttpInput.Interceptor getPrev() {
            return this._prev;
        }

        public HttpInput.Interceptor getNext() {
            return this._next;
        }

        @Override
        public HttpInput.Content readFrom(HttpInput.Content content) {
            return this.getNext().readFrom(this.getPrev().readFrom(content));
        }

        @Override
        public void destroy() {
            if (this._prev instanceof Destroyable) {
                ((Destroyable) this._prev).destroy();
            }
            if (this._next instanceof Destroyable) {
                ((Destroyable) this._next).destroy();
            }
        }
    }

    public static class Content implements Callback {

        protected final ByteBuffer _content;

        public Content(ByteBuffer content) {
            this._content = content;
        }

        public ByteBuffer getByteBuffer() {
            return this._content;
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }

        public int get(byte[] buffer, int offset, int length) {
            length = Math.min(this._content.remaining(), length);
            this._content.get(buffer, offset, length);
            return length;
        }

        public int skip(int length) {
            length = Math.min(this._content.remaining(), length);
            this._content.position(this._content.position() + length);
            return length;
        }

        public boolean hasContent() {
            return this._content.hasRemaining();
        }

        public int remaining() {
            return this._content.remaining();
        }

        public boolean isEmpty() {
            return !this._content.hasRemaining();
        }

        public String toString() {
            return String.format("Content@%x{%s}", this.hashCode(), BufferUtil.toDetailString(this._content));
        }
    }

    protected static class EOFState extends HttpInput.State {
    }

    public static class EofContent extends HttpInput.SentinelContent {

        EofContent(String name) {
            super(name);
        }
    }

    protected class ErrorState extends HttpInput.EOFState {

        final Throwable _error;

        ErrorState(Throwable error) {
            this._error = error;
        }

        @Override
        public Throwable getError() {
            return this._error;
        }

        @Override
        public int noContent() throws IOException {
            if (this._error instanceof IOException) {
                throw (IOException) this._error;
            } else {
                throw new IOException(this._error);
            }
        }

        public String toString() {
            return "ERROR:" + this._error;
        }
    }

    public interface Interceptor {

        HttpInput.Content readFrom(HttpInput.Content var1);
    }

    public static class SentinelContent extends HttpInput.Content {

        private final String _name;

        public SentinelContent(String name) {
            super(BufferUtil.EMPTY_BUFFER);
            this._name = name;
        }

        @Override
        public String toString() {
            return this._name;
        }
    }

    protected abstract static class State {

        public boolean blockForContent(HttpInput in) throws IOException {
            return false;
        }

        public int noContent() throws IOException {
            return -1;
        }

        public Throwable getError() {
            return null;
        }
    }
}
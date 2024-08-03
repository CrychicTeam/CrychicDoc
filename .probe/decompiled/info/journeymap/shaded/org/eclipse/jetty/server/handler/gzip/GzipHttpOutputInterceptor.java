package info.journeymap.shaded.org.eclipse.jetty.server.handler.gzip;

import info.journeymap.shaded.org.eclipse.jetty.http.CompressedContentFormat;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.http.PreEncodedHttpField;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpChannel;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpOutput;
import info.journeymap.shaded.org.eclipse.jetty.server.Response;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.IteratingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.IteratingNestedCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class GzipHttpOutputInterceptor implements HttpOutput.Interceptor {

    public static Logger LOG = Log.getLogger(GzipHttpOutputInterceptor.class);

    private static final byte[] GZIP_HEADER = new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 };

    public static final HttpField VARY_ACCEPT_ENCODING_USER_AGENT = new PreEncodedHttpField(HttpHeader.VARY, HttpHeader.ACCEPT_ENCODING + ", " + HttpHeader.USER_AGENT);

    public static final HttpField VARY_ACCEPT_ENCODING = new PreEncodedHttpField(HttpHeader.VARY, HttpHeader.ACCEPT_ENCODING.asString());

    private final AtomicReference<GzipHttpOutputInterceptor.GZState> _state = new AtomicReference(GzipHttpOutputInterceptor.GZState.MIGHT_COMPRESS);

    private final CRC32 _crc = new CRC32();

    private final GzipFactory _factory;

    private final HttpOutput.Interceptor _interceptor;

    private final HttpChannel _channel;

    private final HttpField _vary;

    private final int _bufferSize;

    private final boolean _syncFlush;

    private Deflater _deflater;

    private ByteBuffer _buffer;

    public GzipHttpOutputInterceptor(GzipFactory factory, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush) {
        this(factory, VARY_ACCEPT_ENCODING_USER_AGENT, channel.getHttpConfiguration().getOutputBufferSize(), channel, next, syncFlush);
    }

    public GzipHttpOutputInterceptor(GzipFactory factory, HttpField vary, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush) {
        this(factory, vary, channel.getHttpConfiguration().getOutputBufferSize(), channel, next, syncFlush);
    }

    public GzipHttpOutputInterceptor(GzipFactory factory, HttpField vary, int bufferSize, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush) {
        this._factory = factory;
        this._channel = channel;
        this._interceptor = next;
        this._vary = vary;
        this._bufferSize = bufferSize;
        this._syncFlush = syncFlush;
    }

    @Override
    public HttpOutput.Interceptor getNextInterceptor() {
        return this._interceptor;
    }

    @Override
    public boolean isOptimizedForDirectBuffers() {
        return false;
    }

    @Override
    public void write(ByteBuffer content, boolean complete, Callback callback) {
        switch((GzipHttpOutputInterceptor.GZState) this._state.get()) {
            case MIGHT_COMPRESS:
                this.commit(content, complete, callback);
                break;
            case NOT_COMPRESSING:
                this._interceptor.write(content, complete, callback);
                return;
            case COMMITTING:
                callback.failed(new WritePendingException());
                break;
            case COMPRESSING:
                this.gzip(content, complete, callback);
                break;
            default:
                callback.failed(new IllegalStateException("state=" + this._state.get()));
        }
    }

    private void addTrailer() {
        int i = this._buffer.limit();
        this._buffer.limit(i + 8);
        int v = (int) this._crc.getValue();
        this._buffer.put(i++, (byte) (v & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 8 & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 16 & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 24 & 0xFF));
        v = this._deflater.getTotalIn();
        this._buffer.put(i++, (byte) (v & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 8 & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 16 & 0xFF));
        this._buffer.put(i++, (byte) (v >>> 24 & 0xFF));
    }

    private void gzip(ByteBuffer content, boolean complete, Callback callback) {
        if (!content.hasRemaining() && !complete) {
            callback.succeeded();
        } else {
            new GzipHttpOutputInterceptor.GzipBufferCB(content, complete, callback).iterate();
        }
    }

    protected void commit(ByteBuffer content, boolean complete, Callback callback) {
        Response response = this._channel.getResponse();
        int sc = response.getStatus();
        if (sc <= 0 || sc >= 200 && sc != 204 && sc != 205 && sc < 300) {
            String ct = response.getContentType();
            if (ct != null) {
                ct = MimeTypes.getContentTypeWithoutCharset(ct);
                if (!this._factory.isMimeTypeGzipable(StringUtil.asciiToLowerCase(ct))) {
                    LOG.debug("{} exclude by mimeType {}", this, ct);
                    this.noCompression();
                    this._interceptor.write(content, complete, callback);
                    return;
                }
            }
            HttpFields fields = response.getHttpFields();
            String ce = fields.get(HttpHeader.CONTENT_ENCODING);
            if (ce != null) {
                LOG.debug("{} exclude by content-encoding {}", this, ce);
                this.noCompression();
                this._interceptor.write(content, complete, callback);
            } else {
                if (this._state.compareAndSet(GzipHttpOutputInterceptor.GZState.MIGHT_COMPRESS, GzipHttpOutputInterceptor.GZState.COMMITTING)) {
                    if (this._vary != null) {
                        if (fields.contains(HttpHeader.VARY)) {
                            fields.addCSV(HttpHeader.VARY, this._vary.getValues());
                        } else {
                            fields.add(this._vary);
                        }
                    }
                    long content_length = response.getContentLength();
                    if (content_length < 0L && complete) {
                        content_length = (long) content.remaining();
                    }
                    this._deflater = this._factory.getDeflater(this._channel.getRequest(), content_length);
                    if (this._deflater == null) {
                        LOG.debug("{} exclude no deflater", this);
                        this._state.set(GzipHttpOutputInterceptor.GZState.NOT_COMPRESSING);
                        this._interceptor.write(content, complete, callback);
                        return;
                    }
                    fields.put(CompressedContentFormat.GZIP._contentEncoding);
                    this._crc.reset();
                    this._buffer = this._channel.getByteBufferPool().acquire(this._bufferSize, false);
                    BufferUtil.fill(this._buffer, GZIP_HEADER, 0, GZIP_HEADER.length);
                    response.setContentLength(-1);
                    String etag = fields.get(HttpHeader.ETAG);
                    if (etag != null) {
                        fields.put(HttpHeader.ETAG, this.etagGzip(etag));
                    }
                    LOG.debug("{} compressing {}", this, this._deflater);
                    this._state.set(GzipHttpOutputInterceptor.GZState.COMPRESSING);
                    this.gzip(content, complete, callback);
                } else {
                    callback.failed(new WritePendingException());
                }
            }
        } else {
            LOG.debug("{} exclude by status {}", this, sc);
            this.noCompression();
            if (sc == 304) {
                String request_etags = (String) this._channel.getRequest().getAttribute("o.e.j.s.h.gzip.GzipHandler.etag");
                String response_etag = response.getHttpFields().get(HttpHeader.ETAG);
                if (request_etags != null && response_etag != null) {
                    String response_etag_gzip = this.etagGzip(response_etag);
                    if (request_etags.contains(response_etag_gzip)) {
                        response.getHttpFields().put(HttpHeader.ETAG, response_etag_gzip);
                    }
                }
            }
            this._interceptor.write(content, complete, callback);
        }
    }

    private String etagGzip(String etag) {
        int end = etag.length() - 1;
        return etag.charAt(end) == '"' ? etag.substring(0, end) + CompressedContentFormat.GZIP._etag + '"' : etag + CompressedContentFormat.GZIP._etag;
    }

    public void noCompression() {
        while (true) {
            switch((GzipHttpOutputInterceptor.GZState) this._state.get()) {
                case MIGHT_COMPRESS:
                    if (!this._state.compareAndSet(GzipHttpOutputInterceptor.GZState.MIGHT_COMPRESS, GzipHttpOutputInterceptor.GZState.NOT_COMPRESSING)) {
                        break;
                    }
                    return;
                case NOT_COMPRESSING:
                    return;
                default:
                    throw new IllegalStateException(((GzipHttpOutputInterceptor.GZState) this._state.get()).toString());
            }
        }
    }

    public void noCompressionIfPossible() {
        while (true) {
            switch((GzipHttpOutputInterceptor.GZState) this._state.get()) {
                case MIGHT_COMPRESS:
                    if (!this._state.compareAndSet(GzipHttpOutputInterceptor.GZState.MIGHT_COMPRESS, GzipHttpOutputInterceptor.GZState.NOT_COMPRESSING)) {
                        break;
                    }
                    return;
                case NOT_COMPRESSING:
                case COMPRESSING:
                    return;
                case COMMITTING:
                default:
                    throw new IllegalStateException(((GzipHttpOutputInterceptor.GZState) this._state.get()).toString());
            }
        }
    }

    public boolean mightCompress() {
        return this._state.get() == GzipHttpOutputInterceptor.GZState.MIGHT_COMPRESS;
    }

    private static enum GZState {

        MIGHT_COMPRESS, NOT_COMPRESSING, COMMITTING, COMPRESSING, FINISHED
    }

    private class GzipBufferCB extends IteratingNestedCallback {

        private ByteBuffer _copy;

        private final ByteBuffer _content;

        private final boolean _last;

        public GzipBufferCB(ByteBuffer content, boolean complete, Callback callback) {
            super(callback);
            this._content = content;
            this._last = complete;
        }

        @Override
        protected IteratingCallback.Action process() throws Exception {
            if (GzipHttpOutputInterceptor.this._deflater == null) {
                return IteratingCallback.Action.SUCCEEDED;
            } else {
                if (GzipHttpOutputInterceptor.this._deflater.needsInput()) {
                    if (BufferUtil.isEmpty(this._content)) {
                        if (GzipHttpOutputInterceptor.this._deflater.finished()) {
                            GzipHttpOutputInterceptor.this._factory.recycle(GzipHttpOutputInterceptor.this._deflater);
                            GzipHttpOutputInterceptor.this._deflater = null;
                            GzipHttpOutputInterceptor.this._channel.getByteBufferPool().release(GzipHttpOutputInterceptor.this._buffer);
                            GzipHttpOutputInterceptor.this._buffer = null;
                            if (this._copy != null) {
                                GzipHttpOutputInterceptor.this._channel.getByteBufferPool().release(this._copy);
                                this._copy = null;
                            }
                            return IteratingCallback.Action.SUCCEEDED;
                        }
                        if (!this._last) {
                            return IteratingCallback.Action.SUCCEEDED;
                        }
                        GzipHttpOutputInterceptor.this._deflater.finish();
                    } else if (this._content.hasArray()) {
                        byte[] array = this._content.array();
                        int off = this._content.arrayOffset() + this._content.position();
                        int len = this._content.remaining();
                        BufferUtil.clear(this._content);
                        GzipHttpOutputInterceptor.this._crc.update(array, off, len);
                        GzipHttpOutputInterceptor.this._deflater.setInput(array, off, len);
                        if (this._last) {
                            GzipHttpOutputInterceptor.this._deflater.finish();
                        }
                    } else {
                        if (this._copy == null) {
                            this._copy = GzipHttpOutputInterceptor.this._channel.getByteBufferPool().acquire(GzipHttpOutputInterceptor.this._bufferSize, false);
                        }
                        BufferUtil.clearToFill(this._copy);
                        int took = BufferUtil.put(this._content, this._copy);
                        BufferUtil.flipToFlush(this._copy, 0);
                        if (took == 0) {
                            throw new IllegalStateException();
                        }
                        byte[] array = this._copy.array();
                        int off = this._copy.arrayOffset() + this._copy.position();
                        int len = this._copy.remaining();
                        GzipHttpOutputInterceptor.this._crc.update(array, off, len);
                        GzipHttpOutputInterceptor.this._deflater.setInput(array, off, len);
                        if (this._last && BufferUtil.isEmpty(this._content)) {
                            GzipHttpOutputInterceptor.this._deflater.finish();
                        }
                    }
                }
                BufferUtil.compact(GzipHttpOutputInterceptor.this._buffer);
                int off = GzipHttpOutputInterceptor.this._buffer.arrayOffset() + GzipHttpOutputInterceptor.this._buffer.limit();
                int len = GzipHttpOutputInterceptor.this._buffer.capacity() - GzipHttpOutputInterceptor.this._buffer.limit() - (this._last ? 8 : 0);
                if (len > 0) {
                    int produced = GzipHttpOutputInterceptor.this._deflater.deflate(GzipHttpOutputInterceptor.this._buffer.array(), off, len, GzipHttpOutputInterceptor.this._syncFlush ? 2 : 0);
                    GzipHttpOutputInterceptor.this._buffer.limit(GzipHttpOutputInterceptor.this._buffer.limit() + produced);
                }
                boolean finished = GzipHttpOutputInterceptor.this._deflater.finished();
                if (finished) {
                    GzipHttpOutputInterceptor.this.addTrailer();
                }
                GzipHttpOutputInterceptor.this._interceptor.write(GzipHttpOutputInterceptor.this._buffer, finished, this);
                return IteratingCallback.Action.SCHEDULED;
            }
        }
    }
}
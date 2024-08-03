package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Supplier;

public class HttpGenerator {

    private static final Logger LOG = Log.getLogger(HttpGenerator.class);

    public static final boolean __STRICT = Boolean.getBoolean("info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator.STRICT");

    private static final byte[] __colon_space = new byte[] { 58, 32 };

    public static final MetaData.Response CONTINUE_100_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 100, null, null, -1L);

    public static final MetaData.Response PROGRESS_102_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 102, null, null, -1L);

    public static final MetaData.Response RESPONSE_500_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 500, null, new HttpFields() {

        {
            this.put(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE);
        }
    }, 0L);

    public static final int CHUNK_SIZE = 12;

    private HttpGenerator.State _state = HttpGenerator.State.START;

    private HttpTokens.EndOfContent _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;

    private long _contentPrepared = 0L;

    private boolean _noContentResponse = false;

    private Boolean _persistent = null;

    private Supplier<HttpFields> _trailers = null;

    private final int _send;

    private static final int SEND_SERVER = 1;

    private static final int SEND_XPOWEREDBY = 2;

    private static final Trie<Boolean> __assumedContentMethods = new ArrayTrie<>(8);

    private boolean _needCRLF = false;

    private static final byte[] ZERO_CHUNK = new byte[] { 48, 13, 10 };

    private static final byte[] LAST_CHUNK = new byte[] { 48, 13, 10, 13, 10 };

    private static final byte[] CONTENT_LENGTH_0 = StringUtil.getBytes("Content-Length: 0\r\n");

    private static final byte[] CONNECTION_CLOSE = StringUtil.getBytes("Connection: close\r\n");

    private static final byte[] HTTP_1_1_SPACE = StringUtil.getBytes(HttpVersion.HTTP_1_1 + " ");

    private static final byte[] TRANSFER_ENCODING_CHUNKED = StringUtil.getBytes("Transfer-Encoding: chunked\r\n");

    private static final byte[][] SEND = new byte[][] { new byte[0], StringUtil.getBytes("Server: Jetty(9.x.x)\r\n"), StringUtil.getBytes("X-Powered-By: Jetty(9.x.x)\r\n"), StringUtil.getBytes("Server: Jetty(9.x.x)\r\nX-Powered-By: Jetty(9.x.x)\r\n") };

    private static final HttpGenerator.PreparedResponse[] __preprepared = new HttpGenerator.PreparedResponse[512];

    public static void setJettyVersion(String serverVersion) {
        SEND[1] = StringUtil.getBytes("Server: " + serverVersion + "\r\n");
        SEND[2] = StringUtil.getBytes("X-Powered-By: " + serverVersion + "\r\n");
        SEND[3] = StringUtil.getBytes("Server: " + serverVersion + "\r\nX-Powered-By: " + serverVersion + "\r\n");
    }

    public HttpGenerator() {
        this(false, false);
    }

    public HttpGenerator(boolean sendServerVersion, boolean sendXPoweredBy) {
        this._send = (sendServerVersion ? 1 : 0) | (sendXPoweredBy ? 2 : 0);
    }

    public void reset() {
        this._state = HttpGenerator.State.START;
        this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
        this._noContentResponse = false;
        this._persistent = null;
        this._contentPrepared = 0L;
        this._needCRLF = false;
        this._trailers = null;
    }

    @Deprecated
    public boolean getSendServerVersion() {
        return (this._send & 1) != 0;
    }

    @Deprecated
    public void setSendServerVersion(boolean sendServerVersion) {
        throw new UnsupportedOperationException();
    }

    public HttpGenerator.State getState() {
        return this._state;
    }

    public boolean isState(HttpGenerator.State state) {
        return this._state == state;
    }

    public boolean isIdle() {
        return this._state == HttpGenerator.State.START;
    }

    public boolean isEnd() {
        return this._state == HttpGenerator.State.END;
    }

    public boolean isCommitted() {
        return this._state.ordinal() >= HttpGenerator.State.COMMITTED.ordinal();
    }

    public boolean isChunking() {
        return this._endOfContent == HttpTokens.EndOfContent.CHUNKED_CONTENT;
    }

    public boolean isNoContent() {
        return this._noContentResponse;
    }

    public void setPersistent(boolean persistent) {
        this._persistent = persistent;
    }

    public boolean isPersistent() {
        return Boolean.TRUE.equals(this._persistent);
    }

    public boolean isWritten() {
        return this._contentPrepared > 0L;
    }

    public long getContentPrepared() {
        return this._contentPrepared;
    }

    public void abort() {
        this._persistent = false;
        this._state = HttpGenerator.State.END;
        this._endOfContent = null;
    }

    public HttpGenerator.Result generateRequest(MetaData.Request info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last) throws IOException {
        switch(this._state) {
            case START:
                if (info == null) {
                    return HttpGenerator.Result.NEED_INFO;
                } else if (header == null) {
                    return HttpGenerator.Result.NEED_HEADER;
                } else {
                    if (this._persistent == null) {
                        this._persistent = info.getHttpVersion().ordinal() > HttpVersion.HTTP_1_0.ordinal();
                        if (!this._persistent && HttpMethod.CONNECT.is(info.getMethod())) {
                            this._persistent = true;
                        }
                    }
                    int pos = BufferUtil.flipToFill(header);
                    HttpGenerator.Result var18;
                    try {
                        this.generateRequestLine(info, header);
                        if (info.getHttpVersion() == HttpVersion.HTTP_0_9) {
                            throw new BadMessageException(500, "HTTP/0.9 not supported");
                        }
                        this.generateHeaders(info, header, content, last);
                        boolean expect100 = info.getFields().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
                        if (expect100) {
                            this._state = HttpGenerator.State.COMMITTED;
                        } else {
                            int len = BufferUtil.length(content);
                            if (len > 0) {
                                this._contentPrepared += (long) len;
                                if (this.isChunking()) {
                                    this.prepareChunk(header, len);
                                }
                            }
                            this._state = last ? HttpGenerator.State.COMPLETING : HttpGenerator.State.COMMITTED;
                        }
                        var18 = HttpGenerator.Result.FLUSH;
                    } catch (BadMessageException var14) {
                        throw var14;
                    } catch (BufferOverflowException var15) {
                        throw new BadMessageException(500, "Request header too large", var15);
                    } catch (Exception var16) {
                        throw new BadMessageException(500, var16.getMessage(), var16);
                    } finally {
                        BufferUtil.flipToFlush(header, pos);
                    }
                    return var18;
                }
            case COMMITTED:
                return this.committed(chunk, content, last);
            case COMPLETING:
                return this.completing(chunk, content);
            case END:
                if (BufferUtil.hasContent(content)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("discarding content in COMPLETING");
                    }
                    BufferUtil.clear(content);
                }
                return HttpGenerator.Result.DONE;
            default:
                throw new IllegalStateException();
        }
    }

    private HttpGenerator.Result committed(ByteBuffer chunk, ByteBuffer content, boolean last) {
        int len = BufferUtil.length(content);
        if (len > 0) {
            if (this.isChunking()) {
                if (chunk == null) {
                    return HttpGenerator.Result.NEED_CHUNK;
                }
                BufferUtil.clearToFill(chunk);
                this.prepareChunk(chunk, len);
                BufferUtil.flipToFlush(chunk, 0);
            }
            this._contentPrepared += (long) len;
        }
        if (last) {
            this._state = HttpGenerator.State.COMPLETING;
            return len > 0 ? HttpGenerator.Result.FLUSH : HttpGenerator.Result.CONTINUE;
        } else {
            return len > 0 ? HttpGenerator.Result.FLUSH : HttpGenerator.Result.DONE;
        }
    }

    private HttpGenerator.Result completing(ByteBuffer chunk, ByteBuffer content) {
        if (BufferUtil.hasContent(content)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("discarding content in COMPLETING");
            }
            BufferUtil.clear(content);
        }
        if (!this.isChunking()) {
            this._state = HttpGenerator.State.END;
            return Boolean.TRUE.equals(this._persistent) ? HttpGenerator.Result.DONE : HttpGenerator.Result.SHUTDOWN_OUT;
        } else {
            if (this._trailers != null) {
                if (chunk == null || chunk.capacity() <= 12) {
                    return HttpGenerator.Result.NEED_CHUNK_TRAILER;
                }
                HttpFields trailers = (HttpFields) this._trailers.get();
                if (trailers != null) {
                    BufferUtil.clearToFill(chunk);
                    this.generateTrailers(chunk, trailers);
                    BufferUtil.flipToFlush(chunk, 0);
                    this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
                    return HttpGenerator.Result.FLUSH;
                }
            }
            if (chunk == null) {
                return HttpGenerator.Result.NEED_CHUNK;
            } else {
                BufferUtil.clearToFill(chunk);
                this.prepareChunk(chunk, 0);
                BufferUtil.flipToFlush(chunk, 0);
                this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
                return HttpGenerator.Result.FLUSH;
            }
        }
    }

    @Deprecated
    public HttpGenerator.Result generateResponse(MetaData.Response info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last) throws IOException {
        return this.generateResponse(info, false, header, chunk, content, last);
    }

    public HttpGenerator.Result generateResponse(MetaData.Response info, boolean head, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last) throws IOException {
        switch(this._state) {
            case START:
                if (info == null) {
                    return HttpGenerator.Result.NEED_INFO;
                } else {
                    HttpVersion version = info.getHttpVersion();
                    if (version == null) {
                        throw new BadMessageException(500, "No version");
                    } else {
                        switch(version) {
                            case HTTP_1_0:
                                if (this._persistent == null) {
                                    this._persistent = Boolean.FALSE;
                                }
                                break;
                            case HTTP_1_1:
                                if (this._persistent == null) {
                                    this._persistent = Boolean.TRUE;
                                }
                                break;
                            default:
                                this._persistent = false;
                                this._endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
                                if (BufferUtil.hasContent(content)) {
                                    this._contentPrepared = this._contentPrepared + (long) content.remaining();
                                }
                                this._state = last ? HttpGenerator.State.COMPLETING : HttpGenerator.State.COMMITTED;
                                return HttpGenerator.Result.FLUSH;
                        }
                        if (header == null) {
                            return HttpGenerator.Result.NEED_HEADER;
                        } else {
                            int pos = BufferUtil.flipToFill(header);
                            try {
                                this.generateResponseLine(info, header);
                                int status = info.getStatus();
                                if (status >= 100 && status < 200) {
                                    this._noContentResponse = true;
                                    if (status != 101) {
                                        header.put(HttpTokens.CRLF);
                                        this._state = HttpGenerator.State.COMPLETING_1XX;
                                        return HttpGenerator.Result.FLUSH;
                                    }
                                } else if (status == 204 || status == 304) {
                                    this._noContentResponse = true;
                                }
                                this.generateHeaders(info, header, content, last);
                                int len = BufferUtil.length(content);
                                if (len > 0) {
                                    this._contentPrepared += (long) len;
                                    if (this.isChunking() && !head) {
                                        this.prepareChunk(header, len);
                                    }
                                }
                                this._state = last ? HttpGenerator.State.COMPLETING : HttpGenerator.State.COMMITTED;
                                return HttpGenerator.Result.FLUSH;
                            } catch (BadMessageException var16) {
                                throw var16;
                            } catch (BufferOverflowException var17) {
                                throw new BadMessageException(500, "Request header too large", var17);
                            } catch (Exception var18) {
                                throw new BadMessageException(500, var18.getMessage(), var18);
                            } finally {
                                BufferUtil.flipToFlush(header, pos);
                            }
                        }
                    }
                }
            case COMMITTED:
                return this.committed(chunk, content, last);
            case COMPLETING:
                return this.completing(chunk, content);
            case END:
                if (BufferUtil.hasContent(content)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("discarding content in COMPLETING");
                    }
                    BufferUtil.clear(content);
                }
                return HttpGenerator.Result.DONE;
            case COMPLETING_1XX:
                this.reset();
                return HttpGenerator.Result.DONE;
            default:
                throw new IllegalStateException();
        }
    }

    private void prepareChunk(ByteBuffer chunk, int remaining) {
        if (this._needCRLF) {
            BufferUtil.putCRLF(chunk);
        }
        if (remaining > 0) {
            BufferUtil.putHexInt(chunk, remaining);
            BufferUtil.putCRLF(chunk);
            this._needCRLF = true;
        } else {
            chunk.put(LAST_CHUNK);
            this._needCRLF = false;
        }
    }

    private void generateTrailers(ByteBuffer buffer, HttpFields trailer) {
        if (this._needCRLF) {
            BufferUtil.putCRLF(buffer);
        }
        buffer.put(ZERO_CHUNK);
        int n = trailer.size();
        for (int f = 0; f < n; f++) {
            HttpField field = trailer.getField(f);
            String v = field.getValue();
            if (v != null && v.length() != 0) {
                putTo(field, buffer);
            }
        }
        BufferUtil.putCRLF(buffer);
    }

    private void generateRequestLine(MetaData.Request request, ByteBuffer header) {
        header.put(StringUtil.getBytes(request.getMethod()));
        header.put((byte) 32);
        header.put(StringUtil.getBytes(request.getURIString()));
        header.put((byte) 32);
        header.put(request.getHttpVersion().toBytes());
        header.put(HttpTokens.CRLF);
    }

    private void generateResponseLine(MetaData.Response response, ByteBuffer header) {
        int status = response.getStatus();
        HttpGenerator.PreparedResponse preprepared = status < __preprepared.length ? __preprepared[status] : null;
        String reason = response.getReason();
        if (preprepared != null) {
            if (reason == null) {
                header.put(preprepared._responseLine);
            } else {
                header.put(preprepared._schemeCode);
                header.put(this.getReasonBytes(reason));
                header.put(HttpTokens.CRLF);
            }
        } else {
            header.put(HTTP_1_1_SPACE);
            header.put((byte) (48 + status / 100));
            header.put((byte) (48 + status % 100 / 10));
            header.put((byte) (48 + status % 10));
            header.put((byte) 32);
            if (reason == null) {
                header.put((byte) (48 + status / 100));
                header.put((byte) (48 + status % 100 / 10));
                header.put((byte) (48 + status % 10));
            } else {
                header.put(this.getReasonBytes(reason));
            }
            header.put(HttpTokens.CRLF);
        }
    }

    private byte[] getReasonBytes(String reason) {
        if (reason.length() > 1024) {
            reason = reason.substring(0, 1024);
        }
        byte[] _bytes = StringUtil.getBytes(reason);
        int i = _bytes.length;
        while (i-- > 0) {
            if (_bytes[i] == 13 || _bytes[i] == 10) {
                _bytes[i] = 63;
            }
        }
        return _bytes;
    }

    private void generateHeaders(MetaData info, ByteBuffer header, ByteBuffer content, boolean last) {
        MetaData.Request request = info instanceof MetaData.Request ? (MetaData.Request) info : null;
        MetaData.Response response = info instanceof MetaData.Response ? (MetaData.Response) info : null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("generateHeaders {} last={} content={}", info, last, BufferUtil.toDetailString(content));
            LOG.debug(info.getFields().toString());
        }
        int send = this._send;
        HttpField transfer_encoding = null;
        boolean http11 = info.getHttpVersion() == HttpVersion.HTTP_1_1;
        boolean close = false;
        this._trailers = http11 ? info.getTrailerSupplier() : null;
        boolean chunked_hint = this._trailers != null;
        boolean content_type = false;
        long content_length = info.getContentLength();
        boolean content_length_field = false;
        HttpFields fields = info.getFields();
        if (fields != null) {
            int n = fields.size();
            for (int f = 0; f < n; f++) {
                HttpField field = fields.getField(f);
                String v = field.getValue();
                if (v != null && v.length() != 0) {
                    HttpHeader h = field.getHeader();
                    if (h == null) {
                        putTo(field, header);
                    } else {
                        switch(h) {
                            case CONTENT_LENGTH:
                                if (content_length < 0L) {
                                    content_length = field.getLongValue();
                                } else if (content_length != field.getLongValue()) {
                                    throw new BadMessageException(500, String.format("Incorrect Content-Length %d!=%d", content_length, field.getLongValue()));
                                }
                                content_length_field = true;
                                break;
                            case CONTENT_TYPE:
                                content_type = true;
                                putTo(field, header);
                                break;
                            case TRANSFER_ENCODING:
                                if (http11) {
                                    transfer_encoding = field;
                                    chunked_hint = field.contains(HttpHeaderValue.CHUNKED.asString());
                                }
                                break;
                            case CONNECTION:
                                putTo(field, header);
                                if (field.contains(HttpHeaderValue.CLOSE.asString())) {
                                    close = true;
                                    this._persistent = false;
                                }
                                if (!http11 && field.contains(HttpHeaderValue.KEEP_ALIVE.asString())) {
                                    this._persistent = true;
                                }
                                break;
                            case SERVER:
                                send &= -2;
                                putTo(field, header);
                                break;
                            default:
                                putTo(field, header);
                        }
                    }
                }
            }
        }
        if (last && content_length < 0L && this._trailers == null) {
            content_length = this._contentPrepared + (long) BufferUtil.length(content);
        }
        boolean assumed_content_request = request != null && Boolean.TRUE.equals(__assumedContentMethods.get(request.getMethod()));
        boolean assumed_content = assumed_content_request || content_type || chunked_hint;
        boolean nocontent_request = request != null && content_length <= 0L && !assumed_content;
        if (!this._noContentResponse && !nocontent_request) {
            if (!http11 || !chunked_hint && (content_length >= 0L || !this._persistent && !assumed_content_request)) {
                if (content_length < 0L || request == null && !this._persistent) {
                    if (response == null) {
                        throw new BadMessageException(500, "Unknown content length for request");
                    }
                    this._endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
                    this._persistent = false;
                    if (content_length >= 0L && (content_length > 0L || assumed_content || content_length_field)) {
                        putContentLength(header, content_length);
                    }
                    if (http11 && !close) {
                        header.put(CONNECTION_CLOSE);
                    }
                } else {
                    this._endOfContent = HttpTokens.EndOfContent.CONTENT_LENGTH;
                    putContentLength(header, content_length);
                }
            } else {
                this._endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
                if (transfer_encoding == null) {
                    header.put(TRANSFER_ENCODING_CHUNKED);
                } else if (transfer_encoding.toString().endsWith(HttpHeaderValue.CHUNKED.toString())) {
                    putTo(transfer_encoding, header);
                    transfer_encoding = null;
                } else {
                    if (chunked_hint) {
                        throw new BadMessageException(500, "Bad Transfer-Encoding");
                    }
                    putTo(new HttpField(HttpHeader.TRANSFER_ENCODING, transfer_encoding.getValue() + ",chunked"), header);
                    transfer_encoding = null;
                }
            }
        } else {
            this._endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
            if (this._contentPrepared > 0L || content_length > 0L) {
                if (this._contentPrepared != 0L || !last) {
                    throw new BadMessageException(500, "Content for no content response");
                }
                content.clear();
                content_length = 0L;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(this._endOfContent.toString());
        }
        if (transfer_encoding != null) {
            if (chunked_hint) {
                String v = transfer_encoding.getValue();
                int c = v.lastIndexOf(44);
                if (c > 0 && v.lastIndexOf(HttpHeaderValue.CHUNKED.toString(), c) > c) {
                    putTo(new HttpField(HttpHeader.TRANSFER_ENCODING, v.substring(0, c).trim()), header);
                }
            } else {
                putTo(transfer_encoding, header);
            }
        }
        int status = response != null ? response.getStatus() : -1;
        if (status > 199) {
            header.put(SEND[send]);
        }
        header.put(HttpTokens.CRLF);
    }

    private static void putContentLength(ByteBuffer header, long contentLength) {
        if (contentLength == 0L) {
            header.put(CONTENT_LENGTH_0);
        } else {
            header.put(HttpHeader.CONTENT_LENGTH.getBytesColonSpace());
            BufferUtil.putDecLong(header, contentLength);
            header.put(HttpTokens.CRLF);
        }
    }

    public static byte[] getReasonBuffer(int code) {
        HttpGenerator.PreparedResponse status = code < __preprepared.length ? __preprepared[code] : null;
        return status != null ? status._reason : null;
    }

    public String toString() {
        return String.format("%s@%x{s=%s}", this.getClass().getSimpleName(), this.hashCode(), this._state);
    }

    private static void putSanitisedName(String s, ByteBuffer buffer) {
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255 && c != '\r' && c != '\n' && c != ':') {
                buffer.put((byte) (255 & c));
            } else {
                buffer.put((byte) 63);
            }
        }
    }

    private static void putSanitisedValue(String s, ByteBuffer buffer) {
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255 && c != '\r' && c != '\n') {
                buffer.put((byte) (255 & c));
            } else {
                buffer.put((byte) 32);
            }
        }
    }

    public static void putTo(HttpField field, ByteBuffer bufferInFillMode) {
        if (field instanceof PreEncodedHttpField) {
            ((PreEncodedHttpField) field).putTo(bufferInFillMode, HttpVersion.HTTP_1_0);
        } else {
            HttpHeader header = field.getHeader();
            if (header != null) {
                bufferInFillMode.put(header.getBytesColonSpace());
                putSanitisedValue(field.getValue(), bufferInFillMode);
            } else {
                putSanitisedName(field.getName(), bufferInFillMode);
                bufferInFillMode.put(__colon_space);
                putSanitisedValue(field.getValue(), bufferInFillMode);
            }
            BufferUtil.putCRLF(bufferInFillMode);
        }
    }

    public static void putTo(HttpFields fields, ByteBuffer bufferInFillMode) {
        for (HttpField field : fields) {
            if (field != null) {
                putTo(field, bufferInFillMode);
            }
        }
        BufferUtil.putCRLF(bufferInFillMode);
    }

    static {
        __assumedContentMethods.put(HttpMethod.POST.asString(), Boolean.TRUE);
        __assumedContentMethods.put(HttpMethod.PUT.asString(), Boolean.TRUE);
        int versionLength = HttpVersion.HTTP_1_1.toString().length();
        for (int i = 0; i < __preprepared.length; i++) {
            HttpStatus.Code code = HttpStatus.getCode(i);
            if (code != null) {
                String reason = code.getMessage();
                byte[] line = new byte[versionLength + 5 + reason.length() + 2];
                HttpVersion.HTTP_1_1.toBuffer().get(line, 0, versionLength);
                line[versionLength + 0] = 32;
                line[versionLength + 1] = (byte) (48 + i / 100);
                line[versionLength + 2] = (byte) (48 + i % 100 / 10);
                line[versionLength + 3] = (byte) (48 + i % 10);
                line[versionLength + 4] = 32;
                for (int j = 0; j < reason.length(); j++) {
                    line[versionLength + 5 + j] = (byte) reason.charAt(j);
                }
                line[versionLength + 5 + reason.length()] = 13;
                line[versionLength + 6 + reason.length()] = 10;
                __preprepared[i] = new HttpGenerator.PreparedResponse();
                __preprepared[i]._schemeCode = Arrays.copyOfRange(line, 0, versionLength + 5);
                __preprepared[i]._reason = Arrays.copyOfRange(line, versionLength + 5, line.length - 2);
                __preprepared[i]._responseLine = line;
            }
        }
    }

    private static class PreparedResponse {

        byte[] _reason;

        byte[] _schemeCode;

        byte[] _responseLine;

        private PreparedResponse() {
        }
    }

    public static enum Result {

        NEED_CHUNK,
        NEED_INFO,
        NEED_HEADER,
        NEED_CHUNK_TRAILER,
        FLUSH,
        CONTINUE,
        SHUTDOWN_OUT,
        DONE
    }

    public static enum State {

        START, COMMITTED, COMPLETING, COMPLETING_1XX, END
    }
}
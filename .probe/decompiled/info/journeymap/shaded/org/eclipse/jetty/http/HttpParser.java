package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTernaryTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Utf8StringBuilder;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;

public class HttpParser {

    public static final Logger LOG = Log.getLogger(HttpParser.class);

    @Deprecated
    public static final String __STRICT = "info.journeymap.shaded.org.eclipse.jetty.http.HttpParser.STRICT";

    public static final int INITIAL_URI_LENGTH = 256;

    public static final Trie<HttpField> CACHE = new ArrayTrie<>(2048);

    private static final EnumSet<HttpParser.State> __idleStates = EnumSet.of(HttpParser.State.START, HttpParser.State.END, HttpParser.State.CLOSE, HttpParser.State.CLOSED);

    private static final EnumSet<HttpParser.State> __completeStates = EnumSet.of(HttpParser.State.END, HttpParser.State.CLOSE, HttpParser.State.CLOSED);

    private final boolean DEBUG = LOG.isDebugEnabled();

    private final HttpParser.HttpHandler _handler;

    private final HttpParser.RequestHandler _requestHandler;

    private final HttpParser.ResponseHandler _responseHandler;

    private final HttpParser.ComplianceHandler _complianceHandler;

    private final int _maxHeaderBytes;

    private final HttpCompliance _compliance;

    private HttpField _field;

    private HttpHeader _header;

    private String _headerString;

    private HttpHeaderValue _value;

    private String _valueString;

    private int _responseStatus;

    private int _headerBytes;

    private boolean _host;

    private boolean _headerComplete;

    private volatile HttpParser.State _state = HttpParser.State.START;

    private volatile HttpParser.FieldState _fieldState = HttpParser.FieldState.FIELD;

    private volatile boolean _eof;

    private HttpMethod _method;

    private String _methodString;

    private HttpVersion _version;

    private Utf8StringBuilder _uri = new Utf8StringBuilder(256);

    private HttpTokens.EndOfContent _endOfContent;

    private long _contentLength;

    private long _contentPosition;

    private int _chunkLength;

    private int _chunkPosition;

    private boolean _headResponse;

    private boolean _cr;

    private ByteBuffer _contentChunk;

    private Trie<HttpField> _connectionFields;

    private int _length;

    private final StringBuilder _string = new StringBuilder();

    private static final HttpParser.CharState[] __charState;

    private static HttpCompliance compliance() {
        Boolean strict = Boolean.getBoolean("info.journeymap.shaded.org.eclipse.jetty.http.HttpParser.STRICT");
        return strict ? HttpCompliance.LEGACY : HttpCompliance.RFC7230;
    }

    public HttpParser(HttpParser.RequestHandler handler) {
        this(handler, -1, compliance());
    }

    public HttpParser(HttpParser.ResponseHandler handler) {
        this(handler, -1, compliance());
    }

    public HttpParser(HttpParser.RequestHandler handler, int maxHeaderBytes) {
        this(handler, maxHeaderBytes, compliance());
    }

    public HttpParser(HttpParser.ResponseHandler handler, int maxHeaderBytes) {
        this(handler, maxHeaderBytes, compliance());
    }

    @Deprecated
    public HttpParser(HttpParser.RequestHandler handler, int maxHeaderBytes, boolean strict) {
        this(handler, maxHeaderBytes, strict ? HttpCompliance.LEGACY : compliance());
    }

    @Deprecated
    public HttpParser(HttpParser.ResponseHandler handler, int maxHeaderBytes, boolean strict) {
        this(handler, maxHeaderBytes, strict ? HttpCompliance.LEGACY : compliance());
    }

    public HttpParser(HttpParser.RequestHandler handler, HttpCompliance compliance) {
        this(handler, -1, compliance);
    }

    public HttpParser(HttpParser.RequestHandler handler, int maxHeaderBytes, HttpCompliance compliance) {
        this._handler = handler;
        this._requestHandler = handler;
        this._responseHandler = null;
        this._maxHeaderBytes = maxHeaderBytes;
        this._compliance = compliance == null ? compliance() : compliance;
        this._complianceHandler = (HttpParser.ComplianceHandler) (handler instanceof HttpParser.ComplianceHandler ? handler : null);
    }

    public HttpParser(HttpParser.ResponseHandler handler, int maxHeaderBytes, HttpCompliance compliance) {
        this._handler = handler;
        this._requestHandler = null;
        this._responseHandler = handler;
        this._maxHeaderBytes = maxHeaderBytes;
        this._compliance = compliance == null ? compliance() : compliance;
        this._complianceHandler = (HttpParser.ComplianceHandler) (handler instanceof HttpParser.ComplianceHandler ? handler : null);
    }

    public HttpParser.HttpHandler getHandler() {
        return this._handler;
    }

    protected boolean complianceViolation(HttpCompliance compliance, String reason) {
        if (this._complianceHandler == null) {
            return this._compliance.ordinal() >= compliance.ordinal();
        } else if (this._compliance.ordinal() < compliance.ordinal()) {
            this._complianceHandler.onComplianceViolation(this._compliance, compliance, reason);
            return false;
        } else {
            return true;
        }
    }

    protected String legacyString(String orig, String cached) {
        return this._compliance == HttpCompliance.LEGACY && !orig.equals(cached) && !this.complianceViolation(HttpCompliance.RFC2616, "case sensitive") ? orig : cached;
    }

    public long getContentLength() {
        return this._contentLength;
    }

    public long getContentRead() {
        return this._contentPosition;
    }

    public void setHeadResponse(boolean head) {
        this._headResponse = head;
    }

    protected void setResponseStatus(int status) {
        this._responseStatus = status;
    }

    public HttpParser.State getState() {
        return this._state;
    }

    public boolean inContentState() {
        return this._state.ordinal() >= HttpParser.State.CONTENT.ordinal() && this._state.ordinal() < HttpParser.State.END.ordinal();
    }

    public boolean inHeaderState() {
        return this._state.ordinal() < HttpParser.State.CONTENT.ordinal();
    }

    public boolean isChunking() {
        return this._endOfContent == HttpTokens.EndOfContent.CHUNKED_CONTENT;
    }

    public boolean isStart() {
        return this.isState(HttpParser.State.START);
    }

    public boolean isClose() {
        return this.isState(HttpParser.State.CLOSE);
    }

    public boolean isClosed() {
        return this.isState(HttpParser.State.CLOSED);
    }

    public boolean isIdle() {
        return __idleStates.contains(this._state);
    }

    public boolean isComplete() {
        return __completeStates.contains(this._state);
    }

    public boolean isState(HttpParser.State state) {
        return this._state == state;
    }

    private byte next(ByteBuffer buffer) {
        byte ch = buffer.get();
        HttpParser.CharState s = __charState[255 & ch];
        switch(s) {
            case ILLEGAL:
                throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
            case LF:
                this._cr = false;
                break;
            case CR:
                if (this._cr) {
                    throw new BadMessageException("Bad EOL");
                }
                this._cr = true;
                if (!buffer.hasRemaining()) {
                    return 0;
                }
                if (this._maxHeaderBytes > 0 && (this._state == HttpParser.State.HEADER || this._state == HttpParser.State.TRAILER)) {
                    this._headerBytes++;
                }
                return this.next(buffer);
            case LEGAL:
                if (this._cr) {
                    throw new BadMessageException("Bad EOL");
                }
        }
        return ch;
    }

    private boolean quickStart(ByteBuffer buffer) {
        if (this._requestHandler != null) {
            this._method = HttpMethod.lookAheadGet(buffer);
            if (this._method != null) {
                this._methodString = this._method.asString();
                buffer.position(buffer.position() + this._methodString.length() + 1);
                this.setState(HttpParser.State.SPACE1);
                return false;
            }
        } else if (this._responseHandler != null) {
            this._version = HttpVersion.lookAheadGet(buffer);
            if (this._version != null) {
                buffer.position(buffer.position() + this._version.asString().length() + 1);
                this.setState(HttpParser.State.SPACE1);
                return false;
            }
        }
        while (this._state == HttpParser.State.START && buffer.hasRemaining()) {
            int ch = this.next(buffer);
            if (ch > 32) {
                this._string.setLength(0);
                this._string.append((char) ch);
                this.setState(this._requestHandler != null ? HttpParser.State.METHOD : HttpParser.State.RESPONSE_VERSION);
                return false;
            }
            if (ch == 0) {
                break;
            }
            if (ch < 0) {
                throw new BadMessageException();
            }
            if (this._maxHeaderBytes > 0 && ++this._headerBytes > this._maxHeaderBytes) {
                LOG.warn("padding is too large >" + this._maxHeaderBytes);
                throw new BadMessageException(400);
            }
        }
        return false;
    }

    private void setString(String s) {
        this._string.setLength(0);
        this._string.append(s);
        this._length = s.length();
    }

    private String takeString() {
        this._string.setLength(this._length);
        String s = this._string.toString();
        this._string.setLength(0);
        this._length = -1;
        return s;
    }

    private boolean handleHeaderContentMessage() {
        boolean handle_header = this._handler.headerComplete();
        this._headerComplete = true;
        boolean handle_content = this._handler.contentComplete();
        boolean handle_message = this._handler.messageComplete();
        return handle_header || handle_content || handle_message;
    }

    private boolean handleContentMessage() {
        boolean handle_content = this._handler.contentComplete();
        boolean handle_message = this._handler.messageComplete();
        return handle_content || handle_message;
    }

    private boolean parseLine(ByteBuffer buffer) {
        boolean handle = false;
        while (this._state.ordinal() < HttpParser.State.HEADER.ordinal() && buffer.hasRemaining() && !handle) {
            byte ch = this.next(buffer);
            if (ch != 0) {
                if (this._maxHeaderBytes > 0 && ++this._headerBytes > this._maxHeaderBytes) {
                    if (this._state == HttpParser.State.URI) {
                        LOG.warn("URI is too large >" + this._maxHeaderBytes);
                        throw new BadMessageException(414);
                    }
                    if (this._requestHandler != null) {
                        LOG.warn("request is too large >" + this._maxHeaderBytes);
                    } else {
                        LOG.warn("response is too large >" + this._maxHeaderBytes);
                    }
                    throw new BadMessageException(431);
                }
                switch(this._state) {
                    case METHOD:
                        if (ch == 32) {
                            this._length = this._string.length();
                            this._methodString = this.takeString();
                            HttpMethod method = HttpMethod.CACHE.get(this._methodString);
                            if (method != null) {
                                this._methodString = this.legacyString(this._methodString, method.asString());
                            }
                            this.setState(HttpParser.State.SPACE1);
                        } else {
                            if (ch < 32) {
                                if (ch == 10) {
                                    throw new BadMessageException("No URI");
                                }
                                throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
                            }
                            this._string.append((char) ch);
                        }
                        continue;
                    case RESPONSE_VERSION:
                        if (ch == 32) {
                            this._length = this._string.length();
                            String version = this.takeString();
                            this._version = HttpVersion.CACHE.get(version);
                            if (this._version == null) {
                                throw new BadMessageException(400, "Unknown Version");
                            }
                            this.setState(HttpParser.State.SPACE1);
                        } else {
                            if (ch < 32) {
                                throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
                            }
                            this._string.append((char) ch);
                        }
                        continue;
                    case SPACE1:
                        if (ch <= 32 && ch >= 0) {
                            if (ch < 32) {
                                throw new BadMessageException(400, this._requestHandler != null ? "No URI" : "No Status");
                            }
                        } else if (this._responseHandler != null) {
                            this.setState(HttpParser.State.STATUS);
                            this.setResponseStatus(ch - 48);
                        } else {
                            this._uri.reset();
                            this.setState(HttpParser.State.URI);
                            if (!buffer.hasArray()) {
                                this._uri.append(ch);
                                continue;
                            }
                            byte[] array = buffer.array();
                            int p = buffer.arrayOffset() + buffer.position();
                            int l = buffer.arrayOffset() + buffer.limit();
                            int i = p;
                            while (i < l && array[i] > 32) {
                                i++;
                            }
                            int len = i - p;
                            this._headerBytes += len;
                            if (this._maxHeaderBytes > 0 && ++this._headerBytes > this._maxHeaderBytes) {
                                LOG.warn("URI is too large >" + this._maxHeaderBytes);
                                throw new BadMessageException(414);
                            }
                            this._uri.append(array, p - 1, len + 1);
                            buffer.position(i - buffer.arrayOffset());
                        }
                        continue;
                    case STATUS:
                        if (ch == 32) {
                            this.setState(HttpParser.State.SPACE2);
                        } else if (ch >= 48 && ch <= 57) {
                            this._responseStatus = this._responseStatus * 10 + (ch - 48);
                        } else {
                            if (ch < 32 && ch >= 0) {
                                this.setState(HttpParser.State.HEADER);
                                handle = this._responseHandler.startResponse(this._version, this._responseStatus, null) || handle;
                                continue;
                            }
                            throw new BadMessageException();
                        }
                        continue;
                    case URI:
                        if (ch == 32) {
                            this.setState(HttpParser.State.SPACE2);
                        } else {
                            if (ch < 32 && ch >= 0) {
                                if (this.complianceViolation(HttpCompliance.RFC7230, "HTTP/0.9")) {
                                    throw new BadMessageException("HTTP/0.9 not supported");
                                }
                                handle = this._requestHandler.startRequest(this._methodString, this._uri.toString(), HttpVersion.HTTP_0_9);
                                this.setState(HttpParser.State.END);
                                BufferUtil.clear(buffer);
                                handle = this.handleHeaderContentMessage() || handle;
                                continue;
                            }
                            this._uri.append(ch);
                        }
                        continue;
                    case SPACE2:
                        if (ch > 32) {
                            this._string.setLength(0);
                            this._string.append((char) ch);
                            if (this._responseHandler != null) {
                                this._length = 1;
                                this.setState(HttpParser.State.REASON);
                            } else {
                                this.setState(HttpParser.State.REQUEST_VERSION);
                                HttpVersion version;
                                if (buffer.position() > 0 && buffer.hasArray()) {
                                    version = HttpVersion.lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position() - 1, buffer.arrayOffset() + buffer.limit());
                                } else {
                                    version = HttpVersion.CACHE.getBest(buffer, 0, buffer.remaining());
                                }
                                if (version != null) {
                                    int pos = buffer.position() + version.asString().length() - 1;
                                    if (pos < buffer.limit()) {
                                        byte n = buffer.get(pos);
                                        if (n == 13) {
                                            this._cr = true;
                                            this._version = version;
                                            this._string.setLength(0);
                                            buffer.position(pos + 1);
                                        } else if (n == 10) {
                                            this._version = version;
                                            this._string.setLength(0);
                                            buffer.position(pos);
                                        }
                                    }
                                }
                            }
                        } else if (ch != 10) {
                            if (ch < 0) {
                                throw new BadMessageException();
                            }
                        } else {
                            if (this._responseHandler != null) {
                                this.setState(HttpParser.State.HEADER);
                                handle = this._responseHandler.startResponse(this._version, this._responseStatus, null) || handle;
                                continue;
                            }
                            if (this.complianceViolation(HttpCompliance.RFC7230, "HTTP/0.9")) {
                                throw new BadMessageException("HTTP/0.9 not supported");
                            }
                            handle = this._requestHandler.startRequest(this._methodString, this._uri.toString(), HttpVersion.HTTP_0_9);
                            this.setState(HttpParser.State.END);
                            BufferUtil.clear(buffer);
                            handle = this.handleHeaderContentMessage() || handle;
                        }
                        continue;
                    case REQUEST_VERSION:
                        if (ch == 10) {
                            if (this._version == null) {
                                this._length = this._string.length();
                                this._version = HttpVersion.CACHE.get(this.takeString());
                            }
                            if (this._version == null) {
                                throw new BadMessageException(400, "Unknown Version");
                            }
                            if (this._connectionFields == null && this._version.getVersion() >= HttpVersion.HTTP_1_1.getVersion() && this._handler.getHeaderCacheSize() > 0) {
                                int header_cache = this._handler.getHeaderCacheSize();
                                this._connectionFields = new ArrayTernaryTrie<>(header_cache);
                            }
                            this.setState(HttpParser.State.HEADER);
                            handle = this._requestHandler.startRequest(this._methodString, this._uri.toString(), this._version) || handle;
                            continue;
                        }
                        if (ch < 32) {
                            throw new BadMessageException();
                        }
                        this._string.append((char) ch);
                        continue;
                    case REASON:
                        if (ch != 10) {
                            if (ch < 32) {
                                throw new BadMessageException();
                            }
                            this._string.append((char) ch);
                            if (ch != 32 && ch != 9) {
                                this._length = this._string.length();
                            }
                            continue;
                        }
                        String reason = this.takeString();
                        this.setState(HttpParser.State.HEADER);
                        handle = this._responseHandler.startResponse(this._version, this._responseStatus, reason) || handle;
                        continue;
                    default:
                        throw new IllegalStateException(this._state.toString());
                }
            }
            break;
        }
        return handle;
    }

    private void parsedHeader() {
        if (this._headerString != null || this._valueString != null) {
            if (this._header != null) {
                boolean add_to_connection_trie = false;
                switch(this._header) {
                    case CONTENT_LENGTH:
                        if (this._endOfContent == HttpTokens.EndOfContent.CONTENT_LENGTH) {
                            throw new BadMessageException(400, "Duplicate Content-Length");
                        }
                        if (this._endOfContent != HttpTokens.EndOfContent.CHUNKED_CONTENT) {
                            this._contentLength = this.convertContentLength(this._valueString);
                            if (this._contentLength <= 0L) {
                                this._endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
                            } else {
                                this._endOfContent = HttpTokens.EndOfContent.CONTENT_LENGTH;
                            }
                        }
                        break;
                    case TRANSFER_ENCODING:
                        if (this._value == HttpHeaderValue.CHUNKED) {
                            this._endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
                            this._contentLength = -1L;
                        } else if (this._valueString.endsWith(HttpHeaderValue.CHUNKED.toString())) {
                            this._endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
                        } else if (this._valueString.contains(HttpHeaderValue.CHUNKED.toString())) {
                            throw new BadMessageException(400, "Bad chunking");
                        }
                        break;
                    case HOST:
                        this._host = true;
                        if (!(this._field instanceof HostPortHttpField) && this._valueString != null && !this._valueString.isEmpty()) {
                            this._field = new HostPortHttpField(this._header, this.legacyString(this._headerString, this._header.asString()), this._valueString);
                            add_to_connection_trie = this._connectionFields != null;
                        }
                        break;
                    case CONNECTION:
                        if (this._valueString != null && this._valueString.contains("close")) {
                            this._connectionFields = null;
                        }
                        break;
                    case AUTHORIZATION:
                    case ACCEPT:
                    case ACCEPT_CHARSET:
                    case ACCEPT_ENCODING:
                    case ACCEPT_LANGUAGE:
                    case COOKIE:
                    case CACHE_CONTROL:
                    case USER_AGENT:
                        add_to_connection_trie = this._connectionFields != null && this._field == null;
                }
                if (add_to_connection_trie && !this._connectionFields.isFull() && this._header != null && this._valueString != null) {
                    if (this._field == null) {
                        this._field = new HttpField(this._header, this.legacyString(this._headerString, this._header.asString()), this._valueString);
                    }
                    this._connectionFields.put(this._field);
                }
            }
            this._handler.parsedHeader(this._field != null ? this._field : new HttpField(this._header, this._headerString, this._valueString));
        }
        this._headerString = this._valueString = null;
        this._header = null;
        this._value = null;
        this._field = null;
    }

    private void parsedTrailer() {
        if (this._headerString != null || this._valueString != null) {
            this._handler.parsedTrailer(this._field != null ? this._field : new HttpField(this._header, this._headerString, this._valueString));
        }
        this._headerString = this._valueString = null;
        this._header = null;
        this._value = null;
        this._field = null;
    }

    private long convertContentLength(String valueString) {
        try {
            return Long.parseLong(valueString);
        } catch (NumberFormatException var3) {
            LOG.ignore(var3);
            throw new BadMessageException(400, "Invalid Content-Length Value");
        }
    }

    protected boolean parseFields(ByteBuffer buffer) {
        while ((this._state == HttpParser.State.HEADER || this._state == HttpParser.State.TRAILER) && buffer.hasRemaining()) {
            byte ch = this.next(buffer);
            if (ch != 0) {
                if (this._maxHeaderBytes > 0 && ++this._headerBytes > this._maxHeaderBytes) {
                    boolean header = this._state == HttpParser.State.HEADER;
                    LOG.warn("{} is too large {}>{}", header ? "Header" : "Trailer", this._headerBytes, this._maxHeaderBytes);
                    throw new BadMessageException(header ? 431 : 413);
                }
                switch(this._fieldState) {
                    case FIELD:
                        switch(ch) {
                            case 9:
                            case 32:
                            case 58:
                                if (this.complianceViolation(HttpCompliance.RFC7230, "header folding")) {
                                    throw new BadMessageException(400, "Header Folding");
                                }
                                if (this._valueString == null) {
                                    this._string.setLength(0);
                                    this._length = 0;
                                } else {
                                    this.setString(this._valueString);
                                    this._string.append(' ');
                                    this._length++;
                                    this._valueString = null;
                                }
                                this.setState(HttpParser.FieldState.VALUE);
                                continue;
                            case 10:
                                if (this._state == HttpParser.State.HEADER) {
                                    this.parsedHeader();
                                } else {
                                    this.parsedTrailer();
                                }
                                this._contentPosition = 0L;
                                if (this._state == HttpParser.State.TRAILER) {
                                    this.setState(HttpParser.State.END);
                                    return this._handler.messageComplete();
                                }
                                if (!this._host && this._version == HttpVersion.HTTP_1_1 && this._requestHandler != null) {
                                    throw new BadMessageException(400, "No Host");
                                }
                                if (this._responseHandler == null || this._responseStatus != 304 && this._responseStatus != 204 && this._responseStatus >= 200) {
                                    if (this._endOfContent == HttpTokens.EndOfContent.UNKNOWN_CONTENT) {
                                        if (this._responseStatus != 0 && this._responseStatus != 304 && this._responseStatus != 204 && this._responseStatus >= 200) {
                                            this._endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
                                        } else {
                                            this._endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
                                        }
                                    }
                                } else {
                                    this._endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
                                }
                                switch(this._endOfContent) {
                                    case EOF_CONTENT:
                                        {
                                            this.setState(HttpParser.State.EOF_CONTENT);
                                            boolean handle = this._handler.headerComplete();
                                            this._headerComplete = true;
                                            return handle;
                                        }
                                    case CHUNKED_CONTENT:
                                        {
                                            this.setState(HttpParser.State.CHUNKED_CONTENT);
                                            boolean handle = this._handler.headerComplete();
                                            this._headerComplete = true;
                                            return handle;
                                        }
                                    case NO_CONTENT:
                                        this.setState(HttpParser.State.END);
                                        return this.handleHeaderContentMessage();
                                    default:
                                        {
                                            this.setState(HttpParser.State.CONTENT);
                                            boolean handle = this._handler.headerComplete();
                                            this._headerComplete = true;
                                            return handle;
                                        }
                                }
                            default:
                                if (ch < 32) {
                                    throw new BadMessageException();
                                }
                                if (this._state == HttpParser.State.HEADER) {
                                    this.parsedHeader();
                                } else {
                                    this.parsedTrailer();
                                }
                                if (buffer.hasRemaining()) {
                                    HttpField field = this._connectionFields == null ? null : this._connectionFields.getBest(buffer, -1, buffer.remaining());
                                    if (field == null) {
                                        field = CACHE.getBest(buffer, -1, buffer.remaining());
                                    }
                                    if (field != null) {
                                        String n;
                                        String v;
                                        if (this._compliance == HttpCompliance.LEGACY) {
                                            String fn = field.getName();
                                            n = this.legacyString(BufferUtil.toString(buffer, buffer.position() - 1, fn.length(), StandardCharsets.US_ASCII), fn);
                                            String fv = field.getValue();
                                            if (fv == null) {
                                                v = null;
                                            } else {
                                                v = this.legacyString(BufferUtil.toString(buffer, buffer.position() + fn.length() + 1, fv.length(), StandardCharsets.ISO_8859_1), fv);
                                                field = new HttpField(field.getHeader(), n, v);
                                            }
                                        } else {
                                            n = field.getName();
                                            v = field.getValue();
                                        }
                                        this._header = field.getHeader();
                                        this._headerString = n;
                                        if (v == null) {
                                            this.setState(HttpParser.FieldState.VALUE);
                                            this._string.setLength(0);
                                            this._length = 0;
                                            buffer.position(buffer.position() + n.length() + 1);
                                            continue;
                                        }
                                        int pos = buffer.position() + n.length() + v.length() + 1;
                                        byte b = buffer.get(pos);
                                        if (b != 13 && b != 10) {
                                            this.setState(HttpParser.FieldState.IN_VALUE);
                                            this.setString(v);
                                            buffer.position(pos);
                                            continue;
                                        }
                                        this._field = field;
                                        this._valueString = v;
                                        this.setState(HttpParser.FieldState.IN_VALUE);
                                        if (b == 13) {
                                            this._cr = true;
                                            buffer.position(pos + 1);
                                            continue;
                                        }
                                        buffer.position(pos);
                                        continue;
                                    }
                                }
                                this.setState(HttpParser.FieldState.IN_NAME);
                                this._string.setLength(0);
                                this._string.append((char) ch);
                                this._length = 1;
                                continue;
                        }
                    case IN_NAME:
                        if (ch == 58) {
                            if (this._headerString == null) {
                                this._headerString = this.takeString();
                                this._header = HttpHeader.CACHE.get(this._headerString);
                            }
                            this._length = -1;
                            this.setState(HttpParser.FieldState.VALUE);
                        } else if (ch > 32) {
                            if (this._header != null) {
                                this.setString(this._header.asString());
                                this._header = null;
                                this._headerString = null;
                            }
                            this._string.append((char) ch);
                            if (ch > 32) {
                                this._length = this._string.length();
                            }
                        } else {
                            if (ch == 10 && !this.complianceViolation(HttpCompliance.RFC7230, "name only header")) {
                                if (this._headerString == null) {
                                    this._headerString = this.takeString();
                                    this._header = HttpHeader.CACHE.get(this._headerString);
                                }
                                this._value = null;
                                this._string.setLength(0);
                                this._valueString = "";
                                this._length = -1;
                                this.setState(HttpParser.FieldState.FIELD);
                                continue;
                            }
                            throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
                        }
                        continue;
                    case VALUE:
                        if (ch <= 32 && ch >= 0) {
                            if (ch == 32 || ch == 9) {
                                continue;
                            }
                            if (ch == 10) {
                                this._value = null;
                                this._string.setLength(0);
                                this._valueString = "";
                                this._length = -1;
                                this.setState(HttpParser.FieldState.FIELD);
                                continue;
                            }
                            throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
                        }
                        this._string.append((char) (255 & ch));
                        this._length = this._string.length();
                        this.setState(HttpParser.FieldState.IN_VALUE);
                        continue;
                    case IN_VALUE:
                        if (ch < 32 && ch >= 0 && ch != 9) {
                            if (ch == 10) {
                                if (this._length > 0) {
                                    this._value = null;
                                    this._valueString = this.takeString();
                                    this._length = -1;
                                }
                                this.setState(HttpParser.FieldState.FIELD);
                                continue;
                            }
                            throw new HttpParser.IllegalCharacterException(this._state, ch, buffer);
                        } else {
                            if (this._valueString != null) {
                                this.setString(this._valueString);
                                this._valueString = null;
                                this._field = null;
                            }
                            this._string.append((char) (255 & ch));
                            if (ch > 32 || ch < 0) {
                                this._length = this._string.length();
                            }
                            continue;
                        }
                    default:
                        throw new IllegalStateException(this._state.toString());
                }
            }
            break;
        }
        return false;
    }

    public boolean parseNext(ByteBuffer buffer) {
        if (this.DEBUG) {
            LOG.debug("parseNext s={} {}", this._state, BufferUtil.toDetailString(buffer));
        }
        try {
            if (this._state == HttpParser.State.START) {
                this._version = null;
                this._method = null;
                this._methodString = null;
                this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
                this._header = null;
                if (this.quickStart(buffer)) {
                    return true;
                }
            }
            if (this._state.ordinal() >= HttpParser.State.START.ordinal() && this._state.ordinal() < HttpParser.State.HEADER.ordinal() && this.parseLine(buffer)) {
                return true;
            }
            if (this._state == HttpParser.State.HEADER && this.parseFields(buffer)) {
                return true;
            }
            if (this._state.ordinal() >= HttpParser.State.CONTENT.ordinal() && this._state.ordinal() < HttpParser.State.TRAILER.ordinal()) {
                if (this._responseStatus > 0 && this._headResponse) {
                    this.setState(HttpParser.State.END);
                    return this.handleContentMessage();
                }
                if (this.parseContent(buffer)) {
                    return true;
                }
            }
            if (this._state == HttpParser.State.TRAILER && this.parseFields(buffer)) {
                return true;
            }
            if (this._state == HttpParser.State.END) {
                while (buffer.remaining() > 0 && buffer.get(buffer.position()) <= 32) {
                    buffer.get();
                }
            } else if (this.isClose() || this.isClosed()) {
                BufferUtil.clear(buffer);
            }
            if (this._eof && !buffer.hasRemaining()) {
                switch(this._state) {
                    case CLOSED:
                        break;
                    case START:
                        this.setState(HttpParser.State.CLOSED);
                        this._handler.earlyEOF();
                        break;
                    case END:
                    case CLOSE:
                        this.setState(HttpParser.State.CLOSED);
                        break;
                    case EOF_CONTENT:
                    case TRAILER:
                        if (this._fieldState == HttpParser.FieldState.FIELD) {
                            this.setState(HttpParser.State.CLOSED);
                            return this.handleContentMessage();
                        }
                        this.setState(HttpParser.State.CLOSED);
                        this._handler.earlyEOF();
                        break;
                    case CONTENT:
                    case CHUNKED_CONTENT:
                    case CHUNK_SIZE:
                    case CHUNK_PARAMS:
                    case CHUNK:
                        this.setState(HttpParser.State.CLOSED);
                        this._handler.earlyEOF();
                        break;
                    default:
                        if (this.DEBUG) {
                            LOG.debug("{} EOF in {}", this, this._state);
                        }
                        this.setState(HttpParser.State.CLOSED);
                        this._handler.badMessage(400, null);
                }
            }
        } catch (BadMessageException var3) {
            BufferUtil.clear(buffer);
            this.badMessage(var3);
        } catch (Throwable var4) {
            BufferUtil.clear(buffer);
            this.badMessage(new BadMessageException(400, this._requestHandler != null ? "Bad Request" : "Bad Response", var4));
        }
        return false;
    }

    protected void badMessage(BadMessageException x) {
        if (this.DEBUG) {
            LOG.debug("Parse exception: " + this + " for " + this._handler, x);
        }
        this.setState(HttpParser.State.CLOSE);
        if (this._headerComplete) {
            this._handler.earlyEOF();
        } else {
            this._handler.badMessage(x._code, x._reason);
        }
    }

    protected boolean parseContent(ByteBuffer buffer) {
        int remaining = buffer.remaining();
        if (remaining == 0 && this._state == HttpParser.State.CONTENT) {
            long content = this._contentLength - this._contentPosition;
            if (content == 0L) {
                this.setState(HttpParser.State.END);
                return this.handleContentMessage();
            }
        }
        for (; this._state.ordinal() < HttpParser.State.TRAILER.ordinal() && remaining > 0; remaining = buffer.remaining()) {
            switch(this._state) {
                case CLOSED:
                    BufferUtil.clear(buffer);
                    return false;
                case START:
                case END:
                case CLOSE:
                case TRAILER:
                default:
                    break;
                case EOF_CONTENT:
                    this._contentChunk = buffer.asReadOnlyBuffer();
                    this._contentPosition += (long) remaining;
                    buffer.position(buffer.position() + remaining);
                    if (this._handler.content(this._contentChunk)) {
                        return true;
                    }
                    break;
                case CONTENT:
                    long content = this._contentLength - this._contentPosition;
                    if (content == 0L) {
                        this.setState(HttpParser.State.END);
                        return this.handleContentMessage();
                    }
                    this._contentChunk = buffer.asReadOnlyBuffer();
                    if ((long) remaining > content) {
                        this._contentChunk.limit(this._contentChunk.position() + (int) content);
                    }
                    this._contentPosition = this._contentPosition + (long) this._contentChunk.remaining();
                    buffer.position(buffer.position() + this._contentChunk.remaining());
                    if (this._handler.content(this._contentChunk)) {
                        return true;
                    }
                    if (this._contentPosition == this._contentLength) {
                        this.setState(HttpParser.State.END);
                        return this.handleContentMessage();
                    }
                    break;
                case CHUNKED_CONTENT:
                    byte chx = this.next(buffer);
                    if (chx > 32) {
                        this._chunkLength = TypeUtil.convertHexDigit(chx);
                        this._chunkPosition = 0;
                        this.setState(HttpParser.State.CHUNK_SIZE);
                    }
                    break;
                case CHUNK_SIZE:
                    byte chxx = this.next(buffer);
                    if (chxx != 0) {
                        if (chxx == 10) {
                            if (this._chunkLength == 0) {
                                this.setState(HttpParser.State.TRAILER);
                                if (this._handler.contentComplete()) {
                                    return true;
                                }
                            } else {
                                this.setState(HttpParser.State.CHUNK);
                            }
                        } else if (chxx > 32 && chxx != 59) {
                            this._chunkLength = this._chunkLength * 16 + TypeUtil.convertHexDigit(chxx);
                        } else {
                            this.setState(HttpParser.State.CHUNK_PARAMS);
                        }
                    }
                    break;
                case CHUNK_PARAMS:
                    byte ch = this.next(buffer);
                    if (ch == 10) {
                        if (this._chunkLength == 0) {
                            this.setState(HttpParser.State.TRAILER);
                            if (this._handler.contentComplete()) {
                                return true;
                            }
                        } else {
                            this.setState(HttpParser.State.CHUNK);
                        }
                    }
                    break;
                case CHUNK:
                    int chunk = this._chunkLength - this._chunkPosition;
                    if (chunk == 0) {
                        this.setState(HttpParser.State.CHUNKED_CONTENT);
                    } else {
                        this._contentChunk = buffer.asReadOnlyBuffer();
                        if (remaining > chunk) {
                            this._contentChunk.limit(this._contentChunk.position() + chunk);
                        }
                        chunk = this._contentChunk.remaining();
                        this._contentPosition += (long) chunk;
                        this._chunkPosition += chunk;
                        buffer.position(buffer.position() + chunk);
                        if (this._handler.content(this._contentChunk)) {
                            return true;
                        }
                    }
            }
        }
        return false;
    }

    public boolean isAtEOF() {
        return this._eof;
    }

    public void atEOF() {
        if (this.DEBUG) {
            LOG.debug("atEOF {}", this);
        }
        this._eof = true;
    }

    public void close() {
        if (this.DEBUG) {
            LOG.debug("close {}", this);
        }
        this.setState(HttpParser.State.CLOSE);
    }

    public void reset() {
        if (this.DEBUG) {
            LOG.debug("reset {}", this);
        }
        if (this._state != HttpParser.State.CLOSE && this._state != HttpParser.State.CLOSED) {
            this.setState(HttpParser.State.START);
            this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
            this._contentLength = -1L;
            this._contentPosition = 0L;
            this._responseStatus = 0;
            this._contentChunk = null;
            this._headerBytes = 0;
            this._host = false;
            this._headerComplete = false;
        }
    }

    protected void setState(HttpParser.State state) {
        if (this.DEBUG) {
            LOG.debug("{} --> {}", this._state, state);
        }
        this._state = state;
    }

    protected void setState(HttpParser.FieldState state) {
        if (this.DEBUG) {
            LOG.debug("{}:{} --> {}", this._state, this._field, state);
        }
        this._fieldState = state;
    }

    public Trie<HttpField> getFieldCache() {
        return this._connectionFields;
    }

    private String getProxyField(ByteBuffer buffer) {
        this._string.setLength(0);
        this._length = 0;
        while (buffer.hasRemaining()) {
            byte ch = this.next(buffer);
            if (ch <= 32) {
                return this._string.toString();
            }
            this._string.append((char) ch);
        }
        throw new BadMessageException();
    }

    public String toString() {
        return String.format("%s{s=%s,%d of %d}", this.getClass().getSimpleName(), this._state, this._contentPosition, this._contentLength);
    }

    static {
        CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE));
        CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE));
        CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.UPGRADE));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip, deflate"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip,deflate,sdch"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.8,en;q=0.6"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.3"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT, "*/*"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT, "image/png,image/*;q=0.8,*/*;q=0.5"));
        CACHE.put(new HttpField(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        CACHE.put(new HttpField(HttpHeader.PRAGMA, "no-cache"));
        CACHE.put(new HttpField(HttpHeader.CACHE_CONTROL, "private, no-cache, no-cache=Set-Cookie, proxy-revalidate"));
        CACHE.put(new HttpField(HttpHeader.CACHE_CONTROL, "no-cache"));
        CACHE.put(new HttpField(HttpHeader.CONTENT_LENGTH, "0"));
        CACHE.put(new HttpField(HttpHeader.CONTENT_ENCODING, "gzip"));
        CACHE.put(new HttpField(HttpHeader.CONTENT_ENCODING, "deflate"));
        CACHE.put(new HttpField(HttpHeader.TRANSFER_ENCODING, "chunked"));
        CACHE.put(new HttpField(HttpHeader.EXPIRES, "Fri, 01 Jan 1990 00:00:00 GMT"));
        for (String type : new String[] { "text/plain", "text/html", "text/xml", "text/json", "application/json", "application/x-www-form-urlencoded" }) {
            HttpField field = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type);
            CACHE.put(field);
            for (String charset : new String[] { "utf-8", "iso-8859-1" }) {
                CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + ";charset=" + charset));
                CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + "; charset=" + charset));
                CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + ";charset=" + charset.toUpperCase(Locale.ENGLISH)));
                CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + "; charset=" + charset.toUpperCase(Locale.ENGLISH)));
            }
        }
        for (HttpHeader h : HttpHeader.values()) {
            if (!CACHE.put(new HttpField(h, (String) null))) {
                throw new IllegalStateException("CACHE FULL");
            }
        }
        CACHE.put(new HttpField(HttpHeader.REFERER, (String) null));
        CACHE.put(new HttpField(HttpHeader.IF_MODIFIED_SINCE, (String) null));
        CACHE.put(new HttpField(HttpHeader.IF_NONE_MATCH, (String) null));
        CACHE.put(new HttpField(HttpHeader.AUTHORIZATION, (String) null));
        CACHE.put(new HttpField(HttpHeader.COOKIE, (String) null));
        __charState = new HttpParser.CharState[256];
        Arrays.fill(__charState, HttpParser.CharState.ILLEGAL);
        __charState[10] = HttpParser.CharState.LF;
        __charState[13] = HttpParser.CharState.CR;
        __charState[9] = HttpParser.CharState.LEGAL;
        __charState[32] = HttpParser.CharState.LEGAL;
        __charState[33] = HttpParser.CharState.LEGAL;
        __charState[35] = HttpParser.CharState.LEGAL;
        __charState[36] = HttpParser.CharState.LEGAL;
        __charState[37] = HttpParser.CharState.LEGAL;
        __charState[38] = HttpParser.CharState.LEGAL;
        __charState[39] = HttpParser.CharState.LEGAL;
        __charState[42] = HttpParser.CharState.LEGAL;
        __charState[43] = HttpParser.CharState.LEGAL;
        __charState[45] = HttpParser.CharState.LEGAL;
        __charState[46] = HttpParser.CharState.LEGAL;
        __charState[94] = HttpParser.CharState.LEGAL;
        __charState[95] = HttpParser.CharState.LEGAL;
        __charState[96] = HttpParser.CharState.LEGAL;
        __charState[124] = HttpParser.CharState.LEGAL;
        __charState[126] = HttpParser.CharState.LEGAL;
        __charState[34] = HttpParser.CharState.LEGAL;
        __charState[92] = HttpParser.CharState.LEGAL;
        __charState[40] = HttpParser.CharState.LEGAL;
        __charState[41] = HttpParser.CharState.LEGAL;
        Arrays.fill(__charState, 33, 40, HttpParser.CharState.LEGAL);
        Arrays.fill(__charState, 42, 92, HttpParser.CharState.LEGAL);
        Arrays.fill(__charState, 93, 127, HttpParser.CharState.LEGAL);
        Arrays.fill(__charState, 128, 256, HttpParser.CharState.LEGAL);
    }

    static enum CharState {

        ILLEGAL, CR, LF, LEGAL
    }

    public interface ComplianceHandler extends HttpParser.HttpHandler {

        void onComplianceViolation(HttpCompliance var1, HttpCompliance var2, String var3);
    }

    public static enum FieldState {

        FIELD, IN_NAME, VALUE, IN_VALUE
    }

    public interface HttpHandler {

        boolean content(ByteBuffer var1);

        boolean headerComplete();

        boolean contentComplete();

        boolean messageComplete();

        void parsedHeader(HttpField var1);

        default void parsedTrailer(HttpField field) {
        }

        void earlyEOF();

        void badMessage(int var1, String var2);

        int getHeaderCacheSize();
    }

    private static class IllegalCharacterException extends BadMessageException {

        private IllegalCharacterException(HttpParser.State state, byte ch, ByteBuffer buffer) {
            super(400, String.format("Illegal character 0x%X", ch));
            HttpParser.LOG.warn(String.format("Illegal character 0x%X in state=%s for buffer %s", ch, state, BufferUtil.toDetailString(buffer)));
        }
    }

    public interface RequestHandler extends HttpParser.HttpHandler {

        boolean startRequest(String var1, String var2, HttpVersion var3);
    }

    public interface ResponseHandler extends HttpParser.HttpHandler {

        boolean startResponse(HttpVersion var1, int var2, String var3);
    }

    public static enum State {

        START,
        METHOD,
        RESPONSE_VERSION,
        SPACE1,
        STATUS,
        URI,
        SPACE2,
        REQUEST_VERSION,
        REASON,
        PROXY,
        HEADER,
        CONTENT,
        EOF_CONTENT,
        CHUNKED_CONTENT,
        CHUNK_SIZE,
        CHUNK_PARAMS,
        CHUNK,
        TRAILER,
        END,
        CLOSE,
        CLOSED
    }
}
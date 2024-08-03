package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.CookieCompliance;
import info.journeymap.shaded.org.eclipse.jetty.http.DateGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpContent;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpCookie;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeaderValue;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpScheme;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpStatus;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpURI;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.http.PreEncodedHttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.Syntax;
import info.journeymap.shaded.org.eclipse.jetty.io.RuntimeIOException;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ErrorHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.session.SessionHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.QuotedStringTokenizer;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletOutputStream;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.IllegalSelectorException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Response implements HttpServletResponse {

    private static final Logger LOG = Log.getLogger(Response.class);

    private static final String __COOKIE_DELIM = "\",;\\ \t";

    private static final String __01Jan1970_COOKIE = DateGenerator.formatCookieDate(0L).trim();

    private static final int __MIN_BUFFER_SIZE = 1;

    private static final HttpField __EXPIRES_01JAN1970 = new PreEncodedHttpField(HttpHeader.EXPIRES, DateGenerator.__01Jan1970);

    private static final ThreadLocal<StringBuilder> __cookieBuilder = ThreadLocal.withInitial(() -> new StringBuilder(128));

    public static final String SET_INCLUDE_HEADER_PREFIX = "info.journeymap.shaded.org.eclipse.jetty.server.include.";

    public static final String HTTP_ONLY_COMMENT = "__HTTP_ONLY__";

    private final HttpChannel _channel;

    private final HttpFields _fields = new HttpFields();

    private final AtomicInteger _include = new AtomicInteger();

    private final HttpOutput _out;

    private int _status = 200;

    private String _reason;

    private Locale _locale;

    private MimeTypes.Type _mimeType;

    private String _characterEncoding;

    private Response.EncodingFrom _encodingFrom = Response.EncodingFrom.NOT_SET;

    private String _contentType;

    private Response.OutputType _outputType = Response.OutputType.NONE;

    private ResponseWriter _writer;

    private long _contentLength = -1L;

    private Supplier<HttpFields> trailers;

    private static final EnumSet<Response.EncodingFrom> __localeOverride = EnumSet.of(Response.EncodingFrom.NOT_SET, Response.EncodingFrom.INFERRED);

    private static final EnumSet<Response.EncodingFrom> __explicitCharset = EnumSet.of(Response.EncodingFrom.SET_LOCALE, Response.EncodingFrom.SET_CHARACTER_ENCODING);

    public Response(HttpChannel channel, HttpOutput out) {
        this._channel = channel;
        this._out = out;
    }

    public HttpChannel getHttpChannel() {
        return this._channel;
    }

    protected void recycle() {
        this._status = 200;
        this._reason = null;
        this._locale = null;
        this._mimeType = null;
        this._characterEncoding = null;
        this._contentType = null;
        this._outputType = Response.OutputType.NONE;
        this._contentLength = -1L;
        this._out.recycle();
        this._fields.clear();
        this._encodingFrom = Response.EncodingFrom.NOT_SET;
    }

    public HttpOutput getHttpOutput() {
        return this._out;
    }

    public boolean isIncluding() {
        return this._include.get() > 0;
    }

    public void include() {
        this._include.incrementAndGet();
    }

    public void included() {
        this._include.decrementAndGet();
        if (this._outputType == Response.OutputType.WRITER) {
            this._writer.reopen();
        }
        this._out.reopen();
    }

    public void addCookie(HttpCookie cookie) {
        if (StringUtil.isBlank(cookie.getName())) {
            throw new IllegalArgumentException("Cookie.name cannot be blank/null");
        } else {
            if (this.getHttpChannel().getHttpConfiguration().isCookieCompliance(CookieCompliance.RFC2965)) {
                this.addSetRFC2965Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(), cookie.getComment(), cookie.isSecure(), cookie.isHttpOnly(), cookie.getVersion());
            } else {
                this.addSetRFC6265Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(), cookie.isSecure(), cookie.isHttpOnly());
            }
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        String comment = cookie.getComment();
        boolean httpOnly = false;
        if (comment != null) {
            int i = comment.indexOf("__HTTP_ONLY__");
            if (i >= 0) {
                httpOnly = true;
                comment = comment.replace("__HTTP_ONLY__", "").trim();
                if (comment.length() == 0) {
                    comment = null;
                }
            }
        }
        if (StringUtil.isBlank(cookie.getName())) {
            throw new IllegalArgumentException("Cookie.name cannot be blank/null");
        } else {
            if (this.getHttpChannel().getHttpConfiguration().isCookieCompliance(CookieCompliance.RFC2965)) {
                this.addSetRFC2965Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), (long) cookie.getMaxAge(), comment, cookie.getSecure(), httpOnly || cookie.isHttpOnly(), cookie.getVersion());
            } else {
                this.addSetRFC6265Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), (long) cookie.getMaxAge(), cookie.getSecure(), httpOnly || cookie.isHttpOnly());
            }
        }
    }

    public void addSetRFC6265Cookie(String name, String value, String domain, String path, long maxAge, boolean isSecure, boolean isHttpOnly) {
        if (name != null && name.length() != 0) {
            Syntax.requireValidRFC2616Token(name, "RFC6265 Cookie name");
            Syntax.requireValidRFC6265CookieValue(value);
            StringBuilder buf = (StringBuilder) __cookieBuilder.get();
            buf.setLength(0);
            buf.append(name).append('=').append(value == null ? "" : value);
            if (path != null && path.length() > 0) {
                buf.append(";Path=").append(path);
            }
            if (domain != null && domain.length() > 0) {
                buf.append(";Domain=").append(domain);
            }
            if (maxAge >= 0L) {
                buf.append(";Expires=");
                if (maxAge == 0L) {
                    buf.append(__01Jan1970_COOKIE);
                } else {
                    DateGenerator.formatCookieDate(buf, System.currentTimeMillis() + 1000L * maxAge);
                }
                buf.append(";Max-Age=");
                buf.append(maxAge);
            }
            if (isSecure) {
                buf.append(";Secure");
            }
            if (isHttpOnly) {
                buf.append(";HttpOnly");
            }
            this._fields.add(HttpHeader.SET_COOKIE, buf.toString());
            this._fields.put(__EXPIRES_01JAN1970);
        } else {
            throw new IllegalArgumentException("Bad cookie name");
        }
    }

    public void addSetRFC2965Cookie(String name, String value, String domain, String path, long maxAge, String comment, boolean isSecure, boolean isHttpOnly, int version) {
        if (name != null && name.length() != 0) {
            StringBuilder buf = (StringBuilder) __cookieBuilder.get();
            buf.setLength(0);
            boolean quote_name = isQuoteNeededForCookie(name);
            quoteOnlyOrAppend(buf, name, quote_name);
            buf.append('=');
            boolean quote_value = isQuoteNeededForCookie(value);
            quoteOnlyOrAppend(buf, value, quote_value);
            boolean has_domain = domain != null && domain.length() > 0;
            boolean quote_domain = has_domain && isQuoteNeededForCookie(domain);
            boolean has_path = path != null && path.length() > 0;
            boolean quote_path = has_path && isQuoteNeededForCookie(path);
            if (version == 0 && (comment != null || quote_name || quote_value || quote_domain || quote_path || QuotedStringTokenizer.isQuoted(name) || QuotedStringTokenizer.isQuoted(value) || QuotedStringTokenizer.isQuoted(path) || QuotedStringTokenizer.isQuoted(domain))) {
                version = 1;
            }
            if (version == 1) {
                buf.append(";Version=1");
            } else if (version > 1) {
                buf.append(";Version=").append(version);
            }
            if (has_path) {
                buf.append(";Path=");
                quoteOnlyOrAppend(buf, path, quote_path);
            }
            if (has_domain) {
                buf.append(";Domain=");
                quoteOnlyOrAppend(buf, domain, quote_domain);
            }
            if (maxAge >= 0L) {
                buf.append(";Expires=");
                if (maxAge == 0L) {
                    buf.append(__01Jan1970_COOKIE);
                } else {
                    DateGenerator.formatCookieDate(buf, System.currentTimeMillis() + 1000L * maxAge);
                }
                if (version >= 1) {
                    buf.append(";Max-Age=");
                    buf.append(maxAge);
                }
            }
            if (isSecure) {
                buf.append(";Secure");
            }
            if (isHttpOnly) {
                buf.append(";HttpOnly");
            }
            if (comment != null) {
                buf.append(";Comment=");
                quoteOnlyOrAppend(buf, comment, isQuoteNeededForCookie(comment));
            }
            this._fields.add(HttpHeader.SET_COOKIE, buf.toString());
            this._fields.put(__EXPIRES_01JAN1970);
        } else {
            throw new IllegalArgumentException("Bad cookie name");
        }
    }

    private static boolean isQuoteNeededForCookie(String s) {
        if (s == null || s.length() == 0) {
            return true;
        } else if (QuotedStringTokenizer.isQuoted(s)) {
            return false;
        } else {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if ("\",;\\ \t".indexOf(c) >= 0) {
                    return true;
                }
                if (c < ' ' || c >= 127) {
                    throw new IllegalArgumentException("Illegal character in cookie value");
                }
            }
            return false;
        }
    }

    private static void quoteOnlyOrAppend(StringBuilder buf, String s, boolean quote) {
        if (quote) {
            QuotedStringTokenizer.quoteOnly(buf, s);
        } else {
            buf.append(s);
        }
    }

    @Override
    public boolean containsHeader(String name) {
        return this._fields.containsKey(name);
    }

    @Override
    public String encodeURL(String url) {
        Request request = this._channel.getRequest();
        SessionHandler sessionManager = request.getSessionHandler();
        if (sessionManager == null) {
            return url;
        } else {
            HttpURI uri = null;
            if (sessionManager.isCheckingRemoteSessionIdEncoding() && URIUtil.hasScheme(url)) {
                uri = new HttpURI(url);
                String path = uri.getPath();
                path = path == null ? "" : path;
                int port = uri.getPort();
                if (port < 0) {
                    port = HttpScheme.HTTPS.asString().equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
                }
                if (!request.getServerName().equalsIgnoreCase(uri.getHost())) {
                    return url;
                }
                if (request.getServerPort() != port) {
                    return url;
                }
                if (!path.startsWith(request.getContextPath())) {
                    return url;
                }
            }
            String sessionURLPrefix = sessionManager.getSessionIdPathParameterNamePrefix();
            if (sessionURLPrefix == null) {
                return url;
            } else if (url == null) {
                return null;
            } else if ((!sessionManager.isUsingCookies() || !request.isRequestedSessionIdFromCookie()) && sessionManager.isUsingURLs()) {
                HttpSession session = request.getSession(false);
                if (session == null) {
                    return url;
                } else if (!sessionManager.isValid(session)) {
                    return url;
                } else {
                    String id = sessionManager.getExtendedId(session);
                    if (uri == null) {
                        uri = new HttpURI(url);
                    }
                    int prefix = url.indexOf(sessionURLPrefix);
                    if (prefix != -1) {
                        int suffix = url.indexOf("?", prefix);
                        if (suffix < 0) {
                            suffix = url.indexOf("#", prefix);
                        }
                        return suffix <= prefix ? url.substring(0, prefix + sessionURLPrefix.length()) + id : url.substring(0, prefix + sessionURLPrefix.length()) + id + url.substring(suffix);
                    } else {
                        int suffix = url.indexOf(63);
                        if (suffix < 0) {
                            suffix = url.indexOf(35);
                        }
                        return suffix < 0 ? url + ((HttpScheme.HTTPS.is(uri.getScheme()) || HttpScheme.HTTP.is(uri.getScheme())) && uri.getPath() == null ? "/" : "") + sessionURLPrefix + id : url.substring(0, suffix) + ((HttpScheme.HTTPS.is(uri.getScheme()) || HttpScheme.HTTP.is(uri.getScheme())) && uri.getPath() == null ? "/" : "") + sessionURLPrefix + id + url.substring(suffix);
                    }
                }
            } else {
                int prefix = url.indexOf(sessionURLPrefix);
                if (prefix != -1) {
                    int suffix = url.indexOf("?", prefix);
                    if (suffix < 0) {
                        suffix = url.indexOf("#", prefix);
                    }
                    return suffix <= prefix ? url.substring(0, prefix) : url.substring(0, prefix) + url.substring(suffix);
                } else {
                    return url;
                }
            }
        }
    }

    @Override
    public String encodeRedirectURL(String url) {
        return this.encodeURL(url);
    }

    @Deprecated
    @Override
    public String encodeUrl(String url) {
        return this.encodeURL(url);
    }

    @Deprecated
    @Override
    public String encodeRedirectUrl(String url) {
        return this.encodeRedirectURL(url);
    }

    @Override
    public void sendError(int sc) throws IOException {
        this.sendError(sc, null);
    }

    @Override
    public void sendError(int code, String message) throws IOException {
        if (!this.isIncluding()) {
            if (this.isCommitted()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Aborting on sendError on committed response {} {}", code, message);
                }
                code = -1;
            } else {
                this.resetBuffer();
            }
            switch(code) {
                case -1:
                    this._channel.abort(new IOException());
                    return;
                case 102:
                    this.sendProcessing();
                    return;
                default:
                    this._mimeType = null;
                    this._characterEncoding = null;
                    this._outputType = Response.OutputType.NONE;
                    this.setHeader(HttpHeader.EXPIRES, null);
                    this.setHeader(HttpHeader.LAST_MODIFIED, null);
                    this.setHeader(HttpHeader.CACHE_CONTROL, null);
                    this.setHeader(HttpHeader.CONTENT_TYPE, null);
                    this.setHeader(HttpHeader.CONTENT_LENGTH, null);
                    this.setStatus(code);
                    Request request = this._channel.getRequest();
                    Throwable cause = (Throwable) request.getAttribute("info.journeymap.shaded.org.javax.servlet.error.exception");
                    if (message == null) {
                        this._reason = HttpStatus.getMessage(code);
                        message = cause == null ? this._reason : cause.toString();
                    } else {
                        this._reason = message;
                    }
                    if (code != 204 && code != 304 && code != 206 && code >= 200) {
                        ContextHandler.Context context = request.getContext();
                        ContextHandler contextHandler = context == null ? this._channel.getState().getContextHandler() : context.getContextHandler();
                        request.setAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code", code);
                        request.setAttribute("info.journeymap.shaded.org.javax.servlet.error.message", message);
                        request.setAttribute("info.journeymap.shaded.org.javax.servlet.error.request_uri", request.getRequestURI());
                        request.setAttribute("info.journeymap.shaded.org.javax.servlet.error.servlet_name", request.getServletName());
                        ErrorHandler error_handler = ErrorHandler.getErrorHandler(this._channel.getServer(), contextHandler);
                        if (error_handler != null) {
                            error_handler.handle(null, request, request, this);
                        } else {
                            this.closeOutput();
                        }
                    }
            }
        }
    }

    public void sendProcessing() throws IOException {
        if (this._channel.isExpecting102Processing() && !this.isCommitted()) {
            this._channel.sendResponse(HttpGenerator.PROGRESS_102_INFO, null, true);
        }
    }

    public void sendRedirect(int code, String location) throws IOException {
        if (code < 300 || code >= 400) {
            throw new IllegalArgumentException("Not a 3xx redirect code");
        } else if (!this.isIncluding()) {
            if (location == null) {
                throw new IllegalArgumentException();
            } else {
                if (!URIUtil.hasScheme(location)) {
                    StringBuilder buf = this._channel.getRequest().getRootURL();
                    if (location.startsWith("/")) {
                        location = URIUtil.canonicalPath(location);
                    } else {
                        String path = this._channel.getRequest().getRequestURI();
                        String parent = path.endsWith("/") ? path : URIUtil.parentPath(path);
                        location = URIUtil.canonicalPath(URIUtil.addPaths(parent, location));
                        if (!location.startsWith("/")) {
                            buf.append('/');
                        }
                    }
                    if (location == null) {
                        throw new IllegalStateException("path cannot be above root");
                    }
                    buf.append(location);
                    location = buf.toString();
                }
                this.resetBuffer();
                this.setHeader(HttpHeader.LOCATION, location);
                this.setStatus(code);
                this.closeOutput();
            }
        }
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        this.sendRedirect(302, location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        if (!this.isIncluding()) {
            this._fields.putDateField(name, date);
        }
    }

    @Override
    public void addDateHeader(String name, long date) {
        if (!this.isIncluding()) {
            this._fields.addDateField(name, date);
        }
    }

    public void setHeader(HttpHeader name, String value) {
        if (HttpHeader.CONTENT_TYPE == name) {
            this.setContentType(value);
        } else {
            if (this.isIncluding()) {
                return;
            }
            this._fields.put(name, value);
            if (HttpHeader.CONTENT_LENGTH == name) {
                if (value == null) {
                    this._contentLength = -1L;
                } else {
                    this._contentLength = Long.parseLong(value);
                }
            }
        }
    }

    @Override
    public void setHeader(String name, String value) {
        if (HttpHeader.CONTENT_TYPE.is(name)) {
            this.setContentType(value);
        } else {
            if (this.isIncluding()) {
                if (!name.startsWith("info.journeymap.shaded.org.eclipse.jetty.server.include.")) {
                    return;
                }
                name = name.substring("info.journeymap.shaded.org.eclipse.jetty.server.include.".length());
            }
            this._fields.put(name, value);
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                if (value == null) {
                    this._contentLength = -1L;
                } else {
                    this._contentLength = Long.parseLong(value);
                }
            }
        }
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this._fields.getFieldNamesCollection();
    }

    @Override
    public String getHeader(String name) {
        return this._fields.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        Collection<String> i = this._fields.getValuesList(name);
        return (Collection<String>) (i == null ? Collections.emptyList() : i);
    }

    @Override
    public void addHeader(String name, String value) {
        if (this.isIncluding()) {
            if (!name.startsWith("info.journeymap.shaded.org.eclipse.jetty.server.include.")) {
                return;
            }
            name = name.substring("info.journeymap.shaded.org.eclipse.jetty.server.include.".length());
        }
        if (HttpHeader.CONTENT_TYPE.is(name)) {
            this.setContentType(value);
        } else if (HttpHeader.CONTENT_LENGTH.is(name)) {
            this.setHeader(name, value);
        } else {
            this._fields.add(name, value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (!this.isIncluding()) {
            this._fields.putLongField(name, (long) value);
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                this._contentLength = (long) value;
            }
        }
    }

    @Override
    public void addIntHeader(String name, int value) {
        if (!this.isIncluding()) {
            this._fields.add(name, Integer.toString(value));
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                this._contentLength = (long) value;
            }
        }
    }

    @Override
    public void setStatus(int sc) {
        if (sc <= 0) {
            throw new IllegalArgumentException();
        } else {
            if (!this.isIncluding()) {
                this._status = sc;
                this._reason = null;
            }
        }
    }

    @Deprecated
    @Override
    public void setStatus(int sc, String sm) {
        this.setStatusWithReason(sc, sm);
    }

    public void setStatusWithReason(int sc, String sm) {
        if (sc <= 0) {
            throw new IllegalArgumentException();
        } else {
            if (!this.isIncluding()) {
                this._status = sc;
                this._reason = sm;
            }
        }
    }

    @Override
    public String getCharacterEncoding() {
        if (this._characterEncoding == null) {
            String encoding = MimeTypes.getCharsetAssumedFromContentType(this._contentType);
            if (encoding != null) {
                return encoding;
            } else {
                encoding = MimeTypes.getCharsetInferredFromContentType(this._contentType);
                return encoding != null ? encoding : "iso-8859-1";
            }
        } else {
            return this._characterEncoding;
        }
    }

    @Override
    public String getContentType() {
        return this._contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this._outputType == Response.OutputType.WRITER) {
            throw new IllegalStateException("WRITER");
        } else {
            this._outputType = Response.OutputType.STREAM;
            return this._out;
        }
    }

    public boolean isWriting() {
        return this._outputType == Response.OutputType.WRITER;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this._outputType == Response.OutputType.STREAM) {
            throw new IllegalStateException("STREAM");
        } else {
            if (this._outputType == Response.OutputType.NONE) {
                String encoding = this._characterEncoding;
                if (encoding == null) {
                    if (this._mimeType != null && this._mimeType.isCharsetAssumed()) {
                        encoding = this._mimeType.getCharsetString();
                    } else {
                        encoding = MimeTypes.getCharsetAssumedFromContentType(this._contentType);
                        if (encoding == null) {
                            encoding = MimeTypes.getCharsetInferredFromContentType(this._contentType);
                            if (encoding == null) {
                                encoding = "iso-8859-1";
                            }
                            this.setCharacterEncoding(encoding, Response.EncodingFrom.INFERRED);
                        }
                    }
                }
                Locale locale = this.getLocale();
                if (this._writer != null && this._writer.isFor(locale, encoding)) {
                    this._writer.reopen();
                } else if ("iso-8859-1".equalsIgnoreCase(encoding)) {
                    this._writer = new ResponseWriter(new Iso88591HttpWriter(this._out), locale, encoding);
                } else if ("utf-8".equalsIgnoreCase(encoding)) {
                    this._writer = new ResponseWriter(new Utf8HttpWriter(this._out), locale, encoding);
                } else {
                    this._writer = new ResponseWriter(new EncodingHttpWriter(this._out, encoding), locale, encoding);
                }
                this._outputType = Response.OutputType.WRITER;
            }
            return this._writer;
        }
    }

    @Override
    public void setContentLength(int len) {
        if (!this.isCommitted() && !this.isIncluding()) {
            if (len > 0) {
                long written = this._out.getWritten();
                if (written > (long) len) {
                    throw new IllegalArgumentException("setContentLength(" + len + ") when already written " + written);
                }
                this._contentLength = (long) len;
                this._fields.putLongField(HttpHeader.CONTENT_LENGTH, (long) len);
                if (this.isAllContentWritten(written)) {
                    try {
                        this.closeOutput();
                    } catch (IOException var5) {
                        throw new RuntimeIOException(var5);
                    }
                }
            } else if (len == 0) {
                long writtenx = this._out.getWritten();
                if (writtenx > 0L) {
                    throw new IllegalArgumentException("setContentLength(0) when already written " + writtenx);
                }
                this._contentLength = (long) len;
                this._fields.put(HttpHeader.CONTENT_LENGTH, "0");
            } else {
                this._contentLength = (long) len;
                this._fields.remove(HttpHeader.CONTENT_LENGTH);
            }
        }
    }

    public long getContentLength() {
        return this._contentLength;
    }

    public boolean isAllContentWritten(long written) {
        return this._contentLength >= 0L && written >= this._contentLength;
    }

    public boolean isContentComplete(long written) {
        return this._contentLength < 0L || written >= this._contentLength;
    }

    public void closeOutput() throws IOException {
        switch(this._outputType) {
            case WRITER:
                this._writer.close();
                if (!this._out.isClosed()) {
                    this._out.close();
                }
                break;
            case STREAM:
                this.getOutputStream().close();
                break;
            default:
                this._out.close();
        }
    }

    public long getLongContentLength() {
        return this._contentLength;
    }

    public void setLongContentLength(long len) {
        if (!this.isCommitted() && !this.isIncluding()) {
            this._contentLength = len;
            this._fields.putLongField(HttpHeader.CONTENT_LENGTH.toString(), len);
        }
    }

    @Override
    public void setContentLengthLong(long length) {
        this.setLongContentLength(length);
    }

    @Override
    public void setCharacterEncoding(String encoding) {
        this.setCharacterEncoding(encoding, Response.EncodingFrom.SET_CHARACTER_ENCODING);
    }

    private void setCharacterEncoding(String encoding, Response.EncodingFrom from) {
        if (!this.isIncluding() && !this.isWriting()) {
            if (this._outputType != Response.OutputType.WRITER && !this.isCommitted()) {
                if (encoding == null) {
                    this._encodingFrom = Response.EncodingFrom.NOT_SET;
                    if (this._characterEncoding != null) {
                        this._characterEncoding = null;
                        if (this._mimeType != null) {
                            this._mimeType = this._mimeType.getBaseType();
                            this._contentType = this._mimeType.asString();
                            this._fields.put(this._mimeType.getContentTypeField());
                        } else if (this._contentType != null) {
                            this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType);
                            this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                        }
                    }
                } else {
                    this._encodingFrom = from;
                    this._characterEncoding = HttpGenerator.__STRICT ? encoding : StringUtil.normalizeCharset(encoding);
                    if (this._mimeType != null) {
                        this._contentType = this._mimeType.getBaseType().asString() + ";charset=" + this._characterEncoding;
                        this._mimeType = MimeTypes.CACHE.get(this._contentType);
                        if (this._mimeType != null && !HttpGenerator.__STRICT) {
                            this._fields.put(this._mimeType.getContentTypeField());
                        } else {
                            this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                        }
                    } else if (this._contentType != null) {
                        this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType) + ";charset=" + this._characterEncoding;
                        this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                    }
                }
            }
        }
    }

    @Override
    public void setContentType(String contentType) {
        if (!this.isCommitted() && !this.isIncluding()) {
            if (contentType == null) {
                if (this.isWriting() && this._characterEncoding != null) {
                    throw new IllegalSelectorException();
                }
                if (this._locale == null) {
                    this._characterEncoding = null;
                }
                this._mimeType = null;
                this._contentType = null;
                this._fields.remove(HttpHeader.CONTENT_TYPE);
            } else {
                this._contentType = contentType;
                this._mimeType = MimeTypes.CACHE.get(contentType);
                String charset;
                if (this._mimeType != null && this._mimeType.getCharset() != null && !this._mimeType.isCharsetAssumed()) {
                    charset = this._mimeType.getCharsetString();
                } else {
                    charset = MimeTypes.getCharsetFromContentType(contentType);
                }
                if (charset == null) {
                    switch(this._encodingFrom) {
                        case NOT_SET:
                        default:
                            break;
                        case INFERRED:
                        case SET_CONTENT_TYPE:
                            if (this.isWriting()) {
                                this._mimeType = null;
                                this._contentType = this._contentType + ";charset=" + this._characterEncoding;
                            } else {
                                this._encodingFrom = Response.EncodingFrom.NOT_SET;
                                this._characterEncoding = null;
                            }
                            break;
                        case SET_LOCALE:
                        case SET_CHARACTER_ENCODING:
                            this._contentType = contentType + ";charset=" + this._characterEncoding;
                            this._mimeType = null;
                    }
                } else if (this.isWriting() && !charset.equalsIgnoreCase(this._characterEncoding)) {
                    this._mimeType = null;
                    this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType);
                    if (this._characterEncoding != null) {
                        this._contentType = this._contentType + ";charset=" + this._characterEncoding;
                    }
                } else {
                    this._characterEncoding = charset;
                    this._encodingFrom = Response.EncodingFrom.SET_CONTENT_TYPE;
                }
                if (!HttpGenerator.__STRICT && this._mimeType != null) {
                    this._contentType = this._mimeType.asString();
                    this._fields.put(this._mimeType.getContentTypeField());
                } else {
                    this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                }
            }
        }
    }

    @Override
    public void setBufferSize(int size) {
        if (this.isCommitted()) {
            throw new IllegalStateException("cannot set buffer size after response is in committed state");
        } else if (this.getContentCount() > 0L) {
            throw new IllegalStateException("cannot set buffer size after response has " + this.getContentCount() + " bytes already written");
        } else {
            if (size < 1) {
                size = 1;
            }
            this._out.setBufferSize(size);
        }
    }

    @Override
    public int getBufferSize() {
        return this._out.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        if (!this._out.isClosed()) {
            this._out.flush();
        }
    }

    @Override
    public void reset() {
        this.reset(false);
    }

    public void reset(boolean preserveCookies) {
        this.resetForForward();
        this._status = 200;
        this._reason = null;
        this._contentLength = -1L;
        List<HttpField> cookies = preserveCookies ? (List) this._fields.stream().filter(f -> f.getHeader() == HttpHeader.SET_COOKIE).collect(Collectors.toList()) : null;
        this._fields.clear();
        String connection = this._channel.getRequest().getHeader(HttpHeader.CONNECTION.asString());
        if (connection != null) {
            for (String value : StringUtil.csvSplit(null, connection, 0, connection.length())) {
                HttpHeaderValue cb = HttpHeaderValue.CACHE.get(value);
                if (cb != null) {
                    switch(cb) {
                        case CLOSE:
                            this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.toString());
                            break;
                        case KEEP_ALIVE:
                            if (HttpVersion.HTTP_1_0.is(this._channel.getRequest().getProtocol())) {
                                this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.toString());
                            }
                            break;
                        case TE:
                            this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.TE.toString());
                    }
                }
            }
        }
        if (preserveCookies) {
            cookies.forEach(this._fields::add);
        } else {
            Request request = this.getHttpChannel().getRequest();
            HttpSession session = request.getSession(false);
            if (session != null && session.isNew()) {
                SessionHandler sh = request.getSessionHandler();
                if (sh != null) {
                    HttpCookie c = sh.getSessionCookie(session, request.getContextPath(), request.isSecure());
                    if (c != null) {
                        this.addCookie(c);
                    }
                }
            }
        }
    }

    public void resetForForward() {
        this.resetBuffer();
        this._outputType = Response.OutputType.NONE;
    }

    @Override
    public void resetBuffer() {
        this._out.resetBuffer();
    }

    public void setTrailers(Supplier<HttpFields> trailers) {
        this.trailers = trailers;
    }

    public Supplier<HttpFields> getTrailers() {
        return this.trailers;
    }

    protected MetaData.Response newResponseMetaData() {
        MetaData.Response info = new MetaData.Response(this._channel.getRequest().getHttpVersion(), this.getStatus(), this.getReason(), this._fields, this.getLongContentLength());
        info.setTrailerSupplier(this.getTrailers());
        return info;
    }

    public MetaData.Response getCommittedMetaData() {
        MetaData.Response meta = this._channel.getCommittedMetaData();
        return meta == null ? this.newResponseMetaData() : meta;
    }

    @Override
    public boolean isCommitted() {
        return this._channel.isCommitted();
    }

    @Override
    public void setLocale(Locale locale) {
        if (locale != null && !this.isCommitted() && !this.isIncluding()) {
            this._locale = locale;
            this._fields.put(HttpHeader.CONTENT_LANGUAGE, locale.toString().replace('_', '-'));
            if (this._outputType == Response.OutputType.NONE) {
                if (this._channel.getRequest().getContext() != null) {
                    String charset = this._channel.getRequest().getContext().getContextHandler().getLocaleEncoding(locale);
                    if (charset != null && charset.length() > 0 && __localeOverride.contains(this._encodingFrom)) {
                        this.setCharacterEncoding(charset, Response.EncodingFrom.SET_LOCALE);
                    }
                }
            }
        }
    }

    @Override
    public Locale getLocale() {
        return this._locale == null ? Locale.getDefault() : this._locale;
    }

    @Override
    public int getStatus() {
        return this._status;
    }

    public String getReason() {
        return this._reason;
    }

    public HttpFields getHttpFields() {
        return this._fields;
    }

    public long getContentCount() {
        return this._out.getWritten();
    }

    public String toString() {
        return String.format("%s %d %s%n%s", this._channel.getRequest().getHttpVersion(), this._status, this._reason == null ? "" : this._reason, this._fields);
    }

    public void putHeaders(HttpContent content, long contentLength, boolean etag) {
        HttpField lm = content.getLastModified();
        if (lm != null) {
            this._fields.put(lm);
        }
        if (contentLength == 0L) {
            this._fields.put(content.getContentLength());
            this._contentLength = content.getContentLengthValue();
        } else if (contentLength > 0L) {
            this._fields.putLongField(HttpHeader.CONTENT_LENGTH, contentLength);
            this._contentLength = contentLength;
        }
        HttpField ct = content.getContentType();
        if (ct != null) {
            if (this._characterEncoding != null && content.getCharacterEncoding() == null && __explicitCharset.contains(this._encodingFrom)) {
                this.setContentType(content.getMimeType().getBaseType().asString());
            } else {
                this._fields.put(ct);
                this._contentType = ct.getValue();
                this._characterEncoding = content.getCharacterEncoding();
                this._mimeType = content.getMimeType();
            }
        }
        HttpField ce = content.getContentEncoding();
        if (ce != null) {
            this._fields.put(ce);
        }
        if (etag) {
            HttpField et = content.getETag();
            if (et != null) {
                this._fields.put(et);
            }
        }
    }

    public static void putHeaders(HttpServletResponse response, HttpContent content, long contentLength, boolean etag) {
        long lml = content.getResource().lastModified();
        if (lml >= 0L) {
            response.setDateHeader(HttpHeader.LAST_MODIFIED.asString(), lml);
        }
        if (contentLength == 0L) {
            contentLength = content.getContentLengthValue();
        }
        if (contentLength >= 0L) {
            if (contentLength < 2147483647L) {
                response.setContentLength((int) contentLength);
            } else {
                response.setHeader(HttpHeader.CONTENT_LENGTH.asString(), Long.toString(contentLength));
            }
        }
        String ct = content.getContentTypeValue();
        if (ct != null && response.getContentType() == null) {
            response.setContentType(ct);
        }
        String ce = content.getContentEncodingValue();
        if (ce != null) {
            response.setHeader(HttpHeader.CONTENT_ENCODING.asString(), ce);
        }
        if (etag) {
            String et = content.getETagValue();
            if (et != null) {
                response.setHeader(HttpHeader.ETAG.asString(), et);
            }
        }
    }

    private static enum EncodingFrom {

        NOT_SET, INFERRED, SET_LOCALE, SET_CONTENT_TYPE, SET_CHARACTER_ENCODING
    }

    public static enum OutputType {

        NONE, STREAM, WRITER
    }
}
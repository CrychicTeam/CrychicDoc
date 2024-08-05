package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.http.HostPortHttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpCookie;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpScheme;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpURI;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.session.Session;
import info.journeymap.shaded.org.eclipse.jetty.server.session.SessionHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.Attributes;
import info.journeymap.shaded.org.eclipse.jetty.util.AttributesMap;
import info.journeymap.shaded.org.eclipse.jetty.util.IO;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiMap;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiPartInputStreamParser;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.UrlEncoded;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.AsyncContext;
import info.journeymap.shaded.org.javax.servlet.AsyncListener;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.MultipartConfigElement;
import info.journeymap.shaded.org.javax.servlet.RequestDispatcher;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletInputStream;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletRequestAttributeEvent;
import info.journeymap.shaded.org.javax.servlet.ServletRequestAttributeListener;
import info.journeymap.shaded.org.javax.servlet.ServletRequestWrapper;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import info.journeymap.shaded.org.javax.servlet.http.HttpUpgradeHandler;
import info.journeymap.shaded.org.javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Request implements HttpServletRequest {

    public static final String __MULTIPART_CONFIG_ELEMENT = "info.journeymap.shaded.org.eclipse.jetty.multipartConfig";

    public static final String __MULTIPART_INPUT_STREAM = "info.journeymap.shaded.org.eclipse.jetty.multiPartInputStream";

    public static final String __MULTIPART_CONTEXT = "info.journeymap.shaded.org.eclipse.jetty.multiPartContext";

    private static final Logger LOG = Log.getLogger(Request.class);

    private static final Collection<Locale> __defaultLocale = Collections.singleton(Locale.getDefault());

    private static final int __NONE = 0;

    private static final int _STREAM = 1;

    private static final int __READER = 2;

    private static final MultiMap<String> NO_PARAMS = new MultiMap<>();

    private final HttpChannel _channel;

    private final List<ServletRequestAttributeListener> _requestAttributeListeners = new ArrayList();

    private final HttpInput _input;

    private MetaData.Request _metaData;

    private String _originalURI;

    private String _contextPath;

    private String _servletPath;

    private String _pathInfo;

    private boolean _secure;

    private String _asyncNotSupportedSource = null;

    private boolean _newContext;

    private boolean _cookiesExtracted = false;

    private boolean _handled = false;

    private boolean _contentParamsExtracted;

    private boolean _requestedSessionIdFromCookie = false;

    private Attributes _attributes;

    private Authentication _authentication;

    private String _characterEncoding;

    private ContextHandler.Context _context;

    private CookieCutter _cookies;

    private DispatcherType _dispatcherType;

    private int _inputState = 0;

    private MultiMap<String> _queryParameters;

    private MultiMap<String> _contentParameters;

    private MultiMap<String> _parameters;

    private String _queryEncoding;

    private BufferedReader _reader;

    private String _readerEncoding;

    private InetSocketAddress _remote;

    private String _requestedSessionId;

    private UserIdentity.Scope _scope;

    private HttpSession _session;

    private SessionHandler _sessionHandler;

    private long _timeStamp;

    private MultiPartInputStreamParser _multiPartInputStream;

    private AsyncContextState _async;

    private HttpFields _trailers;

    public static Request getBaseRequest(ServletRequest request) {
        if (request instanceof Request) {
            return (Request) request;
        } else {
            Object channel = request.getAttribute(HttpChannel.class.getName());
            if (channel instanceof HttpChannel) {
                return ((HttpChannel) channel).getRequest();
            } else {
                while (request instanceof ServletRequestWrapper) {
                    request = ((ServletRequestWrapper) request).getRequest();
                }
                return request instanceof Request ? (Request) request : null;
            }
        }
    }

    public Request(HttpChannel channel, HttpInput input) {
        this._channel = channel;
        this._input = input;
    }

    public HttpFields getHttpFields() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getFields();
    }

    public HttpFields getTrailers() {
        return this._trailers;
    }

    public HttpInput getHttpInput() {
        return this._input;
    }

    public boolean isPush() {
        return Boolean.TRUE.equals(this.getAttribute("info.journeymap.shaded.org.eclipse.jetty.pushed"));
    }

    public boolean isPushSupported() {
        return !this.isPush() && this.getHttpChannel().getHttpTransport().isPushSupported();
    }

    public PushBuilder getPushBuilder() {
        if (!this.isPushSupported()) {
            throw new IllegalStateException(String.format("%s,push=%b,channel=%s", this, this.isPush(), this.getHttpChannel()));
        } else {
            HttpFields fields = new HttpFields(this.getHttpFields().size() + 5);
            boolean conditional = false;
            for (HttpField field : this.getHttpFields()) {
                HttpHeader header = field.getHeader();
                if (header == null) {
                    fields.add(field);
                } else {
                    switch(header) {
                        case IF_MATCH:
                        case IF_RANGE:
                        case IF_UNMODIFIED_SINCE:
                        case RANGE:
                        case EXPECT:
                        case REFERER:
                        case COOKIE:
                        case AUTHORIZATION:
                            break;
                        case IF_NONE_MATCH:
                        case IF_MODIFIED_SINCE:
                            conditional = true;
                            break;
                        default:
                            fields.add(field);
                    }
                }
            }
            String id = null;
            try {
                HttpSession session = this.getSession();
                if (session != null) {
                    session.getLastAccessedTime();
                    id = session.getId();
                } else {
                    id = this.getRequestedSessionId();
                }
            } catch (IllegalStateException var6) {
                id = this.getRequestedSessionId();
            }
            PushBuilder builder = new PushBuilderImpl(this, fields, this.getMethod(), this.getQueryString(), id, conditional);
            builder.addHeader("referer", this.getRequestURL().toString());
            return builder;
        }
    }

    public void addEventListener(EventListener listener) {
        if (listener instanceof ServletRequestAttributeListener) {
            this._requestAttributeListeners.add((ServletRequestAttributeListener) listener);
        }
        if (listener instanceof AsyncListener) {
            throw new IllegalArgumentException(listener.getClass().toString());
        }
    }

    private MultiMap<String> getParameters() {
        if (!this._contentParamsExtracted) {
            this._contentParamsExtracted = true;
            if (this._contentParameters == null) {
                try {
                    this.extractContentParameters();
                } catch (IllegalArgumentException | IllegalStateException var3) {
                    throw new BadMessageException("Unable to parse form content", var3);
                }
            }
        }
        if (this._queryParameters == null) {
            try {
                this.extractQueryParameters();
            } catch (IllegalArgumentException | IllegalStateException var2) {
                throw new BadMessageException("Unable to parse URI query", var2);
            }
        }
        if (this._queryParameters == NO_PARAMS || this._queryParameters.size() == 0) {
            this._parameters = this._contentParameters;
        } else if (this._contentParameters != NO_PARAMS && this._contentParameters.size() != 0) {
            this._parameters = new MultiMap<>();
            this._parameters.addAllValues(this._queryParameters);
            this._parameters.addAllValues(this._contentParameters);
        } else {
            this._parameters = this._queryParameters;
        }
        MultiMap<String> parameters = this._parameters;
        return parameters == null ? NO_PARAMS : parameters;
    }

    private void extractQueryParameters() {
        MetaData.Request metadata = this._metaData;
        if (metadata != null && metadata.getURI() != null && metadata.getURI().hasQuery()) {
            this._queryParameters = new MultiMap<>();
            if (this._queryEncoding == null) {
                metadata.getURI().decodeQueryTo(this._queryParameters);
            } else {
                try {
                    metadata.getURI().decodeQueryTo(this._queryParameters, this._queryEncoding);
                } catch (UnsupportedEncodingException var3) {
                    if (LOG.isDebugEnabled()) {
                        LOG.warn(var3);
                    } else {
                        LOG.warn(var3.toString());
                    }
                }
            }
        } else {
            this._queryParameters = NO_PARAMS;
        }
    }

    private void extractContentParameters() {
        String contentType = this.getContentType();
        if (contentType != null && !contentType.isEmpty()) {
            this._contentParameters = new MultiMap<>();
            contentType = HttpFields.valueParameters(contentType, null);
            int contentLength = this.getContentLength();
            if (contentLength != 0) {
                if (MimeTypes.Type.FORM_ENCODED.is(contentType) && this._inputState == 0 && this._channel.getHttpConfiguration().isFormEncodedMethod(this.getMethod())) {
                    this.extractFormParameters(this._contentParameters);
                } else if (contentType.startsWith("multipart/form-data") && this.getAttribute("info.journeymap.shaded.org.eclipse.jetty.multipartConfig") != null && this._multiPartInputStream == null) {
                    this.extractMultipartParameters(this._contentParameters);
                }
            }
        } else {
            this._contentParameters = NO_PARAMS;
        }
    }

    public void extractFormParameters(MultiMap<String> params) {
        try {
            int maxFormContentSize = -1;
            int maxFormKeys = -1;
            if (this._context != null) {
                maxFormContentSize = this._context.getContextHandler().getMaxFormContentSize();
                maxFormKeys = this._context.getContextHandler().getMaxFormKeys();
            }
            if (maxFormContentSize < 0) {
                Object obj = this._channel.getServer().getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.Request.maxFormContentSize");
                if (obj == null) {
                    maxFormContentSize = 200000;
                } else if (obj instanceof Number) {
                    Number size = (Number) obj;
                    maxFormContentSize = size.intValue();
                } else if (obj instanceof String) {
                    maxFormContentSize = Integer.valueOf((String) obj);
                }
            }
            if (maxFormKeys < 0) {
                Object obj = this._channel.getServer().getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.Request.maxFormKeys");
                if (obj == null) {
                    maxFormKeys = 1000;
                } else if (obj instanceof Number) {
                    Number keys = (Number) obj;
                    maxFormKeys = keys.intValue();
                } else if (obj instanceof String) {
                    maxFormKeys = Integer.valueOf((String) obj);
                }
            }
            int contentLength = this.getContentLength();
            if (contentLength > maxFormContentSize && maxFormContentSize > 0) {
                throw new IllegalStateException("Form too large: " + contentLength + " > " + maxFormContentSize);
            }
            InputStream in = this.getInputStream();
            if (this._input.isAsync()) {
                throw new IllegalStateException("Cannot extract parameters with async IO");
            }
            UrlEncoded.decodeTo(in, params, this.getCharacterEncoding(), contentLength < 0 ? maxFormContentSize : -1, maxFormKeys);
        } catch (IOException var6) {
            if (LOG.isDebugEnabled()) {
                LOG.warn(var6);
            } else {
                LOG.warn(var6.toString());
            }
        }
    }

    private void extractMultipartParameters(MultiMap<String> result) {
        try {
            this.getParts(result);
        } catch (ServletException | IOException var3) {
            LOG.warn(var3);
            throw new RuntimeException(var3);
        }
    }

    @Override
    public AsyncContext getAsyncContext() {
        HttpChannelState state = this.getHttpChannelState();
        if (this._async != null && state.isAsyncStarted()) {
            return this._async;
        } else {
            throw new IllegalStateException(state.getStatusString());
        }
    }

    public HttpChannelState getHttpChannelState() {
        return this._channel.getState();
    }

    @Override
    public Object getAttribute(String name) {
        if (name.startsWith("info.journeymap.shaded.org.eclipse.jetty")) {
            if (Server.class.getName().equals(name)) {
                return this._channel.getServer();
            }
            if (HttpChannel.class.getName().equals(name)) {
                return this._channel;
            }
            if (HttpConnection.class.getName().equals(name) && this._channel.getHttpTransport() instanceof HttpConnection) {
                return this._channel.getHttpTransport();
            }
        }
        return this._attributes == null ? null : this._attributes.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this._attributes == null ? Collections.enumeration(Collections.emptyList()) : AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    public Attributes getAttributes() {
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        return this._attributes;
    }

    public Authentication getAuthentication() {
        return this._authentication;
    }

    @Override
    public String getAuthType() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred) this._authentication).authenticate(this));
        }
        return this._authentication instanceof Authentication.User ? ((Authentication.User) this._authentication).getAuthMethod() : null;
    }

    @Override
    public String getCharacterEncoding() {
        if (this._characterEncoding == null) {
            this.getContentType();
        }
        return this._characterEncoding;
    }

    public HttpChannel getHttpChannel() {
        return this._channel;
    }

    @Override
    public int getContentLength() {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return -1;
        } else {
            return metadata.getContentLength() != Long.MIN_VALUE ? (int) metadata.getContentLength() : (int) metadata.getFields().getLongField(HttpHeader.CONTENT_LENGTH.toString());
        }
    }

    @Override
    public long getContentLengthLong() {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return -1L;
        } else {
            return metadata.getContentLength() != Long.MIN_VALUE ? metadata.getContentLength() : metadata.getFields().getLongField(HttpHeader.CONTENT_LENGTH.toString());
        }
    }

    public long getContentRead() {
        return this._input.getContentConsumed();
    }

    @Override
    public String getContentType() {
        MetaData.Request metadata = this._metaData;
        String content_type = metadata == null ? null : metadata.getFields().get(HttpHeader.CONTENT_TYPE);
        if (this._characterEncoding == null && content_type != null) {
            MimeTypes.Type mime = MimeTypes.CACHE.get(content_type);
            String charset = mime != null && mime.getCharset() != null ? mime.getCharset().toString() : MimeTypes.getCharsetFromContentType(content_type);
            if (charset != null) {
                this._characterEncoding = charset;
            }
        }
        return content_type;
    }

    public ContextHandler.Context getContext() {
        return this._context;
    }

    @Override
    public String getContextPath() {
        return this._contextPath;
    }

    @Override
    public Cookie[] getCookies() {
        MetaData.Request metadata = this._metaData;
        if (metadata != null && !this._cookiesExtracted) {
            this._cookiesExtracted = true;
            for (String c : metadata.getFields().getValuesList(HttpHeader.COOKIE)) {
                if (this._cookies == null) {
                    this._cookies = new CookieCutter();
                }
                this._cookies.addCookieField(c);
            }
            return this._cookies != null && this._cookies.getCookies().length != 0 ? this._cookies.getCookies() : null;
        } else {
            return this._cookies != null && this._cookies.getCookies().length != 0 ? this._cookies.getCookies() : null;
        }
    }

    @Override
    public long getDateHeader(String name) {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? -1L : metadata.getFields().getDateField(name);
    }

    @Override
    public DispatcherType getDispatcherType() {
        return this._dispatcherType;
    }

    @Override
    public String getHeader(String name) {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getFields().get(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? Collections.emptyEnumeration() : metadata.getFields().getFieldNames();
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return Collections.emptyEnumeration();
        } else {
            Enumeration<String> e = metadata.getFields().getValues(name);
            return e == null ? Collections.enumeration(Collections.emptyList()) : e;
        }
    }

    public int getInputState() {
        return this._inputState;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this._inputState != 0 && this._inputState != 1) {
            throw new IllegalStateException("READER");
        } else {
            this._inputState = 1;
            if (this._channel.isExpecting100Continue()) {
                this._channel.continue100(this._input.available());
            }
            return this._input;
        }
    }

    @Override
    public int getIntHeader(String name) {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? -1 : (int) metadata.getFields().getLongField(name);
    }

    @Override
    public Locale getLocale() {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return Locale.getDefault();
        } else {
            List<String> acceptable = metadata.getFields().getQualityCSV(HttpHeader.ACCEPT_LANGUAGE);
            if (acceptable.isEmpty()) {
                return Locale.getDefault();
            } else {
                String language = (String) acceptable.get(0);
                language = HttpFields.stripParameters(language);
                String country = "";
                int dash = language.indexOf(45);
                if (dash > -1) {
                    country = language.substring(dash + 1).trim();
                    language = language.substring(0, dash).trim();
                }
                return new Locale(language, country);
            }
        }
    }

    @Override
    public Enumeration<Locale> getLocales() {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return Collections.enumeration(__defaultLocale);
        } else {
            List<String> acceptable = metadata.getFields().getQualityCSV(HttpHeader.ACCEPT_LANGUAGE);
            if (acceptable.isEmpty()) {
                return Collections.enumeration(__defaultLocale);
            } else {
                List<Locale> locales = (List<Locale>) acceptable.stream().map(language -> {
                    language = HttpFields.stripParameters(language);
                    String country = "";
                    int dash = language.indexOf(45);
                    if (dash > -1) {
                        country = language.substring(dash + 1).trim();
                        language = language.substring(0, dash).trim();
                    }
                    return new Locale(language, country);
                }).collect(Collectors.toList());
                return Collections.enumeration(locales);
            }
        }
    }

    @Override
    public String getLocalAddr() {
        if (this._channel == null) {
            try {
                String name = InetAddress.getLocalHost().getHostAddress();
                if ("0.0.0.0".equals(name)) {
                    return null;
                }
                return name;
            } catch (UnknownHostException var3) {
                LOG.ignore(var3);
            }
        }
        InetSocketAddress local = this._channel.getLocalAddress();
        if (local == null) {
            return "";
        } else {
            InetAddress address = local.getAddress();
            return address == null ? local.getHostString() : address.getHostAddress();
        }
    }

    @Override
    public String getLocalName() {
        if (this._channel != null) {
            InetSocketAddress local = this._channel.getLocalAddress();
            if (local != null) {
                return local.getHostString();
            }
        }
        try {
            String name = InetAddress.getLocalHost().getHostName();
            return "0.0.0.0".equals(name) ? null : name;
        } catch (UnknownHostException var2) {
            LOG.ignore(var2);
            return null;
        }
    }

    @Override
    public int getLocalPort() {
        if (this._channel == null) {
            return 0;
        } else {
            InetSocketAddress local = this._channel.getLocalAddress();
            return local == null ? 0 : local.getPort();
        }
    }

    @Override
    public String getMethod() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getMethod();
    }

    @Override
    public String getParameter(String name) {
        return this.getParameters().getValue(name, 0);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(this.getParameters().toStringArrayMap());
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.getParameters().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        List<String> vals = this.getParameters().getValues(name);
        return vals == null ? null : (String[]) vals.toArray(new String[vals.size()]);
    }

    public MultiMap<String> getQueryParameters() {
        return this._queryParameters;
    }

    public void setQueryParameters(MultiMap<String> queryParameters) {
        this._queryParameters = queryParameters;
    }

    public void setContentParameters(MultiMap<String> contentParameters) {
        this._contentParameters = contentParameters;
    }

    public void resetParameters() {
        this._parameters = null;
    }

    @Override
    public String getPathInfo() {
        return this._pathInfo;
    }

    @Override
    public String getPathTranslated() {
        return this._pathInfo != null && this._context != null ? this._context.getRealPath(this._pathInfo) : null;
    }

    @Override
    public String getProtocol() {
        MetaData.Request metadata = this._metaData;
        if (metadata == null) {
            return null;
        } else {
            HttpVersion version = metadata.getHttpVersion();
            return version == null ? null : version.toString();
        }
    }

    public HttpVersion getHttpVersion() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getHttpVersion();
    }

    public String getQueryEncoding() {
        return this._queryEncoding;
    }

    @Override
    public String getQueryString() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getURI().getQuery();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this._inputState != 0 && this._inputState != 2) {
            throw new IllegalStateException("STREAMED");
        } else if (this._inputState == 2) {
            return this._reader;
        } else {
            String encoding = this.getCharacterEncoding();
            if (encoding == null) {
                encoding = "iso-8859-1";
            }
            if (this._reader == null || !encoding.equalsIgnoreCase(this._readerEncoding)) {
                final ServletInputStream in = this.getInputStream();
                this._readerEncoding = encoding;
                this._reader = new BufferedReader(new InputStreamReader(in, encoding)) {

                    public void close() throws IOException {
                        in.close();
                    }
                };
            }
            this._inputState = 2;
            return this._reader;
        }
    }

    @Override
    public String getRealPath(String path) {
        return this._context == null ? null : this._context.getRealPath(path);
    }

    public InetSocketAddress getRemoteInetSocketAddress() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return remote;
    }

    @Override
    public String getRemoteAddr() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        if (remote == null) {
            return "";
        } else {
            InetAddress address = remote.getAddress();
            return address == null ? remote.getHostString() : address.getHostAddress();
        }
    }

    @Override
    public String getRemoteHost() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return remote == null ? "" : remote.getHostString();
    }

    @Override
    public int getRemotePort() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return remote == null ? 0 : remote.getPort();
    }

    @Override
    public String getRemoteUser() {
        Principal p = this.getUserPrincipal();
        return p == null ? null : p.getName();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        path = URIUtil.compactPath(path);
        if (path != null && this._context != null) {
            if (!path.startsWith("/")) {
                String relTo = URIUtil.addPaths(this._servletPath, this._pathInfo);
                int slash = relTo.lastIndexOf("/");
                if (slash > 1) {
                    relTo = relTo.substring(0, slash + 1);
                } else {
                    relTo = "/";
                }
                path = URIUtil.addPaths(URIUtil.encodePath(relTo), path);
            }
            return this._context.getRequestDispatcher(path);
        } else {
            return null;
        }
    }

    @Override
    public String getRequestedSessionId() {
        return this._requestedSessionId;
    }

    @Override
    public String getRequestURI() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getURI().getPath();
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer(128);
        URIUtil.appendSchemeHostPort(url, this.getScheme(), this.getServerName(), this.getServerPort());
        url.append(this.getRequestURI());
        return url;
    }

    public Response getResponse() {
        return this._channel.getResponse();
    }

    public StringBuilder getRootURL() {
        StringBuilder url = new StringBuilder(128);
        URIUtil.appendSchemeHostPort(url, this.getScheme(), this.getServerName(), this.getServerPort());
        return url;
    }

    @Override
    public String getScheme() {
        MetaData.Request metadata = this._metaData;
        String scheme = metadata == null ? null : metadata.getURI().getScheme();
        return scheme == null ? HttpScheme.HTTP.asString() : scheme;
    }

    @Override
    public String getServerName() {
        MetaData.Request metadata = this._metaData;
        String name = metadata == null ? null : metadata.getURI().getHost();
        return name != null ? name : this.findServerName();
    }

    private String findServerName() {
        MetaData.Request metadata = this._metaData;
        HttpField host = metadata == null ? null : metadata.getFields().getField(HttpHeader.HOST);
        if (host != null) {
            if (!(host instanceof HostPortHttpField) && host.getValue() != null && !host.getValue().isEmpty()) {
                host = new HostPortHttpField(host.getValue());
            }
            if (host instanceof HostPortHttpField) {
                HostPortHttpField authority = (HostPortHttpField) host;
                metadata.getURI().setAuthority(authority.getHost(), authority.getPort());
                return authority.getHost();
            }
        }
        String name = this.getLocalName();
        if (name != null) {
            return name;
        } else {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException var5) {
                LOG.ignore(var5);
                return null;
            }
        }
    }

    @Override
    public int getServerPort() {
        MetaData.Request metadata = this._metaData;
        HttpURI uri = metadata == null ? null : metadata.getURI();
        int port = uri != null && uri.getHost() != null ? uri.getPort() : this.findServerPort();
        if (port <= 0) {
            return this.getScheme().equalsIgnoreCase("https") ? 443 : 80;
        } else {
            return port;
        }
    }

    private int findServerPort() {
        MetaData.Request metadata = this._metaData;
        HttpField host = metadata == null ? null : metadata.getFields().getField(HttpHeader.HOST);
        if (host != null) {
            HostPortHttpField authority = host instanceof HostPortHttpField ? (HostPortHttpField) host : new HostPortHttpField(host.getValue());
            metadata.getURI().setAuthority(authority.getHost(), authority.getPort());
            return authority.getPort();
        } else {
            return this._channel != null ? this.getLocalPort() : -1;
        }
    }

    @Override
    public ServletContext getServletContext() {
        return this._context;
    }

    public String getServletName() {
        return this._scope != null ? this._scope.getName() : null;
    }

    @Override
    public String getServletPath() {
        if (this._servletPath == null) {
            this._servletPath = "";
        }
        return this._servletPath;
    }

    public ServletResponse getServletResponse() {
        return this._channel.getResponse();
    }

    @Override
    public String changeSessionId() {
        HttpSession session = this.getSession(false);
        if (session == null) {
            throw new IllegalStateException("No session");
        } else {
            if (session instanceof Session) {
                Session s = (Session) session;
                s.renewId(this);
                if (this.getRemoteUser() != null) {
                    s.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure", Boolean.TRUE);
                }
                if (s.isIdChanged() && this._sessionHandler.isUsingCookies()) {
                    this._channel.getResponse().addCookie(this._sessionHandler.getSessionCookie(s, this.getContextPath(), this.isSecure()));
                }
            }
            return session.getId();
        }
    }

    @Override
    public HttpSession getSession() {
        return this.getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (this._session != null) {
            if (this._sessionHandler == null || this._sessionHandler.isValid(this._session)) {
                return this._session;
            }
            this._session = null;
        }
        if (!create) {
            return null;
        } else if (this.getResponse().isCommitted()) {
            throw new IllegalStateException("Response is committed");
        } else if (this._sessionHandler == null) {
            throw new IllegalStateException("No SessionManager");
        } else {
            this._session = this._sessionHandler.newHttpSession(this);
            HttpCookie cookie = this._sessionHandler.getSessionCookie(this._session, this.getContextPath(), this.isSecure());
            if (cookie != null) {
                this._channel.getResponse().addCookie(cookie);
            }
            return this._session;
        }
    }

    public SessionHandler getSessionHandler() {
        return this._sessionHandler;
    }

    public long getTimeStamp() {
        return this._timeStamp;
    }

    public HttpURI getHttpURI() {
        MetaData.Request metadata = this._metaData;
        return metadata == null ? null : metadata.getURI();
    }

    public String getOriginalURI() {
        return this._originalURI;
    }

    public void setHttpURI(HttpURI uri) {
        MetaData.Request metadata = this._metaData;
        metadata.setURI(uri);
    }

    public UserIdentity getUserIdentity() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred) this._authentication).authenticate(this));
        }
        return this._authentication instanceof Authentication.User ? ((Authentication.User) this._authentication).getUserIdentity() : null;
    }

    public UserIdentity getResolvedUserIdentity() {
        return this._authentication instanceof Authentication.User ? ((Authentication.User) this._authentication).getUserIdentity() : null;
    }

    public UserIdentity.Scope getUserIdentityScope() {
        return this._scope;
    }

    @Override
    public Principal getUserPrincipal() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred) this._authentication).authenticate(this));
        }
        if (this._authentication instanceof Authentication.User) {
            UserIdentity user = ((Authentication.User) this._authentication).getUserIdentity();
            return user.getUserPrincipal();
        } else {
            return null;
        }
    }

    public boolean isHandled() {
        return this._handled;
    }

    @Override
    public boolean isAsyncStarted() {
        return this.getHttpChannelState().isAsyncStarted();
    }

    @Override
    public boolean isAsyncSupported() {
        return this._asyncNotSupportedSource == null;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return this._requestedSessionId != null && this._requestedSessionIdFromCookie;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return this._requestedSessionId != null && !this._requestedSessionIdFromCookie;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return this._requestedSessionId != null && !this._requestedSessionIdFromCookie;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        if (this._requestedSessionId == null) {
            return false;
        } else {
            HttpSession session = this.getSession(false);
            return session != null && this._sessionHandler.getSessionIdManager().getId(this._requestedSessionId).equals(this._sessionHandler.getId(session));
        }
    }

    @Override
    public boolean isSecure() {
        return this._secure;
    }

    public void setSecure(boolean secure) {
        this._secure = secure;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred) this._authentication).authenticate(this));
        }
        return this._authentication instanceof Authentication.User ? ((Authentication.User) this._authentication).isUserInRole(this._scope, role) : false;
    }

    public void setMetaData(MetaData.Request request) {
        this._metaData = request;
        this.setMethod(request.getMethod());
        HttpURI uri = request.getURI();
        this._originalURI = uri.isAbsolute() && request.getHttpVersion() != HttpVersion.HTTP_2 ? uri.toString() : uri.getPathQuery();
        String path = uri.getDecodedPath();
        String info;
        if (path != null && path.length() != 0) {
            if (!path.startsWith("/")) {
                if (!"*".equals(path) && !HttpMethod.CONNECT.is(this.getMethod())) {
                    this.setPathInfo(path);
                    throw new BadMessageException(400, "Bad URI");
                }
                info = path;
            } else {
                info = URIUtil.canonicalPath(path);
            }
        } else {
            if (!uri.isAbsolute()) {
                this.setPathInfo("");
                throw new BadMessageException(400, "Bad URI");
            }
            path = "/";
            uri.setPath(path);
            info = path;
        }
        if (info == null) {
            this.setPathInfo(path);
            throw new BadMessageException(400, "Bad URI");
        } else {
            this.setPathInfo(info);
        }
    }

    public MetaData.Request getMetaData() {
        return this._metaData;
    }

    public boolean hasMetaData() {
        return this._metaData != null;
    }

    protected void recycle() {
        this._metaData = null;
        this._originalURI = null;
        if (this._context != null) {
            throw new IllegalStateException("Request in context!");
        } else {
            if (this._inputState == 2) {
                try {
                    int r = this._reader.read();
                    while (r != -1) {
                        r = this._reader.read();
                    }
                } catch (Exception var2) {
                    LOG.ignore(var2);
                    this._reader = null;
                }
            }
            this._dispatcherType = null;
            this.setAuthentication(Authentication.NOT_CHECKED);
            this.getHttpChannelState().recycle();
            if (this._async != null) {
                this._async.reset();
            }
            this._async = null;
            this._asyncNotSupportedSource = null;
            this._handled = false;
            if (this._attributes != null) {
                this._attributes.clearAttributes();
            }
            this._characterEncoding = null;
            this._contextPath = null;
            if (this._cookies != null) {
                this._cookies.reset();
            }
            this._cookiesExtracted = false;
            this._context = null;
            this._newContext = false;
            this._pathInfo = null;
            this._queryEncoding = null;
            this._requestedSessionId = null;
            this._requestedSessionIdFromCookie = false;
            this._secure = false;
            this._session = null;
            this._sessionHandler = null;
            this._scope = null;
            this._servletPath = null;
            this._timeStamp = 0L;
            this._queryParameters = null;
            this._contentParameters = null;
            this._parameters = null;
            this._contentParamsExtracted = false;
            this._inputState = 0;
            this._multiPartInputStream = null;
            this._remote = null;
            this._input.recycle();
            this._trailers = null;
        }
    }

    @Override
    public void removeAttribute(String name) {
        Object old_value = this._attributes == null ? null : this._attributes.getAttribute(name);
        if (this._attributes != null) {
            this._attributes.removeAttribute(name);
        }
        if (old_value != null && !this._requestAttributeListeners.isEmpty()) {
            ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(this._context, this, name, old_value);
            for (ServletRequestAttributeListener listener : this._requestAttributeListeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    public void removeEventListener(EventListener listener) {
        this._requestAttributeListeners.remove(listener);
    }

    public void setAsyncSupported(boolean supported, String source) {
        this._asyncNotSupportedSource = supported ? null : (source == null ? "unknown" : source);
    }

    @Override
    public void setAttribute(String name, Object value) {
        Object old_value = this._attributes == null ? null : this._attributes.getAttribute(name);
        if ("info.journeymap.shaded.org.eclipse.jetty.server.Request.queryEncoding".equals(name)) {
            this.setQueryEncoding(value == null ? null : value.toString());
        } else if ("info.journeymap.shaded.org.eclipse.jetty.server.sendContent".equals(name)) {
            LOG.warn("Deprecated: org.eclipse.jetty.server.sendContent");
        }
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        this._attributes.setAttribute(name, value);
        if (!this._requestAttributeListeners.isEmpty()) {
            ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(this._context, this, name, old_value == null ? value : old_value);
            for (ServletRequestAttributeListener l : this._requestAttributeListeners) {
                if (old_value == null) {
                    l.attributeAdded(event);
                } else if (value == null) {
                    l.attributeRemoved(event);
                } else {
                    l.attributeReplaced(event);
                }
            }
        }
    }

    public void setAttributes(Attributes attributes) {
        this._attributes = attributes;
    }

    public void setAuthentication(Authentication authentication) {
        this._authentication = authentication;
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        if (this._inputState == 0) {
            this._characterEncoding = encoding;
            if (!StringUtil.isUTF8(encoding)) {
                try {
                    Charset.forName(encoding);
                } catch (UnsupportedCharsetException var3) {
                    throw new UnsupportedEncodingException(var3.getMessage());
                }
            }
        }
    }

    public void setCharacterEncodingUnchecked(String encoding) {
        this._characterEncoding = encoding;
    }

    public void setContentType(String contentType) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.getFields().put(HttpHeader.CONTENT_TYPE, contentType);
        }
    }

    public void setContext(ContextHandler.Context context) {
        this._newContext = this._context != context;
        this._context = context;
    }

    public boolean takeNewContext() {
        boolean nc = this._newContext;
        this._newContext = false;
        return nc;
    }

    public void setContextPath(String contextPath) {
        this._contextPath = contextPath;
    }

    public void setCookies(Cookie[] cookies) {
        if (this._cookies == null) {
            this._cookies = new CookieCutter();
        }
        this._cookies.setCookies(cookies);
    }

    public void setDispatcherType(DispatcherType type) {
        this._dispatcherType = type;
    }

    public void setHandled(boolean h) {
        this._handled = h;
    }

    public void setMethod(String method) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.setMethod(method);
        }
    }

    public void setHttpVersion(HttpVersion version) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.setHttpVersion(version);
        }
    }

    public boolean isHead() {
        MetaData.Request metadata = this._metaData;
        return metadata != null && HttpMethod.HEAD.is(metadata.getMethod());
    }

    public void setPathInfo(String pathInfo) {
        this._pathInfo = pathInfo;
    }

    public void setQueryEncoding(String queryEncoding) {
        this._queryEncoding = queryEncoding;
    }

    public void setQueryString(String queryString) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.getURI().setQuery(queryString);
        }
        this._queryEncoding = null;
    }

    public void setRemoteAddr(InetSocketAddress addr) {
        this._remote = addr;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this._requestedSessionId = requestedSessionId;
    }

    public void setRequestedSessionIdFromCookie(boolean requestedSessionIdCookie) {
        this._requestedSessionIdFromCookie = requestedSessionIdCookie;
    }

    public void setURIPathQuery(String requestURI) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.getURI().setPathQuery(requestURI);
        }
    }

    public void setScheme(String scheme) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.getURI().setScheme(scheme);
        }
    }

    public void setAuthority(String host, int port) {
        MetaData.Request metadata = this._metaData;
        if (metadata != null) {
            metadata.getURI().setAuthority(host, port);
        }
    }

    public void setServletPath(String servletPath) {
        this._servletPath = servletPath;
    }

    public void setSession(HttpSession session) {
        this._session = session;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this._sessionHandler = sessionHandler;
    }

    public void setTimeStamp(long ts) {
        this._timeStamp = ts;
    }

    public void setUserIdentityScope(UserIdentity.Scope scope) {
        this._scope = scope;
    }

    public void setTrailers(HttpFields trailers) {
        this._trailers = trailers;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        if (this._asyncNotSupportedSource != null) {
            throw new IllegalStateException("!asyncSupported: " + this._asyncNotSupportedSource);
        } else {
            HttpChannelState state = this.getHttpChannelState();
            if (this._async == null) {
                this._async = new AsyncContextState(state);
            }
            AsyncContextEvent event = new AsyncContextEvent(this._context, this._async, state, this, this, this.getResponse());
            state.startAsync(event);
            return this._async;
        }
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        if (this._asyncNotSupportedSource != null) {
            throw new IllegalStateException("!asyncSupported: " + this._asyncNotSupportedSource);
        } else {
            HttpChannelState state = this.getHttpChannelState();
            if (this._async == null) {
                this._async = new AsyncContextState(state);
            }
            AsyncContextEvent event = new AsyncContextEvent(this._context, this._async, state, this, servletRequest, servletResponse);
            event.setDispatchContext(this.getServletContext());
            event.setDispatchPath(URIUtil.encodePath(URIUtil.addPaths(this.getServletPath(), this.getPathInfo())));
            state.startAsync(event);
            return this._async;
        }
    }

    public String toString() {
        return String.format("%s%s%s %s%s@%x", this.getClass().getSimpleName(), this._handled ? "[" : "(", this.getMethod(), this.getHttpURI(), this._handled ? "]" : ")", this.hashCode());
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred) this._authentication).authenticate(this, response));
            return !(this._authentication instanceof Authentication.ResponseSent);
        } else {
            response.sendError(401);
            return false;
        }
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        this.getParts();
        return this._multiPartInputStream.getPart(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        if (this.getContentType() != null && this.getContentType().startsWith("multipart/form-data")) {
            return this.getParts(null);
        } else {
            throw new ServletException("Content-Type != multipart/form-data");
        }
    }

    private Collection<Part> getParts(MultiMap<String> params) throws IOException, ServletException {
        if (this._multiPartInputStream == null) {
            this._multiPartInputStream = (MultiPartInputStreamParser) this.getAttribute("info.journeymap.shaded.org.eclipse.jetty.multiPartInputStream");
        }
        if (this._multiPartInputStream == null) {
            MultipartConfigElement config = (MultipartConfigElement) this.getAttribute("info.journeymap.shaded.org.eclipse.jetty.multipartConfig");
            if (config == null) {
                throw new IllegalStateException("No multipart config for servlet");
            }
            this._multiPartInputStream = new MultiPartInputStreamParser(this.getInputStream(), this.getContentType(), config, this._context != null ? (File) this._context.getAttribute("info.journeymap.shaded.org.javax.servlet.context.tempdir") : null);
            this.setAttribute("info.journeymap.shaded.org.eclipse.jetty.multiPartInputStream", this._multiPartInputStream);
            this.setAttribute("info.journeymap.shaded.org.eclipse.jetty.multiPartContext", this._context);
            Collection<Part> parts = this._multiPartInputStream.getParts();
            ByteArrayOutputStream os = null;
            for (Part p : parts) {
                MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart) p;
                if (mp.getContentDispositionFilename() == null) {
                    String charset = null;
                    if (mp.getContentType() != null) {
                        charset = MimeTypes.getCharsetFromContentType(mp.getContentType());
                    }
                    InputStream is = mp.getInputStream();
                    Throwable var10 = null;
                    try {
                        if (os == null) {
                            os = new ByteArrayOutputStream();
                        }
                        IO.copy(is, os);
                        String content = new String(os.toByteArray(), charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset));
                        if (this._contentParameters == null) {
                            this._contentParameters = params == null ? new MultiMap<>() : params;
                        }
                        this._contentParameters.add(mp.getName(), content);
                    } catch (Throwable var19) {
                        var10 = var19;
                        throw var19;
                    } finally {
                        if (is != null) {
                            if (var10 != null) {
                                try {
                                    is.close();
                                } catch (Throwable var18) {
                                    var10.addSuppressed(var18);
                                }
                            } else {
                                is.close();
                            }
                        }
                    }
                    os.reset();
                }
            }
        }
        return this._multiPartInputStream.getParts();
    }

    @Override
    public void login(String username, String password) throws ServletException {
        if (this._authentication instanceof Authentication.Deferred) {
            this._authentication = ((Authentication.Deferred) this._authentication).login(username, password, this);
            if (this._authentication == null) {
                throw new Authentication.Failed("Authentication failed for username '" + username + "'");
            }
        } else {
            throw new Authentication.Failed("Authenticated failed for username '" + username + "'. Already authenticated as " + this._authentication);
        }
    }

    @Override
    public void logout() throws ServletException {
        if (this._authentication instanceof Authentication.User) {
            ((Authentication.User) this._authentication).logout();
        }
        this._authentication = Authentication.UNAUTHENTICATED;
    }

    public void mergeQueryParameters(String oldQuery, String newQuery, boolean updateQueryString) {
        MultiMap<String> newQueryParams = null;
        if (newQuery != null) {
            newQueryParams = new MultiMap<>();
            UrlEncoded.decodeTo(newQuery, newQueryParams, UrlEncoded.ENCODING);
        }
        MultiMap<String> oldQueryParams = this._queryParameters;
        if (oldQueryParams == null && oldQuery != null) {
            oldQueryParams = new MultiMap<>();
            UrlEncoded.decodeTo(oldQuery, oldQueryParams, this.getQueryEncoding());
        }
        MultiMap<String> mergedQueryParams;
        if (newQueryParams == null || newQueryParams.size() == 0) {
            mergedQueryParams = oldQueryParams == null ? NO_PARAMS : oldQueryParams;
        } else if (oldQueryParams != null && oldQueryParams.size() != 0) {
            mergedQueryParams = new MultiMap<>(newQueryParams);
            mergedQueryParams.addAllValues(oldQueryParams);
        } else {
            mergedQueryParams = newQueryParams == null ? NO_PARAMS : newQueryParams;
        }
        this.setQueryParameters(mergedQueryParams);
        this.resetParameters();
        if (updateQueryString) {
            if (newQuery == null) {
                this.setQueryString(oldQuery);
            } else if (oldQuery == null) {
                this.setQueryString(newQuery);
            } else {
                StringBuilder mergedQuery = new StringBuilder();
                if (newQuery != null) {
                    mergedQuery.append(newQuery);
                }
                for (Entry<String, List<String>> entry : mergedQueryParams.entrySet()) {
                    if (newQueryParams == null || !newQueryParams.containsKey(entry.getKey())) {
                        for (String value : (List) entry.getValue()) {
                            if (mergedQuery.length() > 0) {
                                mergedQuery.append("&");
                            }
                            URIUtil.encodePath(mergedQuery, (String) entry.getKey());
                            mergedQuery.append('=');
                            URIUtil.encodePath(mergedQuery, value);
                        }
                    }
                }
                this.setQueryString(mergedQuery.toString());
            }
        }
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        throw new ServletException("HttpServletRequest.upgrade() not supported in Jetty");
    }
}
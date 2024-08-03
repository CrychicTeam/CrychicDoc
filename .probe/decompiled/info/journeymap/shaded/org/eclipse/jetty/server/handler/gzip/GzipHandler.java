package info.journeymap.shaded.org.eclipse.jetty.server.handler.gzip;

import info.journeymap.shaded.org.eclipse.jetty.http.CompressedContentFormat;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathSpecSet;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpOutput;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.HandlerWrapper;
import info.journeymap.shaded.org.eclipse.jetty.util.IncludeExclude;
import info.journeymap.shaded.org.eclipse.jetty.util.RegexSet;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.zip.Deflater;

public class GzipHandler extends HandlerWrapper implements GzipFactory {

    public static final String GZIP = "gzip";

    public static final String DEFLATE = "deflate";

    public static final int DEFAULT_MIN_GZIP_SIZE = 16;

    private static final Logger LOG = Log.getLogger(GzipHandler.class);

    private int _minGzipSize = 16;

    private int _compressionLevel = -1;

    private boolean _checkGzExists = true;

    private boolean _syncFlush = false;

    private int _inflateBufferSize = -1;

    private EnumSet<DispatcherType> _dispatchers = EnumSet.of(DispatcherType.REQUEST);

    private final ThreadLocal<Deflater> _deflater = new ThreadLocal();

    private final IncludeExclude<String> _agentPatterns = new IncludeExclude<>(RegexSet.class);

    private final IncludeExclude<String> _methods = new IncludeExclude<>();

    private final IncludeExclude<String> _paths = new IncludeExclude<>(PathSpecSet.class);

    private final IncludeExclude<String> _mimeTypes = new IncludeExclude<>();

    private HttpField _vary;

    public GzipHandler() {
        this._methods.include(HttpMethod.GET.asString());
        for (String type : MimeTypes.getKnownMimeTypes()) {
            if ("image/svg+xml".equals(type)) {
                this._paths.exclude("*.svgz");
            } else if (type.startsWith("image/") || type.startsWith("audio/") || type.startsWith("video/")) {
                this._mimeTypes.exclude(type);
            }
        }
        this._mimeTypes.exclude("application/compress");
        this._mimeTypes.exclude("application/zip");
        this._mimeTypes.exclude("application/gzip");
        this._mimeTypes.exclude("application/bzip2");
        this._mimeTypes.exclude("application/brotli");
        this._mimeTypes.exclude("application/x-xz");
        this._mimeTypes.exclude("application/x-rar-compressed");
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} mime types {}", this, this._mimeTypes);
        }
        this._agentPatterns.exclude(".*MSIE 6.0.*");
    }

    public void addExcludedAgentPatterns(String... patterns) {
        this._agentPatterns.exclude(patterns);
    }

    public void addExcludedMethods(String... methods) {
        for (String m : methods) {
            this._methods.exclude(m);
        }
    }

    public EnumSet<DispatcherType> getDispatcherTypes() {
        return this._dispatchers;
    }

    public void setDispatcherTypes(EnumSet<DispatcherType> dispatchers) {
        this._dispatchers = dispatchers;
    }

    public void setDispatcherTypes(DispatcherType... dispatchers) {
        this._dispatchers = EnumSet.copyOf(Arrays.asList(dispatchers));
    }

    public void addExcludedMimeTypes(String... types) {
        for (String t : types) {
            this._mimeTypes.exclude(StringUtil.csvSplit(t));
        }
    }

    public void addExcludedPaths(String... pathspecs) {
        for (String p : pathspecs) {
            this._paths.exclude(StringUtil.csvSplit(p));
        }
    }

    public void addIncludedAgentPatterns(String... patterns) {
        this._agentPatterns.include(patterns);
    }

    public void addIncludedMethods(String... methods) {
        for (String m : methods) {
            this._methods.include(m);
        }
    }

    public boolean isSyncFlush() {
        return this._syncFlush;
    }

    public void setSyncFlush(boolean syncFlush) {
        this._syncFlush = syncFlush;
    }

    public void addIncludedMimeTypes(String... types) {
        for (String t : types) {
            this._mimeTypes.include(StringUtil.csvSplit(t));
        }
    }

    public void addIncludedPaths(String... pathspecs) {
        for (String p : pathspecs) {
            this._paths.include(StringUtil.csvSplit(p));
        }
    }

    @Override
    protected void doStart() throws Exception {
        this._vary = this._agentPatterns.size() > 0 ? GzipHttpOutputInterceptor.VARY_ACCEPT_ENCODING_USER_AGENT : GzipHttpOutputInterceptor.VARY_ACCEPT_ENCODING;
        super.doStart();
    }

    public boolean getCheckGzExists() {
        return this._checkGzExists;
    }

    public int getCompressionLevel() {
        return this._compressionLevel;
    }

    @Override
    public Deflater getDeflater(Request request, long content_length) {
        String ua = request.getHttpFields().get(HttpHeader.USER_AGENT);
        if (ua != null && !this.isAgentGzipable(ua)) {
            LOG.debug("{} excluded user agent {}", this, request);
            return null;
        } else if (content_length >= 0L && content_length < (long) this._minGzipSize) {
            LOG.debug("{} excluded minGzipSize {}", this, request);
            return null;
        } else {
            HttpField accept = request.getHttpFields().getField(HttpHeader.ACCEPT_ENCODING);
            if (accept == null) {
                LOG.debug("{} excluded !accept {}", this, request);
                return null;
            } else {
                boolean gzip = accept.contains("gzip");
                if (!gzip) {
                    LOG.debug("{} excluded not gzip accept {}", this, request);
                    return null;
                } else {
                    Deflater df = (Deflater) this._deflater.get();
                    if (df == null) {
                        df = new Deflater(this._compressionLevel, true);
                    } else {
                        this._deflater.set(null);
                    }
                    return df;
                }
            }
        }
    }

    public String[] getExcludedAgentPatterns() {
        Set<String> excluded = this._agentPatterns.getExcluded();
        return (String[]) excluded.toArray(new String[excluded.size()]);
    }

    public String[] getExcludedMethods() {
        Set<String> excluded = this._methods.getExcluded();
        return (String[]) excluded.toArray(new String[excluded.size()]);
    }

    public String[] getExcludedMimeTypes() {
        Set<String> excluded = this._mimeTypes.getExcluded();
        return (String[]) excluded.toArray(new String[excluded.size()]);
    }

    public String[] getExcludedPaths() {
        Set<String> excluded = this._paths.getExcluded();
        return (String[]) excluded.toArray(new String[excluded.size()]);
    }

    public String[] getIncludedAgentPatterns() {
        Set<String> includes = this._agentPatterns.getIncluded();
        return (String[]) includes.toArray(new String[includes.size()]);
    }

    public String[] getIncludedMethods() {
        Set<String> includes = this._methods.getIncluded();
        return (String[]) includes.toArray(new String[includes.size()]);
    }

    public String[] getIncludedMimeTypes() {
        Set<String> includes = this._mimeTypes.getIncluded();
        return (String[]) includes.toArray(new String[includes.size()]);
    }

    public String[] getIncludedPaths() {
        Set<String> includes = this._paths.getIncluded();
        return (String[]) includes.toArray(new String[includes.size()]);
    }

    @Deprecated
    public String[] getMethods() {
        return this.getIncludedMethods();
    }

    public int getMinGzipSize() {
        return this._minGzipSize;
    }

    protected HttpField getVaryField() {
        return this._vary;
    }

    public int getInflateBufferSize() {
        return this._inflateBufferSize;
    }

    public void setInflateBufferSize(int size) {
        this._inflateBufferSize = size;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = baseRequest.getServletContext();
        String path = context == null ? baseRequest.getRequestURI() : URIUtil.addPaths(baseRequest.getServletPath(), baseRequest.getPathInfo());
        LOG.debug("{} handle {} in {}", this, baseRequest, context);
        if (!this._dispatchers.contains(baseRequest.getDispatcherType())) {
            LOG.debug("{} excluded by dispatcherType {}", this, baseRequest.getDispatcherType());
            this._handler.handle(target, baseRequest, request, response);
        } else {
            if (this._inflateBufferSize > 0) {
                HttpField ce = baseRequest.getHttpFields().getField(HttpHeader.CONTENT_ENCODING);
                if (ce != null && "gzip".equalsIgnoreCase(ce.getValue())) {
                    baseRequest.getHttpFields().remove(HttpHeader.CONTENT_ENCODING);
                    baseRequest.getHttpFields().add(new HttpField("X-Content-Encoding", ce.getValue()));
                    baseRequest.getHttpInput().addInterceptor(new GzipHttpInputInterceptor(baseRequest.getHttpChannel().getByteBufferPool(), this._inflateBufferSize));
                }
            }
            HttpOutput out = baseRequest.getResponse().getHttpOutput();
            for (HttpOutput.Interceptor interceptor = out.getInterceptor(); interceptor != null; interceptor = interceptor.getNextInterceptor()) {
                if (interceptor instanceof GzipHttpOutputInterceptor) {
                    LOG.debug("{} already intercepting {}", this, request);
                    this._handler.handle(target, baseRequest, request, response);
                    return;
                }
            }
            if (!this._methods.test(baseRequest.getMethod())) {
                LOG.debug("{} excluded by method {}", this, request);
                this._handler.handle(target, baseRequest, request, response);
            } else if (!this.isPathGzipable(path)) {
                LOG.debug("{} excluded by path {}", this, request);
                this._handler.handle(target, baseRequest, request, response);
            } else {
                String mimeType = context == null ? MimeTypes.getDefaultMimeByExtension(path) : context.getMimeType(path);
                if (mimeType != null) {
                    mimeType = MimeTypes.getContentTypeWithoutCharset(mimeType);
                    if (!this.isMimeTypeGzipable(mimeType)) {
                        LOG.debug("{} excluded by path suffix mime type {}", this, request);
                        this._handler.handle(target, baseRequest, request, response);
                        return;
                    }
                }
                if (this._checkGzExists && context != null) {
                    String realpath = request.getServletContext().getRealPath(path);
                    if (realpath != null) {
                        File gz = new File(realpath + ".gz");
                        if (gz.exists()) {
                            LOG.debug("{} gzip exists {}", this, request);
                            this._handler.handle(target, baseRequest, request, response);
                            return;
                        }
                    }
                }
                String etag = baseRequest.getHttpFields().get(HttpHeader.IF_NONE_MATCH);
                if (etag != null) {
                    int i = etag.indexOf(CompressedContentFormat.GZIP._etagQuote);
                    if (i > 0) {
                        baseRequest.setAttribute("o.e.j.s.h.gzip.GzipHandler.etag", etag);
                        while (i >= 0) {
                            etag = etag.substring(0, i) + etag.substring(i + CompressedContentFormat.GZIP._etag.length());
                            i = etag.indexOf(CompressedContentFormat.GZIP._etagQuote, i);
                        }
                        baseRequest.getHttpFields().put(new HttpField(HttpHeader.IF_NONE_MATCH, etag));
                    }
                }
                HttpOutput.Interceptor orig_interceptor = out.getInterceptor();
                try {
                    out.setInterceptor(new GzipHttpOutputInterceptor(this, this.getVaryField(), baseRequest.getHttpChannel(), orig_interceptor, this.isSyncFlush()));
                    if (this._handler != null) {
                        this._handler.handle(target, baseRequest, request, response);
                    }
                } finally {
                    if (!baseRequest.isHandled() && !baseRequest.isAsyncStarted()) {
                        out.setInterceptor(orig_interceptor);
                    }
                }
            }
        }
    }

    protected boolean isAgentGzipable(String ua) {
        return ua == null ? false : this._agentPatterns.test(ua);
    }

    @Override
    public boolean isMimeTypeGzipable(String mimetype) {
        return this._mimeTypes.test(mimetype);
    }

    protected boolean isPathGzipable(String requestURI) {
        return requestURI == null ? true : this._paths.test(requestURI);
    }

    @Override
    public void recycle(Deflater deflater) {
        if (this._deflater.get() == null) {
            deflater.reset();
            this._deflater.set(deflater);
        } else {
            deflater.end();
        }
    }

    public void setCheckGzExists(boolean checkGzExists) {
        this._checkGzExists = checkGzExists;
    }

    public void setCompressionLevel(int compressionLevel) {
        this._compressionLevel = compressionLevel;
    }

    public void setExcludedAgentPatterns(String... patterns) {
        this._agentPatterns.getExcluded().clear();
        this.addExcludedAgentPatterns(patterns);
    }

    public void setExcludedMethods(String... methods) {
        this._methods.getExcluded().clear();
        this._methods.exclude(methods);
    }

    public void setExcludedMimeTypes(String... types) {
        this._mimeTypes.getExcluded().clear();
        this._mimeTypes.exclude(types);
    }

    public void setExcludedPaths(String... pathspecs) {
        this._paths.getExcluded().clear();
        this._paths.exclude(pathspecs);
    }

    public void setIncludedAgentPatterns(String... patterns) {
        this._agentPatterns.getIncluded().clear();
        this.addIncludedAgentPatterns(patterns);
    }

    public void setIncludedMethods(String... methods) {
        this._methods.getIncluded().clear();
        this._methods.include(methods);
    }

    public void setIncludedMimeTypes(String... types) {
        this._mimeTypes.getIncluded().clear();
        this._mimeTypes.include(types);
    }

    public void setIncludedPaths(String... pathspecs) {
        this._paths.getIncluded().clear();
        this._paths.include(pathspecs);
    }

    public void setMinGzipSize(int minGzipSize) {
        this._minGzipSize = minGzipSize;
    }

    public void setIncludedMethodList(String csvMethods) {
        this.setIncludedMethods(StringUtil.csvSplit(csvMethods));
    }

    public String getIncludedMethodList() {
        return String.join(",", this.getIncludedMethods());
    }

    public void setExcludedMethodList(String csvMethods) {
        this.setExcludedMethods(StringUtil.csvSplit(csvMethods));
    }

    public String getExcludedMethodList() {
        return String.join(",", this.getExcludedMethods());
    }
}
package info.journeymap.shaded.kotlin.spark.staticfiles;

import info.journeymap.shaded.kotlin.spark.resource.AbstractFileResolvingResource;
import info.journeymap.shaded.kotlin.spark.resource.AbstractResourceHandler;
import info.journeymap.shaded.kotlin.spark.resource.ClassPathResourceHandler;
import info.journeymap.shaded.kotlin.spark.resource.ExternalResource;
import info.journeymap.shaded.kotlin.spark.resource.ExternalResourceHandler;
import info.journeymap.shaded.kotlin.spark.utils.Assert;
import info.journeymap.shaded.kotlin.spark.utils.GzipUtils;
import info.journeymap.shaded.kotlin.spark.utils.IOUtils;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticFilesConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(StaticFilesConfiguration.class);

    private List<AbstractResourceHandler> staticResourceHandlers = null;

    private boolean staticResourcesSet = false;

    private boolean externalStaticResourcesSet = false;

    public static StaticFilesConfiguration servletInstance = new StaticFilesConfiguration();

    private Map<String, String> customHeaders = new HashMap();

    public boolean consume(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        try {
            if (this.consumeWithFileResourceHandlers(httpRequest, httpResponse)) {
                return true;
            }
        } catch (DirectoryTraversal.DirectoryTraversalDetection var4) {
            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Bad request");
            httpResponse.getWriter().flush();
            this.LOG.warn(var4.getMessage() + " directory traversal detection for path: " + httpRequest.getPathInfo());
        }
        return false;
    }

    private boolean consumeWithFileResourceHandlers(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        if (this.staticResourceHandlers != null) {
            for (AbstractResourceHandler staticResourceHandler : this.staticResourceHandlers) {
                AbstractFileResolvingResource resource = staticResourceHandler.getResource(httpRequest);
                if (resource != null && resource.isReadable()) {
                    if (MimeType.shouldGuess()) {
                        httpResponse.setHeader("Content-Type", MimeType.fromResource(resource));
                    }
                    this.customHeaders.forEach(httpResponse::setHeader);
                    InputStream inputStream = resource.getInputStream();
                    Throwable var7 = null;
                    try {
                        OutputStream wrappedOutputStream = GzipUtils.checkAndWrap(httpRequest, httpResponse, false);
                        Throwable var9 = null;
                        try {
                            IOUtils.copy(inputStream, wrappedOutputStream);
                        } catch (Throwable var32) {
                            var9 = var32;
                            throw var32;
                        } finally {
                            if (wrappedOutputStream != null) {
                                if (var9 != null) {
                                    try {
                                        wrappedOutputStream.close();
                                    } catch (Throwable var31) {
                                        var9.addSuppressed(var31);
                                    }
                                } else {
                                    wrappedOutputStream.close();
                                }
                            }
                        }
                    } catch (Throwable var34) {
                        var7 = var34;
                        throw var34;
                    } finally {
                        if (inputStream != null) {
                            if (var7 != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable var30) {
                                    var7.addSuppressed(var30);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        if (this.staticResourceHandlers != null) {
            this.staticResourceHandlers.clear();
            this.staticResourceHandlers = null;
        }
        this.staticResourcesSet = false;
        this.externalStaticResourcesSet = false;
    }

    public synchronized void configure(String folder) {
        Assert.notNull(folder, "'folder' must not be null");
        if (!this.staticResourcesSet) {
            if (this.staticResourceHandlers == null) {
                this.staticResourceHandlers = new ArrayList();
            }
            this.staticResourceHandlers.add(new ClassPathResourceHandler(folder, "index.html"));
            this.LOG.info("StaticResourceHandler configured with folder = " + folder);
            StaticFilesFolder.localConfiguredTo(folder);
            this.staticResourcesSet = true;
        }
    }

    public synchronized void configureExternal(String folder) {
        Assert.notNull(folder, "'folder' must not be null");
        if (!this.externalStaticResourcesSet) {
            try {
                ExternalResource resource = new ExternalResource(folder);
                if (!resource.getFile().isDirectory()) {
                    this.LOG.error("External Static resource location must be a folder");
                    return;
                }
                if (this.staticResourceHandlers == null) {
                    this.staticResourceHandlers = new ArrayList();
                }
                this.staticResourceHandlers.add(new ExternalResourceHandler(folder, "index.html"));
                this.LOG.info("External StaticResourceHandler configured with folder = " + folder);
            } catch (IOException var3) {
                this.LOG.error("Error when creating external StaticResourceHandler", (Throwable) var3);
            }
            StaticFilesFolder.externalConfiguredTo(folder);
            this.externalStaticResourcesSet = true;
        }
    }

    public static StaticFilesConfiguration create() {
        return new StaticFilesConfiguration();
    }

    public void setExpireTimeSeconds(long expireTimeSeconds) {
        this.customHeaders.put("Cache-Control", "private, max-age=" + expireTimeSeconds);
        this.customHeaders.put("Expires", new Date(System.currentTimeMillis() + expireTimeSeconds * 1000L).toString());
    }

    public void putCustomHeaders(Map<String, String> headers) {
        this.customHeaders.putAll(headers);
    }

    public void putCustomHeader(String key, String value) {
        this.customHeaders.put(key, value);
    }
}
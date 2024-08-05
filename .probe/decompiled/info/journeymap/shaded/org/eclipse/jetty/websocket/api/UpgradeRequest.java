package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import java.net.HttpCookie;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UpgradeRequest {

    void addExtensions(ExtensionConfig... var1);

    void addExtensions(String... var1);

    @Deprecated
    void clearHeaders();

    List<HttpCookie> getCookies();

    List<ExtensionConfig> getExtensions();

    String getHeader(String var1);

    int getHeaderInt(String var1);

    Map<String, List<String>> getHeaders();

    List<String> getHeaders(String var1);

    String getHost();

    String getHttpVersion();

    String getMethod();

    String getOrigin();

    Map<String, List<String>> getParameterMap();

    String getProtocolVersion();

    String getQueryString();

    URI getRequestURI();

    Object getSession();

    List<String> getSubProtocols();

    Principal getUserPrincipal();

    boolean hasSubProtocol(String var1);

    boolean isOrigin(String var1);

    boolean isSecure();

    void setCookies(List<HttpCookie> var1);

    void setExtensions(List<ExtensionConfig> var1);

    void setHeader(String var1, List<String> var2);

    void setHeader(String var1, String var2);

    void setHeaders(Map<String, List<String>> var1);

    void setHttpVersion(String var1);

    void setMethod(String var1);

    void setRequestURI(URI var1);

    void setSession(Object var1);

    void setSubProtocols(List<String> var1);

    void setSubProtocols(String... var1);
}
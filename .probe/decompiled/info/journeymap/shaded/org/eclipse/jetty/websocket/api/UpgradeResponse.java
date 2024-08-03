package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UpgradeResponse {

    void addHeader(String var1, String var2);

    String getAcceptedSubProtocol();

    List<ExtensionConfig> getExtensions();

    String getHeader(String var1);

    Set<String> getHeaderNames();

    Map<String, List<String>> getHeaders();

    List<String> getHeaders(String var1);

    int getStatusCode();

    String getStatusReason();

    boolean isSuccess();

    void sendForbidden(String var1) throws IOException;

    void setAcceptedSubProtocol(String var1);

    void setExtensions(List<ExtensionConfig> var1);

    void setHeader(String var1, String var2);

    void setStatusCode(int var1);

    void setStatusReason(String var1);

    void setSuccess(boolean var1);
}
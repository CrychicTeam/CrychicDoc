package info.journeymap.shaded.org.eclipse.jetty.server;

import java.util.Set;

public interface PushBuilder {

    PushBuilder method(String var1);

    PushBuilder queryString(String var1);

    PushBuilder sessionId(String var1);

    PushBuilder conditional(boolean var1);

    PushBuilder setHeader(String var1, String var2);

    PushBuilder addHeader(String var1, String var2);

    PushBuilder removeHeader(String var1);

    PushBuilder path(String var1);

    PushBuilder etag(String var1);

    PushBuilder lastModified(String var1);

    void push();

    String getMethod();

    String getQueryString();

    String getSessionId();

    boolean isConditional();

    Set<String> getHeaderNames();

    String getHeader(String var1);

    String getPath();

    String getEtag();

    String getLastModified();
}
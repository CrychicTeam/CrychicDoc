package info.journeymap.shaded.org.javax.servlet.http;

import java.util.Set;

public interface PushBuilder {

    PushBuilder method(String var1);

    PushBuilder queryString(String var1);

    PushBuilder sessionId(String var1);

    PushBuilder setHeader(String var1, String var2);

    PushBuilder addHeader(String var1, String var2);

    PushBuilder removeHeader(String var1);

    PushBuilder path(String var1);

    void push();

    String getMethod();

    String getQueryString();

    String getSessionId();

    Set<String> getHeaderNames();

    String getHeader(String var1);

    String getPath();
}
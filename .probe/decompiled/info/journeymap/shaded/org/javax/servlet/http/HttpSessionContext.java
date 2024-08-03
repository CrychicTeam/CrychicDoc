package info.journeymap.shaded.org.javax.servlet.http;

import java.util.Enumeration;

@Deprecated
public interface HttpSessionContext {

    @Deprecated
    HttpSession getSession(String var1);

    @Deprecated
    Enumeration<String> getIds();
}
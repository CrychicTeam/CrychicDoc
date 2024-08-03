package info.journeymap.shaded.org.javax.servlet.http;

import info.journeymap.shaded.org.javax.servlet.ServletContext;
import java.util.Enumeration;

public interface HttpSession {

    long getCreationTime();

    String getId();

    long getLastAccessedTime();

    ServletContext getServletContext();

    void setMaxInactiveInterval(int var1);

    int getMaxInactiveInterval();

    @Deprecated
    HttpSessionContext getSessionContext();

    Object getAttribute(String var1);

    @Deprecated
    Object getValue(String var1);

    Enumeration<String> getAttributeNames();

    @Deprecated
    String[] getValueNames();

    void setAttribute(String var1, Object var2);

    @Deprecated
    void putValue(String var1, Object var2);

    void removeAttribute(String var1);

    @Deprecated
    void removeValue(String var1);

    void invalidate();

    boolean isNew();
}
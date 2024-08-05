package info.journeymap.shaded.org.eclipse.jetty.util;

import java.util.Enumeration;

public interface Attributes {

    void removeAttribute(String var1);

    void setAttribute(String var1, Object var2);

    Object getAttribute(String var1);

    Enumeration<String> getAttributeNames();

    void clearAttributes();
}
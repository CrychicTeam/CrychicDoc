package info.journeymap.shaded.org.javax.servlet;

import info.journeymap.shaded.org.javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

public interface ServletContext {

    String TEMPDIR = "info.journeymap.shaded.org.javax.servlet.context.tempdir";

    String ORDERED_LIBS = "info.journeymap.shaded.org.javax.servlet.context.orderedLibs";

    String getContextPath();

    ServletContext getContext(String var1);

    int getMajorVersion();

    int getMinorVersion();

    int getEffectiveMajorVersion();

    int getEffectiveMinorVersion();

    String getMimeType(String var1);

    Set<String> getResourcePaths(String var1);

    URL getResource(String var1) throws MalformedURLException;

    InputStream getResourceAsStream(String var1);

    RequestDispatcher getRequestDispatcher(String var1);

    RequestDispatcher getNamedDispatcher(String var1);

    @Deprecated
    Servlet getServlet(String var1) throws ServletException;

    @Deprecated
    Enumeration<Servlet> getServlets();

    @Deprecated
    Enumeration<String> getServletNames();

    void log(String var1);

    @Deprecated
    void log(Exception var1, String var2);

    void log(String var1, Throwable var2);

    String getRealPath(String var1);

    String getServerInfo();

    String getInitParameter(String var1);

    Enumeration<String> getInitParameterNames();

    boolean setInitParameter(String var1, String var2);

    Object getAttribute(String var1);

    Enumeration<String> getAttributeNames();

    void setAttribute(String var1, Object var2);

    void removeAttribute(String var1);

    String getServletContextName();

    ServletRegistration.Dynamic addServlet(String var1, String var2);

    ServletRegistration.Dynamic addServlet(String var1, Servlet var2);

    ServletRegistration.Dynamic addServlet(String var1, Class<? extends Servlet> var2);

    ServletRegistration.Dynamic addJspFile(String var1, String var2);

    <T extends Servlet> T createServlet(Class<T> var1) throws ServletException;

    ServletRegistration getServletRegistration(String var1);

    Map<String, ? extends ServletRegistration> getServletRegistrations();

    FilterRegistration.Dynamic addFilter(String var1, String var2);

    FilterRegistration.Dynamic addFilter(String var1, Filter var2);

    FilterRegistration.Dynamic addFilter(String var1, Class<? extends Filter> var2);

    <T extends Filter> T createFilter(Class<T> var1) throws ServletException;

    FilterRegistration getFilterRegistration(String var1);

    Map<String, ? extends FilterRegistration> getFilterRegistrations();

    SessionCookieConfig getSessionCookieConfig();

    void setSessionTrackingModes(Set<SessionTrackingMode> var1);

    Set<SessionTrackingMode> getDefaultSessionTrackingModes();

    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();

    void addListener(String var1);

    <T extends EventListener> void addListener(T var1);

    void addListener(Class<? extends EventListener> var1);

    <T extends EventListener> T createListener(Class<T> var1) throws ServletException;

    JspConfigDescriptor getJspConfigDescriptor();

    ClassLoader getClassLoader();

    void declareRoles(String... var1);

    String getVirtualServerName();

    int getSessionTimeout();

    void setSessionTimeout(int var1);

    String getRequestCharacterEncoding();

    void setRequestCharacterEncoding(String var1);

    String getResponseCharacterEncoding();

    void setResponseCharacterEncoding(String var1);
}
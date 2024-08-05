package info.journeymap.shaded.org.javax.servlet.http;

import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public interface HttpServletRequest extends ServletRequest {

    String BASIC_AUTH = "BASIC";

    String FORM_AUTH = "FORM";

    String CLIENT_CERT_AUTH = "CLIENT_CERT";

    String DIGEST_AUTH = "DIGEST";

    String getAuthType();

    Cookie[] getCookies();

    long getDateHeader(String var1);

    String getHeader(String var1);

    Enumeration<String> getHeaders(String var1);

    Enumeration<String> getHeaderNames();

    int getIntHeader(String var1);

    default HttpServletMapping getHttpServletMapping() {
        return new HttpServletMapping() {

            @Override
            public String getMatchValue() {
                return "";
            }

            @Override
            public String getPattern() {
                return "";
            }

            @Override
            public String getServletName() {
                return "";
            }

            @Override
            public MappingMatch getMappingMatch() {
                return null;
            }

            public String toString() {
                return "MappingImpl{matchValue=" + this.getMatchValue() + ", pattern=" + this.getPattern() + ", servletName=" + this.getServletName() + ", mappingMatch=" + this.getMappingMatch() + "} HttpServletRequest {" + HttpServletRequest.this.toString() + '}';
            }
        };
    }

    String getMethod();

    String getPathInfo();

    String getPathTranslated();

    default PushBuilder newPushBuilder() {
        return null;
    }

    String getContextPath();

    String getQueryString();

    String getRemoteUser();

    boolean isUserInRole(String var1);

    Principal getUserPrincipal();

    String getRequestedSessionId();

    String getRequestURI();

    StringBuffer getRequestURL();

    String getServletPath();

    HttpSession getSession(boolean var1);

    HttpSession getSession();

    String changeSessionId();

    boolean isRequestedSessionIdValid();

    boolean isRequestedSessionIdFromCookie();

    boolean isRequestedSessionIdFromURL();

    @Deprecated
    boolean isRequestedSessionIdFromUrl();

    boolean authenticate(HttpServletResponse var1) throws IOException, ServletException;

    void login(String var1, String var2) throws ServletException;

    void logout() throws ServletException;

    Collection<Part> getParts() throws IOException, ServletException;

    Part getPart(String var1) throws IOException, ServletException;

    <T extends HttpUpgradeHandler> T upgrade(Class<T> var1) throws IOException, ServletException;

    default Map<String, String> getTrailerFields() {
        return Collections.emptyMap();
    }

    default boolean isTrailerFieldsReady() {
        return true;
    }
}
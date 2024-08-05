package info.journeymap.shaded.org.javax.servlet.http;

import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

public class HttpServletRequestWrapper extends ServletRequestWrapper implements HttpServletRequest {

    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private HttpServletRequest _getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    @Override
    public String getAuthType() {
        return this._getHttpServletRequest().getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return this._getHttpServletRequest().getCookies();
    }

    @Override
    public long getDateHeader(String name) {
        return this._getHttpServletRequest().getDateHeader(name);
    }

    @Override
    public String getHeader(String name) {
        return this._getHttpServletRequest().getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return this._getHttpServletRequest().getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return this._getHttpServletRequest().getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return this._getHttpServletRequest().getIntHeader(name);
    }

    @Override
    public HttpServletMapping getHttpServletMapping() {
        return this._getHttpServletRequest().getHttpServletMapping();
    }

    @Override
    public String getMethod() {
        return this._getHttpServletRequest().getMethod();
    }

    @Override
    public String getPathInfo() {
        return this._getHttpServletRequest().getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return this._getHttpServletRequest().getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return this._getHttpServletRequest().getContextPath();
    }

    @Override
    public String getQueryString() {
        return this._getHttpServletRequest().getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return this._getHttpServletRequest().getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return this._getHttpServletRequest().isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return this._getHttpServletRequest().getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return this._getHttpServletRequest().getRequestedSessionId();
    }

    @Override
    public String getRequestURI() {
        return this._getHttpServletRequest().getRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return this._getHttpServletRequest().getRequestURL();
    }

    @Override
    public String getServletPath() {
        return this._getHttpServletRequest().getServletPath();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return this._getHttpServletRequest().getSession(create);
    }

    @Override
    public HttpSession getSession() {
        return this._getHttpServletRequest().getSession();
    }

    @Override
    public String changeSessionId() {
        return this._getHttpServletRequest().changeSessionId();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return this._getHttpServletRequest().isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return this._getHttpServletRequest().isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return this._getHttpServletRequest().isRequestedSessionIdFromURL();
    }

    @Deprecated
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return this._getHttpServletRequest().isRequestedSessionIdFromUrl();
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return this._getHttpServletRequest().authenticate(response);
    }

    @Override
    public void login(String username, String password) throws ServletException {
        this._getHttpServletRequest().login(username, password);
    }

    @Override
    public void logout() throws ServletException {
        this._getHttpServletRequest().logout();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return this._getHttpServletRequest().getParts();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return this._getHttpServletRequest().getPart(name);
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return this._getHttpServletRequest().upgrade(handlerClass);
    }

    @Override
    public PushBuilder newPushBuilder() {
        return this._getHttpServletRequest().newPushBuilder();
    }

    @Override
    public Map<String, String> getTrailerFields() {
        return this._getHttpServletRequest().getTrailerFields();
    }

    @Override
    public boolean isTrailerFieldsReady() {
        return this._getHttpServletRequest().isTrailerFieldsReady();
    }
}
package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.ServletResponseWrapper;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ServletResponseHttpWrapper extends ServletResponseWrapper implements HttpServletResponse {

    public ServletResponseHttpWrapper(ServletResponse response) {
        super(response);
    }

    @Override
    public void addCookie(Cookie cookie) {
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
    }

    @Override
    public void sendError(int sc) throws IOException {
    }

    @Override
    public void sendRedirect(String location) throws IOException {
    }

    @Override
    public void setDateHeader(String name, long date) {
    }

    @Override
    public void addDateHeader(String name, long date) {
    }

    @Override
    public void setHeader(String name, String value) {
    }

    @Override
    public void addHeader(String name, String value) {
    }

    @Override
    public void setIntHeader(String name, int value) {
    }

    @Override
    public void addIntHeader(String name, int value) {
    }

    @Override
    public void setStatus(int sc) {
    }

    @Override
    public void setStatus(int sc, String sm) {
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
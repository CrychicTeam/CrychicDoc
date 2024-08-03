package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.io.IOException;

public class Response {

    private static final Logger LOG = LoggerFactory.getLogger(Response.class);

    private HttpServletResponse response;

    private String body;

    protected Response() {
    }

    Response(HttpServletResponse response) {
        this.response = response;
    }

    public void status(int statusCode) {
        this.response.setStatus(statusCode);
    }

    public int status() {
        return this.response.getStatus();
    }

    public void type(String contentType) {
        this.response.setContentType(contentType);
    }

    public String type() {
        return this.response.getContentType();
    }

    public void body(String body) {
        this.body = body;
    }

    public String body() {
        return this.body;
    }

    public HttpServletResponse raw() {
        return this.response;
    }

    public void redirect(String location) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Redirecting ({} {} to {}", "Found", 302, location);
        }
        try {
            this.response.sendRedirect(location);
        } catch (IOException var3) {
            LOG.warn("Redirect failure", (Throwable) var3);
        }
    }

    public void redirect(String location, int httpStatusCode) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Redirecting ({} to {}", httpStatusCode, location);
        }
        this.response.setStatus(httpStatusCode);
        this.response.setHeader("Location", location);
        this.response.setHeader("Connection", "close");
        try {
            this.response.sendError(httpStatusCode);
        } catch (IOException var4) {
            LOG.warn("Exception when trying to redirect permanently", (Throwable) var4);
        }
    }

    public void header(String header, String value) {
        this.response.addHeader(header, value);
    }

    public void cookie(String name, String value) {
        this.cookie(name, value, -1, false);
    }

    public void cookie(String name, String value, int maxAge) {
        this.cookie(name, value, maxAge, false);
    }

    public void cookie(String name, String value, int maxAge, boolean secured) {
        this.cookie(name, value, maxAge, secured, false);
    }

    public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.cookie("", "", name, value, maxAge, secured, httpOnly);
    }

    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        this.cookie("", path, name, value, maxAge, secured, false);
    }

    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.cookie("", path, name, value, maxAge, secured, httpOnly);
    }

    public void cookie(String domain, String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookie.setHttpOnly(httpOnly);
        this.response.addCookie(cookie);
    }

    public void removeCookie(String name) {
        this.removeCookie(null, name);
    }

    public void removeCookie(String path, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath(path);
        cookie.setMaxAge(0);
        this.response.addCookie(cookie);
    }
}
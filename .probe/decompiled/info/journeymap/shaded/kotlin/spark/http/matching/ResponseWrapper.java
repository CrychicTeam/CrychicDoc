package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.Response;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;

class ResponseWrapper extends Response {

    private Response delegate;

    private boolean redirected = false;

    static ResponseWrapper create() {
        return new ResponseWrapper();
    }

    private ResponseWrapper() {
    }

    public void setDelegate(Response delegate) {
        this.delegate = delegate;
    }

    Response getDelegate() {
        return this.delegate;
    }

    @Override
    public void status(int statusCode) {
        this.delegate.status(statusCode);
    }

    @Override
    public int status() {
        return this.delegate.status();
    }

    @Override
    public void body(String body) {
        this.delegate.body(body);
    }

    @Override
    public String body() {
        return this.delegate.body();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    @Override
    public HttpServletResponse raw() {
        return this.delegate.raw();
    }

    @Override
    public void redirect(String location) {
        this.redirected = true;
        this.delegate.redirect(location);
    }

    @Override
    public void redirect(String location, int httpStatusCode) {
        this.redirected = true;
        this.delegate.redirect(location, httpStatusCode);
    }

    boolean isRedirected() {
        return this.redirected;
    }

    @Override
    public void header(String header, String value) {
        this.delegate.header(header, value);
    }

    public String toString() {
        return this.delegate.toString();
    }

    @Override
    public void type(String contentType) {
        this.delegate.type(contentType);
    }

    @Override
    public String type() {
        return this.delegate.type();
    }

    @Override
    public void cookie(String name, String value) {
        this.delegate.cookie(name, value);
    }

    @Override
    public void cookie(String name, String value, int maxAge) {
        this.delegate.cookie(name, value, maxAge);
    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured) {
        this.delegate.cookie(name, value, maxAge, secured);
    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        this.delegate.cookie(path, name, value, maxAge, secured);
    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.delegate.cookie(path, name, value, maxAge, secured, httpOnly);
    }

    @Override
    public void cookie(String domain, String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        this.delegate.cookie(domain, path, name, value, maxAge, secured, httpOnly);
    }

    @Override
    public void removeCookie(String name) {
        this.delegate.removeCookie(name);
    }

    @Override
    public void removeCookie(String path, String name) {
        this.delegate.removeCookie(path, name);
    }
}
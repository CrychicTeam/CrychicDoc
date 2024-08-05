package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.Access;
import info.journeymap.shaded.kotlin.spark.QueryParamsMap;
import info.journeymap.shaded.kotlin.spark.Request;
import info.journeymap.shaded.kotlin.spark.Session;
import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

final class RequestWrapper extends Request {

    private Request delegate;

    static RequestWrapper create() {
        return new RequestWrapper();
    }

    private RequestWrapper() {
    }

    public void setDelegate(Request delegate) {
        this.delegate = delegate;
    }

    Request getDelegate() {
        return this.delegate;
    }

    @Override
    public void changeMatch(RouteMatch match) {
        Access.changeMatch(this.delegate, match);
    }

    @Override
    public String requestMethod() {
        return this.delegate.requestMethod();
    }

    @Override
    public String scheme() {
        return this.delegate.scheme();
    }

    @Override
    public int port() {
        return this.delegate.port();
    }

    @Override
    public String pathInfo() {
        return this.delegate.pathInfo();
    }

    @Override
    public String servletPath() {
        return this.delegate.servletPath();
    }

    @Override
    public String contextPath() {
        return this.delegate.contextPath();
    }

    @Override
    public String contentType() {
        return this.delegate.contentType();
    }

    @Override
    public String body() {
        return this.delegate.body();
    }

    @Override
    public byte[] bodyAsBytes() {
        return this.delegate.bodyAsBytes();
    }

    @Override
    public int contentLength() {
        return this.delegate.contentLength();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    @Override
    public Map<String, String> params() {
        return this.delegate.params();
    }

    @Override
    public String params(String param) {
        return this.delegate.params(param);
    }

    @Override
    public String[] splat() {
        return this.delegate.splat();
    }

    @Override
    public String host() {
        return this.delegate.host();
    }

    @Override
    public String ip() {
        return this.delegate.ip();
    }

    @Override
    public String queryParams(String queryParam) {
        return this.delegate.queryParams(queryParam);
    }

    @Override
    public String[] queryParamsValues(String queryParam) {
        return this.delegate.queryParamsValues(queryParam);
    }

    @Override
    public String headers(String header) {
        return this.delegate.headers(header);
    }

    @Override
    public Set<String> queryParams() {
        return this.delegate.queryParams();
    }

    @Override
    public Set<String> headers() {
        return this.delegate.headers();
    }

    @Override
    public String queryString() {
        return this.delegate.queryString();
    }

    @Override
    public HttpServletRequest raw() {
        return this.delegate.raw();
    }

    public String toString() {
        return this.delegate.toString();
    }

    @Override
    public String userAgent() {
        return this.delegate.userAgent();
    }

    @Override
    public String url() {
        return this.delegate.url();
    }

    @Override
    public String uri() {
        return this.delegate.uri();
    }

    @Override
    public String protocol() {
        return this.delegate.protocol();
    }

    @Override
    public void attribute(String attribute, Object value) {
        this.delegate.attribute(attribute, value);
    }

    @Override
    public <T> T attribute(String attribute) {
        return this.delegate.attribute(attribute);
    }

    @Override
    public Set<String> attributes() {
        return this.delegate.attributes();
    }

    @Override
    public Session session() {
        return this.delegate.session();
    }

    @Override
    public Session session(boolean create) {
        return this.delegate.session(create);
    }

    @Override
    public QueryParamsMap queryMap() {
        return this.delegate.queryMap();
    }

    @Override
    public QueryParamsMap queryMap(String key) {
        return this.delegate.queryMap(key);
    }

    @Override
    public Map<String, String> cookies() {
        return this.delegate.cookies();
    }

    @Override
    public String cookie(String name) {
        return this.delegate.cookie(name);
    }
}
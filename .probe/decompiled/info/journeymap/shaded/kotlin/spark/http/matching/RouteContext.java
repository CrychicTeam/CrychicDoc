package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.Response;
import info.journeymap.shaded.kotlin.spark.route.HttpMethod;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;

final class RouteContext {

    private info.journeymap.shaded.kotlin.spark.route.Routes routeMatcher;

    private HttpServletRequest httpRequest;

    private String uri;

    private String acceptType;

    private Body body;

    private RequestWrapper requestWrapper;

    private ResponseWrapper responseWrapper;

    private Response response;

    private HttpMethod httpMethod;

    static RouteContext create() {
        return new RouteContext();
    }

    private RouteContext() {
    }

    public info.journeymap.shaded.kotlin.spark.route.Routes routeMatcher() {
        return this.routeMatcher;
    }

    public RouteContext withMatcher(info.journeymap.shaded.kotlin.spark.route.Routes routeMatcher) {
        this.routeMatcher = routeMatcher;
        return this;
    }

    public RouteContext withHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
        return this;
    }

    public RouteContext withAcceptType(String acceptType) {
        this.acceptType = acceptType;
        return this;
    }

    public RouteContext withBody(Body body) {
        this.body = body;
        return this;
    }

    public RouteContext withRequestWrapper(RequestWrapper requestWrapper) {
        this.requestWrapper = requestWrapper;
        return this;
    }

    public RouteContext withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public RouteContext withResponseWrapper(ResponseWrapper responseWrapper) {
        this.responseWrapper = responseWrapper;
        return this;
    }

    public RouteContext withResponse(Response response) {
        this.response = response;
        return this;
    }

    public RouteContext withHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public HttpServletRequest httpRequest() {
        return this.httpRequest;
    }

    public String uri() {
        return this.uri;
    }

    public String acceptType() {
        return this.acceptType;
    }

    public Body body() {
        return this.body;
    }

    public RequestWrapper requestWrapper() {
        return this.requestWrapper;
    }

    public ResponseWrapper responseWrapper() {
        return this.responseWrapper;
    }

    public Response response() {
        return this.response;
    }

    public HttpMethod httpMethod() {
        return this.httpMethod;
    }
}
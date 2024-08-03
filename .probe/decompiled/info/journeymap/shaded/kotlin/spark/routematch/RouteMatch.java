package info.journeymap.shaded.kotlin.spark.routematch;

public class RouteMatch {

    private Object target;

    private String matchUri;

    private String requestURI;

    private String acceptType;

    public RouteMatch(Object target, String matchUri, String requestUri, String acceptType) {
        this.target = target;
        this.matchUri = matchUri;
        this.requestURI = requestUri;
        this.acceptType = acceptType;
    }

    public String getAcceptType() {
        return this.acceptType;
    }

    public Object getTarget() {
        return this.target;
    }

    public String getMatchUri() {
        return this.matchUri;
    }

    public String getRequestURI() {
        return this.requestURI;
    }
}
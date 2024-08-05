package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;

public final class RequestResponseFactory {

    private RequestResponseFactory() {
    }

    public static Request create(HttpServletRequest request) {
        return new Request(request);
    }

    public static Request create(RouteMatch match, HttpServletRequest request) {
        return new Request(match, request);
    }

    public static Response create(HttpServletResponse response) {
        return new Response(response);
    }
}
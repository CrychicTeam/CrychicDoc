package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;

public final class Access {

    private Access() {
    }

    public static void changeMatch(Request request, RouteMatch match) {
        request.changeMatch(match);
    }
}
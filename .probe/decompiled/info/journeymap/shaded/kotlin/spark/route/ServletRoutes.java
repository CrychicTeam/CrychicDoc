package info.journeymap.shaded.kotlin.spark.route;

public final class ServletRoutes {

    private static Routes routes = null;

    private ServletRoutes() {
    }

    public static synchronized Routes get() {
        if (routes == null) {
            routes = new Routes();
        }
        return routes;
    }
}
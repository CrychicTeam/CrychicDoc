package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.util.HashMap;

public class CustomErrorPages {

    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorPages.class);

    public static final String NOT_FOUND = "<html><body><h2>404 Not found</h2></body></html>";

    public static final String INTERNAL_ERROR = "<html><body><h2>500 Internal Server Error</h2></body></html>";

    private final HashMap<Integer, Object> customPages = new HashMap();

    private final HashMap<Integer, String> defaultPages = new HashMap();

    public static boolean existsFor(int status) {
        return getInstance().customPages.containsKey(status);
    }

    public static Object getFor(int status, Request request, Response response) {
        Object customRenderer = getInstance().customPages.get(status);
        Object customPage = getInstance().getDefaultFor(status);
        if (customRenderer instanceof String) {
            customPage = customRenderer;
        } else if (customRenderer instanceof Route) {
            try {
                customPage = ((Route) customRenderer).handle(request, response);
            } catch (Exception var6) {
                LOG.warn("Custom error page handler for status code {} has thrown an exception: {}. Using default page instead.", status, var6.getMessage());
            }
        }
        return customPage;
    }

    public String getDefaultFor(int status) {
        String defaultPage = (String) this.defaultPages.get(status);
        return defaultPage != null ? defaultPage : "<html><body><h2>HTTP Status " + status + "</h2></body></html>";
    }

    static void add(int status, String page) {
        getInstance().customPages.put(status, page);
    }

    static void add(int status, Route route) {
        getInstance().customPages.put(status, route);
    }

    private CustomErrorPages() {
        this.defaultPages.put(404, "<html><body><h2>404 Not found</h2></body></html>");
        this.defaultPages.put(500, "<html><body><h2>500 Internal Server Error</h2></body></html>");
    }

    private static CustomErrorPages getInstance() {
        return CustomErrorPages.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        private static final CustomErrorPages INSTANCE = new CustomErrorPages();
    }
}
package info.journeymap.shaded.kotlin.spark.route;

import info.journeymap.shaded.kotlin.spark.FilterImpl;
import info.journeymap.shaded.kotlin.spark.RouteImpl;
import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;
import info.journeymap.shaded.kotlin.spark.utils.MimeParse;
import info.journeymap.shaded.kotlin.spark.utils.StringUtils;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {

    private static final Logger LOG = LoggerFactory.getLogger(Routes.class);

    private static final char SINGLE_QUOTE = '\'';

    private List<RouteEntry> routes = new ArrayList();

    public static Routes create() {
        return new Routes();
    }

    protected Routes() {
    }

    public void add(HttpMethod httpMethod, RouteImpl route) {
        this.add(httpMethod, route.getPath(), route.getAcceptType(), route);
    }

    public void add(HttpMethod httpMethod, FilterImpl filter) {
        this.add(httpMethod, filter.getPath(), filter.getAcceptType(), filter);
    }

    public RouteMatch find(HttpMethod httpMethod, String path, String acceptType) {
        List<RouteEntry> routeEntries = this.findTargetsForRequestedRoute(httpMethod, path);
        RouteEntry entry = this.findTargetWithGivenAcceptType(routeEntries, acceptType);
        return entry != null ? new RouteMatch(entry.target, entry.path, path, acceptType) : null;
    }

    public List<RouteMatch> findMultiple(HttpMethod httpMethod, String path, String acceptType) {
        List<RouteMatch> matchSet = new ArrayList();
        for (RouteEntry routeEntry : this.findTargetsForRequestedRoute(httpMethod, path)) {
            if (acceptType != null) {
                String bestMatch = MimeParse.bestMatch(Arrays.asList(routeEntry.acceptedType), acceptType);
                if (this.routeWithGivenAcceptType(bestMatch)) {
                    matchSet.add(new RouteMatch(routeEntry.target, routeEntry.path, path, acceptType));
                }
            } else {
                matchSet.add(new RouteMatch(routeEntry.target, routeEntry.path, path, acceptType));
            }
        }
        return matchSet;
    }

    public void clear() {
        this.routes.clear();
    }

    public boolean remove(String path, String httpMethod) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path cannot be null or blank");
        } else if (StringUtils.isEmpty(httpMethod)) {
            throw new IllegalArgumentException("httpMethod cannot be null or blank");
        } else {
            HttpMethod method = HttpMethod.valueOf(httpMethod);
            return this.removeRoute(method, path);
        }
    }

    public boolean remove(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path cannot be null or blank");
        } else {
            return this.removeRoute((HttpMethod) null, path);
        }
    }

    private void add(HttpMethod method, String url, String acceptedType, Object target) {
        RouteEntry entry = new RouteEntry();
        entry.httpMethod = method;
        entry.path = url;
        entry.target = target;
        entry.acceptedType = acceptedType;
        LOG.debug("Adds route: " + entry);
        this.routes.add(entry);
    }

    private Map<String, RouteEntry> getAcceptedMimeTypes(List<RouteEntry> routes) {
        Map<String, RouteEntry> acceptedTypes = new HashMap();
        for (RouteEntry routeEntry : routes) {
            if (!acceptedTypes.containsKey(routeEntry.acceptedType)) {
                acceptedTypes.put(routeEntry.acceptedType, routeEntry);
            }
        }
        return acceptedTypes;
    }

    private boolean routeWithGivenAcceptType(String bestMatch) {
        return !"".equals(bestMatch);
    }

    private List<RouteEntry> findTargetsForRequestedRoute(HttpMethod httpMethod, String path) {
        List<RouteEntry> matchSet = new ArrayList();
        for (RouteEntry entry : this.routes) {
            if (entry.matches(httpMethod, path)) {
                matchSet.add(entry);
            }
        }
        return matchSet;
    }

    private RouteEntry findTargetWithGivenAcceptType(List<RouteEntry> routeMatches, String acceptType) {
        if (acceptType != null && routeMatches.size() > 0) {
            Map<String, RouteEntry> acceptedMimeTypes = this.getAcceptedMimeTypes(routeMatches);
            String bestMatch = MimeParse.bestMatch(acceptedMimeTypes.keySet(), acceptType);
            return this.routeWithGivenAcceptType(bestMatch) ? (RouteEntry) acceptedMimeTypes.get(bestMatch) : null;
        } else {
            return routeMatches.size() > 0 ? (RouteEntry) routeMatches.get(0) : null;
        }
    }

    private boolean removeRoute(HttpMethod httpMethod, String path) {
        List<RouteEntry> forRemoval = new ArrayList();
        for (RouteEntry routeEntry : this.routes) {
            HttpMethod httpMethodToMatch = httpMethod;
            if (httpMethod == null) {
                httpMethodToMatch = routeEntry.httpMethod;
            }
            if (routeEntry.matches(httpMethodToMatch, path)) {
                LOG.debug("Removing path {}", path, httpMethod == null ? "" : " with HTTP method " + httpMethod);
                forRemoval.add(routeEntry);
            }
        }
        return this.routes.removeAll(forRemoval);
    }

    @Deprecated
    public void add(String route, String acceptType, Object target) {
        try {
            int singleQuoteIndex = route.indexOf(39);
            String httpMethod = route.substring(0, singleQuoteIndex).trim().toLowerCase();
            String url = route.substring(singleQuoteIndex + 1, route.length() - 1).trim();
            HttpMethod method;
            try {
                method = HttpMethod.valueOf(httpMethod);
            } catch (IllegalArgumentException var9) {
                LOG.error("The @Route value: " + route + " has an invalid HTTP method part: " + httpMethod + ".");
                return;
            }
            this.add(method, url, acceptType, target);
        } catch (Exception var10) {
            LOG.error("The @Route value: " + route + " is not in the correct format", (Throwable) var10);
        }
    }
}
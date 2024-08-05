package info.journeymap.shaded.kotlin.spark.route;

import info.journeymap.shaded.kotlin.spark.utils.SparkUtils;
import java.util.List;

class RouteEntry {

    HttpMethod httpMethod;

    String path;

    String acceptedType;

    Object target;

    RouteEntry() {
    }

    RouteEntry(RouteEntry entry) {
        this.httpMethod = entry.httpMethod;
        this.path = entry.path;
        this.acceptedType = entry.acceptedType;
        this.target = entry.target;
    }

    boolean matches(HttpMethod httpMethod, String path) {
        if ((httpMethod == HttpMethod.before || httpMethod == HttpMethod.after || httpMethod == HttpMethod.afterafter) && this.httpMethod == httpMethod && this.path.equals("+/*paths")) {
            return true;
        } else {
            boolean match = false;
            if (this.httpMethod == httpMethod) {
                match = this.matchPath(path);
            }
            return match;
        }
    }

    private boolean matchPath(String path) {
        if (this.path.endsWith("*") || (!path.endsWith("/") || this.path.endsWith("/")) && (!this.path.endsWith("/") || path.endsWith("/"))) {
            if (this.path.equals(path)) {
                return true;
            } else {
                List<String> thisPathList = SparkUtils.convertRouteToList(this.path);
                List<String> pathList = SparkUtils.convertRouteToList(path);
                int thisPathSize = thisPathList.size();
                int pathSize = pathList.size();
                if (thisPathSize == pathSize) {
                    for (int i = 0; i < thisPathSize; i++) {
                        String thisPathPart = (String) thisPathList.get(i);
                        String pathPart = (String) pathList.get(i);
                        if (i == thisPathSize - 1 && thisPathPart.equals("*") && this.path.endsWith("*")) {
                            return true;
                        }
                        if (!thisPathPart.startsWith(":") && !thisPathPart.equals(pathPart) && !thisPathPart.equals("*")) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    if (this.path.endsWith("*")) {
                        if (pathSize == thisPathSize - 1 && path.endsWith("/")) {
                            pathList.add("");
                            pathList.add("");
                            pathSize += 2;
                        }
                        if (thisPathSize < pathSize) {
                            for (int i = 0; i < thisPathSize; i++) {
                                String thisPathPartx = (String) thisPathList.get(i);
                                String pathPartx = (String) pathList.get(i);
                                if (thisPathPartx.equals("*") && i == thisPathSize - 1 && this.path.endsWith("*")) {
                                    return true;
                                }
                                if (!thisPathPartx.startsWith(":") && !thisPathPartx.equals(pathPartx) && !thisPathPartx.equals("*")) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return this.httpMethod.name() + ", " + this.path + ", " + this.target;
    }
}
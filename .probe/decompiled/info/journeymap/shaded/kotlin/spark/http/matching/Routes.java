package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.Request;
import info.journeymap.shaded.kotlin.spark.RequestResponseFactory;
import info.journeymap.shaded.kotlin.spark.RouteImpl;
import info.journeymap.shaded.kotlin.spark.route.HttpMethod;
import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;

final class Routes {

    static void execute(RouteContext context) throws Exception {
        Object content = context.body().get();
        RouteMatch match = context.routeMatcher().find(context.httpMethod(), context.uri(), context.acceptType());
        Object target = null;
        if (match != null) {
            target = match.getTarget();
        } else if (context.httpMethod() == HttpMethod.head && context.body().notSet()) {
            content = context.routeMatcher().find(HttpMethod.get, context.uri(), context.acceptType()) != null ? "" : null;
        }
        if (target != null) {
            Object result = null;
            if (target instanceof RouteImpl) {
                RouteImpl route = (RouteImpl) target;
                if (context.requestWrapper().getDelegate() == null) {
                    Request request = RequestResponseFactory.create(match, context.httpRequest());
                    context.requestWrapper().setDelegate(request);
                } else {
                    context.requestWrapper().changeMatch(match);
                }
                context.responseWrapper().setDelegate(context.response());
                Object element = route.handle(context.requestWrapper(), context.responseWrapper());
                result = route.render(element);
            }
            if (result != null) {
                content = result;
                if (result instanceof String) {
                    String contentStr = (String) result;
                    if (!contentStr.equals("")) {
                        context.responseWrapper().body(contentStr);
                    }
                }
            }
        }
        context.body().set(content);
    }
}
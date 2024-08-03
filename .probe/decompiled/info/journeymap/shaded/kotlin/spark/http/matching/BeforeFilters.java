package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.FilterImpl;
import info.journeymap.shaded.kotlin.spark.Request;
import info.journeymap.shaded.kotlin.spark.RequestResponseFactory;
import info.journeymap.shaded.kotlin.spark.route.HttpMethod;
import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;

final class BeforeFilters {

    static void execute(RouteContext context) throws Exception {
        Object content = context.body().get();
        for (RouteMatch filterMatch : context.routeMatcher().findMultiple(HttpMethod.before, context.uri(), context.acceptType())) {
            Object filterTarget = filterMatch.getTarget();
            if (filterTarget instanceof FilterImpl) {
                Request request = RequestResponseFactory.create(filterMatch, context.httpRequest());
                FilterImpl filter = (FilterImpl) filterTarget;
                context.requestWrapper().setDelegate(request);
                context.responseWrapper().setDelegate(context.response());
                filter.handle(context.requestWrapper(), context.responseWrapper());
                String bodyAfterFilter = context.response().body();
                if (bodyAfterFilter != null) {
                    content = bodyAfterFilter;
                }
            }
        }
        context.body().set(content);
    }
}
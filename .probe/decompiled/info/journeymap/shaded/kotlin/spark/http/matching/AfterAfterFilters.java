package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.FilterImpl;
import info.journeymap.shaded.kotlin.spark.Request;
import info.journeymap.shaded.kotlin.spark.RequestResponseFactory;
import info.journeymap.shaded.kotlin.spark.route.HttpMethod;
import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;

final class AfterAfterFilters {

    static void execute(RouteContext context) throws Exception {
        Object content = context.body().get();
        for (RouteMatch filterMatch : context.routeMatcher().findMultiple(HttpMethod.afterafter, context.uri(), context.acceptType())) {
            Object filterTarget = filterMatch.getTarget();
            if (filterTarget instanceof FilterImpl) {
                if (context.requestWrapper().getDelegate() == null) {
                    Request request = RequestResponseFactory.create(filterMatch, context.httpRequest());
                    context.requestWrapper().setDelegate(request);
                } else {
                    context.requestWrapper().changeMatch(filterMatch);
                }
                context.responseWrapper().setDelegate(context.response());
                FilterImpl filter = (FilterImpl) filterTarget;
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
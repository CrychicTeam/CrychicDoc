package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.route.HttpMethod;

abstract class Routable {

    protected abstract void addRoute(HttpMethod var1, RouteImpl var2);

    @Deprecated
    protected abstract void addRoute(String var1, RouteImpl var2);

    protected abstract void addFilter(HttpMethod var1, FilterImpl var2);

    @Deprecated
    protected abstract void addFilter(String var1, FilterImpl var2);

    public void get(String path, Route route) {
        this.addRoute(HttpMethod.get, RouteImpl.create(path, route));
    }

    public void post(String path, Route route) {
        this.addRoute(HttpMethod.post, RouteImpl.create(path, route));
    }

    public void put(String path, Route route) {
        this.addRoute(HttpMethod.put, RouteImpl.create(path, route));
    }

    public void patch(String path, Route route) {
        this.addRoute(HttpMethod.patch, RouteImpl.create(path, route));
    }

    public void delete(String path, Route route) {
        this.addRoute(HttpMethod.delete, RouteImpl.create(path, route));
    }

    public void head(String path, Route route) {
        this.addRoute(HttpMethod.head, RouteImpl.create(path, route));
    }

    public void trace(String path, Route route) {
        this.addRoute(HttpMethod.trace, RouteImpl.create(path, route));
    }

    public void connect(String path, Route route) {
        this.addRoute(HttpMethod.connect, RouteImpl.create(path, route));
    }

    public void options(String path, Route route) {
        this.addRoute(HttpMethod.options, RouteImpl.create(path, route));
    }

    public void before(String path, Filter filter) {
        this.addFilter(HttpMethod.before, FilterImpl.create(path, filter));
    }

    public void after(String path, Filter filter) {
        this.addFilter(HttpMethod.after, FilterImpl.create(path, filter));
    }

    public void get(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.get, RouteImpl.create(path, acceptType, route));
    }

    public void post(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.post, RouteImpl.create(path, acceptType, route));
    }

    public void put(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.put, RouteImpl.create(path, acceptType, route));
    }

    public void patch(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.patch, RouteImpl.create(path, acceptType, route));
    }

    public void delete(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.delete, RouteImpl.create(path, acceptType, route));
    }

    public void head(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.head, RouteImpl.create(path, acceptType, route));
    }

    public void trace(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.trace, RouteImpl.create(path, acceptType, route));
    }

    public void connect(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.connect, RouteImpl.create(path, acceptType, route));
    }

    public void options(String path, String acceptType, Route route) {
        this.addRoute(HttpMethod.options, RouteImpl.create(path, acceptType, route));
    }

    public void before(Filter filter) {
        this.addFilter(HttpMethod.before, FilterImpl.create("+/*paths", filter));
    }

    public void after(Filter filter) {
        this.addFilter(HttpMethod.after, FilterImpl.create("+/*paths", filter));
    }

    public void before(String path, String acceptType, Filter filter) {
        this.addFilter(HttpMethod.before, FilterImpl.create(path, acceptType, filter));
    }

    public void after(String path, String acceptType, Filter filter) {
        this.addFilter(HttpMethod.after, FilterImpl.create(path, acceptType, filter));
    }

    public void afterAfter(Filter filter) {
        this.addFilter(HttpMethod.afterafter, FilterImpl.create("+/*paths", filter));
    }

    public void afterAfter(String path, Filter filter) {
        this.addFilter(HttpMethod.afterafter, FilterImpl.create(path, filter));
    }

    public void get(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.get, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void get(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.get, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void post(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.post, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void post(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.post, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void put(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.put, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void put(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.put, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void delete(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.delete, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void delete(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.delete, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void patch(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.patch, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void patch(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.patch, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void head(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.head, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void head(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.head, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void trace(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.trace, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void trace(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.trace, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void connect(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.connect, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void connect(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.connect, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void options(String path, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.options, TemplateViewRouteImpl.create(path, route, engine));
    }

    public void options(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        this.addRoute(HttpMethod.options, TemplateViewRouteImpl.create(path, acceptType, route, engine));
    }

    public void get(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.get, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void get(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.get, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void post(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.post, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void post(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.post, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void put(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.put, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void put(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.put, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void delete(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.delete, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void delete(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.delete, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void head(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.head, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void head(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.head, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void connect(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.connect, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void connect(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.connect, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void trace(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.trace, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void trace(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.trace, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void options(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.options, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void options(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.options, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }

    public void patch(String path, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.patch, ResponseTransformerRouteImpl.create(path, route, transformer));
    }

    public void patch(String path, String acceptType, Route route, ResponseTransformer transformer) {
        this.addRoute(HttpMethod.patch, ResponseTransformerRouteImpl.create(path, acceptType, route, transformer));
    }
}
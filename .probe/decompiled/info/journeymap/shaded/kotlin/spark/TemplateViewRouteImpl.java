package info.journeymap.shaded.kotlin.spark;

public abstract class TemplateViewRouteImpl extends RouteImpl {

    public static TemplateViewRouteImpl create(String path, TemplateViewRoute route, TemplateEngine engine) {
        return create(path, "*/*", route, engine);
    }

    public static TemplateViewRouteImpl create(String path, String acceptType, final TemplateViewRoute route, final TemplateEngine engine) {
        return new TemplateViewRouteImpl(path, acceptType, route) {

            public String render(ModelAndView modelAndView) {
                return engine.render(modelAndView);
            }

            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.handle(request, response);
            }
        };
    }

    protected TemplateViewRouteImpl(String path, String acceptType, TemplateViewRoute route) {
        super(path, acceptType, route);
    }

    @Override
    public Object render(Object object) {
        ModelAndView modelAndView = (ModelAndView) object;
        return this.render(modelAndView);
    }

    public ModelAndView modelAndView(Object model, String viewName) {
        return new ModelAndView(model, viewName);
    }

    public abstract Object render(ModelAndView var1);
}
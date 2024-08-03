package info.journeymap.shaded.kotlin.spark;

public abstract class TemplateEngine {

    public String render(Object object) {
        ModelAndView modelAndView = (ModelAndView) object;
        return this.render(modelAndView);
    }

    public ModelAndView modelAndView(Object model, String viewName) {
        return new ModelAndView(model, viewName);
    }

    public abstract String render(ModelAndView var1);
}
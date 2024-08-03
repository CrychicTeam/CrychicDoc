package info.journeymap.shaded.kotlin.spark;

public class ModelAndView {

    private Object model;

    private String viewName;

    public ModelAndView(Object model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    public Object getModel() {
        return this.model;
    }

    public String getViewName() {
        return this.viewName;
    }
}
package info.journeymap.shaded.org.javax.servlet;

public class ServletRequestAttributeEvent extends ServletRequestEvent {

    private static final long serialVersionUID = -1466635426192317793L;

    private String name;

    private Object value;

    public ServletRequestAttributeEvent(ServletContext sc, ServletRequest request, String name, Object value) {
        super(sc, request);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
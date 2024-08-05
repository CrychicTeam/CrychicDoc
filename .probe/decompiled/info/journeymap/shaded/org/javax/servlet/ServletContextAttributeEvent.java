package info.journeymap.shaded.org.javax.servlet;

public class ServletContextAttributeEvent extends ServletContextEvent {

    private static final long serialVersionUID = -5804680734245618303L;

    private String name;

    private Object value;

    public ServletContextAttributeEvent(ServletContext source, String name, Object value) {
        super(source);
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
package info.journeymap.shaded.org.eclipse.jetty.util;

public interface Decorator {

    <T> T decorate(T var1);

    void destroy(Object var1);
}
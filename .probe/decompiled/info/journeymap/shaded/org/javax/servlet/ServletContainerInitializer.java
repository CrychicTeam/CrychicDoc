package info.journeymap.shaded.org.javax.servlet;

import java.util.Set;

public interface ServletContainerInitializer {

    void onStartup(Set<Class<?>> var1, ServletContext var2) throws ServletException;
}
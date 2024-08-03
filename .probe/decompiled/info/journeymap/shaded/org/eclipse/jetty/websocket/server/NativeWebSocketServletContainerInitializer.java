package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.javax.servlet.ServletContainerInitializer;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import java.util.Set;

public class NativeWebSocketServletContainerInitializer implements ServletContainerInitializer {

    public static NativeWebSocketConfiguration getDefaultFrom(ServletContext context) {
        String KEY = NativeWebSocketConfiguration.class.getName();
        NativeWebSocketConfiguration configuration = (NativeWebSocketConfiguration) context.getAttribute(KEY);
        if (configuration == null) {
            configuration = new NativeWebSocketConfiguration(context);
            context.setAttribute(KEY, configuration);
        }
        return configuration;
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        getDefaultFrom(ctx);
    }
}
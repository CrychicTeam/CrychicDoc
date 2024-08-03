package info.journeymap.shaded.org.eclipse.jetty.websocket.servlet;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface WebSocketServletFactory {

    boolean acceptWebSocket(HttpServletRequest var1, HttpServletResponse var2) throws IOException;

    boolean acceptWebSocket(WebSocketCreator var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException;

    void start() throws Exception;

    void stop() throws Exception;

    WebSocketCreator getCreator();

    ExtensionFactory getExtensionFactory();

    WebSocketPolicy getPolicy();

    boolean isUpgradeRequest(HttpServletRequest var1, HttpServletResponse var2);

    void register(Class<?> var1);

    void setCreator(WebSocketCreator var1);

    public static class Loader {

        static final String DEFAULT_IMPL = "info.journeymap.shaded.org.eclipse.jetty.websocket.server.WebSocketServerFactory";

        public static WebSocketServletFactory load(ServletContext ctx, WebSocketPolicy policy) {
            try {
                Class<? extends WebSocketServletFactory> wsClazz = Class.forName("info.journeymap.shaded.org.eclipse.jetty.websocket.server.WebSocketServerFactory", true, Thread.currentThread().getContextClassLoader());
                Constructor<? extends WebSocketServletFactory> ctor = wsClazz.getDeclaredConstructor(ServletContext.class, WebSocketPolicy.class);
                return (WebSocketServletFactory) ctor.newInstance(ctx, policy);
            } catch (ClassNotFoundException var4) {
                throw new RuntimeException("Unable to load org.eclipse.jetty.websocket.server.WebSocketServerFactory", var4);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var5) {
                throw new RuntimeException("Unable to instantiate org.eclipse.jetty.websocket.server.WebSocketServerFactory", var5);
            }
        }
    }
}
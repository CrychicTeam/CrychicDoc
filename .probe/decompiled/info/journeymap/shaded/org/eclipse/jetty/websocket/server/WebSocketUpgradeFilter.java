package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.MappedResource;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathSpec;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.servlet.FilterHolder;
import info.journeymap.shaded.org.eclipse.jetty.servlet.ServletContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.FilterChain;
import info.journeymap.shaded.org.javax.servlet.FilterConfig;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

@ManagedObject("WebSocket Upgrade Filter")
public class WebSocketUpgradeFilter implements Filter, MappedWebSocketCreator, Dumpable {

    private static final Logger LOG = Log.getLogger(WebSocketUpgradeFilter.class);

    public static final String CONTEXT_ATTRIBUTE_KEY = "contextAttributeKey";

    public static final String CONFIG_ATTRIBUTE_KEY = "configAttributeKey";

    private NativeWebSocketConfiguration configuration;

    private String instanceKey;

    private boolean localConfiguration = false;

    private boolean alreadySetToAttribute = false;

    public static WebSocketUpgradeFilter configureContext(ServletContextHandler context) throws ServletException {
        WebSocketUpgradeFilter filter = (WebSocketUpgradeFilter) context.getAttribute(WebSocketUpgradeFilter.class.getName());
        if (filter != null) {
            return filter;
        } else {
            NativeWebSocketConfiguration configuration = NativeWebSocketServletContainerInitializer.getDefaultFrom(context.getServletContext());
            filter = new WebSocketUpgradeFilter(configuration);
            filter.setToAttribute(context, WebSocketUpgradeFilter.class.getName());
            String name = "Jetty_WebSocketUpgradeFilter";
            String pathSpec = "/*";
            EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
            FilterHolder fholder = new FilterHolder(filter);
            fholder.setName(name);
            fholder.setAsyncSupported(true);
            fholder.setInitParameter("contextAttributeKey", WebSocketUpgradeFilter.class.getName());
            context.addFilter(fholder, pathSpec, dispatcherTypes);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding [{}] {} mapped to {} to {}", name, filter, pathSpec, context);
            }
            return filter;
        }
    }

    @Deprecated
    public static WebSocketUpgradeFilter configureContext(ServletContext context) throws ServletException {
        ContextHandler handler = ContextHandler.getContextHandler(context);
        if (handler == null) {
            throw new ServletException("Not running on Jetty, WebSocket support unavailable");
        } else if (!(handler instanceof ServletContextHandler)) {
            throw new ServletException("Not running in Jetty ServletContextHandler, WebSocket support via " + WebSocketUpgradeFilter.class.getName() + " unavailable");
        } else {
            return configureContext((ServletContextHandler) handler);
        }
    }

    public WebSocketUpgradeFilter() {
    }

    public WebSocketUpgradeFilter(WebSocketServerFactory factory) {
        this(new NativeWebSocketConfiguration(factory));
    }

    public WebSocketUpgradeFilter(NativeWebSocketConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void addMapping(PathSpec spec, WebSocketCreator creator) {
        this.configuration.addMapping(spec, creator);
    }

    @Deprecated
    @Override
    public void addMapping(info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap.PathSpec spec, WebSocketCreator creator) {
        this.configuration.addMapping(spec, creator);
    }

    @Override
    public void addMapping(String spec, WebSocketCreator creator) {
        this.configuration.addMapping(spec, creator);
    }

    @Override
    public boolean removeMapping(String spec) {
        return this.configuration.removeMapping(spec);
    }

    @Override
    public void destroy() {
        try {
            this.alreadySetToAttribute = false;
            if (this.localConfiguration) {
                this.configuration.stop();
            }
        } catch (Exception var2) {
            LOG.ignore(var2);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.configuration == null) {
            LOG.debug("WebSocketUpgradeFilter is not operational - missing " + NativeWebSocketConfiguration.class.getName());
            chain.doFilter(request, response);
        } else {
            WebSocketServletFactory factory = this.configuration.getFactory();
            if (factory == null) {
                LOG.debug("WebSocketUpgradeFilter is not operational - no WebSocketServletFactory configured");
                chain.doFilter(request, response);
            } else {
                try {
                    HttpServletRequest httpreq = (HttpServletRequest) request;
                    HttpServletResponse httpresp = (HttpServletResponse) response;
                    if (!factory.isUpgradeRequest(httpreq, httpresp)) {
                        chain.doFilter(request, response);
                        return;
                    }
                    String contextPath = httpreq.getContextPath();
                    String target = httpreq.getRequestURI();
                    if (target.startsWith(contextPath)) {
                        target = target.substring(contextPath.length());
                    }
                    MappedResource<WebSocketCreator> resource = this.configuration.getMatch(target);
                    if (resource == null) {
                        chain.doFilter(request, response);
                        return;
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("WebSocket Upgrade detected on {} for endpoint {}", target, resource);
                    }
                    WebSocketCreator creator = resource.getResource();
                    httpreq.setAttribute(PathSpec.class.getName(), resource.getPathSpec());
                    if (factory.acceptWebSocket(creator, httpreq, httpresp)) {
                        return;
                    }
                    if (response.isCommitted()) {
                        return;
                    }
                } catch (ClassCastException var11) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Not a HttpServletRequest, skipping WebSocketUpgradeFilter");
                    }
                }
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        out.append(indent).append(" +- configuration=").append(this.configuration.toString()).append("\n");
        this.configuration.dump(out, indent);
    }

    public WebSocketServletFactory getFactory() {
        return this.configuration.getFactory();
    }

    @ManagedAttribute(value = "configuration", readonly = true)
    public NativeWebSocketConfiguration getConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(this.getClass().getName() + " not initialized yet");
        } else {
            return this.configuration;
        }
    }

    @Override
    public WebSocketCreator getMapping(String target) {
        return this.getConfiguration().getMapping(target);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            String configurationKey = config.getInitParameter("configAttributeKey");
            if (configurationKey == null) {
                configurationKey = NativeWebSocketConfiguration.class.getName();
            }
            if (this.configuration == null) {
                this.configuration = (NativeWebSocketConfiguration) config.getServletContext().getAttribute(configurationKey);
                if (this.configuration == null) {
                    throw new ServletException("Unable to find required instance of " + NativeWebSocketConfiguration.class.getName() + " at ServletContext attribute '" + configurationKey + "'");
                }
            } else if (config.getServletContext().getAttribute(configurationKey) == null) {
                config.getServletContext().setAttribute(configurationKey, this.configuration);
            }
            if (!this.configuration.isRunning()) {
                this.localConfiguration = true;
                this.configuration.start();
            }
            String max = config.getInitParameter("maxIdleTime");
            if (max != null) {
                this.getFactory().getPolicy().setIdleTimeout(Long.parseLong(max));
            }
            max = config.getInitParameter("maxTextMessageSize");
            if (max != null) {
                this.getFactory().getPolicy().setMaxTextMessageSize(Integer.parseInt(max));
            }
            max = config.getInitParameter("maxBinaryMessageSize");
            if (max != null) {
                this.getFactory().getPolicy().setMaxBinaryMessageSize(Integer.parseInt(max));
            }
            max = config.getInitParameter("inputBufferSize");
            if (max != null) {
                this.getFactory().getPolicy().setInputBufferSize(Integer.parseInt(max));
            }
            this.instanceKey = config.getInitParameter("contextAttributeKey");
            if (this.instanceKey == null) {
                this.instanceKey = WebSocketUpgradeFilter.class.getName();
            }
            this.setToAttribute(config.getServletContext(), this.instanceKey);
        } catch (ServletException var4) {
            throw var4;
        } catch (Throwable var5) {
            throw new ServletException(var5);
        }
    }

    private void setToAttribute(ServletContextHandler context, String key) throws ServletException {
        this.setToAttribute(context.getServletContext(), key);
    }

    public void setToAttribute(ServletContext context, String key) throws ServletException {
        if (!this.alreadySetToAttribute) {
            if (context.getAttribute(key) != null) {
                throw new ServletException(WebSocketUpgradeFilter.class.getName() + " is defined twice for the same context attribute key '" + key + "'.  Make sure you have different init-param '" + "contextAttributeKey" + "' values set");
            } else {
                context.setAttribute(key, this);
                this.alreadySetToAttribute = true;
            }
        }
    }

    public String toString() {
        return String.format("%s[configuration=%s]", this.getClass().getSimpleName(), this.configuration);
    }
}
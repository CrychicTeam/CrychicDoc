package info.journeymap.shaded.org.eclipse.jetty.servlet;

import info.journeymap.shaded.org.eclipse.jetty.security.ConstraintAware;
import info.journeymap.shaded.org.eclipse.jetty.security.ConstraintMapping;
import info.journeymap.shaded.org.eclipse.jetty.security.ConstraintSecurityHandler;
import info.journeymap.shaded.org.eclipse.jetty.security.SecurityHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.Dispatcher;
import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ErrorHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.HandlerCollection;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.HandlerWrapper;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.gzip.GzipHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.session.SessionHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.DecoratedObjectFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.DeprecationWarning;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.FilterRegistration;
import info.journeymap.shaded.org.javax.servlet.RequestDispatcher;
import info.journeymap.shaded.org.javax.servlet.Servlet;
import info.journeymap.shaded.org.javax.servlet.ServletContextEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContextListener;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRegistration;
import info.journeymap.shaded.org.javax.servlet.ServletSecurityElement;
import info.journeymap.shaded.org.javax.servlet.SessionCookieConfig;
import info.journeymap.shaded.org.javax.servlet.SessionTrackingMode;
import info.journeymap.shaded.org.javax.servlet.descriptor.JspConfigDescriptor;
import info.journeymap.shaded.org.javax.servlet.descriptor.JspPropertyGroupDescriptor;
import info.journeymap.shaded.org.javax.servlet.descriptor.TaglibDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ManagedObject("Servlet Context Handler")
public class ServletContextHandler extends ContextHandler {

    private static final Logger LOG = Log.getLogger(ServletContextHandler.class);

    public static final int SESSIONS = 1;

    public static final int SECURITY = 2;

    public static final int GZIP = 4;

    public static final int NO_SESSIONS = 0;

    public static final int NO_SECURITY = 0;

    protected final DecoratedObjectFactory _objFactory;

    protected Class<? extends SecurityHandler> _defaultSecurityHandlerClass = ConstraintSecurityHandler.class;

    protected SessionHandler _sessionHandler;

    protected SecurityHandler _securityHandler;

    protected ServletHandler _servletHandler;

    protected GzipHandler _gzipHandler;

    protected int _options;

    protected JspConfigDescriptor _jspConfig;

    public ServletContextHandler() {
        this(null, null, null, null, null);
    }

    public ServletContextHandler(int options) {
        this(null, null, options);
    }

    public ServletContextHandler(HandlerContainer parent, String contextPath) {
        this(parent, contextPath, null, null, null, null);
    }

    public ServletContextHandler(HandlerContainer parent, String contextPath, int options) {
        this(parent, contextPath, null, null, null, null, options);
    }

    public ServletContextHandler(HandlerContainer parent, String contextPath, boolean sessions, boolean security) {
        this(parent, contextPath, (sessions ? 1 : 0) | (security ? 2 : 0));
    }

    public ServletContextHandler(HandlerContainer parent, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        this(parent, null, sessionHandler, securityHandler, servletHandler, errorHandler);
    }

    public ServletContextHandler(HandlerContainer parent, String contextPath, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        this(parent, contextPath, sessionHandler, securityHandler, servletHandler, errorHandler, 0);
    }

    public ServletContextHandler(HandlerContainer parent, String contextPath, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler, int options) {
        super((ContextHandler.Context) null);
        this._options = options;
        this._scontext = new ServletContextHandler.Context();
        this._sessionHandler = sessionHandler;
        this._securityHandler = securityHandler;
        this._servletHandler = servletHandler;
        this._objFactory = new DecoratedObjectFactory();
        this._objFactory.addDecorator(new DeprecationWarning());
        if (contextPath != null) {
            this.setContextPath(contextPath);
        }
        if (parent instanceof HandlerWrapper) {
            ((HandlerWrapper) parent).setHandler(this);
        } else if (parent instanceof HandlerCollection) {
            ((HandlerCollection) parent).addHandler(this);
        }
        this.relinkHandlers();
        if (errorHandler != null) {
            this.setErrorHandler(errorHandler);
        }
    }

    @Override
    public void setHandler(Handler handler) {
        if (handler != null) {
            LOG.warn("ServletContextHandler.setHandler should not be called directly. Use insertHandler or setSessionHandler etc.");
        }
        super.setHandler(handler);
    }

    private void doSetHandler(HandlerWrapper wrapper, Handler handler) {
        if (wrapper == this) {
            super.setHandler(handler);
        } else {
            wrapper.setHandler(handler);
        }
    }

    private void relinkHandlers() {
        HandlerWrapper handler = this;
        if (this.getSessionHandler() != null) {
            while (!(handler.getHandler() instanceof SessionHandler) && !(handler.getHandler() instanceof SecurityHandler) && !(handler.getHandler() instanceof GzipHandler) && !(handler.getHandler() instanceof ServletHandler) && handler.getHandler() instanceof HandlerWrapper) {
                handler = (HandlerWrapper) handler.getHandler();
            }
            if (handler.getHandler() != this._sessionHandler) {
                this.doSetHandler(handler, this._sessionHandler);
            }
            handler = this._sessionHandler;
        }
        if (this.getSecurityHandler() != null) {
            while (!(handler.getHandler() instanceof SecurityHandler) && !(handler.getHandler() instanceof GzipHandler) && !(handler.getHandler() instanceof ServletHandler) && handler.getHandler() instanceof HandlerWrapper) {
                handler = (HandlerWrapper) handler.getHandler();
            }
            if (handler.getHandler() != this._securityHandler) {
                this.doSetHandler(handler, this._securityHandler);
            }
            handler = this._securityHandler;
        }
        if (this.getGzipHandler() != null) {
            while (!(handler.getHandler() instanceof GzipHandler) && !(handler.getHandler() instanceof ServletHandler) && handler.getHandler() instanceof HandlerWrapper) {
                handler = (HandlerWrapper) handler.getHandler();
            }
            if (handler.getHandler() != this._gzipHandler) {
                this.doSetHandler(handler, this._gzipHandler);
            }
            handler = this._gzipHandler;
        }
        if (this.getServletHandler() != null) {
            while (!(handler.getHandler() instanceof ServletHandler) && handler.getHandler() instanceof HandlerWrapper) {
                handler = (HandlerWrapper) handler.getHandler();
            }
            if (handler.getHandler() != this._servletHandler) {
                this.doSetHandler(handler, this._servletHandler);
            }
            handler = this._servletHandler;
        }
    }

    @Override
    protected void doStart() throws Exception {
        this.getServletContext().setAttribute(DecoratedObjectFactory.ATTR, this._objFactory);
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        this._objFactory.clear();
    }

    public Class<? extends SecurityHandler> getDefaultSecurityHandlerClass() {
        return this._defaultSecurityHandlerClass;
    }

    public void setDefaultSecurityHandlerClass(Class<? extends SecurityHandler> defaultSecurityHandlerClass) {
        this._defaultSecurityHandlerClass = defaultSecurityHandlerClass;
    }

    protected SessionHandler newSessionHandler() {
        return new SessionHandler();
    }

    protected SecurityHandler newSecurityHandler() {
        try {
            return (SecurityHandler) this._defaultSecurityHandlerClass.newInstance();
        } catch (Exception var2) {
            throw new IllegalStateException(var2);
        }
    }

    protected ServletHandler newServletHandler() {
        return new ServletHandler();
    }

    @Override
    protected void startContext() throws Exception {
        ServletContextHandler.ServletContainerInitializerCaller sciBean = this.getBean(ServletContextHandler.ServletContainerInitializerCaller.class);
        if (sciBean != null) {
            sciBean.start();
        }
        if (this._servletHandler != null && this._servletHandler.getListeners() != null) {
            for (ListenerHolder holder : this._servletHandler.getListeners()) {
                this._objFactory.decorate(holder.getListener());
            }
        }
        super.startContext();
        if (this._servletHandler != null) {
            this._servletHandler.initialize();
        }
    }

    @Override
    protected void stopContext() throws Exception {
        super.stopContext();
    }

    @ManagedAttribute(value = "context security handler", readonly = true)
    public SecurityHandler getSecurityHandler() {
        if (this._securityHandler == null && (this._options & 2) != 0 && !this.isStarted()) {
            this._securityHandler = this.newSecurityHandler();
        }
        return this._securityHandler;
    }

    @ManagedAttribute(value = "context servlet handler", readonly = true)
    public ServletHandler getServletHandler() {
        if (this._servletHandler == null && !this.isStarted()) {
            this._servletHandler = this.newServletHandler();
        }
        return this._servletHandler;
    }

    @ManagedAttribute(value = "context session handler", readonly = true)
    public SessionHandler getSessionHandler() {
        if (this._sessionHandler == null && (this._options & 1) != 0 && !this.isStarted()) {
            this._sessionHandler = this.newSessionHandler();
        }
        return this._sessionHandler;
    }

    @ManagedAttribute(value = "context gzip handler", readonly = true)
    public GzipHandler getGzipHandler() {
        if (this._gzipHandler == null && (this._options & 4) != 0 && !this.isStarted()) {
            this._gzipHandler = new GzipHandler();
        }
        return this._gzipHandler;
    }

    public ServletHolder addServlet(String className, String pathSpec) {
        return this.getServletHandler().addServletWithMapping(className, pathSpec);
    }

    public ServletHolder addServlet(Class<? extends Servlet> servlet, String pathSpec) {
        return this.getServletHandler().addServletWithMapping(servlet, pathSpec);
    }

    public void addServlet(ServletHolder servlet, String pathSpec) {
        this.getServletHandler().addServletWithMapping(servlet, pathSpec);
    }

    public void addFilter(FilterHolder holder, String pathSpec, EnumSet<DispatcherType> dispatches) {
        this.getServletHandler().addFilterWithMapping(holder, pathSpec, dispatches);
    }

    public FilterHolder addFilter(Class<? extends Filter> filterClass, String pathSpec, EnumSet<DispatcherType> dispatches) {
        return this.getServletHandler().addFilterWithMapping(filterClass, pathSpec, dispatches);
    }

    public FilterHolder addFilter(String filterClass, String pathSpec, EnumSet<DispatcherType> dispatches) {
        return this.getServletHandler().addFilterWithMapping(filterClass, pathSpec, dispatches);
    }

    protected ServletRegistration.Dynamic dynamicHolderAdded(ServletHolder holder) {
        return holder.getRegistration();
    }

    protected void addRoles(String... roleNames) {
        if (this._securityHandler != null && this._securityHandler instanceof ConstraintAware) {
            HashSet<String> union = new HashSet();
            Set<String> existing = ((ConstraintAware) this._securityHandler).getRoles();
            if (existing != null) {
                union.addAll(existing);
            }
            union.addAll(Arrays.asList(roleNames));
            ((ConstraintSecurityHandler) this._securityHandler).setRoles(union);
        }
    }

    public Set<String> setServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement) {
        Collection<String> pathSpecs = registration.getMappings();
        if (pathSpecs != null) {
            for (String pathSpec : pathSpecs) {
                for (ConstraintMapping m : ConstraintSecurityHandler.createConstraintsWithMappingsForPath(registration.getName(), pathSpec, servletSecurityElement)) {
                    ((ConstraintAware) this.getSecurityHandler()).addConstraintMapping(m);
                }
            }
        }
        return Collections.emptySet();
    }

    @Override
    public void callContextInitialized(ServletContextListener l, ServletContextEvent e) {
        try {
            if (this.isProgrammaticListener(l)) {
                this.getServletContext().setEnabled(false);
            }
            super.callContextInitialized(l, e);
        } finally {
            this.getServletContext().setEnabled(true);
        }
    }

    @Override
    public void callContextDestroyed(ServletContextListener l, ServletContextEvent e) {
        super.callContextDestroyed(l, e);
    }

    private boolean replaceHandler(Handler handler, Handler replace) {
        HandlerWrapper wrapper;
        for (wrapper = this; wrapper.getHandler() != handler; wrapper = (HandlerWrapper) wrapper.getHandler()) {
            if (!(wrapper.getHandler() instanceof HandlerWrapper)) {
                return false;
            }
        }
        this.doSetHandler(wrapper, replace);
        return true;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else {
            Handler next = null;
            if (this._sessionHandler != null) {
                next = this._sessionHandler.getHandler();
                this._sessionHandler.setHandler(null);
                this.replaceHandler(this._sessionHandler, sessionHandler);
            }
            this._sessionHandler = sessionHandler;
            if (next != null && this._sessionHandler.getHandler() == null) {
                this._sessionHandler.setHandler(next);
            }
            this.relinkHandlers();
        }
    }

    public void setSecurityHandler(SecurityHandler securityHandler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else {
            Handler next = null;
            if (this._securityHandler != null) {
                next = this._securityHandler.getHandler();
                this._securityHandler.setHandler(null);
                this.replaceHandler(this._securityHandler, securityHandler);
            }
            this._securityHandler = securityHandler;
            if (next != null && this._securityHandler.getHandler() == null) {
                this._securityHandler.setHandler(next);
            }
            this.relinkHandlers();
        }
    }

    public void setGzipHandler(GzipHandler gzipHandler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else {
            Handler next = null;
            if (this._gzipHandler != null) {
                next = this._gzipHandler.getHandler();
                this._gzipHandler.setHandler(null);
                this.replaceHandler(this._gzipHandler, gzipHandler);
            }
            this._gzipHandler = gzipHandler;
            if (next != null && this._gzipHandler.getHandler() == null) {
                this._gzipHandler.setHandler(next);
            }
            this.relinkHandlers();
        }
    }

    public void setServletHandler(ServletHandler servletHandler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        } else {
            Handler next = null;
            if (this._servletHandler != null) {
                next = this._servletHandler.getHandler();
                this._servletHandler.setHandler(null);
                this.replaceHandler(this._servletHandler, servletHandler);
            }
            this._servletHandler = servletHandler;
            if (next != null && this._servletHandler.getHandler() == null) {
                this._servletHandler.setHandler(next);
            }
            this.relinkHandlers();
        }
    }

    @Override
    public void insertHandler(HandlerWrapper handler) {
        if (handler instanceof SessionHandler) {
            this.setSessionHandler((SessionHandler) handler);
        } else if (handler instanceof SecurityHandler) {
            this.setSecurityHandler((SecurityHandler) handler);
        } else if (handler instanceof GzipHandler) {
            this.setGzipHandler((GzipHandler) handler);
        } else if (handler instanceof ServletHandler) {
            this.setServletHandler((ServletHandler) handler);
        } else {
            HandlerWrapper tail = handler;
            while (tail.getHandler() instanceof HandlerWrapper) {
                tail = (HandlerWrapper) tail.getHandler();
            }
            if (tail.getHandler() != null) {
                throw new IllegalArgumentException("bad tail of inserted wrapper chain");
            }
            HandlerWrapper h = this;
            while (h.getHandler() instanceof HandlerWrapper) {
                HandlerWrapper wrapper = (HandlerWrapper) h.getHandler();
                if (wrapper instanceof SessionHandler || wrapper instanceof SecurityHandler || wrapper instanceof ServletHandler) {
                    break;
                }
                h = wrapper;
            }
            Handler next = h.getHandler();
            this.doSetHandler(h, handler);
            this.doSetHandler(tail, next);
        }
        this.relinkHandlers();
    }

    public DecoratedObjectFactory getObjectFactory() {
        return this._objFactory;
    }

    @Deprecated
    public List<ServletContextHandler.Decorator> getDecorators() {
        List<ServletContextHandler.Decorator> ret = new ArrayList();
        for (info.journeymap.shaded.org.eclipse.jetty.util.Decorator decorator : this._objFactory) {
            ret.add(new ServletContextHandler.LegacyDecorator(decorator));
        }
        return Collections.unmodifiableList(ret);
    }

    @Deprecated
    public void setDecorators(List<ServletContextHandler.Decorator> decorators) {
        this._objFactory.setDecorators(decorators);
    }

    @Deprecated
    public void addDecorator(ServletContextHandler.Decorator decorator) {
        this._objFactory.addDecorator(decorator);
    }

    void destroyServlet(Servlet servlet) {
        this._objFactory.destroy(servlet);
    }

    void destroyFilter(Filter filter) {
        this._objFactory.destroy(filter);
    }

    public class Context extends ContextHandler.Context {

        @Override
        public RequestDispatcher getNamedDispatcher(String name) {
            ContextHandler context = ServletContextHandler.this;
            if (ServletContextHandler.this._servletHandler == null) {
                return null;
            } else {
                ServletHolder holder = ServletContextHandler.this._servletHandler.getServlet(name);
                return holder != null && holder.isEnabled() ? new Dispatcher(context, name) : null;
            }
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
            if (ServletContextHandler.this.isStarted()) {
                throw new IllegalStateException();
            } else if (filterName == null || "".equals(filterName.trim())) {
                throw new IllegalStateException("Missing filter name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                FilterHolder holder = handler.getFilter(filterName);
                if (holder == null) {
                    holder = handler.newFilterHolder(Source.JAVAX_API);
                    holder.setName(filterName);
                    holder.setHeldClass(filterClass);
                    handler.addFilter(holder);
                    return holder.getRegistration();
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setHeldClass(filterClass);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, String className) {
            if (ServletContextHandler.this.isStarted()) {
                throw new IllegalStateException();
            } else if (filterName == null || "".equals(filterName.trim())) {
                throw new IllegalStateException("Missing filter name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                FilterHolder holder = handler.getFilter(filterName);
                if (holder == null) {
                    holder = handler.newFilterHolder(Source.JAVAX_API);
                    holder.setName(filterName);
                    holder.setClassName(className);
                    handler.addFilter(holder);
                    return holder.getRegistration();
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setClassName(className);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
            if (ServletContextHandler.this.isStarted()) {
                throw new IllegalStateException();
            } else if (filterName == null || "".equals(filterName.trim())) {
                throw new IllegalStateException("Missing filter name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                FilterHolder holder = handler.getFilter(filterName);
                if (holder == null) {
                    holder = handler.newFilterHolder(Source.JAVAX_API);
                    holder.setName(filterName);
                    holder.setFilter(filter);
                    handler.addFilter(holder);
                    return holder.getRegistration();
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setFilter(filter);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (servletName == null || "".equals(servletName.trim())) {
                throw new IllegalStateException("Missing servlet name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                ServletHolder holder = handler.getServlet(servletName);
                if (holder == null) {
                    holder = handler.newServletHolder(Source.JAVAX_API);
                    holder.setName(servletName);
                    holder.setHeldClass(servletClass);
                    handler.addServlet(holder);
                    return ServletContextHandler.this.dynamicHolderAdded(holder);
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setHeldClass(servletClass);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, String className) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (servletName == null || "".equals(servletName.trim())) {
                throw new IllegalStateException("Missing servlet name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                ServletHolder holder = handler.getServlet(servletName);
                if (holder == null) {
                    holder = handler.newServletHolder(Source.JAVAX_API);
                    holder.setName(servletName);
                    holder.setClassName(className);
                    handler.addServlet(holder);
                    return ServletContextHandler.this.dynamicHolderAdded(holder);
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setClassName(className);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (servletName == null || "".equals(servletName.trim())) {
                throw new IllegalStateException("Missing servlet name");
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                ServletHolder holder = handler.getServlet(servletName);
                if (holder == null) {
                    holder = handler.newServletHolder(Source.JAVAX_API);
                    holder.setName(servletName);
                    holder.setServlet(servlet);
                    handler.addServlet(holder);
                    return ServletContextHandler.this.dynamicHolderAdded(holder);
                } else if (holder.getClassName() == null && holder.getHeldClass() == null) {
                    holder.setServlet(servlet);
                    return holder.getRegistration();
                } else {
                    return null;
                }
            }
        }

        @Override
        public boolean setInitParameter(String name, String value) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                return super.setInitParameter(name, value);
            }
        }

        @Override
        public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
            try {
                T f = (T) this.createInstance(c);
                return ServletContextHandler.this._objFactory.decorate(f);
            } catch (Exception var3) {
                throw new ServletException(var3);
            }
        }

        @Override
        public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
            try {
                T s = (T) this.createInstance(c);
                return ServletContextHandler.this._objFactory.decorate(s);
            } catch (Exception var3) {
                throw new ServletException(var3);
            }
        }

        @Override
        public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
            return ServletContextHandler.this._sessionHandler != null ? ServletContextHandler.this._sessionHandler.getDefaultSessionTrackingModes() : null;
        }

        @Override
        public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
            return ServletContextHandler.this._sessionHandler != null ? ServletContextHandler.this._sessionHandler.getEffectiveSessionTrackingModes() : null;
        }

        @Override
        public FilterRegistration getFilterRegistration(String filterName) {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                FilterHolder holder = ServletContextHandler.this.getServletHandler().getFilter(filterName);
                return holder == null ? null : holder.getRegistration();
            }
        }

        @Override
        public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                HashMap<String, FilterRegistration> registrations = new HashMap();
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                FilterHolder[] holders = handler.getFilters();
                if (holders != null) {
                    for (FilterHolder holder : holders) {
                        registrations.put(holder.getName(), holder.getRegistration());
                    }
                }
                return registrations;
            }
        }

        @Override
        public ServletRegistration getServletRegistration(String servletName) {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletHolder holder = ServletContextHandler.this.getServletHandler().getServlet(servletName);
                return holder == null ? null : holder.getRegistration();
            }
        }

        @Override
        public Map<String, ? extends ServletRegistration> getServletRegistrations() {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                HashMap<String, ServletRegistration> registrations = new HashMap();
                ServletHandler handler = ServletContextHandler.this.getServletHandler();
                ServletHolder[] holders = handler.getServlets();
                if (holders != null) {
                    for (ServletHolder holder : holders) {
                        registrations.put(holder.getName(), holder.getRegistration());
                    }
                }
                return registrations;
            }
        }

        @Override
        public SessionCookieConfig getSessionCookieConfig() {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                return ServletContextHandler.this._sessionHandler != null ? ServletContextHandler.this._sessionHandler.getSessionCookieConfig() : null;
            }
        }

        @Override
        public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                if (ServletContextHandler.this._sessionHandler != null) {
                    ServletContextHandler.this._sessionHandler.setSessionTrackingModes(sessionTrackingModes);
                }
            }
        }

        @Override
        public void addListener(String className) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                super.addListener(className);
            }
        }

        @Override
        public <T extends EventListener> void addListener(T t) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                super.addListener(t);
                ListenerHolder holder = ServletContextHandler.this.getServletHandler().newListenerHolder(Source.JAVAX_API);
                holder.setListener(t);
                ServletContextHandler.this.getServletHandler().addListener(holder);
            }
        }

        @Override
        public void addListener(Class<? extends EventListener> listenerClass) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                super.addListener(listenerClass);
            }
        }

        @Override
        public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
            try {
                T l = (T) this.createInstance(clazz);
                return ServletContextHandler.this._objFactory.decorate(l);
            } catch (Exception var3) {
                throw new ServletException(var3);
            }
        }

        @Override
        public JspConfigDescriptor getJspConfigDescriptor() {
            return ServletContextHandler.this._jspConfig;
        }

        @Override
        public void setJspConfigDescriptor(JspConfigDescriptor d) {
            ServletContextHandler.this._jspConfig = d;
        }

        @Override
        public void declareRoles(String... roleNames) {
            if (!ServletContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                ServletContextHandler.this.addRoles(roleNames);
            }
        }
    }

    @Deprecated
    public interface Decorator extends info.journeymap.shaded.org.eclipse.jetty.util.Decorator {
    }

    public static class JspConfig implements JspConfigDescriptor {

        private List<TaglibDescriptor> _taglibs = new ArrayList();

        private List<JspPropertyGroupDescriptor> _jspPropertyGroups = new ArrayList();

        @Override
        public Collection<TaglibDescriptor> getTaglibs() {
            return new ArrayList(this._taglibs);
        }

        public void addTaglibDescriptor(TaglibDescriptor d) {
            this._taglibs.add(d);
        }

        @Override
        public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups() {
            return new ArrayList(this._jspPropertyGroups);
        }

        public void addJspPropertyGroup(JspPropertyGroupDescriptor g) {
            this._jspPropertyGroups.add(g);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("JspConfigDescriptor: \n");
            for (TaglibDescriptor taglib : this._taglibs) {
                sb.append(taglib + "\n");
            }
            for (JspPropertyGroupDescriptor jpg : this._jspPropertyGroups) {
                sb.append(jpg + "\n");
            }
            return sb.toString();
        }
    }

    public static class JspPropertyGroup implements JspPropertyGroupDescriptor {

        private List<String> _urlPatterns = new ArrayList();

        private String _elIgnored;

        private String _pageEncoding;

        private String _scriptingInvalid;

        private String _isXml;

        private List<String> _includePreludes = new ArrayList();

        private List<String> _includeCodas = new ArrayList();

        private String _deferredSyntaxAllowedAsLiteral;

        private String _trimDirectiveWhitespaces;

        private String _defaultContentType;

        private String _buffer;

        private String _errorOnUndeclaredNamespace;

        @Override
        public Collection<String> getUrlPatterns() {
            return new ArrayList(this._urlPatterns);
        }

        public void addUrlPattern(String s) {
            if (!this._urlPatterns.contains(s)) {
                this._urlPatterns.add(s);
            }
        }

        @Override
        public String getElIgnored() {
            return this._elIgnored;
        }

        public void setElIgnored(String s) {
            this._elIgnored = s;
        }

        @Override
        public String getPageEncoding() {
            return this._pageEncoding;
        }

        public void setPageEncoding(String pageEncoding) {
            this._pageEncoding = pageEncoding;
        }

        public void setScriptingInvalid(String scriptingInvalid) {
            this._scriptingInvalid = scriptingInvalid;
        }

        public void setIsXml(String isXml) {
            this._isXml = isXml;
        }

        public void setDeferredSyntaxAllowedAsLiteral(String deferredSyntaxAllowedAsLiteral) {
            this._deferredSyntaxAllowedAsLiteral = deferredSyntaxAllowedAsLiteral;
        }

        public void setTrimDirectiveWhitespaces(String trimDirectiveWhitespaces) {
            this._trimDirectiveWhitespaces = trimDirectiveWhitespaces;
        }

        public void setDefaultContentType(String defaultContentType) {
            this._defaultContentType = defaultContentType;
        }

        public void setBuffer(String buffer) {
            this._buffer = buffer;
        }

        public void setErrorOnUndeclaredNamespace(String errorOnUndeclaredNamespace) {
            this._errorOnUndeclaredNamespace = errorOnUndeclaredNamespace;
        }

        @Override
        public String getScriptingInvalid() {
            return this._scriptingInvalid;
        }

        @Override
        public String getIsXml() {
            return this._isXml;
        }

        @Override
        public Collection<String> getIncludePreludes() {
            return new ArrayList(this._includePreludes);
        }

        public void addIncludePrelude(String prelude) {
            if (!this._includePreludes.contains(prelude)) {
                this._includePreludes.add(prelude);
            }
        }

        @Override
        public Collection<String> getIncludeCodas() {
            return new ArrayList(this._includeCodas);
        }

        public void addIncludeCoda(String coda) {
            if (!this._includeCodas.contains(coda)) {
                this._includeCodas.add(coda);
            }
        }

        @Override
        public String getDeferredSyntaxAllowedAsLiteral() {
            return this._deferredSyntaxAllowedAsLiteral;
        }

        @Override
        public String getTrimDirectiveWhitespaces() {
            return this._trimDirectiveWhitespaces;
        }

        @Override
        public String getDefaultContentType() {
            return this._defaultContentType;
        }

        @Override
        public String getBuffer() {
            return this._buffer;
        }

        @Override
        public String getErrorOnUndeclaredNamespace() {
            return this._errorOnUndeclaredNamespace;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("JspPropertyGroupDescriptor:");
            sb.append(" el-ignored=" + this._elIgnored);
            sb.append(" is-xml=" + this._isXml);
            sb.append(" page-encoding=" + this._pageEncoding);
            sb.append(" scripting-invalid=" + this._scriptingInvalid);
            sb.append(" deferred-syntax-allowed-as-literal=" + this._deferredSyntaxAllowedAsLiteral);
            sb.append(" trim-directive-whitespaces" + this._trimDirectiveWhitespaces);
            sb.append(" default-content-type=" + this._defaultContentType);
            sb.append(" buffer=" + this._buffer);
            sb.append(" error-on-undeclared-namespace=" + this._errorOnUndeclaredNamespace);
            for (String prelude : this._includePreludes) {
                sb.append(" include-prelude=" + prelude);
            }
            for (String coda : this._includeCodas) {
                sb.append(" include-coda=" + coda);
            }
            return sb.toString();
        }
    }

    private static class LegacyDecorator implements ServletContextHandler.Decorator {

        private info.journeymap.shaded.org.eclipse.jetty.util.Decorator decorator;

        public LegacyDecorator(info.journeymap.shaded.org.eclipse.jetty.util.Decorator decorator) {
            this.decorator = decorator;
        }

        @Override
        public <T> T decorate(T o) {
            return this.decorator.decorate(o);
        }

        @Override
        public void destroy(Object o) {
            this.decorator.destroy(o);
        }
    }

    public interface ServletContainerInitializerCaller extends LifeCycle {
    }

    public static class TagLib implements TaglibDescriptor {

        private String _uri;

        private String _location;

        @Override
        public String getTaglibURI() {
            return this._uri;
        }

        public void setTaglibURI(String uri) {
            this._uri = uri;
        }

        @Override
        public String getTaglibLocation() {
            return this._location;
        }

        public void setTaglibLocation(String location) {
            this._location = location;
        }

        public String toString() {
            return "TagLibDescriptor: taglib-uri=" + this._uri + " location=" + this._location;
        }
    }
}
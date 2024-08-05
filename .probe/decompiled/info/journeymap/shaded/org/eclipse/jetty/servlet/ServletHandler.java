package info.journeymap.shaded.org.eclipse.jetty.servlet;

import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.MappedResource;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathMappings;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathSpec;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.ServletPathSpec;
import info.journeymap.shaded.org.eclipse.jetty.security.IdentityService;
import info.journeymap.shaded.org.eclipse.jetty.security.SecurityHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.ServletRequestHttpWrapper;
import info.journeymap.shaded.org.eclipse.jetty.server.ServletResponseHttpWrapper;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ScopedHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.LazyList;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiException;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiMap;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.FilterChain;
import info.journeymap.shaded.org.javax.servlet.Servlet;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRegistration;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.ServletSecurityElement;
import info.journeymap.shaded.org.javax.servlet.http.HttpServlet;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@ManagedObject("Servlet Handler")
public class ServletHandler extends ScopedHandler {

    private static final Logger LOG = Log.getLogger(ServletHandler.class);

    public static final String __DEFAULT_SERVLET = "default";

    private ServletContextHandler _contextHandler;

    private ServletContext _servletContext;

    private FilterHolder[] _filters = new FilterHolder[0];

    private FilterMapping[] _filterMappings;

    private int _matchBeforeIndex = -1;

    private int _matchAfterIndex = -1;

    private boolean _filterChainsCached = true;

    private int _maxFilterChainsCacheSize = 512;

    private boolean _startWithUnavailable = false;

    private boolean _ensureDefaultServlet = true;

    private IdentityService _identityService;

    private boolean _allowDuplicateMappings = false;

    private ServletHolder[] _servlets = new ServletHolder[0];

    private ServletMapping[] _servletMappings;

    private final Map<String, FilterHolder> _filterNameMap = new HashMap();

    private List<FilterMapping> _filterPathMappings;

    private MultiMap<FilterMapping> _filterNameMappings;

    private final Map<String, ServletHolder> _servletNameMap = new HashMap();

    private PathMappings<ServletHolder> _servletPathMap;

    private ListenerHolder[] _listeners = new ListenerHolder[0];

    protected final ConcurrentMap<String, FilterChain>[] _chainCache = new ConcurrentMap[31];

    protected final Queue<String>[] _chainLRU = new Queue[31];

    @Override
    protected synchronized void doStart() throws Exception {
        ContextHandler.Context context = ContextHandler.getCurrentContext();
        this._servletContext = (ServletContext) (context == null ? new ContextHandler.StaticContext() : context);
        this._contextHandler = (ServletContextHandler) (context == null ? null : context.getContextHandler());
        if (this._contextHandler != null) {
            SecurityHandler security_handler = this._contextHandler.getChildHandlerByClass(SecurityHandler.class);
            if (security_handler != null) {
                this._identityService = security_handler.getIdentityService();
            }
        }
        this.updateNameMappings();
        this.updateMappings();
        if (this.getServletMapping("/") == null && this.isEnsureDefaultServlet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding Default404Servlet to {}", this);
            }
            this.addServletWithMapping(ServletHandler.Default404Servlet.class, "/");
            this.updateMappings();
            this.getServletMapping("/").setDefault(true);
        }
        if (this.isFilterChainsCached()) {
            this._chainCache[1] = new ConcurrentHashMap();
            this._chainCache[2] = new ConcurrentHashMap();
            this._chainCache[4] = new ConcurrentHashMap();
            this._chainCache[8] = new ConcurrentHashMap();
            this._chainCache[16] = new ConcurrentHashMap();
            this._chainLRU[1] = new ConcurrentLinkedQueue();
            this._chainLRU[2] = new ConcurrentLinkedQueue();
            this._chainLRU[4] = new ConcurrentLinkedQueue();
            this._chainLRU[8] = new ConcurrentLinkedQueue();
            this._chainLRU[16] = new ConcurrentLinkedQueue();
        }
        if (this._contextHandler == null) {
            this.initialize();
        }
        super.doStart();
    }

    public boolean isEnsureDefaultServlet() {
        return this._ensureDefaultServlet;
    }

    public void setEnsureDefaultServlet(boolean ensureDefaultServlet) {
        this._ensureDefaultServlet = ensureDefaultServlet;
    }

    @Override
    protected void start(LifeCycle l) throws Exception {
        if (!(l instanceof Holder)) {
            super.start(l);
        }
    }

    @Override
    protected synchronized void doStop() throws Exception {
        super.doStop();
        List<FilterHolder> filterHolders = new ArrayList();
        List<FilterMapping> filterMappings = ArrayUtil.asMutableList(this._filterMappings);
        if (this._filters != null) {
            int i = this._filters.length;
            while (i-- > 0) {
                try {
                    this._filters[i].stop();
                } catch (Exception var14) {
                    LOG.warn("EXCEPTION ", var14);
                }
                if (this._filters[i].getSource() != Source.EMBEDDED) {
                    this._filterNameMap.remove(this._filters[i].getName());
                    ListIterator<FilterMapping> fmitor = filterMappings.listIterator();
                    while (fmitor.hasNext()) {
                        FilterMapping fm = (FilterMapping) fmitor.next();
                        if (fm.getFilterName().equals(this._filters[i].getName())) {
                            fmitor.remove();
                        }
                    }
                } else {
                    filterHolders.add(this._filters[i]);
                }
            }
        }
        FilterHolder[] fhs = (FilterHolder[]) LazyList.toArray(filterHolders, FilterHolder.class);
        this.updateBeans(this._filters, fhs);
        this._filters = fhs;
        FilterMapping[] fms = (FilterMapping[]) LazyList.toArray(filterMappings, FilterMapping.class);
        this.updateBeans(this._filterMappings, fms);
        this._filterMappings = fms;
        this._matchAfterIndex = this._filterMappings != null && this._filterMappings.length != 0 ? this._filterMappings.length - 1 : -1;
        this._matchBeforeIndex = -1;
        List<ServletHolder> servletHolders = new ArrayList();
        List<ServletMapping> servletMappings = ArrayUtil.asMutableList(this._servletMappings);
        if (this._servlets != null) {
            int i = this._servlets.length;
            while (i-- > 0) {
                try {
                    this._servlets[i].stop();
                } catch (Exception var13) {
                    LOG.warn("EXCEPTION ", var13);
                }
                if (this._servlets[i].getSource() != Source.EMBEDDED) {
                    this._servletNameMap.remove(this._servlets[i].getName());
                    ListIterator<ServletMapping> smitor = servletMappings.listIterator();
                    while (smitor.hasNext()) {
                        ServletMapping sm = (ServletMapping) smitor.next();
                        if (sm.getServletName().equals(this._servlets[i].getName())) {
                            smitor.remove();
                        }
                    }
                } else {
                    servletHolders.add(this._servlets[i]);
                }
            }
        }
        ServletHolder[] shs = (ServletHolder[]) LazyList.toArray(servletHolders, ServletHolder.class);
        this.updateBeans(this._servlets, shs);
        this._servlets = shs;
        ServletMapping[] sms = (ServletMapping[]) LazyList.toArray(servletMappings, ServletMapping.class);
        this.updateBeans(this._servletMappings, sms);
        this._servletMappings = sms;
        List<ListenerHolder> listenerHolders = new ArrayList();
        if (this._listeners != null) {
            int i = this._listeners.length;
            while (i-- > 0) {
                try {
                    this._listeners[i].stop();
                } catch (Exception var12) {
                    LOG.warn("EXCEPTION ", var12);
                }
                if (this._listeners[i].getSource() == Source.EMBEDDED) {
                    listenerHolders.add(this._listeners[i]);
                }
            }
        }
        ListenerHolder[] listeners = (ListenerHolder[]) LazyList.toArray(listenerHolders, ListenerHolder.class);
        this.updateBeans(this._listeners, listeners);
        this._listeners = listeners;
        this._filterPathMappings = null;
        this._filterNameMappings = null;
        this._servletPathMap = null;
    }

    protected IdentityService getIdentityService() {
        return this._identityService;
    }

    @ManagedAttribute(value = "filters", readonly = true)
    public FilterMapping[] getFilterMappings() {
        return this._filterMappings;
    }

    @ManagedAttribute(value = "filters", readonly = true)
    public FilterHolder[] getFilters() {
        return this._filters;
    }

    public MappedResource<ServletHolder> getHolderEntry(String pathInContext) {
        return this._servletPathMap == null ? null : this._servletPathMap.getMatch(pathInContext);
    }

    public ServletContext getServletContext() {
        return this._servletContext;
    }

    @ManagedAttribute(value = "mappings of servlets", readonly = true)
    public ServletMapping[] getServletMappings() {
        return this._servletMappings;
    }

    public ServletMapping getServletMapping(String pathSpec) {
        if (pathSpec != null && this._servletMappings != null) {
            ServletMapping mapping = null;
            for (int i = 0; i < this._servletMappings.length && mapping == null; i++) {
                ServletMapping m = this._servletMappings[i];
                if (m.getPathSpecs() != null) {
                    for (String p : m.getPathSpecs()) {
                        if (pathSpec.equals(p)) {
                            mapping = m;
                            break;
                        }
                    }
                }
            }
            return mapping;
        } else {
            return null;
        }
    }

    @ManagedAttribute(value = "servlets", readonly = true)
    public ServletHolder[] getServlets() {
        return this._servlets;
    }

    public ServletHolder getServlet(String name) {
        return (ServletHolder) this._servletNameMap.get(name);
    }

    @Override
    public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String old_servlet_path = baseRequest.getServletPath();
        String old_path_info = baseRequest.getPathInfo();
        DispatcherType type = baseRequest.getDispatcherType();
        ServletHolder servlet_holder = null;
        UserIdentity.Scope old_scope = null;
        if (target.startsWith("/")) {
            MappedResource<ServletHolder> entry = this.getHolderEntry(target);
            if (entry != null) {
                PathSpec pathSpec = entry.getPathSpec();
                servlet_holder = entry.getResource();
                String servlet_path = pathSpec.getPathMatch(target);
                String path_info = pathSpec.getPathInfo(target);
                if (DispatcherType.INCLUDE.equals(type)) {
                    baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.include.servlet_path", servlet_path);
                    baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.include.path_info", path_info);
                } else {
                    baseRequest.setServletPath(servlet_path);
                    baseRequest.setPathInfo(path_info);
                }
            }
        } else {
            servlet_holder = (ServletHolder) this._servletNameMap.get(target);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("servlet {}|{}|{} -> {}", baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), servlet_holder);
        }
        try {
            old_scope = baseRequest.getUserIdentityScope();
            baseRequest.setUserIdentityScope(servlet_holder);
            this.nextScope(target, baseRequest, request, response);
        } finally {
            if (old_scope != null) {
                baseRequest.setUserIdentityScope(old_scope);
            }
            if (!DispatcherType.INCLUDE.equals(type)) {
                baseRequest.setServletPath(old_servlet_path);
                baseRequest.setPathInfo(old_path_info);
            }
        }
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletHolder servlet_holder = (ServletHolder) baseRequest.getUserIdentityScope();
        FilterChain chain = null;
        if (target.startsWith("/")) {
            if (servlet_holder != null && this._filterMappings != null && this._filterMappings.length > 0) {
                chain = this.getFilterChain(baseRequest, target, servlet_holder);
            }
        } else if (servlet_holder != null && this._filterMappings != null && this._filterMappings.length > 0) {
            chain = this.getFilterChain(baseRequest, null, servlet_holder);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("chain={}", chain);
        }
        try {
            if (servlet_holder == null) {
                this.notFound(baseRequest, request, response);
            } else {
                ServletRequest req = request;
                if (request instanceof ServletRequestHttpWrapper) {
                    req = ((ServletRequestHttpWrapper) request).getRequest();
                }
                ServletResponse res = response;
                if (response instanceof ServletResponseHttpWrapper) {
                    res = ((ServletResponseHttpWrapper) response).getResponse();
                }
                servlet_holder.prepare(baseRequest, req, res);
                if (chain != null) {
                    chain.doFilter(req, res);
                } else {
                    servlet_holder.handle(baseRequest, req, res);
                }
            }
        } finally {
            if (servlet_holder != null) {
                baseRequest.setHandled(true);
            }
        }
    }

    protected FilterChain getFilterChain(Request baseRequest, String pathInContext, ServletHolder servletHolder) {
        String key = pathInContext == null ? servletHolder.getName() : pathInContext;
        int dispatch = FilterMapping.dispatch(baseRequest.getDispatcherType());
        if (this._filterChainsCached && this._chainCache != null) {
            FilterChain chain = (FilterChain) this._chainCache[dispatch].get(key);
            if (chain != null) {
                return chain;
            }
        }
        List<FilterHolder> filters = new ArrayList();
        if (pathInContext != null && this._filterPathMappings != null) {
            for (FilterMapping filterPathMapping : this._filterPathMappings) {
                if (filterPathMapping.appliesTo(pathInContext, dispatch)) {
                    filters.add(filterPathMapping.getFilterHolder());
                }
            }
        }
        if (servletHolder != null && this._filterNameMappings != null && !this._filterNameMappings.isEmpty()) {
            Object o = this._filterNameMappings.get(servletHolder.getName());
            for (int i = 0; i < LazyList.size(o); i++) {
                FilterMapping mapping = LazyList.get(o, i);
                if (mapping.appliesTo(dispatch)) {
                    filters.add(mapping.getFilterHolder());
                }
            }
            o = this._filterNameMappings.get("*");
            for (int ix = 0; ix < LazyList.size(o); ix++) {
                FilterMapping mapping = LazyList.get(o, ix);
                if (mapping.appliesTo(dispatch)) {
                    filters.add(mapping.getFilterHolder());
                }
            }
        }
        if (filters.isEmpty()) {
            return null;
        } else {
            FilterChain chain = null;
            if (this._filterChainsCached) {
                if (filters.size() > 0) {
                    chain = this.newCachedChain(filters, servletHolder);
                }
                Map<String, FilterChain> cache = this._chainCache[dispatch];
                Queue<String> lru = this._chainLRU[dispatch];
                while (this._maxFilterChainsCacheSize > 0 && cache.size() >= this._maxFilterChainsCacheSize) {
                    String k = (String) lru.poll();
                    if (k == null) {
                        cache.clear();
                        break;
                    }
                    cache.remove(k);
                }
                cache.put(key, chain);
                lru.add(key);
            } else if (filters.size() > 0) {
                chain = new ServletHandler.Chain(baseRequest, filters, servletHolder);
            }
            return chain;
        }
    }

    protected void invalidateChainsCache() {
        if (this._chainLRU[1] != null) {
            this._chainLRU[1].clear();
            this._chainLRU[2].clear();
            this._chainLRU[4].clear();
            this._chainLRU[8].clear();
            this._chainLRU[16].clear();
            this._chainCache[1].clear();
            this._chainCache[2].clear();
            this._chainCache[4].clear();
            this._chainCache[8].clear();
            this._chainCache[16].clear();
        }
    }

    public boolean isAvailable() {
        if (!this.isStarted()) {
            return false;
        } else {
            ServletHolder[] holders = this.getServlets();
            for (ServletHolder holder : holders) {
                if (holder != null && !holder.isAvailable()) {
                    return false;
                }
            }
            return true;
        }
    }

    public void setStartWithUnavailable(boolean start) {
        this._startWithUnavailable = start;
    }

    public boolean isAllowDuplicateMappings() {
        return this._allowDuplicateMappings;
    }

    public void setAllowDuplicateMappings(boolean allowDuplicateMappings) {
        this._allowDuplicateMappings = allowDuplicateMappings;
    }

    public boolean isStartWithUnavailable() {
        return this._startWithUnavailable;
    }

    public void initialize() throws Exception {
        MultiException mx = new MultiException();
        if (this._filters != null) {
            for (FilterHolder f : this._filters) {
                try {
                    f.start();
                    f.initialize();
                } catch (Exception var10) {
                    mx.add(var10);
                }
            }
        }
        if (this._servlets != null) {
            ServletHolder[] servlets = (ServletHolder[]) this._servlets.clone();
            Arrays.sort(servlets);
            for (ServletHolder servlet : servlets) {
                try {
                    servlet.start();
                    servlet.initialize();
                } catch (Throwable var9) {
                    LOG.debug("EXCEPTION ", var9);
                    mx.add(var9);
                }
            }
        }
        for (Holder<?> h : this.getBeans(Holder.class)) {
            try {
                if (!h.isStarted()) {
                    h.start();
                    h.initialize();
                }
            } catch (Exception var8) {
                mx.add(var8);
            }
        }
        mx.ifExceptionThrow();
    }

    public boolean isFilterChainsCached() {
        return this._filterChainsCached;
    }

    public void addListener(ListenerHolder listener) {
        if (listener != null) {
            this.setListeners(ArrayUtil.addToArray(this.getListeners(), listener, ListenerHolder.class));
        }
    }

    public ListenerHolder[] getListeners() {
        return this._listeners;
    }

    public void setListeners(ListenerHolder[] listeners) {
        if (listeners != null) {
            for (ListenerHolder holder : listeners) {
                holder.setServletHandler(this);
            }
        }
        this.updateBeans(this._listeners, listeners);
        this._listeners = listeners;
    }

    public ListenerHolder newListenerHolder(Source source) {
        return new ListenerHolder(source);
    }

    public ServletHandler.CachedChain newCachedChain(List<FilterHolder> filters, ServletHolder servletHolder) {
        return new ServletHandler.CachedChain(filters, servletHolder);
    }

    public ServletHolder newServletHolder(Source source) {
        return new ServletHolder(source);
    }

    public ServletHolder addServletWithMapping(String className, String pathSpec) {
        ServletHolder holder = this.newServletHolder(Source.EMBEDDED);
        holder.setClassName(className);
        this.addServletWithMapping(holder, pathSpec);
        return holder;
    }

    public ServletHolder addServletWithMapping(Class<? extends Servlet> servlet, String pathSpec) {
        ServletHolder holder = this.newServletHolder(Source.EMBEDDED);
        holder.setHeldClass(servlet);
        this.addServletWithMapping(holder, pathSpec);
        return holder;
    }

    public void addServletWithMapping(ServletHolder servlet, String pathSpec) {
        ServletHolder[] holders = this.getServlets();
        if (holders != null) {
            holders = (ServletHolder[]) holders.clone();
        }
        try {
            synchronized (this) {
                if (servlet != null && !this.containsServletHolder(servlet)) {
                    this.setServlets(ArrayUtil.addToArray(holders, servlet, ServletHolder.class));
                }
            }
            ServletMapping mapping = new ServletMapping();
            mapping.setServletName(servlet.getName());
            mapping.setPathSpec(pathSpec);
            this.setServletMappings(ArrayUtil.addToArray(this.getServletMappings(), mapping, ServletMapping.class));
        } catch (RuntimeException var7) {
            this.setServlets(holders);
            throw var7;
        }
    }

    public void addServlet(ServletHolder holder) {
        if (holder != null) {
            synchronized (this) {
                if (!this.containsServletHolder(holder)) {
                    this.setServlets(ArrayUtil.addToArray(this.getServlets(), holder, ServletHolder.class));
                }
            }
        }
    }

    public void addServletMapping(ServletMapping mapping) {
        this.setServletMappings(ArrayUtil.addToArray(this.getServletMappings(), mapping, ServletMapping.class));
    }

    public Set<String> setServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement) {
        return this._contextHandler != null ? this._contextHandler.setServletSecurity(registration, servletSecurityElement) : Collections.emptySet();
    }

    public FilterHolder newFilterHolder(Source source) {
        return new FilterHolder(source);
    }

    public FilterHolder getFilter(String name) {
        return (FilterHolder) this._filterNameMap.get(name);
    }

    public FilterHolder addFilterWithMapping(Class<? extends Filter> filter, String pathSpec, EnumSet<DispatcherType> dispatches) {
        FilterHolder holder = this.newFilterHolder(Source.EMBEDDED);
        holder.setHeldClass(filter);
        this.addFilterWithMapping(holder, pathSpec, dispatches);
        return holder;
    }

    public FilterHolder addFilterWithMapping(String className, String pathSpec, EnumSet<DispatcherType> dispatches) {
        FilterHolder holder = this.newFilterHolder(Source.EMBEDDED);
        holder.setClassName(className);
        this.addFilterWithMapping(holder, pathSpec, dispatches);
        return holder;
    }

    public void addFilterWithMapping(FilterHolder holder, String pathSpec, EnumSet<DispatcherType> dispatches) {
        FilterHolder[] holders = this.getFilters();
        if (holders != null) {
            holders = (FilterHolder[]) holders.clone();
        }
        try {
            synchronized (this) {
                if (holder != null && !this.containsFilterHolder(holder)) {
                    this.setFilters(ArrayUtil.addToArray(holders, holder, FilterHolder.class));
                }
            }
            FilterMapping mapping = new FilterMapping();
            mapping.setFilterName(holder.getName());
            mapping.setPathSpec(pathSpec);
            mapping.setDispatcherTypes(dispatches);
            this.addFilterMapping(mapping);
        } catch (Throwable var8) {
            this.setFilters(holders);
            throw var8;
        }
    }

    public FilterHolder addFilterWithMapping(Class<? extends Filter> filter, String pathSpec, int dispatches) {
        FilterHolder holder = this.newFilterHolder(Source.EMBEDDED);
        holder.setHeldClass(filter);
        this.addFilterWithMapping(holder, pathSpec, dispatches);
        return holder;
    }

    public FilterHolder addFilterWithMapping(String className, String pathSpec, int dispatches) {
        FilterHolder holder = this.newFilterHolder(Source.EMBEDDED);
        holder.setClassName(className);
        this.addFilterWithMapping(holder, pathSpec, dispatches);
        return holder;
    }

    public void addFilterWithMapping(FilterHolder holder, String pathSpec, int dispatches) {
        FilterHolder[] holders = this.getFilters();
        if (holders != null) {
            holders = (FilterHolder[]) holders.clone();
        }
        try {
            synchronized (this) {
                if (holder != null && !this.containsFilterHolder(holder)) {
                    this.setFilters(ArrayUtil.addToArray(holders, holder, FilterHolder.class));
                }
            }
            FilterMapping mapping = new FilterMapping();
            mapping.setFilterName(holder.getName());
            mapping.setPathSpec(pathSpec);
            mapping.setDispatches(dispatches);
            this.addFilterMapping(mapping);
        } catch (Throwable var8) {
            this.setFilters(holders);
            throw var8;
        }
    }

    @Deprecated
    public FilterHolder addFilter(String className, String pathSpec, EnumSet<DispatcherType> dispatches) {
        return this.addFilterWithMapping(className, pathSpec, dispatches);
    }

    public void addFilter(FilterHolder filter, FilterMapping filterMapping) {
        if (filter != null) {
            synchronized (this) {
                if (!this.containsFilterHolder(filter)) {
                    this.setFilters(ArrayUtil.addToArray(this.getFilters(), filter, FilterHolder.class));
                }
            }
        }
        if (filterMapping != null) {
            this.addFilterMapping(filterMapping);
        }
    }

    public void addFilter(FilterHolder filter) {
        if (filter != null) {
            synchronized (this) {
                if (!this.containsFilterHolder(filter)) {
                    this.setFilters(ArrayUtil.addToArray(this.getFilters(), filter, FilterHolder.class));
                }
            }
        }
    }

    public void addFilterMapping(FilterMapping mapping) {
        if (mapping != null) {
            Source source = mapping.getFilterHolder() == null ? null : mapping.getFilterHolder().getSource();
            FilterMapping[] mappings = this.getFilterMappings();
            if (mappings == null || mappings.length == 0) {
                this.setFilterMappings(this.insertFilterMapping(mapping, 0, false));
                if (source != null && source == Source.JAVAX_API) {
                    this._matchAfterIndex = 0;
                }
            } else if (source != null && Source.JAVAX_API == source) {
                this.setFilterMappings(this.insertFilterMapping(mapping, mappings.length - 1, false));
                if (this._matchAfterIndex < 0) {
                    this._matchAfterIndex = this.getFilterMappings().length - 1;
                }
            } else if (this._matchAfterIndex < 0) {
                this.setFilterMappings(this.insertFilterMapping(mapping, mappings.length - 1, false));
            } else {
                FilterMapping[] new_mappings = this.insertFilterMapping(mapping, this._matchAfterIndex, true);
                this._matchAfterIndex++;
                this.setFilterMappings(new_mappings);
            }
        }
    }

    public void prependFilterMapping(FilterMapping mapping) {
        if (mapping != null) {
            Source source = mapping.getFilterHolder() == null ? null : mapping.getFilterHolder().getSource();
            FilterMapping[] mappings = this.getFilterMappings();
            if (mappings != null && mappings.length != 0) {
                if (source == null || Source.JAVAX_API != source) {
                    FilterMapping[] new_mappings = this.insertFilterMapping(mapping, 0, true);
                    this.setFilterMappings(new_mappings);
                } else if (this._matchBeforeIndex < 0) {
                    this._matchBeforeIndex = 0;
                    FilterMapping[] new_mappings = this.insertFilterMapping(mapping, 0, true);
                    this.setFilterMappings(new_mappings);
                } else {
                    FilterMapping[] new_mappings = this.insertFilterMapping(mapping, this._matchBeforeIndex, false);
                    this._matchBeforeIndex++;
                    this.setFilterMappings(new_mappings);
                }
                if (this._matchAfterIndex >= 0) {
                    this._matchAfterIndex++;
                }
            } else {
                this.setFilterMappings(this.insertFilterMapping(mapping, 0, false));
                if (source != null && Source.JAVAX_API == source) {
                    this._matchBeforeIndex = 0;
                }
            }
        }
    }

    protected FilterMapping[] insertFilterMapping(FilterMapping mapping, int pos, boolean before) {
        if (pos < 0) {
            throw new IllegalArgumentException("FilterMapping insertion pos < 0");
        } else {
            FilterMapping[] mappings = this.getFilterMappings();
            if (mappings != null && mappings.length != 0) {
                FilterMapping[] new_mappings = new FilterMapping[mappings.length + 1];
                if (before) {
                    System.arraycopy(mappings, 0, new_mappings, 0, pos);
                    new_mappings[pos] = mapping;
                    System.arraycopy(mappings, pos, new_mappings, pos + 1, mappings.length - pos);
                } else {
                    System.arraycopy(mappings, 0, new_mappings, 0, pos + 1);
                    new_mappings[pos + 1] = mapping;
                    if (mappings.length > pos + 1) {
                        System.arraycopy(mappings, pos + 1, new_mappings, pos + 2, mappings.length - (pos + 1));
                    }
                }
                return new_mappings;
            } else {
                return new FilterMapping[] { mapping };
            }
        }
    }

    protected synchronized void updateNameMappings() {
        this._filterNameMap.clear();
        if (this._filters != null) {
            for (FilterHolder filter : this._filters) {
                this._filterNameMap.put(filter.getName(), filter);
                filter.setServletHandler(this);
            }
        }
        this._servletNameMap.clear();
        if (this._servlets != null) {
            for (ServletHolder servlet : this._servlets) {
                this._servletNameMap.put(servlet.getName(), servlet);
                servlet.setServletHandler(this);
            }
        }
    }

    protected synchronized void updateMappings() {
        if (this._filterMappings == null) {
            this._filterPathMappings = null;
            this._filterNameMappings = null;
        } else {
            this._filterPathMappings = new ArrayList();
            this._filterNameMappings = new MultiMap<>();
            for (FilterMapping filtermapping : this._filterMappings) {
                FilterHolder filter_holder = (FilterHolder) this._filterNameMap.get(filtermapping.getFilterName());
                if (filter_holder == null) {
                    throw new IllegalStateException("No filter named " + filtermapping.getFilterName());
                }
                filtermapping.setFilterHolder(filter_holder);
                if (filtermapping.getPathSpecs() != null) {
                    this._filterPathMappings.add(filtermapping);
                }
                if (filtermapping.getServletNames() != null) {
                    String[] names = filtermapping.getServletNames();
                    for (String name : names) {
                        if (name != null) {
                            this._filterNameMappings.add(name, filtermapping);
                        }
                    }
                }
            }
        }
        if (this._servletMappings != null && this._servletNameMap != null) {
            PathMappings<ServletHolder> pm = new PathMappings<>();
            Map<String, ServletMapping> servletPathMappings = new HashMap();
            HashMap<String, List<ServletMapping>> sms = new HashMap();
            for (ServletMapping servletMapping : this._servletMappings) {
                String[] pathSpecs = servletMapping.getPathSpecs();
                if (pathSpecs != null) {
                    for (String pathSpec : pathSpecs) {
                        List<ServletMapping> mappings = (List<ServletMapping>) sms.get(pathSpec);
                        if (mappings == null) {
                            mappings = new ArrayList();
                            sms.put(pathSpec, mappings);
                        }
                        mappings.add(servletMapping);
                    }
                }
            }
            for (String pathSpec : sms.keySet()) {
                List<ServletMapping> mappings = (List<ServletMapping>) sms.get(pathSpec);
                ServletMapping finalMapping = null;
                for (ServletMapping mapping : mappings) {
                    ServletHolder servlet_holder = (ServletHolder) this._servletNameMap.get(mapping.getServletName());
                    if (servlet_holder == null) {
                        throw new IllegalStateException("No such servlet: " + mapping.getServletName());
                    }
                    if (servlet_holder.isEnabled()) {
                        if (finalMapping == null) {
                            finalMapping = mapping;
                        } else if (finalMapping.isDefault()) {
                            finalMapping = mapping;
                        } else if (this.isAllowDuplicateMappings()) {
                            LOG.warn("Multiple servlets map to path {}: {} and {}, choosing {}", pathSpec, finalMapping.getServletName(), mapping.getServletName(), mapping);
                            finalMapping = mapping;
                        } else if (!mapping.isDefault()) {
                            ServletHolder finalMappedServlet = (ServletHolder) this._servletNameMap.get(finalMapping.getServletName());
                            throw new IllegalStateException("Multiple servlets map to path " + pathSpec + ": " + finalMappedServlet.getName() + "[mapped:" + finalMapping.getSource() + "]," + mapping.getServletName() + "[mapped:" + mapping.getSource() + "]");
                        }
                    }
                }
                if (finalMapping == null) {
                    throw new IllegalStateException("No acceptable servlet mappings for " + pathSpec);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Path={}[{}] mapped to servlet={}[{}]", pathSpec, finalMapping.getSource(), finalMapping.getServletName(), ((ServletHolder) this._servletNameMap.get(finalMapping.getServletName())).getSource());
                }
                servletPathMappings.put(pathSpec, finalMapping);
                pm.put(new ServletPathSpec(pathSpec), (ServletHolder) this._servletNameMap.get(finalMapping.getServletName()));
            }
            this._servletPathMap = pm;
        } else {
            this._servletPathMap = null;
        }
        if (this._chainCache != null) {
            int i = this._chainCache.length;
            while (i-- > 0) {
                if (this._chainCache[i] != null) {
                    this._chainCache[i].clear();
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("filterNameMap=" + this._filterNameMap);
            LOG.debug("pathFilters=" + this._filterPathMappings);
            LOG.debug("servletFilterMap=" + this._filterNameMappings);
            LOG.debug("servletPathMap=" + this._servletPathMap);
            LOG.debug("servletNameMap=" + this._servletNameMap);
        }
        try {
            if (this._contextHandler != null && this._contextHandler.isStarted() || this._contextHandler == null && this.isStarted()) {
                this.initialize();
            }
        } catch (Exception var14) {
            throw new RuntimeException(var14);
        }
    }

    protected void notFound(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Not Found {}", request.getRequestURI());
        }
        if (this.getHandler() != null) {
            this.nextHandle(URIUtil.addPaths(request.getServletPath(), request.getPathInfo()), baseRequest, request, response);
        }
    }

    protected synchronized boolean containsFilterHolder(FilterHolder holder) {
        if (this._filters == null) {
            return false;
        } else {
            boolean found = false;
            for (FilterHolder f : this._filters) {
                if (f == holder) {
                    found = true;
                }
            }
            return found;
        }
    }

    protected synchronized boolean containsServletHolder(ServletHolder holder) {
        if (this._servlets == null) {
            return false;
        } else {
            boolean found = false;
            for (ServletHolder s : this._servlets) {
                if (s == holder) {
                    found = true;
                }
            }
            return found;
        }
    }

    public void setFilterChainsCached(boolean filterChainsCached) {
        this._filterChainsCached = filterChainsCached;
    }

    public void setFilterMappings(FilterMapping[] filterMappings) {
        this.updateBeans(this._filterMappings, filterMappings);
        this._filterMappings = filterMappings;
        if (this.isStarted()) {
            this.updateMappings();
        }
        this.invalidateChainsCache();
    }

    public synchronized void setFilters(FilterHolder[] holders) {
        if (holders != null) {
            for (FilterHolder holder : holders) {
                holder.setServletHandler(this);
            }
        }
        this.updateBeans(this._filters, holders);
        this._filters = holders;
        this.updateNameMappings();
        this.invalidateChainsCache();
    }

    public void setServletMappings(ServletMapping[] servletMappings) {
        this.updateBeans(this._servletMappings, servletMappings);
        this._servletMappings = servletMappings;
        if (this.isStarted()) {
            this.updateMappings();
        }
        this.invalidateChainsCache();
    }

    public synchronized void setServlets(ServletHolder[] holders) {
        if (holders != null) {
            for (ServletHolder holder : holders) {
                holder.setServletHandler(this);
            }
        }
        this.updateBeans(this._servlets, holders);
        this._servlets = holders;
        this.updateNameMappings();
        this.invalidateChainsCache();
    }

    public int getMaxFilterChainsCacheSize() {
        return this._maxFilterChainsCacheSize;
    }

    public void setMaxFilterChainsCacheSize(int maxFilterChainsCacheSize) {
        this._maxFilterChainsCacheSize = maxFilterChainsCacheSize;
    }

    void destroyServlet(Servlet servlet) {
        if (this._contextHandler != null) {
            this._contextHandler.destroyServlet(servlet);
        }
    }

    void destroyFilter(Filter filter) {
        if (this._contextHandler != null) {
            this._contextHandler.destroyFilter(filter);
        }
    }

    protected class CachedChain implements FilterChain {

        FilterHolder _filterHolder;

        ServletHandler.CachedChain _next;

        ServletHolder _servletHolder;

        protected CachedChain(List<FilterHolder> filters, ServletHolder servletHolder) {
            if (filters.size() > 0) {
                this._filterHolder = (FilterHolder) filters.get(0);
                filters.remove(0);
                this._next = ServletHandler.this.new CachedChain(filters, servletHolder);
            } else {
                this._servletHolder = servletHolder;
            }
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            Request baseRequest = Request.getBaseRequest(request);
            if (this._filterHolder != null) {
                if (ServletHandler.LOG.isDebugEnabled()) {
                    ServletHandler.LOG.debug("call filter {}", this._filterHolder);
                }
                Filter filter = this._filterHolder.getFilter();
                if (baseRequest.isAsyncSupported() && !this._filterHolder.isAsyncSupported()) {
                    try {
                        baseRequest.setAsyncSupported(false, this._filterHolder.toString());
                        filter.doFilter(request, response, this._next);
                    } finally {
                        baseRequest.setAsyncSupported(true, null);
                    }
                } else {
                    filter.doFilter(request, response, this._next);
                }
            } else {
                HttpServletRequest srequest = (HttpServletRequest) request;
                if (this._servletHolder == null) {
                    ServletHandler.this.notFound(baseRequest, srequest, (HttpServletResponse) response);
                } else {
                    if (ServletHandler.LOG.isDebugEnabled()) {
                        ServletHandler.LOG.debug("call servlet " + this._servletHolder);
                    }
                    this._servletHolder.handle(baseRequest, request, response);
                }
            }
        }

        public String toString() {
            if (this._filterHolder != null) {
                return this._filterHolder + "->" + this._next.toString();
            } else {
                return this._servletHolder != null ? this._servletHolder.toString() : "null";
            }
        }
    }

    private class Chain implements FilterChain {

        final Request _baseRequest;

        final List<FilterHolder> _chain;

        final ServletHolder _servletHolder;

        int _filter = 0;

        private Chain(Request baseRequest, List<FilterHolder> filters, ServletHolder servletHolder) {
            this._baseRequest = baseRequest;
            this._chain = filters;
            this._servletHolder = servletHolder;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (ServletHandler.LOG.isDebugEnabled()) {
                ServletHandler.LOG.debug("doFilter " + this._filter);
            }
            if (this._filter < this._chain.size()) {
                FilterHolder holder = (FilterHolder) this._chain.get(this._filter++);
                if (ServletHandler.LOG.isDebugEnabled()) {
                    ServletHandler.LOG.debug("call filter " + holder);
                }
                Filter filter = holder.getFilter();
                if (!holder.isAsyncSupported() && this._baseRequest.isAsyncSupported()) {
                    try {
                        this._baseRequest.setAsyncSupported(false, holder.toString());
                        filter.doFilter(request, response, this);
                    } finally {
                        this._baseRequest.setAsyncSupported(true, null);
                    }
                } else {
                    filter.doFilter(request, response, this);
                }
            } else {
                HttpServletRequest srequest = (HttpServletRequest) request;
                if (this._servletHolder == null) {
                    ServletHandler.this.notFound(Request.getBaseRequest(request), srequest, (HttpServletResponse) response);
                } else {
                    if (ServletHandler.LOG.isDebugEnabled()) {
                        ServletHandler.LOG.debug("call servlet {}", this._servletHolder);
                    }
                    this._servletHolder.handle(this._baseRequest, request, response);
                }
            }
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            for (FilterHolder f : this._chain) {
                b.append(f.toString());
                b.append("->");
            }
            b.append(this._servletHolder);
            return b.toString();
        }
    }

    public static class Default404Servlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.sendError(404);
        }
    }
}
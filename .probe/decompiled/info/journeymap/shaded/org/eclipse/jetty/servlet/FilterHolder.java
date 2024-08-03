package info.journeymap.shaded.org.eclipse.jetty.servlet;

import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.FilterConfig;
import info.journeymap.shaded.org.javax.servlet.FilterRegistration;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class FilterHolder extends Holder<Filter> {

    private static final Logger LOG = Log.getLogger(FilterHolder.class);

    private transient Filter _filter;

    private transient FilterHolder.Config _config;

    private transient FilterRegistration.Dynamic _registration;

    public FilterHolder() {
        this(Source.EMBEDDED);
    }

    public FilterHolder(Source source) {
        super(source);
    }

    public FilterHolder(Class<? extends Filter> filter) {
        this(Source.EMBEDDED);
        this.setHeldClass(filter);
    }

    public FilterHolder(Filter filter) {
        this(Source.EMBEDDED);
        this.setFilter(filter);
    }

    @Override
    public void doStart() throws Exception {
        super.doStart();
        if (!Filter.class.isAssignableFrom(this._class)) {
            String msg = this._class + " is not a javax.servlet.Filter";
            super.stop();
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void initialize() throws Exception {
        if (!this._initialized) {
            super.initialize();
            if (this._filter == null) {
                try {
                    ServletContext context = this._servletHandler.getServletContext();
                    this._filter = context instanceof ServletContextHandler.Context ? ((ServletContextHandler.Context) context).createFilter(this.getHeldClass()) : (Filter) this.getHeldClass().newInstance();
                } catch (ServletException var3) {
                    Throwable cause = var3.getRootCause();
                    if (cause instanceof InstantiationException) {
                        throw (InstantiationException) cause;
                    }
                    if (cause instanceof IllegalAccessException) {
                        throw (IllegalAccessException) cause;
                    }
                    throw var3;
                }
            }
            this._config = new FilterHolder.Config();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Filter.init {}", this._filter);
            }
            this._filter.init(this._config);
        }
        this._initialized = true;
    }

    @Override
    public void doStop() throws Exception {
        if (this._filter != null) {
            try {
                this.destroyInstance(this._filter);
            } catch (Exception var2) {
                LOG.warn(var2);
            }
        }
        if (!this._extInstance) {
            this._filter = null;
        }
        this._config = null;
        this._initialized = false;
        super.doStop();
    }

    @Override
    public void destroyInstance(Object o) throws Exception {
        if (o != null) {
            Filter f = (Filter) o;
            f.destroy();
            this.getServletHandler().destroyFilter(f);
        }
    }

    public synchronized void setFilter(Filter filter) {
        this._filter = filter;
        this._extInstance = true;
        this.setHeldClass(filter.getClass());
        if (this.getName() == null) {
            this.setName(filter.getClass().getName());
        }
    }

    public Filter getFilter() {
        return this._filter;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        super.dump(out, indent);
        if (this._filter instanceof Dumpable) {
            ((Dumpable) this._filter).dump(out, indent);
        }
    }

    public FilterRegistration.Dynamic getRegistration() {
        if (this._registration == null) {
            this._registration = new FilterHolder.Registration();
        }
        return this._registration;
    }

    class Config extends Holder<Filter>.HolderConfig implements FilterConfig {

        @Override
        public String getFilterName() {
            return FilterHolder.this._name;
        }
    }

    protected class Registration extends Holder<Filter>.HolderRegistration implements FilterRegistration.Dynamic {

        @Override
        public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
            FilterHolder.this.illegalStateIfContextStarted();
            FilterMapping mapping = new FilterMapping();
            mapping.setFilterHolder(FilterHolder.this);
            mapping.setServletNames(servletNames);
            mapping.setDispatcherTypes(dispatcherTypes);
            if (isMatchAfter) {
                FilterHolder.this._servletHandler.addFilterMapping(mapping);
            } else {
                FilterHolder.this._servletHandler.prependFilterMapping(mapping);
            }
        }

        @Override
        public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns) {
            FilterHolder.this.illegalStateIfContextStarted();
            FilterMapping mapping = new FilterMapping();
            mapping.setFilterHolder(FilterHolder.this);
            mapping.setPathSpecs(urlPatterns);
            mapping.setDispatcherTypes(dispatcherTypes);
            if (isMatchAfter) {
                FilterHolder.this._servletHandler.addFilterMapping(mapping);
            } else {
                FilterHolder.this._servletHandler.prependFilterMapping(mapping);
            }
        }

        @Override
        public Collection<String> getServletNameMappings() {
            FilterMapping[] mappings = FilterHolder.this._servletHandler.getFilterMappings();
            List<String> names = new ArrayList();
            for (FilterMapping mapping : mappings) {
                if (mapping.getFilterHolder() == FilterHolder.this) {
                    String[] servlets = mapping.getServletNames();
                    if (servlets != null && servlets.length > 0) {
                        names.addAll(Arrays.asList(servlets));
                    }
                }
            }
            return names;
        }

        @Override
        public Collection<String> getUrlPatternMappings() {
            FilterMapping[] mappings = FilterHolder.this._servletHandler.getFilterMappings();
            List<String> patterns = new ArrayList();
            for (FilterMapping mapping : mappings) {
                if (mapping.getFilterHolder() == FilterHolder.this) {
                    String[] specs = mapping.getPathSpecs();
                    patterns.addAll(TypeUtil.<String>asList(specs));
                }
            }
            return patterns;
        }
    }
}
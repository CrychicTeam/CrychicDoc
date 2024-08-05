package info.journeymap.shaded.org.eclipse.jetty.servlet;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.Registration;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@ManagedObject("Holder - a container for servlets and the like")
public class Holder<T> extends BaseHolder<T> {

    private static final Logger LOG = Log.getLogger(Holder.class);

    protected final Map<String, String> _initParams = new HashMap(3);

    protected String _displayName;

    protected boolean _asyncSupported;

    protected String _name;

    protected boolean _initialized = false;

    protected Holder(Source source) {
        super(source);
        switch(this._source.getOrigin()) {
            case JAVAX_API:
            case DESCRIPTOR:
            case ANNOTATION:
                this._asyncSupported = false;
                break;
            default:
                this._asyncSupported = true;
        }
    }

    @ManagedAttribute(value = "Display Name", readonly = true)
    public String getDisplayName() {
        return this._displayName;
    }

    public String getInitParameter(String param) {
        return this._initParams == null ? null : (String) this._initParams.get(param);
    }

    public Enumeration<String> getInitParameterNames() {
        return this._initParams == null ? Collections.enumeration(Collections.EMPTY_LIST) : Collections.enumeration(this._initParams.keySet());
    }

    @ManagedAttribute(value = "Initial Parameters", readonly = true)
    public Map<String, String> getInitParameters() {
        return this._initParams;
    }

    @ManagedAttribute(value = "Name", readonly = true)
    public String getName() {
        return this._name;
    }

    public void destroyInstance(Object instance) throws Exception {
    }

    @Override
    public void setClassName(String className) {
        super.setClassName(className);
        if (this._name == null) {
            this._name = className + "-" + Integer.toHexString(this.hashCode());
        }
    }

    @Override
    public void setHeldClass(Class<? extends T> held) {
        super.setHeldClass(held);
        if (held != null && this._name == null) {
            this._name = held.getName() + "-" + Integer.toHexString(this.hashCode());
        }
    }

    public void setDisplayName(String name) {
        this._displayName = name;
    }

    public void setInitParameter(String param, String value) {
        this._initParams.put(param, value);
    }

    public void setInitParameters(Map<String, String> map) {
        this._initParams.clear();
        this._initParams.putAll(map);
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setAsyncSupported(boolean suspendable) {
        this._asyncSupported = suspendable;
    }

    public boolean isAsyncSupported() {
        return this._asyncSupported;
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        super.dump(out, indent);
        ContainerLifeCycle.dump(out, indent, this._initParams.entrySet());
    }

    @Override
    public String dump() {
        return super.dump();
    }

    public String toString() {
        return String.format("%s@%x==%s", this._name, this.hashCode(), this._className);
    }

    protected class HolderConfig {

        public ServletContext getServletContext() {
            return Holder.this._servletHandler.getServletContext();
        }

        public String getInitParameter(String param) {
            return Holder.this.getInitParameter(param);
        }

        public Enumeration<String> getInitParameterNames() {
            return Holder.this.getInitParameterNames();
        }
    }

    protected class HolderRegistration implements Registration.Dynamic {

        @Override
        public void setAsyncSupported(boolean isAsyncSupported) {
            Holder.this.illegalStateIfContextStarted();
            Holder.this.setAsyncSupported(isAsyncSupported);
        }

        public void setDescription(String description) {
            if (Holder.LOG.isDebugEnabled()) {
                Holder.LOG.debug(this + " is " + description);
            }
        }

        @Override
        public String getClassName() {
            return Holder.this.getClassName();
        }

        @Override
        public String getInitParameter(String name) {
            return Holder.this.getInitParameter(name);
        }

        @Override
        public Map<String, String> getInitParameters() {
            return Holder.this.getInitParameters();
        }

        @Override
        public String getName() {
            return Holder.this.getName();
        }

        @Override
        public boolean setInitParameter(String name, String value) {
            Holder.this.illegalStateIfContextStarted();
            if (name == null) {
                throw new IllegalArgumentException("init parameter name required");
            } else if (value == null) {
                throw new IllegalArgumentException("non-null value required for init parameter " + name);
            } else if (Holder.this.getInitParameter(name) != null) {
                return false;
            } else {
                Holder.this.setInitParameter(name, value);
                return true;
            }
        }

        @Override
        public Set<String> setInitParameters(Map<String, String> initParameters) {
            Holder.this.illegalStateIfContextStarted();
            Set<String> clash = null;
            for (Entry<String, String> entry : initParameters.entrySet()) {
                if (entry.getKey() == null) {
                    throw new IllegalArgumentException("init parameter name required");
                }
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("non-null value required for init parameter " + (String) entry.getKey());
                }
                if (Holder.this.getInitParameter((String) entry.getKey()) != null) {
                    if (clash == null) {
                        clash = new HashSet();
                    }
                    clash.add(entry.getKey());
                }
            }
            if (clash != null) {
                return clash;
            } else {
                Holder.this.getInitParameters().putAll(initParameters);
                return Collections.emptySet();
            }
        }
    }
}
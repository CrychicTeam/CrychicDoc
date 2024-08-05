package info.journeymap.shaded.org.eclipse.jetty.servlet;

import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.Loader;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.UnavailableException;
import java.io.IOException;

public abstract class BaseHolder<T> extends AbstractLifeCycle implements Dumpable {

    private static final Logger LOG = Log.getLogger(BaseHolder.class);

    protected final Source _source;

    protected transient Class<? extends T> _class;

    protected String _className;

    protected boolean _extInstance;

    protected ServletHandler _servletHandler;

    protected BaseHolder(Source source) {
        this._source = source;
    }

    public Source getSource() {
        return this._source;
    }

    public void initialize() throws Exception {
        if (!this.isStarted()) {
            throw new IllegalStateException("Not started: " + this);
        }
    }

    @Override
    public void doStart() throws Exception {
        if (this._class != null || this._className != null && !this._className.equals("")) {
            if (this._class == null) {
                try {
                    this._class = Loader.loadClass(this._className);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Holding {} from {}", this._class, this._class.getClassLoader());
                    }
                } catch (Exception var2) {
                    LOG.warn(var2);
                    throw new UnavailableException("Class loading error for holder " + this.toString());
                }
            }
        } else {
            throw new UnavailableException("No class in holder " + this.toString());
        }
    }

    @Override
    public void doStop() throws Exception {
        if (!this._extInstance) {
            this._class = null;
        }
    }

    @ManagedAttribute(value = "Class Name", readonly = true)
    public String getClassName() {
        return this._className;
    }

    public Class<? extends T> getHeldClass() {
        return this._class;
    }

    public ServletHandler getServletHandler() {
        return this._servletHandler;
    }

    public void setServletHandler(ServletHandler servletHandler) {
        this._servletHandler = servletHandler;
    }

    public void setClassName(String className) {
        this._className = className;
        this._class = null;
    }

    public void setHeldClass(Class<? extends T> held) {
        this._class = held;
        if (held != null) {
            this._className = held.getName();
        }
    }

    protected void illegalStateIfContextStarted() {
        if (this._servletHandler != null) {
            ServletContext context = this._servletHandler.getServletContext();
            if (context instanceof ContextHandler.Context && ((ContextHandler.Context) context).getContextHandler().isStarted()) {
                throw new IllegalStateException("Started");
            }
        }
    }

    public boolean isInstance() {
        return this._extInstance;
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        out.append(this.toString()).append(" - ").append(AbstractLifeCycle.getState(this)).append("\n");
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }
}
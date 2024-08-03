package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletContextAttributeEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContextAttributeListener;
import info.journeymap.shaded.org.javax.servlet.ServletContextEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContextListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ManagedAttributeListener implements ServletContextListener, ServletContextAttributeListener {

    private static final Logger LOG = Log.getLogger(ManagedAttributeListener.class);

    final Set<String> _managedAttributes = new HashSet();

    final ContextHandler _context;

    public ManagedAttributeListener(ContextHandler context, String... managedAttributes) {
        this._context = context;
        for (String attr : managedAttributes) {
            this._managedAttributes.add(attr);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("managedAttributes {}", this._managedAttributes);
        }
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        if (this._managedAttributes.contains(event.getName())) {
            this.updateBean(event.getName(), event.getValue(), event.getServletContext().getAttribute(event.getName()));
        }
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        if (this._managedAttributes.contains(event.getName())) {
            this.updateBean(event.getName(), event.getValue(), null);
        }
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        if (this._managedAttributes.contains(event.getName())) {
            this.updateBean(event.getName(), null, event.getValue());
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Enumeration<String> e = event.getServletContext().getAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (this._managedAttributes.contains(name)) {
                this.updateBean(name, null, event.getServletContext().getAttribute(name));
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Enumeration<String> e = this._context.getServletContext().getAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (this._managedAttributes.contains(name)) {
                this.updateBean(name, event.getServletContext().getAttribute(name), null);
            }
        }
    }

    protected void updateBean(String name, Object oldBean, Object newBean) {
        LOG.info("update {} {}->{} on {}", name, oldBean, newBean, this._context);
        if (LOG.isDebugEnabled()) {
            LOG.debug("update {} {}->{} on {}", name, oldBean, newBean, this._context);
        }
        this._context.updateBean(oldBean, newBean, false);
    }
}
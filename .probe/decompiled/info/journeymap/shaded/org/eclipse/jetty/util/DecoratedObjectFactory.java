package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DecoratedObjectFactory implements Iterable<Decorator> {

    private static final Logger LOG = Log.getLogger(DecoratedObjectFactory.class);

    public static final String ATTR = DecoratedObjectFactory.class.getName();

    private List<Decorator> decorators = new ArrayList();

    public void addDecorator(Decorator decorator) {
        LOG.debug("Adding Decorator: {}", decorator);
        this.decorators.add(decorator);
    }

    public void clear() {
        this.decorators.clear();
    }

    public <T> T createInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating Instance: " + clazz);
        }
        T o = (T) clazz.newInstance();
        return this.decorate(o);
    }

    public <T> T decorate(T obj) {
        T f = obj;
        for (int i = this.decorators.size() - 1; i >= 0; i--) {
            f = ((Decorator) this.decorators.get(i)).decorate(f);
        }
        return f;
    }

    public void destroy(Object obj) {
        for (Decorator decorator : this.decorators) {
            decorator.destroy(obj);
        }
    }

    public List<Decorator> getDecorators() {
        return Collections.unmodifiableList(this.decorators);
    }

    public Iterator<Decorator> iterator() {
        return this.decorators.iterator();
    }

    public void setDecorators(List<? extends Decorator> decorators) {
        this.decorators.clear();
        if (decorators != null) {
            this.decorators.addAll(decorators);
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getClass().getName()).append("[decorators=");
        str.append(Integer.toString(this.decorators.size()));
        str.append("]");
        return str.toString();
    }
}
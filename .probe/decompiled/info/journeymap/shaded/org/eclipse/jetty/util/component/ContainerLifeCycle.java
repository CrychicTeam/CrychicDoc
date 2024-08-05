package info.journeymap.shaded.org.eclipse.jetty.util.component;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ManagedObject("Implementation of Container and LifeCycle")
public class ContainerLifeCycle extends AbstractLifeCycle implements Container, Destroyable, Dumpable {

    private static final Logger LOG = Log.getLogger(ContainerLifeCycle.class);

    private final List<ContainerLifeCycle.Bean> _beans = new CopyOnWriteArrayList();

    private final List<Container.Listener> _listeners = new CopyOnWriteArrayList();

    private boolean _doStarted;

    private boolean _destroyed;

    @Override
    protected void doStart() throws Exception {
        if (this._destroyed) {
            throw new IllegalStateException("Destroyed container cannot be restarted");
        } else {
            this._doStarted = true;
            for (ContainerLifeCycle.Bean b : this._beans) {
                if (b._bean instanceof LifeCycle) {
                    LifeCycle l = (LifeCycle) b._bean;
                    switch(b._managed) {
                        case MANAGED:
                            if (!l.isRunning()) {
                                this.start(l);
                            }
                            break;
                        case AUTO:
                            if (l.isRunning()) {
                                this.unmanage(b);
                            } else {
                                this.manage(b);
                                this.start(l);
                            }
                    }
                }
            }
            super.doStart();
        }
    }

    protected void start(LifeCycle l) throws Exception {
        l.start();
    }

    protected void stop(LifeCycle l) throws Exception {
        l.stop();
    }

    @Override
    protected void doStop() throws Exception {
        this._doStarted = false;
        super.doStop();
        List<ContainerLifeCycle.Bean> reverse = new ArrayList(this._beans);
        Collections.reverse(reverse);
        for (ContainerLifeCycle.Bean b : reverse) {
            if (b._managed == ContainerLifeCycle.Managed.MANAGED && b._bean instanceof LifeCycle) {
                LifeCycle l = (LifeCycle) b._bean;
                this.stop(l);
            }
        }
    }

    @Override
    public void destroy() {
        this._destroyed = true;
        List<ContainerLifeCycle.Bean> reverse = new ArrayList(this._beans);
        Collections.reverse(reverse);
        for (ContainerLifeCycle.Bean b : reverse) {
            if (b._bean instanceof Destroyable && (b._managed == ContainerLifeCycle.Managed.MANAGED || b._managed == ContainerLifeCycle.Managed.POJO)) {
                Destroyable d = (Destroyable) b._bean;
                d.destroy();
            }
        }
        this._beans.clear();
    }

    public boolean contains(Object bean) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (b._bean == bean) {
                return true;
            }
        }
        return false;
    }

    public boolean isManaged(Object bean) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (b._bean == bean) {
                return b.isManaged();
            }
        }
        return false;
    }

    @Override
    public boolean addBean(Object o) {
        if (o instanceof LifeCycle) {
            LifeCycle l = (LifeCycle) o;
            return this.addBean(o, l.isRunning() ? ContainerLifeCycle.Managed.UNMANAGED : ContainerLifeCycle.Managed.AUTO);
        } else {
            return this.addBean(o, ContainerLifeCycle.Managed.POJO);
        }
    }

    public boolean addBean(Object o, boolean managed) {
        return o instanceof LifeCycle ? this.addBean(o, managed ? ContainerLifeCycle.Managed.MANAGED : ContainerLifeCycle.Managed.UNMANAGED) : this.addBean(o, managed ? ContainerLifeCycle.Managed.POJO : ContainerLifeCycle.Managed.UNMANAGED);
    }

    public boolean addBean(Object o, ContainerLifeCycle.Managed managed) {
        if (o != null && !this.contains(o)) {
            ContainerLifeCycle.Bean new_bean = new ContainerLifeCycle.Bean(o);
            if (o instanceof Container.Listener) {
                this.addEventListener((Container.Listener) o);
            }
            this._beans.add(new_bean);
            for (Container.Listener l : this._listeners) {
                l.beanAdded(this, o);
            }
            try {
                switch(managed) {
                    case MANAGED:
                        this.manage(new_bean);
                        if (this.isStarting() && this._doStarted) {
                            LifeCycle l = (LifeCycle) o;
                            if (!l.isRunning()) {
                                this.start(l);
                            }
                        }
                        break;
                    case AUTO:
                        if (o instanceof LifeCycle) {
                            LifeCycle l = (LifeCycle) o;
                            if (this.isStarting()) {
                                if (l.isRunning()) {
                                    this.unmanage(new_bean);
                                } else if (this._doStarted) {
                                    this.manage(new_bean);
                                    this.start(l);
                                } else {
                                    new_bean._managed = ContainerLifeCycle.Managed.AUTO;
                                }
                            } else if (this.isStarted()) {
                                this.unmanage(new_bean);
                            } else {
                                new_bean._managed = ContainerLifeCycle.Managed.AUTO;
                            }
                        } else {
                            new_bean._managed = ContainerLifeCycle.Managed.POJO;
                        }
                        break;
                    case UNMANAGED:
                        this.unmanage(new_bean);
                        break;
                    case POJO:
                        new_bean._managed = ContainerLifeCycle.Managed.POJO;
                }
            } catch (Error | RuntimeException var6) {
                throw var6;
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} added {}", this, new_bean);
            }
            return true;
        } else {
            return false;
        }
    }

    public void addManaged(LifeCycle lifecycle) {
        this.addBean(lifecycle, true);
        try {
            if (this.isRunning() && !lifecycle.isRunning()) {
                this.start(lifecycle);
            }
        } catch (Error | RuntimeException var3) {
            throw var3;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public void addEventListener(Container.Listener listener) {
        if (!this._listeners.contains(listener)) {
            this._listeners.add(listener);
            for (ContainerLifeCycle.Bean b : this._beans) {
                listener.beanAdded(this, b._bean);
                if (listener instanceof Container.InheritedListener && b.isManaged() && b._bean instanceof Container) {
                    if (b._bean instanceof ContainerLifeCycle) {
                        ((ContainerLifeCycle) b._bean).addBean(listener, false);
                    } else {
                        ((Container) b._bean).addBean(listener);
                    }
                }
            }
        }
    }

    public void manage(Object bean) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (b._bean == bean) {
                this.manage(b);
                return;
            }
        }
        throw new IllegalArgumentException("Unknown bean " + bean);
    }

    private void manage(ContainerLifeCycle.Bean bean) {
        if (bean._managed != ContainerLifeCycle.Managed.MANAGED) {
            bean._managed = ContainerLifeCycle.Managed.MANAGED;
            if (bean._bean instanceof Container) {
                for (Container.Listener l : this._listeners) {
                    if (l instanceof Container.InheritedListener) {
                        if (bean._bean instanceof ContainerLifeCycle) {
                            ((ContainerLifeCycle) bean._bean).addBean(l, false);
                        } else {
                            ((Container) bean._bean).addBean(l);
                        }
                    }
                }
            }
            if (bean._bean instanceof AbstractLifeCycle) {
                ((AbstractLifeCycle) bean._bean).setStopTimeout(this.getStopTimeout());
            }
        }
    }

    public void unmanage(Object bean) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (b._bean == bean) {
                this.unmanage(b);
                return;
            }
        }
        throw new IllegalArgumentException("Unknown bean " + bean);
    }

    private void unmanage(ContainerLifeCycle.Bean bean) {
        if (bean._managed != ContainerLifeCycle.Managed.UNMANAGED) {
            if (bean._managed == ContainerLifeCycle.Managed.MANAGED && bean._bean instanceof Container) {
                for (Container.Listener l : this._listeners) {
                    if (l instanceof Container.InheritedListener) {
                        ((Container) bean._bean).removeBean(l);
                    }
                }
            }
            bean._managed = ContainerLifeCycle.Managed.UNMANAGED;
        }
    }

    @Override
    public Collection<Object> getBeans() {
        return this.getBeans(Object.class);
    }

    public void setBeans(Collection<Object> beans) {
        for (Object bean : beans) {
            this.addBean(bean);
        }
    }

    @Override
    public <T> Collection<T> getBeans(Class<T> clazz) {
        ArrayList<T> beans = new ArrayList();
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (clazz.isInstance(b._bean)) {
                beans.add(clazz.cast(b._bean));
            }
        }
        return beans;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (clazz.isInstance(b._bean)) {
                return (T) clazz.cast(b._bean);
            }
        }
        return null;
    }

    public void removeBeans() {
        for (ContainerLifeCycle.Bean b : new ArrayList(this._beans)) {
            this.remove(b);
        }
    }

    private ContainerLifeCycle.Bean getBean(Object o) {
        for (ContainerLifeCycle.Bean b : this._beans) {
            if (b._bean == o) {
                return b;
            }
        }
        return null;
    }

    @Override
    public boolean removeBean(Object o) {
        ContainerLifeCycle.Bean b = this.getBean(o);
        return b != null && this.remove(b);
    }

    private boolean remove(ContainerLifeCycle.Bean bean) {
        if (!this._beans.remove(bean)) {
            return false;
        } else {
            boolean wasManaged = bean.isManaged();
            this.unmanage(bean);
            for (Container.Listener l : this._listeners) {
                l.beanRemoved(this, bean._bean);
            }
            if (bean._bean instanceof Container.Listener) {
                this.removeEventListener((Container.Listener) bean._bean);
            }
            if (wasManaged && bean._bean instanceof LifeCycle) {
                try {
                    this.stop((LifeCycle) bean._bean);
                } catch (Error | RuntimeException var5) {
                    throw var5;
                } catch (Exception var6) {
                    throw new RuntimeException(var6);
                }
            }
            return true;
        }
    }

    @Override
    public void removeEventListener(Container.Listener listener) {
        if (this._listeners.remove(listener)) {
            for (ContainerLifeCycle.Bean b : this._beans) {
                listener.beanRemoved(this, b._bean);
                if (listener instanceof Container.InheritedListener && b.isManaged() && b._bean instanceof Container) {
                    ((Container) b._bean).removeBean(listener);
                }
            }
        }
    }

    @Override
    public void setStopTimeout(long stopTimeout) {
        super.setStopTimeout(stopTimeout);
        for (ContainerLifeCycle.Bean bean : this._beans) {
            if (bean.isManaged() && bean._bean instanceof AbstractLifeCycle) {
                ((AbstractLifeCycle) bean._bean).setStopTimeout(stopTimeout);
            }
        }
    }

    @ManagedOperation("Dump the object to stderr")
    public void dumpStdErr() {
        try {
            this.dump(System.err, "");
        } catch (IOException var2) {
            LOG.warn(var2);
        }
    }

    @ManagedOperation("Dump the object to a string")
    @Override
    public String dump() {
        return dump(this);
    }

    public static String dump(Dumpable dumpable) {
        StringBuilder b = new StringBuilder();
        try {
            dumpable.dump(b, "");
        } catch (IOException var3) {
            LOG.warn(var3);
        }
        return b.toString();
    }

    public void dump(Appendable out) throws IOException {
        this.dump(out, "");
    }

    protected void dumpThis(Appendable out) throws IOException {
        out.append(String.valueOf(this)).append(" - ").append(this.getState()).append("\n");
    }

    public static void dumpObject(Appendable out, Object o) throws IOException {
        try {
            if (o instanceof LifeCycle) {
                out.append(String.valueOf(o)).append(" - ").append(AbstractLifeCycle.getState((LifeCycle) o)).append("\n");
            } else {
                out.append(String.valueOf(o)).append("\n");
            }
        } catch (Throwable var3) {
            out.append(" => ").append(var3.toString()).append('\n');
        }
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpBeans(out, indent);
    }

    protected void dumpBeans(Appendable out, String indent, Collection<?>... collections) throws IOException {
        this.dumpThis(out);
        int size = this._beans.size();
        for (Collection<?> c : collections) {
            size += c.size();
        }
        if (size != 0) {
            int i = 0;
            for (ContainerLifeCycle.Bean b : this._beans) {
                i++;
                switch(b._managed) {
                    case MANAGED:
                        out.append(indent).append(" += ");
                        if (b._bean instanceof Dumpable) {
                            ((Dumpable) b._bean).dump(out, indent + (i == size ? "    " : " |  "));
                        } else {
                            dumpObject(out, b._bean);
                        }
                        break;
                    case AUTO:
                        out.append(indent).append(" +? ");
                        if (b._bean instanceof Dumpable) {
                            ((Dumpable) b._bean).dump(out, indent + (i == size ? "    " : " |  "));
                        } else {
                            dumpObject(out, b._bean);
                        }
                        break;
                    case UNMANAGED:
                        out.append(indent).append(" +~ ");
                        dumpObject(out, b._bean);
                        break;
                    case POJO:
                        out.append(indent).append(" +- ");
                        if (b._bean instanceof Dumpable) {
                            ((Dumpable) b._bean).dump(out, indent + (i == size ? "    " : " |  "));
                        } else {
                            dumpObject(out, b._bean);
                        }
                }
            }
            if (i < size) {
                out.append(indent).append(" |\n");
            }
            Collection[] var14 = collections;
            int var16 = collections.length;
            for (int var17 = 0; var17 < var16; var17++) {
                for (Object o : var14[var17]) {
                    i++;
                    out.append(indent).append(" +> ");
                    if (o instanceof Dumpable) {
                        ((Dumpable) o).dump(out, indent + (i == size ? "    " : " |  "));
                    } else {
                        dumpObject(out, o);
                    }
                }
            }
        }
    }

    public static void dump(Appendable out, String indent, Collection<?>... collections) throws IOException {
        if (collections.length != 0) {
            int size = 0;
            for (Collection<?> c : collections) {
                size += c.size();
            }
            if (size != 0) {
                int i = 0;
                Collection[] var12 = collections;
                int var13 = collections.length;
                for (int var14 = 0; var14 < var13; var14++) {
                    for (Object o : var12[var14]) {
                        i++;
                        out.append(indent).append(" +- ");
                        if (o instanceof Dumpable) {
                            ((Dumpable) o).dump(out, indent + (i == size ? "    " : " |  "));
                        } else {
                            dumpObject(out, o);
                        }
                    }
                }
            }
        }
    }

    public void updateBean(Object oldBean, Object newBean) {
        if (newBean != oldBean) {
            if (oldBean != null) {
                this.removeBean(oldBean);
            }
            if (newBean != null) {
                this.addBean(newBean);
            }
        }
    }

    public void updateBean(Object oldBean, Object newBean, boolean managed) {
        if (newBean != oldBean) {
            if (oldBean != null) {
                this.removeBean(oldBean);
            }
            if (newBean != null) {
                this.addBean(newBean, managed);
            }
        }
    }

    public void updateBeans(Object[] oldBeans, Object[] newBeans) {
        if (oldBeans != null) {
            label59: for (Object o : oldBeans) {
                if (newBeans != null) {
                    for (Object n : newBeans) {
                        if (o == n) {
                            continue label59;
                        }
                    }
                }
                this.removeBean(o);
            }
        }
        if (newBeans != null) {
            label41: for (Object nx : newBeans) {
                if (oldBeans != null) {
                    for (Object o : oldBeans) {
                        if (o == nx) {
                            continue label41;
                        }
                    }
                }
                this.addBean(nx);
            }
        }
    }

    private static class Bean {

        private final Object _bean;

        private volatile ContainerLifeCycle.Managed _managed = ContainerLifeCycle.Managed.POJO;

        private Bean(Object b) {
            if (b == null) {
                throw new NullPointerException();
            } else {
                this._bean = b;
            }
        }

        public boolean isManaged() {
            return this._managed == ContainerLifeCycle.Managed.MANAGED;
        }

        public String toString() {
            return String.format("{%s,%s}", this._bean, this._managed);
        }
    }

    static enum Managed {

        POJO, MANAGED, UNMANAGED, AUTO
    }
}
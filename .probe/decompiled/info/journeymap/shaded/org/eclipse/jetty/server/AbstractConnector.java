package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.ArrayByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.FutureCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@ManagedObject("Abstract implementation of the Connector Interface")
public abstract class AbstractConnector extends ContainerLifeCycle implements Connector, Dumpable {

    protected final Logger LOG = Log.getLogger(AbstractConnector.class);

    private final Map<String, ConnectionFactory> _factories = new LinkedHashMap();

    private final Server _server;

    private final Executor _executor;

    private final Scheduler _scheduler;

    private final ByteBufferPool _byteBufferPool;

    private final Thread[] _acceptors;

    private final Set<EndPoint> _endpoints = Collections.newSetFromMap(new ConcurrentHashMap());

    private final Set<EndPoint> _immutableEndPoints = Collections.unmodifiableSet(this._endpoints);

    private volatile CountDownLatch _stopping;

    private long _idleTimeout = 30000L;

    private String _defaultProtocol;

    private ConnectionFactory _defaultConnectionFactory;

    private String _name;

    private int _acceptorPriorityDelta = -2;

    public AbstractConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, ConnectionFactory... factories) {
        this._server = server;
        this._executor = (Executor) (executor != null ? executor : this._server.getThreadPool());
        if (scheduler == null) {
            scheduler = this._server.getBean(Scheduler.class);
        }
        this._scheduler = (Scheduler) (scheduler != null ? scheduler : new ScheduledExecutorScheduler());
        if (pool == null) {
            pool = this._server.getBean(ByteBufferPool.class);
        }
        this._byteBufferPool = (ByteBufferPool) (pool != null ? pool : new ArrayByteBufferPool());
        this.addBean(this._server, false);
        this.addBean(this._executor);
        if (executor == null) {
            this.unmanage(this._executor);
        }
        this.addBean(this._scheduler);
        this.addBean(this._byteBufferPool);
        for (ConnectionFactory factory : factories) {
            this.addConnectionFactory(factory);
        }
        int cores = Runtime.getRuntime().availableProcessors();
        if (acceptors < 0) {
            acceptors = Math.max(1, Math.min(4, cores / 8));
        }
        if (acceptors > cores) {
            this.LOG.warn("Acceptors should be <= availableProcessors: " + this);
        }
        this._acceptors = new Thread[acceptors];
    }

    @Override
    public Server getServer() {
        return this._server;
    }

    @Override
    public Executor getExecutor() {
        return this._executor;
    }

    @Override
    public ByteBufferPool getByteBufferPool() {
        return this._byteBufferPool;
    }

    @ManagedAttribute("Idle timeout")
    @Override
    public long getIdleTimeout() {
        return this._idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this._idleTimeout = idleTimeout;
    }

    @ManagedAttribute("number of acceptor threads")
    public int getAcceptors() {
        return this._acceptors.length;
    }

    @Override
    protected void doStart() throws Exception {
        if (this._defaultProtocol == null) {
            throw new IllegalStateException("No default protocol for " + this);
        } else {
            this._defaultConnectionFactory = this.getConnectionFactory(this._defaultProtocol);
            if (this._defaultConnectionFactory == null) {
                throw new IllegalStateException("No protocol factory for default protocol '" + this._defaultProtocol + "' in " + this);
            } else {
                SslConnectionFactory ssl = this.getConnectionFactory(SslConnectionFactory.class);
                if (ssl != null) {
                    String next = ssl.getNextProtocol();
                    ConnectionFactory cf = this.getConnectionFactory(next);
                    if (cf == null) {
                        throw new IllegalStateException("No protocol factory for SSL next protocol: '" + next + "' in " + this);
                    }
                }
                super.doStart();
                this._stopping = new CountDownLatch(this._acceptors.length);
                for (int i = 0; i < this._acceptors.length; i++) {
                    AbstractConnector.Acceptor a = new AbstractConnector.Acceptor(i);
                    this.addBean(a);
                    this.getExecutor().execute(a);
                }
                this.LOG.info("Started {}", this);
            }
        }
    }

    protected void interruptAcceptors() {
        synchronized (this) {
            for (Thread thread : this._acceptors) {
                if (thread != null) {
                    thread.interrupt();
                }
            }
        }
    }

    @Override
    public Future<Void> shutdown() {
        return new FutureCallback(true);
    }

    @Override
    protected void doStop() throws Exception {
        this.interruptAcceptors();
        long stopTimeout = this.getStopTimeout();
        CountDownLatch stopping = this._stopping;
        if (stopTimeout > 0L && stopping != null && this.getAcceptors() > 0) {
            stopping.await(stopTimeout, TimeUnit.MILLISECONDS);
        }
        this._stopping = null;
        super.doStop();
        for (AbstractConnector.Acceptor a : this.getBeans(AbstractConnector.Acceptor.class)) {
            this.removeBean(a);
        }
        this.LOG.info("Stopped {}", this);
    }

    public void join() throws InterruptedException {
        this.join(0L);
    }

    public void join(long timeout) throws InterruptedException {
        synchronized (this) {
            for (Thread thread : this._acceptors) {
                if (thread != null) {
                    thread.join(timeout);
                }
            }
        }
    }

    protected abstract void accept(int var1) throws IOException, InterruptedException;

    protected boolean isAccepting() {
        return this.isRunning();
    }

    @Override
    public ConnectionFactory getConnectionFactory(String protocol) {
        synchronized (this._factories) {
            return (ConnectionFactory) this._factories.get(StringUtil.asciiToLowerCase(protocol));
        }
    }

    @Override
    public <T> T getConnectionFactory(Class<T> factoryType) {
        synchronized (this._factories) {
            for (ConnectionFactory f : this._factories.values()) {
                if (factoryType.isAssignableFrom(f.getClass())) {
                    return (T) f;
                }
            }
            return null;
        }
    }

    public void addConnectionFactory(ConnectionFactory factory) {
        synchronized (this._factories) {
            Set<ConnectionFactory> to_remove = new HashSet();
            for (String key : factory.getProtocols()) {
                key = StringUtil.asciiToLowerCase(key);
                ConnectionFactory old = (ConnectionFactory) this._factories.remove(key);
                if (old != null) {
                    if (old.getProtocol().equals(this._defaultProtocol)) {
                        this._defaultProtocol = null;
                    }
                    to_remove.add(old);
                }
                this._factories.put(key, factory);
            }
            for (ConnectionFactory f : this._factories.values()) {
                to_remove.remove(f);
            }
            for (ConnectionFactory old : to_remove) {
                this.removeBean(old);
                if (this.LOG.isDebugEnabled()) {
                    this.LOG.debug("{} removed {}", this, old);
                }
            }
            this.addBean(factory);
            if (this._defaultProtocol == null) {
                this._defaultProtocol = factory.getProtocol();
            }
            if (this.LOG.isDebugEnabled()) {
                this.LOG.debug("{} added {}", this, factory);
            }
        }
    }

    public void addFirstConnectionFactory(ConnectionFactory factory) {
        synchronized (this._factories) {
            List<ConnectionFactory> existings = new ArrayList(this._factories.values());
            this._factories.clear();
            this.addConnectionFactory(factory);
            for (ConnectionFactory existing : existings) {
                this.addConnectionFactory(existing);
            }
            this._defaultProtocol = factory.getProtocol();
        }
    }

    public void addIfAbsentConnectionFactory(ConnectionFactory factory) {
        synchronized (this._factories) {
            String key = StringUtil.asciiToLowerCase(factory.getProtocol());
            if (this._factories.containsKey(key)) {
                if (this.LOG.isDebugEnabled()) {
                    this.LOG.debug("{} addIfAbsent ignored {}", this, factory);
                }
            } else {
                this._factories.put(key, factory);
                this.addBean(factory);
                if (this._defaultProtocol == null) {
                    this._defaultProtocol = factory.getProtocol();
                }
                if (this.LOG.isDebugEnabled()) {
                    this.LOG.debug("{} addIfAbsent added {}", this, factory);
                }
            }
        }
    }

    public ConnectionFactory removeConnectionFactory(String protocol) {
        synchronized (this._factories) {
            ConnectionFactory factory = (ConnectionFactory) this._factories.remove(StringUtil.asciiToLowerCase(protocol));
            this.removeBean(factory);
            return factory;
        }
    }

    @Override
    public Collection<ConnectionFactory> getConnectionFactories() {
        synchronized (this._factories) {
            return this._factories.values();
        }
    }

    public void setConnectionFactories(Collection<ConnectionFactory> factories) {
        synchronized (this._factories) {
            for (ConnectionFactory factory : new ArrayList(this._factories.values())) {
                this.removeConnectionFactory(factory.getProtocol());
            }
            for (ConnectionFactory factory : factories) {
                if (factory != null) {
                    this.addConnectionFactory(factory);
                }
            }
        }
    }

    @ManagedAttribute("The priority delta to apply to acceptor threads")
    public int getAcceptorPriorityDelta() {
        return this._acceptorPriorityDelta;
    }

    public void setAcceptorPriorityDelta(int acceptorPriorityDelta) {
        int old = this._acceptorPriorityDelta;
        this._acceptorPriorityDelta = acceptorPriorityDelta;
        if (old != acceptorPriorityDelta && this.isStarted()) {
            for (Thread thread : this._acceptors) {
                thread.setPriority(Math.max(1, Math.min(10, thread.getPriority() - old + acceptorPriorityDelta)));
            }
        }
    }

    @ManagedAttribute("Protocols supported by this connector")
    @Override
    public List<String> getProtocols() {
        synchronized (this._factories) {
            return new ArrayList(this._factories.keySet());
        }
    }

    public void clearConnectionFactories() {
        synchronized (this._factories) {
            this._factories.clear();
        }
    }

    @ManagedAttribute("This connector's default protocol")
    public String getDefaultProtocol() {
        return this._defaultProtocol;
    }

    public void setDefaultProtocol(String defaultProtocol) {
        this._defaultProtocol = StringUtil.asciiToLowerCase(defaultProtocol);
        if (this.isRunning()) {
            this._defaultConnectionFactory = this.getConnectionFactory(this._defaultProtocol);
        }
    }

    @Override
    public ConnectionFactory getDefaultConnectionFactory() {
        return this.isStarted() ? this._defaultConnectionFactory : this.getConnectionFactory(this._defaultProtocol);
    }

    protected boolean handleAcceptFailure(Throwable previous, Throwable current) {
        if (this.isAccepting()) {
            if (previous == null) {
                this.LOG.warn(current);
            } else {
                this.LOG.debug(current);
            }
            try {
                Thread.sleep(1000L);
                return true;
            } catch (Throwable var4) {
                return false;
            }
        } else {
            this.LOG.ignore(current);
            return false;
        }
    }

    @Override
    public Collection<EndPoint> getConnectedEndPoints() {
        return this._immutableEndPoints;
    }

    protected void onEndPointOpened(EndPoint endp) {
        this._endpoints.add(endp);
    }

    protected void onEndPointClosed(EndPoint endp) {
        this._endpoints.remove(endp);
    }

    @Override
    public Scheduler getScheduler() {
        return this._scheduler;
    }

    @Override
    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String toString() {
        return String.format("%s@%x{%s,%s}", this._name == null ? this.getClass().getSimpleName() : this._name, this.hashCode(), this.getDefaultProtocol(), this.getProtocols());
    }

    private class Acceptor implements Runnable {

        private final int _id;

        private String _name;

        private Acceptor(int id) {
            this._id = id;
        }

        public void run() {
            Thread thread = Thread.currentThread();
            String name = thread.getName();
            this._name = String.format("%s-acceptor-%d@%x-%s", name, this._id, this.hashCode(), AbstractConnector.this.toString());
            thread.setName(this._name);
            int priority = thread.getPriority();
            if (AbstractConnector.this._acceptorPriorityDelta != 0) {
                thread.setPriority(Math.max(1, Math.min(10, priority + AbstractConnector.this._acceptorPriorityDelta)));
            }
            synchronized (AbstractConnector.this) {
                AbstractConnector.this._acceptors[this._id] = thread;
            }
            try {
                Throwable exception = null;
                while (AbstractConnector.this.isAccepting()) {
                    try {
                        AbstractConnector.this.accept(this._id);
                        exception = null;
                    } catch (Throwable var17) {
                        if (!AbstractConnector.this.handleAcceptFailure(exception, var17)) {
                            break;
                        }
                        exception = var17;
                    }
                }
            } finally {
                thread.setName(name);
                if (AbstractConnector.this._acceptorPriorityDelta != 0) {
                    thread.setPriority(priority);
                }
                synchronized (AbstractConnector.this) {
                    AbstractConnector.this._acceptors[this._id] = null;
                }
                CountDownLatch stopping = AbstractConnector.this._stopping;
                if (stopping != null) {
                    stopping.countDown();
                }
            }
        }

        public String toString() {
            String name = this._name;
            return name == null ? String.format("acceptor-%d@%x", this._id, this.hashCode()) : name;
        }
    }
}
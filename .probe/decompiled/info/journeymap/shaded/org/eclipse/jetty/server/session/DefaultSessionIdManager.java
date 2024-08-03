package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.server.SessionIdManager;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@ManagedObject
public class DefaultSessionIdManager extends ContainerLifeCycle implements SessionIdManager {

    private static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    private static final String __NEW_SESSION_ID = "info.journeymap.shaded.org.eclipse.jetty.server.newSessionId";

    protected static final AtomicLong COUNTER = new AtomicLong();

    protected Random _random;

    protected boolean _weakRandom;

    protected String _workerName;

    protected String _workerAttr;

    protected long _reseed = 100000L;

    protected Server _server;

    protected HouseKeeper _houseKeeper;

    protected boolean _ownHouseKeeper;

    public DefaultSessionIdManager(Server server) {
        this._server = server;
    }

    public DefaultSessionIdManager(Server server, Random random) {
        this(server);
        this._random = random;
    }

    public void setServer(Server server) {
        this._server = server;
    }

    public Server getServer() {
        return this._server;
    }

    @Override
    public void setSessionHouseKeeper(HouseKeeper houseKeeper) {
        this.updateBean(this._houseKeeper, houseKeeper);
        this._houseKeeper = houseKeeper;
        this._houseKeeper.setSessionIdManager(this);
    }

    @Override
    public HouseKeeper getSessionHouseKeeper() {
        return this._houseKeeper;
    }

    @ManagedAttribute(value = "unique name for this node", readonly = true)
    @Override
    public String getWorkerName() {
        return this._workerName;
    }

    public void setWorkerName(String workerName) {
        if (this.isRunning()) {
            throw new IllegalStateException(this.getState());
        } else {
            if (workerName == null) {
                this._workerName = "";
            } else {
                if (workerName.contains(".")) {
                    throw new IllegalArgumentException("Name cannot contain '.'");
                }
                this._workerName = workerName;
            }
        }
    }

    public Random getRandom() {
        return this._random;
    }

    public synchronized void setRandom(Random random) {
        this._random = random;
        this._weakRandom = false;
    }

    public long getReseed() {
        return this._reseed;
    }

    public void setReseed(long reseed) {
        this._reseed = reseed;
    }

    @Override
    public String newSessionId(HttpServletRequest request, long created) {
        if (request == null) {
            return this.newSessionId(created);
        } else {
            String requested_id = request.getRequestedSessionId();
            if (requested_id != null) {
                String cluster_id = this.getId(requested_id);
                if (this.isIdInUse(cluster_id)) {
                    return cluster_id;
                }
            }
            String new_id = (String) request.getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.newSessionId");
            if (new_id != null && this.isIdInUse(new_id)) {
                return new_id;
            } else {
                String id = this.newSessionId((long) request.hashCode());
                request.setAttribute("info.journeymap.shaded.org.eclipse.jetty.server.newSessionId", id);
                return id;
            }
        }
    }

    public String newSessionId(long seedTerm) {
        String id = null;
        synchronized (this._random) {
            while (id == null || id.length() == 0) {
                long r0 = this._weakRandom ? (long) this.hashCode() ^ Runtime.getRuntime().freeMemory() ^ (long) this._random.nextInt() ^ seedTerm << 32 : this._random.nextLong();
                if (r0 < 0L) {
                    r0 = -r0;
                }
                if (this._reseed > 0L && r0 % this._reseed == 1L) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Reseeding {}", this);
                    }
                    if (this._random instanceof SecureRandom) {
                        SecureRandom secure = (SecureRandom) this._random;
                        secure.setSeed(secure.generateSeed(8));
                    } else {
                        this._random.setSeed(this._random.nextLong() ^ System.currentTimeMillis() ^ seedTerm ^ Runtime.getRuntime().freeMemory());
                    }
                }
                long r1 = this._weakRandom ? (long) this.hashCode() ^ Runtime.getRuntime().freeMemory() ^ (long) this._random.nextInt() ^ seedTerm << 32 : this._random.nextLong();
                if (r1 < 0L) {
                    r1 = -r1;
                }
                id = Long.toString(r0, 36) + Long.toString(r1, 36);
                if (!StringUtil.isBlank(this._workerName)) {
                    id = this._workerName + id;
                }
                id = id + Long.toString(COUNTER.getAndIncrement());
            }
            return id;
        }
    }

    @Override
    public boolean isIdInUse(String id) {
        if (id == null) {
            return false;
        } else {
            boolean inUse = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Checking {} is in use by at least one context", id);
            }
            try {
                for (SessionHandler manager : this.getSessionHandlers()) {
                    if (manager.isIdInUse(id)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Context {} reports id in use", manager);
                        }
                        inUse = true;
                        break;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Checked {}, in use:", id, inUse);
                }
                return inUse;
            } catch (Exception var5) {
                LOG.warn("Problem checking if id {} is in use", id, var5);
                return false;
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (this._server == null) {
            throw new IllegalStateException("No Server for SessionIdManager");
        } else {
            this.initRandom();
            if (this._workerName == null) {
                String inst = System.getenv("JETTY_WORKER_INSTANCE");
                this._workerName = "node" + (inst == null ? "0" : inst);
            }
            LOG.info("DefaultSessionIdManager workerName={}", this._workerName);
            this._workerAttr = this._workerName != null && this._workerName.startsWith("$") ? this._workerName.substring(1) : null;
            if (this._houseKeeper == null) {
                LOG.info("No SessionScavenger set, using defaults");
                this._ownHouseKeeper = true;
                this._houseKeeper = new HouseKeeper();
                this._houseKeeper.setSessionIdManager(this);
                this.addBean(this._houseKeeper, true);
            }
            this._houseKeeper.start();
        }
    }

    @Override
    protected void doStop() throws Exception {
        this._houseKeeper.stop();
        if (this._ownHouseKeeper) {
            this._houseKeeper = null;
        }
        this._random = null;
    }

    public void initRandom() {
        if (this._random == null) {
            try {
                this._random = new SecureRandom();
            } catch (Exception var2) {
                LOG.warn("Could not generate SecureRandom for session-id randomness", var2);
                this._random = new Random();
                this._weakRandom = true;
            }
        } else {
            this._random.setSeed(this._random.nextLong() ^ System.currentTimeMillis() ^ (long) this.hashCode() ^ Runtime.getRuntime().freeMemory());
        }
    }

    @Override
    public String getExtendedId(String clusterId, HttpServletRequest request) {
        if (!StringUtil.isBlank(this._workerName)) {
            if (this._workerAttr == null) {
                return clusterId + '.' + this._workerName;
            }
            String worker = (String) request.getAttribute(this._workerAttr);
            if (worker != null) {
                return clusterId + '.' + worker;
            }
        }
        return clusterId;
    }

    @Override
    public String getId(String extendedId) {
        int dot = extendedId.lastIndexOf(46);
        return dot > 0 ? extendedId.substring(0, dot) : extendedId;
    }

    @Override
    public void expireAll(String id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Expiring {}", id);
        }
        for (SessionHandler manager : this.getSessionHandlers()) {
            manager.invalidate(id);
        }
    }

    @Override
    public void invalidateAll(String id) {
        for (SessionHandler manager : this.getSessionHandlers()) {
            manager.invalidate(id);
        }
    }

    @Override
    public String renewSessionId(String oldClusterId, String oldNodeId, HttpServletRequest request) {
        String newClusterId = this.newSessionId((long) request.hashCode());
        for (SessionHandler manager : this.getSessionHandlers()) {
            manager.renewSessionId(oldClusterId, oldNodeId, newClusterId, this.getExtendedId(newClusterId, request));
        }
        return newClusterId;
    }

    @Override
    public Set<SessionHandler> getSessionHandlers() {
        Set<SessionHandler> handlers = new HashSet();
        Handler[] contexts = this._server.getChildHandlersByClass(ContextHandler.class);
        for (int i = 0; contexts != null && i < contexts.length; i++) {
            SessionHandler sessionHandler = ((ContextHandler) contexts[i]).getChildHandlerByClass(SessionHandler.class);
            if (sessionHandler != null) {
                handlers.add(sessionHandler);
            }
        }
        return handlers;
    }

    public String toString() {
        return String.format("%s[worker=%s]", super.toString(), this._workerName);
    }
}
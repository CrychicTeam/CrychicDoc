package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;

public class SessionContext {

    public static final String NULL_VHOST = "0.0.0.0";

    private ContextHandler.Context _context;

    private SessionHandler _sessionHandler;

    private String _workerName;

    private String _canonicalContextPath;

    private String _vhost;

    public SessionContext(String workerName, ContextHandler.Context context) {
        if (context != null) {
            this._sessionHandler = context.getContextHandler().getChildHandlerByClass(SessionHandler.class);
        }
        this._workerName = workerName;
        this._context = context;
        this._canonicalContextPath = this.canonicalizeContextPath(this._context);
        this._vhost = this.canonicalizeVHost(this._context);
    }

    public String getWorkerName() {
        return this._workerName;
    }

    public SessionHandler getSessionHandler() {
        return this._sessionHandler;
    }

    public ContextHandler.Context getContext() {
        return this._context;
    }

    public String getCanonicalContextPath() {
        return this._canonicalContextPath;
    }

    public String getVhost() {
        return this._vhost;
    }

    public String toString() {
        return this._workerName + "_" + this._canonicalContextPath + "_" + this._vhost;
    }

    public void run(Runnable r) {
        if (this._context != null) {
            this._context.getContextHandler().handle(r);
        } else {
            r.run();
        }
    }

    private String canonicalizeContextPath(ContextHandler.Context context) {
        return context == null ? "" : this.canonicalize(context.getContextPath());
    }

    private String canonicalizeVHost(ContextHandler.Context context) {
        String vhost = "0.0.0.0";
        if (context == null) {
            return vhost;
        } else {
            String[] vhosts = context.getContextHandler().getVirtualHosts();
            return vhosts != null && vhosts.length != 0 && vhosts[0] != null ? vhosts[0] : vhost;
        }
    }

    private String canonicalize(String path) {
        return path == null ? "" : path.replace('/', '_').replace('.', '_').replace('\\', '_');
    }
}
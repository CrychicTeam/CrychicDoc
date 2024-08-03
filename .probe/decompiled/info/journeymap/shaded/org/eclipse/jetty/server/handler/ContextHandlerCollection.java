package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpChannelState;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTernaryTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ManagedObject("Context Handler Collection")
public class ContextHandlerCollection extends HandlerCollection {

    private static final Logger LOG = Log.getLogger(ContextHandlerCollection.class);

    private final ConcurrentMap<ContextHandler, Handler> _contextBranches = new ConcurrentHashMap();

    private volatile Trie<Entry<String, ContextHandlerCollection.Branch[]>> _pathBranches;

    private Class<? extends ContextHandler> _contextClass = ContextHandler.class;

    public ContextHandlerCollection() {
        super(true);
    }

    public ContextHandlerCollection(ContextHandler... contexts) {
        super(true, contexts);
    }

    @ManagedOperation("update the mapping of context path to context")
    public void mapContexts() {
        this._contextBranches.clear();
        if (this.getHandlers() == null) {
            this._pathBranches = new ArrayTernaryTrie<>(false, 16);
        } else {
            Map<String, ContextHandlerCollection.Branch[]> map = new HashMap();
            for (Handler handler : this.getHandlers()) {
                ContextHandlerCollection.Branch branch = new ContextHandlerCollection.Branch(handler);
                for (String contextPath : branch.getContextPaths()) {
                    ContextHandlerCollection.Branch[] branches = (ContextHandlerCollection.Branch[]) map.get(contextPath);
                    map.put(contextPath, ArrayUtil.addToArray(branches, branch, ContextHandlerCollection.Branch.class));
                }
                for (ContextHandler context : branch.getContextHandlers()) {
                    this._contextBranches.putIfAbsent(context, branch.getHandler());
                }
            }
            for (Entry<String, ContextHandlerCollection.Branch[]> entry : map.entrySet()) {
                ContextHandlerCollection.Branch[] branches = (ContextHandlerCollection.Branch[]) entry.getValue();
                ContextHandlerCollection.Branch[] sorted = new ContextHandlerCollection.Branch[branches.length];
                int i = 0;
                for (ContextHandlerCollection.Branch branch : branches) {
                    if (branch.hasVirtualHost()) {
                        sorted[i++] = branch;
                    }
                }
                for (ContextHandlerCollection.Branch branchx : branches) {
                    if (!branchx.hasVirtualHost()) {
                        sorted[i++] = branchx;
                    }
                }
                entry.setValue(sorted);
            }
            int capacity = 512;
            label60: while (true) {
                Trie<Entry<String, ContextHandlerCollection.Branch[]>> trie = new ArrayTernaryTrie<>(false, capacity);
                for (Entry<String, ContextHandlerCollection.Branch[]> entry : map.entrySet()) {
                    if (!trie.put(((String) entry.getKey()).substring(1), entry)) {
                        capacity += 512;
                        continue label60;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    for (String ctx : trie.keySet()) {
                        LOG.debug("{}->{}", ctx, Arrays.asList((Object[]) trie.get(ctx).getValue()));
                    }
                }
                this._pathBranches = trie;
                return;
            }
        }
    }

    @Override
    public void setHandlers(Handler[] handlers) {
        super.setHandlers(handlers);
        if (this.isStarted()) {
            this.mapContexts();
        }
    }

    @Override
    protected void doStart() throws Exception {
        this.mapContexts();
        super.doStart();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Handler[] handlers = this.getHandlers();
        if (handlers != null && handlers.length != 0) {
            HttpChannelState async = baseRequest.getHttpChannelState();
            if (async.isAsync()) {
                ContextHandler context = async.getContextHandler();
                if (context != null) {
                    Handler branch = (Handler) this._contextBranches.get(context);
                    if (branch == null) {
                        context.handle(target, baseRequest, request, response);
                    } else {
                        branch.handle(target, baseRequest, request, response);
                    }
                    return;
                }
            }
            if (target.startsWith("/")) {
                int limit = target.length() - 1;
                while (limit >= 0) {
                    Entry<String, ContextHandlerCollection.Branch[]> branches = this._pathBranches.getBest(target, 1, limit);
                    if (branches == null) {
                        break;
                    }
                    int l = ((String) branches.getKey()).length();
                    if (l == 1 || target.length() == l || target.charAt(l) == '/') {
                        for (ContextHandlerCollection.Branch branch : (ContextHandlerCollection.Branch[]) branches.getValue()) {
                            branch.getHandler().handle(target, baseRequest, request, response);
                            if (baseRequest.isHandled()) {
                                return;
                            }
                        }
                    }
                    limit = l - 2;
                }
            } else {
                for (int i = 0; i < handlers.length; i++) {
                    handlers[i].handle(target, baseRequest, request, response);
                    if (baseRequest.isHandled()) {
                        return;
                    }
                }
            }
        }
    }

    public ContextHandler addContext(String contextPath, String resourceBase) {
        try {
            ContextHandler context = (ContextHandler) this._contextClass.newInstance();
            context.setContextPath(contextPath);
            context.setResourceBase(resourceBase);
            this.addHandler(context);
            return context;
        } catch (Exception var4) {
            LOG.debug(var4);
            throw new Error(var4);
        }
    }

    public Class<?> getContextClass() {
        return this._contextClass;
    }

    public void setContextClass(Class<? extends ContextHandler> contextClass) {
        if (contextClass != null && ContextHandler.class.isAssignableFrom(contextClass)) {
            this._contextClass = contextClass;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static final class Branch {

        private final Handler _handler;

        private final ContextHandler[] _contexts;

        Branch(Handler handler) {
            this._handler = handler;
            if (handler instanceof ContextHandler) {
                this._contexts = new ContextHandler[] { (ContextHandler) handler };
            } else if (handler instanceof HandlerContainer) {
                Handler[] contexts = ((HandlerContainer) handler).getChildHandlersByClass(ContextHandler.class);
                this._contexts = new ContextHandler[contexts.length];
                System.arraycopy(contexts, 0, this._contexts, 0, contexts.length);
            } else {
                this._contexts = new ContextHandler[0];
            }
        }

        Set<String> getContextPaths() {
            Set<String> set = new HashSet();
            for (ContextHandler context : this._contexts) {
                set.add(context.getContextPath());
            }
            return set;
        }

        boolean hasVirtualHost() {
            for (ContextHandler context : this._contexts) {
                if (context.getVirtualHosts() != null && context.getVirtualHosts().length > 0) {
                    return true;
                }
            }
            return false;
        }

        ContextHandler[] getContextHandlers() {
            return this._contexts;
        }

        Handler getHandler() {
            return this._handler;
        }

        public String toString() {
            return String.format("{%s,%s}", this._handler, Arrays.asList(this._contexts));
        }
    }
}
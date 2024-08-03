package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.MappedResource;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathMappings;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathSpec;
import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.UriTemplatePathSpec;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap.RegexPathSpec;
import info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap.ServletPathSpec;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Iterator;

public class NativeWebSocketConfiguration extends ContainerLifeCycle implements MappedWebSocketCreator, Dumpable {

    private final WebSocketServerFactory factory;

    private final PathMappings<WebSocketCreator> mappings = new PathMappings<>();

    public NativeWebSocketConfiguration(ServletContext context) {
        this(new WebSocketServerFactory(context));
    }

    public NativeWebSocketConfiguration(WebSocketServerFactory webSocketServerFactory) {
        this.factory = webSocketServerFactory;
        this.addBean(this.factory);
    }

    @Override
    public void doStop() throws Exception {
        this.mappings.removeIf(mapped -> !(mapped.getResource() instanceof NativeWebSocketConfiguration.PersistedWebSocketCreator));
        super.doStop();
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.mappings.dump(out, indent);
    }

    public WebSocketServerFactory getFactory() {
        return this.factory;
    }

    public MappedResource<WebSocketCreator> getMatch(String target) {
        return this.mappings.getMatch(target);
    }

    public WebSocketPolicy getPolicy() {
        return this.factory.getPolicy();
    }

    @Override
    public void addMapping(PathSpec pathSpec, WebSocketCreator creator) {
        WebSocketCreator wsCreator = creator;
        if (!this.isRunning()) {
            wsCreator = new NativeWebSocketConfiguration.PersistedWebSocketCreator(creator);
        }
        this.mappings.put(pathSpec, wsCreator);
    }

    @Deprecated
    @Override
    public void addMapping(info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap.PathSpec spec, WebSocketCreator creator) {
        if (spec instanceof ServletPathSpec) {
            this.addMapping(new info.journeymap.shaded.org.eclipse.jetty.http.pathmap.ServletPathSpec(spec.getSpec()), creator);
        } else {
            if (!(spec instanceof RegexPathSpec)) {
                throw new RuntimeException("Unsupported (Deprecated) PathSpec implementation type: " + spec.getClass().getName());
            }
            this.addMapping(new info.journeymap.shaded.org.eclipse.jetty.http.pathmap.RegexPathSpec(spec.getSpec()), creator);
        }
    }

    public void addMapping(PathSpec pathSpec, Class<?> endpointClass) {
        this.mappings.put(pathSpec, (req, resp) -> {
            try {
                return endpointClass.newInstance();
            } catch (IllegalAccessException | InstantiationException var4) {
                throw new WebSocketException("Unable to create instance of " + endpointClass.getName(), var4);
            }
        });
    }

    @Override
    public void addMapping(String rawspec, WebSocketCreator creator) {
        PathSpec spec = this.toPathSpec(rawspec);
        this.addMapping(spec, creator);
    }

    private PathSpec toPathSpec(String rawspec) {
        if (rawspec.charAt(0) != '/' && !rawspec.startsWith("*.") && !rawspec.startsWith("servlet|")) {
            if (rawspec.charAt(0) == '^' || rawspec.startsWith("regex|")) {
                return new info.journeymap.shaded.org.eclipse.jetty.http.pathmap.RegexPathSpec(rawspec);
            } else if (rawspec.startsWith("uri-template|")) {
                return new UriTemplatePathSpec(rawspec.substring("uri-template|".length()));
            } else {
                throw new IllegalArgumentException("Unrecognized path spec syntax [" + rawspec + "]");
            }
        } else {
            return new info.journeymap.shaded.org.eclipse.jetty.http.pathmap.ServletPathSpec(rawspec);
        }
    }

    @Override
    public WebSocketCreator getMapping(String rawspec) {
        PathSpec pathSpec = this.toPathSpec(rawspec);
        for (MappedResource<WebSocketCreator> mapping : this.mappings) {
            if (mapping.getPathSpec().equals(pathSpec)) {
                return mapping.getResource();
            }
        }
        return null;
    }

    @Override
    public boolean removeMapping(String rawspec) {
        PathSpec pathSpec = this.toPathSpec(rawspec);
        boolean removed = false;
        Iterator<MappedResource<WebSocketCreator>> iterator = this.mappings.iterator();
        while (iterator.hasNext()) {
            MappedResource<WebSocketCreator> mapping = (MappedResource<WebSocketCreator>) iterator.next();
            if (mapping.getPathSpec().equals(pathSpec)) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public void addMapping(String rawspec, Class<?> endpointClass) {
        PathSpec pathSpec = this.toPathSpec(rawspec);
        this.addMapping(pathSpec, endpointClass);
    }

    private class PersistedWebSocketCreator implements WebSocketCreator {

        private final WebSocketCreator delegate;

        public PersistedWebSocketCreator(WebSocketCreator delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
            return this.delegate.createWebSocket(req, resp);
        }

        public String toString() {
            return "Persisted[" + super.toString() + "]";
        }
    }
}
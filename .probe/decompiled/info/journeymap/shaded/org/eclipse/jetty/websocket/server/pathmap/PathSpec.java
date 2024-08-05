package info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap;

@Deprecated
public abstract class PathSpec {

    private final String spec;

    protected PathSpec(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return this.spec;
    }
}
package info.journeymap.shaded.org.eclipse.jetty.servlet;

public class Source {

    public static final Source EMBEDDED = new Source(Source.Origin.EMBEDDED, null);

    public static final Source JAVAX_API = new Source(Source.Origin.JAVAX_API, null);

    public Source.Origin _origin;

    public String _resource;

    public Source(Source.Origin o, String resource) {
        if (o == null) {
            throw new IllegalArgumentException("Origin is null");
        } else {
            this._origin = o;
            this._resource = resource;
        }
    }

    public Source.Origin getOrigin() {
        return this._origin;
    }

    public String getResource() {
        return this._resource;
    }

    public String toString() {
        return this._origin + ":" + this._resource;
    }

    public static enum Origin {

        EMBEDDED, JAVAX_API, DESCRIPTOR, ANNOTATION
    }
}
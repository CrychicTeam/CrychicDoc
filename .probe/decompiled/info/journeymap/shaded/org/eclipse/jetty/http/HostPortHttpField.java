package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.HostPort;

public class HostPortHttpField extends HttpField {

    final HostPort _hostPort;

    public HostPortHttpField(String authority) {
        this(HttpHeader.HOST, HttpHeader.HOST.asString(), authority);
    }

    protected HostPortHttpField(HttpHeader header, String name, String authority) {
        super(header, name, authority);
        try {
            this._hostPort = new HostPort(authority);
        } catch (Exception var5) {
            throw new BadMessageException(400, "Bad HostPort", var5);
        }
    }

    public String getHost() {
        return this._hostPort.getHost();
    }

    public int getPort() {
        return this._hostPort.getPort();
    }

    public int getPort(int defaultPort) {
        return this._hostPort.getPort(defaultPort);
    }
}
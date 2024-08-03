package info.journeymap.shaded.org.eclipse.jetty.util;

public class HostPort {

    private final String _host;

    private final int _port;

    public HostPort(String authority) throws IllegalArgumentException {
        if (authority == null) {
            throw new IllegalArgumentException("No Authority");
        } else {
            try {
                if (authority.isEmpty()) {
                    this._host = authority;
                    this._port = 0;
                } else if (authority.charAt(0) == '[') {
                    int close = authority.lastIndexOf(93);
                    if (close < 0) {
                        throw new IllegalArgumentException("Bad IPv6 host");
                    }
                    this._host = authority.substring(0, close + 1);
                    if (authority.length() > close + 1) {
                        if (authority.charAt(close + 1) != ':') {
                            throw new IllegalArgumentException("Bad IPv6 port");
                        }
                        this._port = StringUtil.toInt(authority, close + 2);
                    } else {
                        this._port = 0;
                    }
                } else {
                    int c = authority.lastIndexOf(58);
                    if (c >= 0) {
                        this._host = authority.substring(0, c);
                        this._port = StringUtil.toInt(authority, c + 1);
                    } else {
                        this._host = authority;
                        this._port = 0;
                    }
                }
            } catch (IllegalArgumentException var3) {
                throw var3;
            } catch (Exception var4) {
                final Exception ex = var4;
                throw new IllegalArgumentException("Bad HostPort") {

                    {
                        this.initCause(ex);
                    }
                };
            }
            if (this._host == null) {
                throw new IllegalArgumentException("Bad host");
            } else if (this._port < 0) {
                throw new IllegalArgumentException("Bad port");
            }
        }
    }

    public String getHost() {
        return this._host;
    }

    public int getPort() {
        return this._port;
    }

    public int getPort(int defaultPort) {
        return this._port > 0 ? this._port : defaultPort;
    }

    public static String normalizeHost(String host) {
        return !host.isEmpty() && host.charAt(0) != '[' && host.indexOf(58) >= 0 ? "[" + host + "]" : host;
    }
}
package info.journeymap.shaded.org.eclipse.jetty.http;

import java.util.concurrent.TimeUnit;

public class HttpCookie {

    private final String _name;

    private final String _value;

    private final String _comment;

    private final String _domain;

    private final long _maxAge;

    private final String _path;

    private final boolean _secure;

    private final int _version;

    private final boolean _httpOnly;

    private final long _expiration;

    public HttpCookie(String name, String value) {
        this(name, value, -1L);
    }

    public HttpCookie(String name, String value, String domain, String path) {
        this(name, value, domain, path, -1L, false, false);
    }

    public HttpCookie(String name, String value, long maxAge) {
        this(name, value, null, null, maxAge, false, false);
    }

    public HttpCookie(String name, String value, String domain, String path, long maxAge, boolean httpOnly, boolean secure) {
        this(name, value, domain, path, maxAge, httpOnly, secure, null, 0);
    }

    public HttpCookie(String name, String value, String domain, String path, long maxAge, boolean httpOnly, boolean secure, String comment, int version) {
        this._name = name;
        this._value = value;
        this._domain = domain;
        this._path = path;
        this._maxAge = maxAge;
        this._httpOnly = httpOnly;
        this._secure = secure;
        this._comment = comment;
        this._version = version;
        this._expiration = maxAge < 0L ? -1L : System.nanoTime() + TimeUnit.SECONDS.toNanos(maxAge);
    }

    public String getName() {
        return this._name;
    }

    public String getValue() {
        return this._value;
    }

    public String getComment() {
        return this._comment;
    }

    public String getDomain() {
        return this._domain;
    }

    public long getMaxAge() {
        return this._maxAge;
    }

    public String getPath() {
        return this._path;
    }

    public boolean isSecure() {
        return this._secure;
    }

    public int getVersion() {
        return this._version;
    }

    public boolean isHttpOnly() {
        return this._httpOnly;
    }

    public boolean isExpired(long timeNanos) {
        return this._expiration >= 0L && timeNanos >= this._expiration;
    }

    public String asString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getName()).append("=").append(this.getValue());
        if (this.getDomain() != null) {
            builder.append(";$Domain=").append(this.getDomain());
        }
        if (this.getPath() != null) {
            builder.append(";$Path=").append(this.getPath());
        }
        return builder.toString();
    }
}
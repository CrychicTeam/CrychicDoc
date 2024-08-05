package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.MultiMap;
import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.UrlEncoded;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpURI {

    private String _scheme;

    private String _user;

    private String _host;

    private int _port;

    private String _path;

    private String _param;

    private String _query;

    private String _fragment;

    String _uri;

    String _decodedPath;

    public static HttpURI createHttpURI(String scheme, String host, int port, String path, String param, String query, String fragment) {
        if (port == 80 && HttpScheme.HTTP.is(scheme)) {
            port = 0;
        }
        if (port == 443 && HttpScheme.HTTPS.is(scheme)) {
            port = 0;
        }
        return new HttpURI(scheme, host, port, path, param, query, fragment);
    }

    public HttpURI() {
    }

    public HttpURI(String scheme, String host, int port, String path, String param, String query, String fragment) {
        this._scheme = scheme;
        this._host = host;
        this._port = port;
        this._path = path;
        this._param = param;
        this._query = query;
        this._fragment = fragment;
    }

    public HttpURI(HttpURI uri) {
        this(uri._scheme, uri._host, uri._port, uri._path, uri._param, uri._query, uri._fragment);
        this._uri = uri._uri;
    }

    public HttpURI(String uri) {
        this._port = -1;
        this.parse(HttpURI.State.START, uri, 0, uri.length());
    }

    public HttpURI(URI uri) {
        this._uri = null;
        this._scheme = uri.getScheme();
        this._host = uri.getHost();
        if (this._host == null && uri.getRawSchemeSpecificPart().startsWith("//")) {
            this._host = "";
        }
        this._port = uri.getPort();
        this._user = uri.getUserInfo();
        this._path = uri.getRawPath();
        this._decodedPath = uri.getPath();
        if (this._decodedPath != null) {
            int p = this._decodedPath.lastIndexOf(59);
            if (p >= 0) {
                this._param = this._decodedPath.substring(p + 1);
            }
        }
        this._query = uri.getRawQuery();
        this._fragment = uri.getFragment();
        this._decodedPath = null;
    }

    public HttpURI(String scheme, String host, int port, String pathQuery) {
        this._uri = null;
        this._scheme = scheme;
        this._host = host;
        this._port = port;
        this.parse(HttpURI.State.PATH, pathQuery, 0, pathQuery.length());
    }

    public void parse(String uri) {
        this.clear();
        this._uri = uri;
        this.parse(HttpURI.State.START, uri, 0, uri.length());
    }

    public void parseRequestTarget(String method, String uri) {
        this.clear();
        this._uri = uri;
        if (HttpMethod.CONNECT.is(method)) {
            this._path = uri;
        } else {
            this.parse(uri.startsWith("/") ? HttpURI.State.PATH : HttpURI.State.START, uri, 0, uri.length());
        }
    }

    @Deprecated
    public void parseConnect(String uri) {
        this.clear();
        this._uri = uri;
        this._path = uri;
    }

    public void parse(String uri, int offset, int length) {
        this.clear();
        int end = offset + length;
        this._uri = uri.substring(offset, end);
        this.parse(HttpURI.State.START, uri, offset, end);
    }

    private void parse(HttpURI.State state, String uri, int offset, int end) {
        boolean encoded = false;
        int mark = offset;
        int path_mark = 0;
        for (int i = offset; i < end; i++) {
            char c = uri.charAt(i);
            switch(state) {
                case START:
                    switch(c) {
                        case '#':
                            mark = i + 1;
                            state = HttpURI.State.FRAGMENT;
                            continue;
                        case '*':
                            this._path = "*";
                            state = HttpURI.State.ASTERISK;
                            continue;
                        case '/':
                            mark = i;
                            state = HttpURI.State.HOST_OR_PATH;
                            continue;
                        case ';':
                            mark = i + 1;
                            state = HttpURI.State.PARAM;
                            continue;
                        case '?':
                            this._path = "";
                            mark = i + 1;
                            state = HttpURI.State.QUERY;
                            continue;
                        default:
                            mark = i;
                            if (this._scheme == null) {
                                state = HttpURI.State.SCHEME_OR_PATH;
                            } else {
                                path_mark = i;
                                state = HttpURI.State.PATH;
                            }
                            continue;
                    }
                case SCHEME_OR_PATH:
                    switch(c) {
                        case '#':
                            this._path = uri.substring(mark, i);
                            state = HttpURI.State.FRAGMENT;
                            continue;
                        case '%':
                            encoded = true;
                            state = HttpURI.State.PATH;
                            continue;
                        case '/':
                            state = HttpURI.State.PATH;
                            continue;
                        case ':':
                            this._scheme = uri.substring(mark, i);
                            state = HttpURI.State.START;
                            continue;
                        case ';':
                            mark = i + 1;
                            state = HttpURI.State.PARAM;
                            continue;
                        case '?':
                            this._path = uri.substring(mark, i);
                            mark = i + 1;
                            state = HttpURI.State.QUERY;
                        default:
                            continue;
                    }
                case HOST_OR_PATH:
                    switch(c) {
                        case '#':
                        case ';':
                        case '?':
                        case '@':
                            i--;
                            path_mark = mark;
                            state = HttpURI.State.PATH;
                            continue;
                        case '/':
                            this._host = "";
                            mark = i + 1;
                            state = HttpURI.State.HOST;
                            continue;
                        default:
                            path_mark = mark;
                            state = HttpURI.State.PATH;
                            continue;
                    }
                case HOST:
                    switch(c) {
                        case '/':
                            this._host = uri.substring(mark, i);
                            mark = i;
                            path_mark = i;
                            state = HttpURI.State.PATH;
                            continue;
                        case ':':
                            if (i > mark) {
                                this._host = uri.substring(mark, i);
                            }
                            mark = i + 1;
                            state = HttpURI.State.PORT;
                            continue;
                        case '@':
                            if (this._user != null) {
                                throw new IllegalArgumentException("Bad authority");
                            }
                            this._user = uri.substring(mark, i);
                            mark = i + 1;
                            continue;
                        case '[':
                            state = HttpURI.State.IPV6;
                        default:
                            continue;
                    }
                case IPV6:
                    switch(c) {
                        case '/':
                            throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
                        case ']':
                            c = uri.charAt(++i);
                            this._host = uri.substring(mark, i);
                            if (c == ':') {
                                mark = i + 1;
                                state = HttpURI.State.PORT;
                            } else {
                                mark = i;
                                path_mark = i;
                                state = HttpURI.State.PATH;
                            }
                        default:
                            continue;
                    }
                case PORT:
                    if (c == '@') {
                        if (this._user != null) {
                            throw new IllegalArgumentException("Bad authority");
                        }
                        this._user = this._host + ":" + uri.substring(mark, i);
                        mark = i + 1;
                        state = HttpURI.State.HOST;
                    } else if (c == '/') {
                        this._port = TypeUtil.parseInt(uri, mark, i - mark, 10);
                        mark = i;
                        path_mark = i;
                        state = HttpURI.State.PATH;
                    }
                    break;
                case PATH:
                    switch(c) {
                        case '#':
                            this._path = uri.substring(path_mark, i);
                            mark = i + 1;
                            state = HttpURI.State.FRAGMENT;
                            continue;
                        case '%':
                            encoded = true;
                            continue;
                        case ';':
                            mark = i + 1;
                            state = HttpURI.State.PARAM;
                            continue;
                        case '?':
                            this._path = uri.substring(path_mark, i);
                            mark = i + 1;
                            state = HttpURI.State.QUERY;
                        default:
                            continue;
                    }
                case PARAM:
                    switch(c) {
                        case '#':
                            this._path = uri.substring(path_mark, i);
                            this._param = uri.substring(mark, i);
                            mark = i + 1;
                            state = HttpURI.State.FRAGMENT;
                            continue;
                        case '/':
                            encoded = true;
                            state = HttpURI.State.PATH;
                            continue;
                        case ';':
                            mark = i + 1;
                            continue;
                        case '?':
                            this._path = uri.substring(path_mark, i);
                            this._param = uri.substring(mark, i);
                            mark = i + 1;
                            state = HttpURI.State.QUERY;
                        default:
                            continue;
                    }
                case QUERY:
                    if (c == '#') {
                        this._query = uri.substring(mark, i);
                        mark = i + 1;
                        state = HttpURI.State.FRAGMENT;
                    }
                    break;
                case ASTERISK:
                    throw new IllegalArgumentException("Bad character '*'");
                case FRAGMENT:
                    this._fragment = uri.substring(mark, end);
                    i = end;
            }
        }
        switch(state) {
            case START:
            case ASTERISK:
            default:
                break;
            case SCHEME_OR_PATH:
                this._path = uri.substring(mark, end);
                break;
            case HOST_OR_PATH:
                this._path = uri.substring(mark, end);
                break;
            case HOST:
                if (end > mark) {
                    this._host = uri.substring(mark, end);
                }
                break;
            case IPV6:
                throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
            case PORT:
                this._port = TypeUtil.parseInt(uri, mark, end - mark, 10);
                break;
            case PATH:
                this._path = uri.substring(path_mark, end);
                break;
            case PARAM:
                this._path = uri.substring(path_mark, end);
                this._param = uri.substring(mark, end);
                break;
            case QUERY:
                this._query = uri.substring(mark, end);
                break;
            case FRAGMENT:
                this._fragment = uri.substring(mark, end);
        }
        if (!encoded) {
            if (this._param == null) {
                this._decodedPath = this._path;
            } else {
                this._decodedPath = this._path.substring(0, this._path.length() - this._param.length() - 1);
            }
        }
    }

    public String getScheme() {
        return this._scheme;
    }

    public String getHost() {
        return this._host != null && this._host.length() == 0 ? null : this._host;
    }

    public int getPort() {
        return this._port;
    }

    public String getPath() {
        return this._path;
    }

    public String getDecodedPath() {
        if (this._decodedPath == null && this._path != null) {
            this._decodedPath = URIUtil.decodePath(this._path);
        }
        return this._decodedPath;
    }

    public String getParam() {
        return this._param;
    }

    public String getQuery() {
        return this._query;
    }

    public boolean hasQuery() {
        return this._query != null && this._query.length() > 0;
    }

    public String getFragment() {
        return this._fragment;
    }

    public void decodeQueryTo(MultiMap<String> parameters) {
        if (this._query != this._fragment) {
            UrlEncoded.decodeUtf8To(this._query, parameters);
        }
    }

    public void decodeQueryTo(MultiMap<String> parameters, String encoding) throws UnsupportedEncodingException {
        this.decodeQueryTo(parameters, Charset.forName(encoding));
    }

    public void decodeQueryTo(MultiMap<String> parameters, Charset encoding) throws UnsupportedEncodingException {
        if (this._query != this._fragment) {
            if (encoding != null && !StandardCharsets.UTF_8.equals(encoding)) {
                UrlEncoded.decodeTo(this._query, parameters, encoding);
            } else {
                UrlEncoded.decodeUtf8To(this._query, parameters);
            }
        }
    }

    public void clear() {
        this._uri = null;
        this._scheme = null;
        this._host = null;
        this._port = -1;
        this._path = null;
        this._param = null;
        this._query = null;
        this._fragment = null;
        this._decodedPath = null;
    }

    public boolean isAbsolute() {
        return this._scheme != null && this._scheme.length() > 0;
    }

    public String toString() {
        if (this._uri == null) {
            StringBuilder out = new StringBuilder();
            if (this._scheme != null) {
                out.append(this._scheme).append(':');
            }
            if (this._host != null) {
                out.append("//");
                if (this._user != null) {
                    out.append(this._user).append('@');
                }
                out.append(this._host);
            }
            if (this._port > 0) {
                out.append(':').append(this._port);
            }
            if (this._path != null) {
                out.append(this._path);
            }
            if (this._query != null) {
                out.append('?').append(this._query);
            }
            if (this._fragment != null) {
                out.append('#').append(this._fragment);
            }
            if (out.length() > 0) {
                this._uri = out.toString();
            } else {
                this._uri = "";
            }
        }
        return this._uri;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof HttpURI) ? false : this.toString().equals(o.toString());
        }
    }

    public void setScheme(String scheme) {
        this._scheme = scheme;
        this._uri = null;
    }

    public void setAuthority(String host, int port) {
        this._host = host;
        this._port = port;
        this._uri = null;
    }

    public void setPath(String path) {
        this._uri = null;
        this._path = path;
        this._decodedPath = null;
    }

    public void setPathQuery(String path) {
        this._uri = null;
        this._path = null;
        this._decodedPath = null;
        this._param = null;
        this._fragment = null;
        if (path != null) {
            this.parse(HttpURI.State.PATH, path, 0, path.length());
        }
    }

    public void setQuery(String query) {
        this._query = query;
        this._uri = null;
    }

    public URI toURI() throws URISyntaxException {
        return new URI(this._scheme, null, this._host, this._port, this._path, this._query == null ? null : UrlEncoded.decodeString(this._query), this._fragment);
    }

    public String getPathQuery() {
        return this._query == null ? this._path : this._path + "?" + this._query;
    }

    public String getAuthority() {
        return this._port > 0 ? this._host + ":" + this._port : this._host;
    }

    public String getUser() {
        return this._user;
    }

    private static enum State {

        START,
        HOST_OR_PATH,
        SCHEME_OR_PATH,
        HOST,
        IPV6,
        PORT,
        PATH,
        PARAM,
        QUERY,
        FRAGMENT,
        ASTERISK
    }
}
package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.QuotedCSV;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CookieCutter {

    private static final Logger LOG = Log.getLogger(CookieCutter.class);

    private Cookie[] _cookies;

    private Cookie[] _lastCookies;

    private final List<String> _fieldList = new ArrayList();

    int _fields;

    public Cookie[] getCookies() {
        if (this._cookies != null) {
            return this._cookies;
        } else {
            if (this._lastCookies != null && this._fields == this._fieldList.size()) {
                this._cookies = this._lastCookies;
            } else {
                this.parseFields();
            }
            this._lastCookies = this._cookies;
            return this._cookies;
        }
    }

    public void setCookies(Cookie[] cookies) {
        this._cookies = cookies;
        this._lastCookies = null;
        this._fieldList.clear();
        this._fields = 0;
    }

    public void reset() {
        this._cookies = null;
        this._fields = 0;
    }

    public void addCookieField(String f) {
        if (f != null) {
            f = f.trim();
            if (f.length() != 0) {
                if (this._fieldList.size() > this._fields) {
                    if (f.equals(this._fieldList.get(this._fields))) {
                        this._fields++;
                        return;
                    }
                    while (this._fieldList.size() > this._fields) {
                        this._fieldList.remove(this._fields);
                    }
                }
                this._cookies = null;
                this._lastCookies = null;
                this._fieldList.add(this._fields++, f);
            }
        }
    }

    protected void parseFields() {
        this._lastCookies = null;
        this._cookies = null;
        List<Cookie> cookies = new ArrayList();
        int version = 0;
        while (this._fieldList.size() > this._fields) {
            this._fieldList.remove(this._fields);
        }
        for (String hdr : this._fieldList) {
            String name = null;
            String value = null;
            Cookie cookie = null;
            boolean invalue = false;
            boolean quoted = false;
            boolean escaped = false;
            int tokenstart = -1;
            int tokenend = -1;
            int i = 0;
            int length = hdr.length();
            for (int last = length - 1; i < length; i++) {
                char c = hdr.charAt(i);
                if (quoted) {
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    switch(c) {
                        case '"':
                            tokenend = i;
                            quoted = false;
                            if (i == last) {
                                if (invalue) {
                                    value = hdr.substring(tokenstart, i + 1);
                                } else {
                                    name = hdr.substring(tokenstart, i + 1);
                                    value = "";
                                }
                            }
                            break;
                        case '\\':
                            escaped = true;
                        default:
                            continue;
                    }
                } else if (invalue) {
                    switch(c) {
                        case '\t':
                        case ' ':
                            continue;
                        case '"':
                            if (tokenstart < 0) {
                                quoted = true;
                                tokenstart = i;
                            }
                            tokenend = i;
                            if (i != last) {
                                continue;
                            }
                            value = hdr.substring(tokenstart, i + 1);
                            break;
                        case ';':
                            if (tokenstart >= 0) {
                                value = hdr.substring(tokenstart, tokenend + 1);
                            } else {
                                value = "";
                            }
                            tokenstart = -1;
                            invalue = false;
                            break;
                        default:
                            if (tokenstart < 0) {
                                tokenstart = i;
                            }
                            tokenend = i;
                            if (i != last) {
                                continue;
                            }
                            value = hdr.substring(tokenstart, i + 1);
                    }
                } else {
                    switch(c) {
                        case '\t':
                        case ' ':
                            continue;
                        case '"':
                            if (tokenstart < 0) {
                                quoted = true;
                                tokenstart = i;
                            }
                            tokenend = i;
                            if (i != last) {
                                continue;
                            }
                            name = hdr.substring(tokenstart, i + 1);
                            value = "";
                            break;
                        case ';':
                            if (tokenstart >= 0) {
                                name = hdr.substring(tokenstart, tokenend + 1);
                                value = "";
                            }
                            tokenstart = -1;
                            break;
                        case '=':
                            if (tokenstart >= 0) {
                                name = hdr.substring(tokenstart, tokenend + 1);
                            }
                            tokenstart = -1;
                            invalue = true;
                            continue;
                        default:
                            if (tokenstart < 0) {
                                tokenstart = i;
                            }
                            tokenend = i;
                            if (i != last) {
                                continue;
                            }
                            name = hdr.substring(tokenstart, i + 1);
                            value = "";
                    }
                }
                if (value != null && name != null) {
                    name = QuotedCSV.unquote(name);
                    value = QuotedCSV.unquote(value);
                    try {
                        if (name.startsWith("$")) {
                            String lowercaseName = name.toLowerCase(Locale.ENGLISH);
                            if ("$path".equals(lowercaseName)) {
                                if (cookie != null) {
                                    cookie.setPath(value);
                                }
                            } else if ("$domain".equals(lowercaseName)) {
                                if (cookie != null) {
                                    cookie.setDomain(value);
                                }
                            } else if ("$port".equals(lowercaseName)) {
                                if (cookie != null) {
                                    cookie.setComment("$port=" + value);
                                }
                            } else if ("$version".equals(lowercaseName)) {
                                version = Integer.parseInt(value);
                            }
                        } else {
                            cookie = new Cookie(name, value);
                            if (version > 0) {
                                cookie.setVersion(version);
                            }
                            cookies.add(cookie);
                        }
                    } catch (Exception var18) {
                        LOG.debug(var18);
                    }
                    name = null;
                    value = null;
                }
            }
        }
        this._cookies = (Cookie[]) cookies.toArray(new Cookie[cookies.size()]);
        this._lastCookies = this._cookies;
    }
}
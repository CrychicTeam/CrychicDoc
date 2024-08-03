package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.routematch.RouteMatch;
import info.journeymap.shaded.kotlin.spark.utils.IOUtils;
import info.journeymap.shaded.kotlin.spark.utils.SparkUtils;
import info.journeymap.shaded.kotlin.spark.utils.StringUtils;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Request {

    private static final Logger LOG = LoggerFactory.getLogger(Request.class);

    private static final String USER_AGENT = "user-agent";

    private Map<String, String> params;

    private List<String> splat;

    private QueryParamsMap queryMap;

    private HttpServletRequest servletRequest;

    private Session session = null;

    private boolean validSession = false;

    private String body = null;

    private byte[] bodyAsBytes = null;

    private Set<String> headers = null;

    protected Request() {
    }

    Request(RouteMatch match, HttpServletRequest request) {
        this.servletRequest = request;
        this.changeMatch(match);
    }

    Request(HttpServletRequest request) {
        this.servletRequest = request;
        this.params = new HashMap();
        this.splat = new ArrayList();
    }

    protected void changeMatch(RouteMatch match) {
        List<String> requestList = SparkUtils.convertRouteToList(match.getRequestURI());
        List<String> matchedList = SparkUtils.convertRouteToList(match.getMatchUri());
        this.params = getParams(requestList, matchedList);
        this.splat = getSplat(requestList, matchedList);
    }

    public Map<String, String> params() {
        return Collections.unmodifiableMap(this.params);
    }

    public String params(String param) {
        if (param == null) {
            return null;
        } else {
            return param.startsWith(":") ? (String) this.params.get(param.toLowerCase()) : (String) this.params.get(":" + param.toLowerCase());
        }
    }

    public String[] splat() {
        return (String[]) this.splat.toArray(new String[this.splat.size()]);
    }

    public String requestMethod() {
        return this.servletRequest.getMethod();
    }

    public String scheme() {
        return this.servletRequest.getScheme();
    }

    public String host() {
        return this.servletRequest.getHeader("host");
    }

    public String userAgent() {
        return this.servletRequest.getHeader("user-agent");
    }

    public int port() {
        return this.servletRequest.getServerPort();
    }

    public String pathInfo() {
        return this.servletRequest.getPathInfo();
    }

    public String servletPath() {
        return this.servletRequest.getServletPath();
    }

    public String contextPath() {
        return this.servletRequest.getContextPath();
    }

    public String url() {
        return this.servletRequest.getRequestURL().toString();
    }

    public String contentType() {
        return this.servletRequest.getContentType();
    }

    public String ip() {
        return this.servletRequest.getRemoteAddr();
    }

    public String body() {
        if (this.body == null) {
            this.body = StringUtils.toString(this.bodyAsBytes(), this.servletRequest.getCharacterEncoding());
        }
        return this.body;
    }

    public byte[] bodyAsBytes() {
        if (this.bodyAsBytes == null) {
            this.readBodyAsBytes();
        }
        return this.bodyAsBytes;
    }

    private void readBodyAsBytes() {
        try {
            this.bodyAsBytes = IOUtils.toByteArray(this.servletRequest.getInputStream());
        } catch (Exception var2) {
            LOG.warn("Exception when reading body", (Throwable) var2);
        }
    }

    public int contentLength() {
        return this.servletRequest.getContentLength();
    }

    public String queryParams(String queryParam) {
        return this.servletRequest.getParameter(queryParam);
    }

    public String queryParamOrDefault(String queryParam, String defaultValue) {
        String value = this.queryParams(queryParam);
        return value != null ? value : defaultValue;
    }

    public String[] queryParamsValues(String queryParam) {
        return this.servletRequest.getParameterValues(queryParam);
    }

    public String headers(String header) {
        return this.servletRequest.getHeader(header);
    }

    public Set<String> queryParams() {
        return this.servletRequest.getParameterMap().keySet();
    }

    public Set<String> headers() {
        if (this.headers == null) {
            this.headers = new TreeSet();
            Enumeration<String> enumeration = this.servletRequest.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                this.headers.add(enumeration.nextElement());
            }
        }
        return this.headers;
    }

    public String queryString() {
        return this.servletRequest.getQueryString();
    }

    public void attribute(String attribute, Object value) {
        this.servletRequest.setAttribute(attribute, value);
    }

    public <T> T attribute(String attribute) {
        return (T) this.servletRequest.getAttribute(attribute);
    }

    public Set<String> attributes() {
        Set<String> attrList = new HashSet();
        Enumeration<String> attributes = this.servletRequest.getAttributeNames();
        while (attributes.hasMoreElements()) {
            attrList.add(attributes.nextElement());
        }
        return attrList;
    }

    public HttpServletRequest raw() {
        return this.servletRequest;
    }

    public QueryParamsMap queryMap() {
        this.initQueryMap();
        return this.queryMap;
    }

    public QueryParamsMap queryMap(String key) {
        return this.queryMap().get(key);
    }

    private void initQueryMap() {
        if (this.queryMap == null) {
            this.queryMap = new QueryParamsMap(this.raw());
        }
    }

    public Session session() {
        if (this.session == null || !this.validSession) {
            this.validSession(true);
            this.session = new Session(this.servletRequest.getSession(), this);
        }
        return this.session;
    }

    public Session session(boolean create) {
        if (this.session == null || !this.validSession) {
            HttpSession httpSession = this.servletRequest.getSession(create);
            if (httpSession != null) {
                this.validSession(true);
                this.session = new Session(httpSession, this);
            } else {
                this.session = null;
            }
        }
        return this.session;
    }

    public Map<String, String> cookies() {
        Map<String, String> result = new HashMap();
        Cookie[] cookies = this.servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                result.put(cookie.getName(), cookie.getValue());
            }
        }
        return result;
    }

    public String cookie(String name) {
        Cookie[] cookies = this.servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String uri() {
        return this.servletRequest.getRequestURI();
    }

    public String protocol() {
        return this.servletRequest.getProtocol();
    }

    private static Map<String, String> getParams(List<String> request, List<String> matched) {
        Map<String, String> params = new HashMap();
        for (int i = 0; i < request.size() && i < matched.size(); i++) {
            String matchedPart = (String) matched.get(i);
            if (SparkUtils.isParam(matchedPart)) {
                try {
                    String decodedReq = URLDecoder.decode((String) request.get(i), "UTF-8");
                    LOG.debug("matchedPart: " + matchedPart + " = " + decodedReq);
                    params.put(matchedPart.toLowerCase(), decodedReq);
                } catch (UnsupportedEncodingException var6) {
                }
            }
        }
        return Collections.unmodifiableMap(params);
    }

    private static List<String> getSplat(List<String> request, List<String> matched) {
        int nbrOfRequestParts = request.size();
        int nbrOfMatchedParts = matched.size();
        boolean sameLength = nbrOfRequestParts == nbrOfMatchedParts;
        List<String> splat = new ArrayList();
        for (int i = 0; i < nbrOfRequestParts && i < nbrOfMatchedParts; i++) {
            String matchedPart = (String) matched.get(i);
            if (SparkUtils.isSplat(matchedPart)) {
                StringBuilder splatParam = new StringBuilder((String) request.get(i));
                if (!sameLength && i == nbrOfMatchedParts - 1) {
                    for (int j = i + 1; j < nbrOfRequestParts; j++) {
                        splatParam.append("/");
                        splatParam.append((String) request.get(j));
                    }
                }
                try {
                    String decodedSplat = URLDecoder.decode(splatParam.toString(), "UTF-8");
                    splat.add(decodedSplat);
                } catch (UnsupportedEncodingException var10) {
                }
            }
        }
        return Collections.unmodifiableList(splat);
    }

    void validSession(boolean validSession) {
        this.validSession = validSession;
    }
}
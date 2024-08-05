package info.journeymap.shaded.org.eclipse.jetty.websocket.servlet;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.UpgradeResponse;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ServletUpgradeResponse implements UpgradeResponse {

    private HttpServletResponse response;

    private boolean extensionsNegotiated = false;

    private boolean subprotocolNegotiated = false;

    private Map<String, List<String>> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    private List<ExtensionConfig> extensions = new ArrayList();

    private boolean success = false;

    private int status;

    public ServletUpgradeResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void addHeader(String name, String value) {
        if (value != null) {
            List<String> values = (List<String>) this.headers.get(name);
            if (values == null) {
                values = new ArrayList();
                this.headers.put(name, values);
            }
            values.add(value);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        if (this.response != null) {
            this.response.setHeader(name, null);
        }
        List<String> values = (List<String>) this.headers.get(name);
        if (values == null) {
            values = new ArrayList();
            this.headers.put(name, values);
        } else {
            values.clear();
        }
        values.add(value);
    }

    public void complete() {
        if (this.response != null) {
            Map<String, Collection<String>> real = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            for (String name : this.response.getHeaderNames()) {
                real.put(name, this.response.getHeaders(name));
            }
            for (Entry<String, List<String>> entry : this.getHeaders().entrySet()) {
                for (String value : (List) entry.getValue()) {
                    this.response.addHeader((String) entry.getKey(), value);
                }
            }
            for (Entry<String, Collection<String>> entry : real.entrySet()) {
                String name = (String) entry.getKey();
                Collection<String> prepend = (Collection<String>) entry.getValue();
                List<String> values = (List<String>) this.headers.getOrDefault(name, new ArrayList());
                values.addAll(0, prepend);
                this.headers.put(name, values);
            }
            this.status = this.response.getStatus();
            this.response = null;
        }
    }

    @Override
    public String getAcceptedSubProtocol() {
        return this.getHeader("Sec-WebSocket-Protocol");
    }

    @Override
    public List<ExtensionConfig> getExtensions() {
        return this.extensions;
    }

    @Override
    public String getHeader(String name) {
        if (this.response != null) {
            String value = this.response.getHeader(name);
            if (value != null) {
                return value;
            }
        }
        List<String> values = (List<String>) this.headers.get(name);
        return values != null && !values.isEmpty() ? (String) values.get(0) : null;
    }

    @Override
    public Set<String> getHeaderNames() {
        if (this.response == null) {
            return this.headers.keySet();
        } else {
            Set<String> h = new HashSet(this.response.getHeaderNames());
            h.addAll(this.headers.keySet());
            return h;
        }
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    @Override
    public List<String> getHeaders(String name) {
        if (this.response == null) {
            return (List<String>) this.headers.get(name);
        } else {
            List<String> values = new ArrayList(this.response.getHeaders(name));
            values.addAll((Collection) this.headers.get(name));
            return values.isEmpty() ? null : values;
        }
    }

    @Override
    public int getStatusCode() {
        return this.response != null ? this.response.getStatus() : this.status;
    }

    @Override
    public String getStatusReason() {
        throw new UnsupportedOperationException("Servlet's do not support Status Reason");
    }

    public boolean isCommitted() {
        return this.response != null ? this.response.isCommitted() : true;
    }

    public boolean isExtensionsNegotiated() {
        return this.extensionsNegotiated;
    }

    public boolean isSubprotocolNegotiated() {
        return this.subprotocolNegotiated;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    public void sendError(int statusCode, String message) throws IOException {
        this.setSuccess(false);
        HttpServletResponse r = this.response;
        this.complete();
        r.sendError(statusCode, message);
        r.flushBuffer();
    }

    @Override
    public void sendForbidden(String message) throws IOException {
        this.setSuccess(false);
        HttpServletResponse r = this.response;
        this.complete();
        r.sendError(403, message);
        r.flushBuffer();
    }

    @Override
    public void setAcceptedSubProtocol(String protocol) {
        this.response.setHeader("Sec-WebSocket-Protocol", protocol);
        this.subprotocolNegotiated = true;
    }

    @Override
    public void setExtensions(List<ExtensionConfig> configs) {
        this.extensions.clear();
        this.extensions.addAll(configs);
        String value = ExtensionConfig.toHeaderValue(configs);
        this.response.setHeader("Sec-WebSocket-Extensions", value);
        this.extensionsNegotiated = true;
    }

    @Override
    public void setStatusCode(int statusCode) {
        if (this.response != null) {
            this.response.setStatus(statusCode);
        }
    }

    @Override
    public void setStatusReason(String statusReason) {
        throw new UnsupportedOperationException("Servlet's do not support Status Reason");
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String toString() {
        return String.format("r=%s s=%d h=%s", this.response, this.status, this.headers);
    }
}
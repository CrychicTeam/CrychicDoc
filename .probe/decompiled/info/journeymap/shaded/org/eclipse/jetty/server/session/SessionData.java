package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionData implements Serializable {

    private static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    private static final long serialVersionUID = 1L;

    protected String _id;

    protected String _contextPath;

    protected String _vhost;

    protected String _lastNode;

    protected long _expiry;

    protected long _created;

    protected long _cookieSet;

    protected long _accessed;

    protected long _lastAccessed;

    protected long _maxInactiveMs;

    protected Map<String, Object> _attributes;

    protected boolean _dirty;

    protected long _lastSaved;

    public SessionData(String id, String cpath, String vhost, long created, long accessed, long lastAccessed, long maxInactiveMs) {
        this(id, cpath, vhost, created, accessed, lastAccessed, maxInactiveMs, new ConcurrentHashMap());
    }

    public SessionData(String id, String cpath, String vhost, long created, long accessed, long lastAccessed, long maxInactiveMs, Map<String, Object> attributes) {
        this._id = id;
        this.setContextPath(cpath);
        this.setVhost(vhost);
        this._created = created;
        this._accessed = accessed;
        this._lastAccessed = lastAccessed;
        this._maxInactiveMs = maxInactiveMs;
        this.calcAndSetExpiry();
        this._attributes = attributes;
    }

    public void copy(SessionData data) {
        if (data != null) {
            if (data.getId() == null || !this.getId().equals(data.getId())) {
                throw new IllegalStateException("Can only copy data for same session id");
            } else if (data != this) {
                this.setLastNode(data.getLastNode());
                this.setContextPath(data.getContextPath());
                this.setVhost(data.getVhost());
                this.setCookieSet(data.getCookieSet());
                this.setCreated(data.getCreated());
                this.setAccessed(data.getAccessed());
                this.setLastAccessed(data.getLastAccessed());
                this.setMaxInactiveMs(data.getMaxInactiveMs());
                this.setExpiry(data.getExpiry());
                this.setLastSaved(data.getLastSaved());
                this.clearAllAttributes();
                this.putAllAttributes(data.getAllAttributes());
            }
        }
    }

    public long getLastSaved() {
        return this._lastSaved;
    }

    public void setLastSaved(long lastSaved) {
        this._lastSaved = lastSaved;
    }

    public boolean isDirty() {
        return this._dirty;
    }

    public void setDirty(boolean dirty) {
        this._dirty = dirty;
    }

    public Object getAttribute(String name) {
        return this._attributes.get(name);
    }

    public Set<String> getKeys() {
        return this._attributes.keySet();
    }

    public Object setAttribute(String name, Object value) {
        Object old = value == null ? this._attributes.remove(name) : this._attributes.put(name, value);
        if (value == null && old == null) {
            return old;
        } else {
            this.setDirty(name);
            return old;
        }
    }

    public void setDirty(String name) {
        this.setDirty(true);
    }

    public void putAllAttributes(Map<String, Object> attributes) {
        this._attributes.putAll(attributes);
    }

    public void clearAllAttributes() {
        this._attributes.clear();
    }

    public Map<String, Object> getAllAttributes() {
        return Collections.unmodifiableMap(this._attributes);
    }

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getContextPath() {
        return this._contextPath;
    }

    public void setContextPath(String contextPath) {
        this._contextPath = contextPath;
    }

    public String getVhost() {
        return this._vhost;
    }

    public void setVhost(String vhost) {
        this._vhost = vhost;
    }

    public String getLastNode() {
        return this._lastNode;
    }

    public void setLastNode(String lastNode) {
        this._lastNode = lastNode;
    }

    public long getExpiry() {
        return this._expiry;
    }

    public void setExpiry(long expiry) {
        this._expiry = expiry;
    }

    public long calcExpiry() {
        return this.calcExpiry(System.currentTimeMillis());
    }

    public long calcExpiry(long time) {
        return this.getMaxInactiveMs() <= 0L ? 0L : time + this.getMaxInactiveMs();
    }

    public void calcAndSetExpiry(long time) {
        this.setExpiry(this.calcExpiry(time));
    }

    public void calcAndSetExpiry() {
        this.setExpiry(this.calcExpiry());
    }

    public long getCreated() {
        return this._created;
    }

    public void setCreated(long created) {
        this._created = created;
    }

    public long getCookieSet() {
        return this._cookieSet;
    }

    public void setCookieSet(long cookieSet) {
        this._cookieSet = cookieSet;
    }

    public long getAccessed() {
        return this._accessed;
    }

    public void setAccessed(long accessed) {
        this._accessed = accessed;
    }

    public long getLastAccessed() {
        return this._lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
        this._lastAccessed = lastAccessed;
    }

    public long getMaxInactiveMs() {
        return this._maxInactiveMs;
    }

    public void setMaxInactiveMs(long maxInactive) {
        this._maxInactiveMs = maxInactive;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(this._id);
        out.writeUTF(this._contextPath);
        out.writeUTF(this._vhost);
        out.writeLong(this._accessed);
        out.writeLong(this._lastAccessed);
        out.writeLong(this._created);
        out.writeLong(this._cookieSet);
        out.writeUTF(this._lastNode);
        out.writeLong(this._expiry);
        out.writeLong(this._maxInactiveMs);
        out.writeObject(this._attributes);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this._id = in.readUTF();
        this._contextPath = in.readUTF();
        this._vhost = in.readUTF();
        this._accessed = in.readLong();
        this._lastAccessed = in.readLong();
        this._created = in.readLong();
        this._cookieSet = in.readLong();
        this._lastNode = in.readUTF();
        this._expiry = in.readLong();
        this._maxInactiveMs = in.readLong();
        this._attributes = (Map<String, Object>) in.readObject();
    }

    public boolean isExpiredAt(long time) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Testing expiry on session {}: expires at {} now {} maxIdle {}", this._id, this.getExpiry(), time, this.getMaxInactiveMs());
        }
        return this.getMaxInactiveMs() <= 0L ? false : this.getExpiry() <= time;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=" + this._id);
        builder.append(", contextpath=" + this._contextPath);
        builder.append(", vhost=" + this._vhost);
        builder.append(", accessed=" + this._accessed);
        builder.append(", lastaccessed=" + this._lastAccessed);
        builder.append(", created=" + this._created);
        builder.append(", cookieset=" + this._cookieSet);
        builder.append(", lastnode=" + this._lastNode);
        builder.append(", expiry=" + this._expiry);
        builder.append(", maxinactive=" + this._maxInactiveMs);
        return builder.toString();
    }
}
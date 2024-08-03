package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.utils.Assert;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

public class Session {

    private final Request request;

    private final HttpSession session;

    Session(HttpSession session, Request request) {
        Assert.notNull(session, "session cannot be null");
        Assert.notNull(request, "request cannot be null");
        this.session = session;
        this.request = request;
    }

    public HttpSession raw() {
        return this.session;
    }

    public <T> T attribute(String name) {
        return (T) this.session.getAttribute(name);
    }

    public void attribute(String name, Object value) {
        this.session.setAttribute(name, value);
    }

    public Set<String> attributes() {
        TreeSet<String> attributes = new TreeSet();
        Enumeration<String> enumeration = this.session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            attributes.add(enumeration.nextElement());
        }
        return attributes;
    }

    public long creationTime() {
        return this.session.getCreationTime();
    }

    public String id() {
        return this.session.getId();
    }

    public long lastAccessedTime() {
        return this.session.getLastAccessedTime();
    }

    public int maxInactiveInterval() {
        return this.session.getMaxInactiveInterval();
    }

    public void maxInactiveInterval(int interval) {
        this.session.setMaxInactiveInterval(interval);
    }

    public void invalidate() {
        this.request.validSession(false);
        this.session.invalidate();
    }

    public boolean isNew() {
        return this.session.isNew();
    }

    public void removeAttribute(String name) {
        this.session.removeAttribute(name);
    }
}
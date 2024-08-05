package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import java.util.Set;

@ManagedObject
public class NullSessionDataStore extends AbstractSessionDataStore {

    @Override
    public SessionData load(String id) throws Exception {
        return null;
    }

    @Override
    public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs) {
        return new SessionData(id, this._context.getCanonicalContextPath(), this._context.getVhost(), created, accessed, lastAccessed, maxInactiveMs);
    }

    @Override
    public boolean delete(String id) throws Exception {
        return true;
    }

    @Override
    public void doStore(String id, SessionData data, long lastSaveTime) throws Exception {
    }

    @Override
    public Set<String> doGetExpired(Set<String> candidates) {
        return candidates;
    }

    @ManagedAttribute(value = "does this store serialize sessions", readonly = true)
    @Override
    public boolean isPassivating() {
        return false;
    }

    @Override
    public boolean exists(String id) {
        return false;
    }
}
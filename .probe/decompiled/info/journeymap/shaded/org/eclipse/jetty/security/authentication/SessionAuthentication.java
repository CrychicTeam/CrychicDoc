package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.security.AbstractUserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.security.LoginService;
import info.journeymap.shaded.org.eclipse.jetty.security.SecurityHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionActivationListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionBindingEvent;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionBindingListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SessionAuthentication extends AbstractUserAuthentication implements Serializable, HttpSessionActivationListener, HttpSessionBindingListener {

    private static final Logger LOG = Log.getLogger(SessionAuthentication.class);

    private static final long serialVersionUID = -4643200685888258706L;

    public static final String __J_AUTHENTICATED = "info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity";

    private final String _name;

    private final Object _credentials;

    private transient HttpSession _session;

    public SessionAuthentication(String method, UserIdentity userIdentity, Object credentials) {
        super(method, userIdentity);
        this._name = userIdentity.getUserPrincipal().getName();
        this._credentials = credentials;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security == null) {
            throw new IllegalStateException("!SecurityHandler");
        } else {
            LoginService login_service = security.getLoginService();
            if (login_service == null) {
                throw new IllegalStateException("!LoginService");
            } else {
                this._userIdentity = login_service.login(this._name, this._credentials, null);
                LOG.debug("Deserialized and relogged in {}", this);
            }
        }
    }

    @Override
    public void logout() {
        if (this._session != null && this._session.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity") != null) {
            this._session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.UserIdentity");
        }
        this.doLogout();
    }

    private void doLogout() {
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security != null) {
            security.logout(this);
        }
        if (this._session != null) {
            this._session.removeAttribute("info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure");
        }
    }

    public String toString() {
        return String.format("%s@%x{%s,%s}", this.getClass().getSimpleName(), this.hashCode(), this._session == null ? "-" : this._session.getId(), this._userIdentity);
    }

    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        if (this._session == null) {
            this._session = se.getSession();
        }
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        if (this._session == null) {
            this._session = event.getSession();
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        this.doLogout();
    }
}
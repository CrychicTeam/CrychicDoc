package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.security.Authenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.IdentityService;
import info.journeymap.shaded.org.eclipse.jetty.security.LoginService;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Response;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.server.session.Session;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;

public abstract class LoginAuthenticator implements Authenticator {

    private static final Logger LOG = Log.getLogger(LoginAuthenticator.class);

    protected LoginService _loginService;

    protected IdentityService _identityService;

    private boolean _renewSession;

    protected LoginAuthenticator() {
    }

    @Override
    public void prepareRequest(ServletRequest request) {
    }

    public UserIdentity login(String username, Object password, ServletRequest request) {
        UserIdentity user = this._loginService.login(username, password, request);
        if (user != null) {
            this.renewSession((HttpServletRequest) request, request instanceof Request ? ((Request) request).getResponse() : null);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void setConfiguration(Authenticator.AuthConfiguration configuration) {
        this._loginService = configuration.getLoginService();
        if (this._loginService == null) {
            throw new IllegalStateException("No LoginService for " + this + " in " + configuration);
        } else {
            this._identityService = configuration.getIdentityService();
            if (this._identityService == null) {
                throw new IllegalStateException("No IdentityService for " + this + " in " + configuration);
            } else {
                this._renewSession = configuration.isSessionRenewedOnAuthentication();
            }
        }
    }

    public LoginService getLoginService() {
        return this._loginService;
    }

    protected HttpSession renewSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);
        if (this._renewSession && httpSession != null) {
            synchronized (httpSession) {
                if (httpSession.getAttribute("info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure") != Boolean.TRUE) {
                    if (httpSession instanceof Session) {
                        Session s = (Session) httpSession;
                        String oldId = s.getId();
                        s.renewId(request);
                        s.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure", Boolean.TRUE);
                        if (s.isIdChanged() && response != null && response instanceof Response) {
                            ((Response) response).addCookie(s.getSessionHandler().getSessionCookie(s, request.getContextPath(), request.isSecure()));
                        }
                        LOG.debug("renew {}->{}", oldId, s.getId());
                    } else {
                        LOG.warn("Unable to renew session " + httpSession);
                    }
                    return httpSession;
                }
            }
        }
        return httpSession;
    }
}
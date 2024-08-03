package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.security.ServerAuthException;
import info.journeymap.shaded.org.eclipse.jetty.security.UserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpnegoAuthenticator extends LoginAuthenticator {

    private static final Logger LOG = Log.getLogger(SpnegoAuthenticator.class);

    private String _authMethod = "SPNEGO";

    public SpnegoAuthenticator() {
    }

    public SpnegoAuthenticator(String authMethod) {
        this._authMethod = authMethod;
    }

    @Override
    public String getAuthMethod() {
        return this._authMethod;
    }

    @Override
    public Authentication validateRequest(ServletRequest request, ServletResponse response, boolean mandatory) throws ServerAuthException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header = req.getHeader(HttpHeader.AUTHORIZATION.asString());
        if (!mandatory) {
            return new DeferredAuthentication(this);
        } else if (header == null) {
            try {
                if (DeferredAuthentication.isDeferred(res)) {
                    return Authentication.UNAUTHENTICATED;
                } else {
                    LOG.debug("SpengoAuthenticator: sending challenge");
                    res.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), HttpHeader.NEGOTIATE.asString());
                    res.sendError(401);
                    return Authentication.SEND_CONTINUE;
                }
            } catch (IOException var9) {
                throw new ServerAuthException(var9);
            }
        } else {
            if (header != null && header.startsWith(HttpHeader.NEGOTIATE.asString())) {
                String spnegoToken = header.substring(10);
                UserIdentity user = this.login(null, spnegoToken, request);
                if (user != null) {
                    return new UserAuthentication(this.getAuthMethod(), user);
                }
            }
            return Authentication.UNAUTHENTICATED;
        }
    }

    @Override
    public boolean secureResponse(ServletRequest request, ServletResponse response, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
        return true;
    }
}
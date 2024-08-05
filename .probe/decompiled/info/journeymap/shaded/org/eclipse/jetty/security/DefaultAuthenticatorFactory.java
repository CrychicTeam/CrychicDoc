package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.security.authentication.BasicAuthenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.authentication.ClientCertAuthenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.authentication.DigestAuthenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.authentication.FormAuthenticator;
import info.journeymap.shaded.org.eclipse.jetty.security.authentication.SpnegoAuthenticator;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.javax.servlet.ServletContext;

public class DefaultAuthenticatorFactory implements Authenticator.Factory {

    LoginService _loginService;

    @Override
    public Authenticator getAuthenticator(Server server, ServletContext context, Authenticator.AuthConfiguration configuration, IdentityService identityService, LoginService loginService) {
        String auth = configuration.getAuthMethod();
        Authenticator authenticator = null;
        if (auth == null || "BASIC".equalsIgnoreCase(auth)) {
            authenticator = new BasicAuthenticator();
        } else if ("DIGEST".equalsIgnoreCase(auth)) {
            authenticator = new DigestAuthenticator();
        } else if ("FORM".equalsIgnoreCase(auth)) {
            authenticator = new FormAuthenticator();
        } else if ("SPNEGO".equalsIgnoreCase(auth)) {
            authenticator = new SpnegoAuthenticator();
        } else if ("NEGOTIATE".equalsIgnoreCase(auth)) {
            authenticator = new SpnegoAuthenticator("NEGOTIATE");
        }
        if ("CLIENT_CERT".equalsIgnoreCase(auth) || "CLIENT-CERT".equalsIgnoreCase(auth)) {
            authenticator = new ClientCertAuthenticator();
        }
        return authenticator;
    }

    public LoginService getLoginService() {
        return this._loginService;
    }

    public void setLoginService(LoginService loginService) {
        this._loginService = loginService;
    }
}
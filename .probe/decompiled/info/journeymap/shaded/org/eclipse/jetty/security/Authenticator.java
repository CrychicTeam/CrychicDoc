package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import java.util.Set;

public interface Authenticator {

    void setConfiguration(Authenticator.AuthConfiguration var1);

    String getAuthMethod();

    void prepareRequest(ServletRequest var1);

    Authentication validateRequest(ServletRequest var1, ServletResponse var2, boolean var3) throws ServerAuthException;

    boolean secureResponse(ServletRequest var1, ServletResponse var2, boolean var3, Authentication.User var4) throws ServerAuthException;

    public interface AuthConfiguration {

        String getAuthMethod();

        String getRealmName();

        String getInitParameter(String var1);

        Set<String> getInitParameterNames();

        LoginService getLoginService();

        IdentityService getIdentityService();

        boolean isSessionRenewedOnAuthentication();
    }

    public interface Factory {

        Authenticator getAuthenticator(Server var1, ServletContext var2, Authenticator.AuthConfiguration var3, IdentityService var4, LoginService var5);
    }
}
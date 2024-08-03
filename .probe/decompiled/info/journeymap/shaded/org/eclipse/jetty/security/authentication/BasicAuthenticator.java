package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.security.ServerAuthException;
import info.journeymap.shaded.org.eclipse.jetty.security.UserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.B64Code;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BasicAuthenticator extends LoginAuthenticator {

    @Override
    public String getAuthMethod() {
        return "BASIC";
    }

    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String credentials = request.getHeader(HttpHeader.AUTHORIZATION.asString());
        try {
            if (!mandatory) {
                return new DeferredAuthentication(this);
            } else {
                if (credentials != null) {
                    int space = credentials.indexOf(32);
                    if (space > 0) {
                        String method = credentials.substring(0, space);
                        if ("basic".equalsIgnoreCase(method)) {
                            credentials = credentials.substring(space + 1);
                            credentials = B64Code.decode(credentials, StandardCharsets.ISO_8859_1);
                            int i = credentials.indexOf(58);
                            if (i > 0) {
                                String username = credentials.substring(0, i);
                                String password = credentials.substring(i + 1);
                                UserIdentity user = this.login(username, password, request);
                                if (user != null) {
                                    return new UserAuthentication(this.getAuthMethod(), user);
                                }
                            }
                        }
                    }
                }
                if (DeferredAuthentication.isDeferred(response)) {
                    return Authentication.UNAUTHENTICATED;
                } else {
                    response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), "basic realm=\"" + this._loginService.getName() + '"');
                    response.sendError(401);
                    return Authentication.SEND_CONTINUE;
                }
            }
        } catch (IOException var13) {
            throw new ServerAuthException(var13);
        }
    }

    @Override
    public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
        return true;
    }
}
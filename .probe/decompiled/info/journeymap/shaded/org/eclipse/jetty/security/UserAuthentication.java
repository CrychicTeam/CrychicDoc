package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;

public class UserAuthentication extends AbstractUserAuthentication {

    public UserAuthentication(String method, UserIdentity userIdentity) {
        super(method, userIdentity);
    }

    public String toString() {
        return "{User," + this.getAuthMethod() + "," + this._userIdentity + "}";
    }

    @Override
    public void logout() {
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security != null) {
            security.logout(this);
        }
    }
}
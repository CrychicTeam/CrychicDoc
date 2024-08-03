package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import java.security.Principal;
import javax.security.auth.Subject;

public class DefaultUserIdentity implements UserIdentity {

    private final Subject _subject;

    private final Principal _userPrincipal;

    private final String[] _roles;

    public DefaultUserIdentity(Subject subject, Principal userPrincipal, String[] roles) {
        this._subject = subject;
        this._userPrincipal = userPrincipal;
        this._roles = roles;
    }

    @Override
    public Subject getSubject() {
        return this._subject;
    }

    @Override
    public Principal getUserPrincipal() {
        return this._userPrincipal;
    }

    @Override
    public boolean isUserInRole(String role, UserIdentity.Scope scope) {
        if ("*".equals(role)) {
            return false;
        } else {
            String roleToTest = null;
            if (scope != null && scope.getRoleRefMap() != null) {
                roleToTest = (String) scope.getRoleRefMap().get(role);
            }
            if (roleToTest == null) {
                roleToTest = role;
            }
            for (String r : this._roles) {
                if (r.equals(roleToTest)) {
                    return true;
                }
            }
            return false;
        }
    }

    public String toString() {
        return DefaultUserIdentity.class.getSimpleName() + "('" + this._userPrincipal + "')";
    }
}
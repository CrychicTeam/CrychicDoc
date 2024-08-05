package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import java.io.Serializable;
import java.util.Set;

public abstract class AbstractUserAuthentication implements Authentication.User, Serializable {

    private static final long serialVersionUID = -6290411814232723403L;

    protected String _method;

    protected transient UserIdentity _userIdentity;

    public AbstractUserAuthentication(String method, UserIdentity userIdentity) {
        this._method = method;
        this._userIdentity = userIdentity;
    }

    @Override
    public String getAuthMethod() {
        return this._method;
    }

    @Override
    public UserIdentity getUserIdentity() {
        return this._userIdentity;
    }

    @Override
    public boolean isUserInRole(UserIdentity.Scope scope, String role) {
        String roleToTest = null;
        if (scope != null && scope.getRoleRefMap() != null) {
            roleToTest = (String) scope.getRoleRefMap().get(role);
        }
        if (roleToTest == null) {
            roleToTest = role;
        }
        if ("**".equals(roleToTest.trim())) {
            return !this.declaredRolesContains("**") ? true : this._userIdentity.isUserInRole(role, scope);
        } else {
            return this._userIdentity.isUserInRole(role, scope);
        }
    }

    public boolean declaredRolesContains(String roleName) {
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security == null) {
            return false;
        } else if (!(security instanceof ConstraintAware)) {
            return false;
        } else {
            Set<String> declaredRoles = ((ConstraintAware) security).getRoles();
            return declaredRoles != null && declaredRoles.contains(roleName);
        }
    }
}
package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import java.security.Principal;
import javax.security.auth.Subject;

public class DefaultIdentityService implements IdentityService {

    @Override
    public Object associate(UserIdentity user) {
        return null;
    }

    @Override
    public void disassociate(Object previous) {
    }

    @Override
    public Object setRunAs(UserIdentity user, RunAsToken token) {
        return token;
    }

    @Override
    public void unsetRunAs(Object lastToken) {
    }

    @Override
    public RunAsToken newRunAsToken(String runAsName) {
        return new RoleRunAsToken(runAsName);
    }

    @Override
    public UserIdentity getSystemUserIdentity() {
        return null;
    }

    @Override
    public UserIdentity newUserIdentity(Subject subject, Principal userPrincipal, String[] roles) {
        return new DefaultUserIdentity(subject, userPrincipal, roles);
    }
}
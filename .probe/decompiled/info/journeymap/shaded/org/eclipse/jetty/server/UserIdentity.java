package info.journeymap.shaded.org.eclipse.jetty.server;

import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;

public interface UserIdentity {

    UserIdentity UNAUTHENTICATED_IDENTITY = new UserIdentity.UnauthenticatedUserIdentity() {

        @Override
        public Subject getSubject() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role, UserIdentity.Scope scope) {
            return false;
        }

        public String toString() {
            return "UNAUTHENTICATED";
        }
    };

    Subject getSubject();

    Principal getUserPrincipal();

    boolean isUserInRole(String var1, UserIdentity.Scope var2);

    public interface Scope {

        String getContextPath();

        String getName();

        Map<String, String> getRoleRefMap();
    }

    public interface UnauthenticatedUserIdentity extends UserIdentity {
    }
}
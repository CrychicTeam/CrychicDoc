package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;

public interface Authentication {

    Authentication UNAUTHENTICATED = new Authentication() {

        public String toString() {
            return "UNAUTHENTICATED";
        }
    };

    Authentication NOT_CHECKED = new Authentication() {

        public String toString() {
            return "NOT CHECKED";
        }
    };

    Authentication SEND_CONTINUE = new Authentication.Challenge() {

        public String toString() {
            return "CHALLENGE";
        }
    };

    Authentication SEND_FAILURE = new Authentication.Failure() {

        public String toString() {
            return "FAILURE";
        }
    };

    Authentication SEND_SUCCESS = new Authentication.SendSuccess() {

        public String toString() {
            return "SEND_SUCCESS";
        }
    };

    public interface Challenge extends Authentication.ResponseSent {
    }

    public interface Deferred extends Authentication {

        Authentication authenticate(ServletRequest var1);

        Authentication authenticate(ServletRequest var1, ServletResponse var2);

        Authentication login(String var1, Object var2, ServletRequest var3);
    }

    public static class Failed extends QuietServletException {

        public Failed(String message) {
            super(message);
        }
    }

    public interface Failure extends Authentication.ResponseSent {
    }

    public interface ResponseSent extends Authentication {
    }

    public interface SendSuccess extends Authentication.ResponseSent {
    }

    public interface User extends Authentication {

        String getAuthMethod();

        UserIdentity getUserIdentity();

        boolean isUserInRole(UserIdentity.Scope var1, String var2);

        void logout();
    }

    public interface Wrapped extends Authentication {

        HttpServletRequest getHttpServletRequest();

        HttpServletResponse getHttpServletResponse();
    }
}
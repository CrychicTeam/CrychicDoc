package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;

public interface LoginService {

    String getName();

    UserIdentity login(String var1, Object var2, ServletRequest var3);

    boolean validate(UserIdentity var1);

    IdentityService getIdentityService();

    void setIdentityService(IdentityService var1);

    void logout(UserIdentity var1);
}
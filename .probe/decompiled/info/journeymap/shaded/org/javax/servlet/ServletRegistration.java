package info.journeymap.shaded.org.javax.servlet;

import java.util.Collection;
import java.util.Set;

public interface ServletRegistration extends Registration {

    Set<String> addMapping(String... var1);

    Collection<String> getMappings();

    String getRunAsRole();

    public interface Dynamic extends ServletRegistration, Registration.Dynamic {

        void setLoadOnStartup(int var1);

        Set<String> setServletSecurity(ServletSecurityElement var1);

        void setMultipartConfig(MultipartConfigElement var1);

        void setRunAsRole(String var1);
    }
}
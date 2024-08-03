package info.journeymap.shaded.org.javax.servlet;

import java.util.Collection;
import java.util.EnumSet;

public interface FilterRegistration extends Registration {

    void addMappingForServletNames(EnumSet<DispatcherType> var1, boolean var2, String... var3);

    Collection<String> getServletNameMappings();

    void addMappingForUrlPatterns(EnumSet<DispatcherType> var1, boolean var2, String... var3);

    Collection<String> getUrlPatternMappings();

    public interface Dynamic extends FilterRegistration, Registration.Dynamic {
    }
}
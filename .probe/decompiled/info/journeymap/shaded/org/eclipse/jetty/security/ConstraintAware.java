package info.journeymap.shaded.org.eclipse.jetty.security;

import java.util.List;
import java.util.Set;

public interface ConstraintAware {

    List<ConstraintMapping> getConstraintMappings();

    Set<String> getRoles();

    void setConstraintMappings(List<ConstraintMapping> var1, Set<String> var2);

    void addConstraintMapping(ConstraintMapping var1);

    void addRole(String var1);

    void setDenyUncoveredHttpMethods(boolean var1);

    boolean isDenyUncoveredHttpMethods();

    boolean checkPathsWithUncoveredHttpMethods();
}
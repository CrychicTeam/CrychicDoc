package info.journeymap.shaded.org.eclipse.jetty.util.component;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;

@ManagedObject
public interface Destroyable {

    @ManagedOperation(value = "Destroys this component", impact = "ACTION")
    void destroy();
}
package info.journeymap.shaded.org.eclipse.jetty.util.component;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import java.io.IOException;

@ManagedObject("Dumpable Object")
public interface Dumpable {

    @ManagedOperation(value = "Dump the nested Object state as a String", impact = "INFO")
    String dump();

    void dump(Appendable var1, String var2) throws IOException;
}
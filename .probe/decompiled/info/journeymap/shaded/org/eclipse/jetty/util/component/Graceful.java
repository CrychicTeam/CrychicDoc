package info.journeymap.shaded.org.eclipse.jetty.util.component;

import java.util.concurrent.Future;

public interface Graceful {

    Future<Void> shutdown();
}
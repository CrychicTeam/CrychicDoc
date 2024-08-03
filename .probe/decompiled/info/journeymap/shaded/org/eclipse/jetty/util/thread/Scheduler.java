package info.journeymap.shaded.org.eclipse.jetty.util.thread;

import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import java.util.concurrent.TimeUnit;

public interface Scheduler extends LifeCycle {

    Scheduler.Task schedule(Runnable var1, long var2, TimeUnit var4);

    public interface Task {

        boolean cancel();
    }
}
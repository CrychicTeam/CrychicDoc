package info.journeymap.shaded.org.eclipse.jetty.util.component;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import java.util.EventListener;

@ManagedObject("Lifecycle Interface for startable components")
public interface LifeCycle {

    @ManagedOperation(value = "Starts the instance", impact = "ACTION")
    void start() throws Exception;

    @ManagedOperation(value = "Stops the instance", impact = "ACTION")
    void stop() throws Exception;

    boolean isRunning();

    boolean isStarted();

    boolean isStarting();

    boolean isStopping();

    boolean isStopped();

    boolean isFailed();

    void addLifeCycleListener(LifeCycle.Listener var1);

    void removeLifeCycleListener(LifeCycle.Listener var1);

    static void start(Object object) {
        if (object instanceof LifeCycle) {
            try {
                ((LifeCycle) object).start();
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    static void stop(Object object) {
        if (object instanceof LifeCycle) {
            try {
                ((LifeCycle) object).stop();
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public interface Listener extends EventListener {

        void lifeCycleStarting(LifeCycle var1);

        void lifeCycleStarted(LifeCycle var1);

        void lifeCycleFailure(LifeCycle var1, Throwable var2);

        void lifeCycleStopping(LifeCycle var1);

        void lifeCycleStopped(LifeCycle var1);
    }
}
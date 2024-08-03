package fuzs.puzzleslib.api.core.v1.context;

@Deprecated(forRemoval = true)
@FunctionalInterface
public interface ModLifecycleContext {

    void enqueueWork(Runnable var1);
}
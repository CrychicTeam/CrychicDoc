package fuzs.puzzleslib.api.core.v1;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public class DistTypeExecutor {

    @Nullable
    public static <T> T callWhenOn(DistType envType, Supplier<Callable<T>> toRun) {
        if (ModLoaderEnvironment.INSTANCE.isEnvironmentType(envType)) {
            try {
                return (T) ((Callable) toRun.get()).call();
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            return null;
        }
    }

    public static void runWhenOn(DistType envType, Supplier<Runnable> toRun) {
        if (ModLoaderEnvironment.INSTANCE.isEnvironmentType(envType)) {
            ((Runnable) toRun.get()).run();
        }
    }

    @Nullable
    public static <T> T getWhenOn(DistType envType, Supplier<Supplier<T>> toGet) {
        return (T) (ModLoaderEnvironment.INSTANCE.isEnvironmentType(envType) ? ((Supplier) toGet.get()).get() : null);
    }

    public static <T> T getForDistType(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return (T) (switch(ModLoaderEnvironment.INSTANCE.getEnvironmentType()) {
            case CLIENT ->
                ((Supplier) clientTarget.get()).get();
            case SERVER ->
                ((Supplier) serverTarget.get()).get();
        });
    }
}
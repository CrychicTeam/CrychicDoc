package dev.architectury.utils;

import dev.architectury.platform.Platform;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;

public final class EnvExecutor {

    public static void runInEnv(Dist type, Supplier<Runnable> runnableSupplier) {
        runInEnv(Env.fromPlatform(type), runnableSupplier);
    }

    public static void runInEnv(Env type, Supplier<Runnable> runnableSupplier) {
        if (Platform.getEnvironment() == type) {
            ((Runnable) runnableSupplier.get()).run();
        }
    }

    public static <T> Optional<T> getInEnv(Dist type, Supplier<Supplier<T>> runnableSupplier) {
        return getInEnv(Env.fromPlatform(type), runnableSupplier);
    }

    public static <T> Optional<T> getInEnv(Env type, Supplier<Supplier<T>> runnableSupplier) {
        return Platform.getEnvironment() == type ? Optional.ofNullable(((Supplier) runnableSupplier.get()).get()) : Optional.empty();
    }

    public static <T> T getEnvSpecific(Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
        return (T) (Platform.getEnvironment() == Env.CLIENT ? ((Supplier) client.get()).get() : ((Supplier) server.get()).get());
    }

    private EnvExecutor() {
    }
}
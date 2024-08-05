package org.violetmoon.zeta.multiloader;

import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus.Internal;

public enum Env {

    CLIENT, SERVER;

    public static final Env CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (this.isCurrent()) {
            ((Runnable) run.get()).run();
        }
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return (T) (switch(CURRENT) {
            case CLIENT ->
                ((Supplier) clientTarget.get()).get();
            case SERVER ->
                ((Supplier) serverTarget.get()).get();
        });
    }

    @Internal
    public static Env getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? CLIENT : SERVER;
    }
}
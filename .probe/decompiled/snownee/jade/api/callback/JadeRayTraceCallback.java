package snownee.jade.api.callback;

import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;

@FunctionalInterface
public interface JadeRayTraceCallback {

    @Nullable
    Accessor<?> onRayTrace(HitResult var1, @Nullable Accessor<?> var2, @Nullable Accessor<?> var3);
}
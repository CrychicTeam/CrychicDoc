package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public final class ExplosionEvents {

    public static final EventInvoker<ExplosionEvents.Start> START = EventInvoker.lookup(ExplosionEvents.Start.class);

    public static final EventInvoker<ExplosionEvents.Detonate> DETONATE = EventInvoker.lookup(ExplosionEvents.Detonate.class);

    private ExplosionEvents() {
    }

    @FunctionalInterface
    public interface Detonate {

        void onExplosionDetonate(Level var1, Explosion var2, List<BlockPos> var3, List<Entity> var4);
    }

    @FunctionalInterface
    public interface Start {

        EventResult onExplosionStart(Level var1, Explosion var2);
    }
}
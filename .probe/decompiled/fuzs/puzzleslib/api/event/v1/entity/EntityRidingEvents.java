package fuzs.puzzleslib.api.event.v1.entity;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class EntityRidingEvents {

    public static final EventInvoker<EntityRidingEvents.Start> START = EventInvoker.lookup(EntityRidingEvents.Start.class);

    public static final EventInvoker<EntityRidingEvents.Stop> STOP = EventInvoker.lookup(EntityRidingEvents.Stop.class);

    private EntityRidingEvents() {
    }

    @FunctionalInterface
    public interface Start {

        EventResult onStartRiding(Level var1, Entity var2, Entity var3);
    }

    @FunctionalInterface
    public interface Stop {

        EventResult onStopRiding(Level var1, Entity var2, Entity var3);
    }
}
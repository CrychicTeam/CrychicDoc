package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public interface ExplosionEvent {

    Event<ExplosionEvent.Pre> PRE = EventFactory.createEventResult();

    Event<ExplosionEvent.Detonate> DETONATE = EventFactory.createLoop();

    public interface Detonate {

        void explode(Level var1, Explosion var2, List<Entity> var3);
    }

    public interface Pre {

        EventResult explode(Level var1, Explosion var2);
    }
}
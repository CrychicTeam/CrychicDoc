package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface LightningEvent {

    Event<LightningEvent.Strike> STRIKE = EventFactory.createLoop();

    public interface Strike {

        void onStrike(LightningBolt var1, Level var2, Vec3 var3, List<Entity> var4);
    }
}
package net.minecraftforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class EntityMobGriefingEvent extends EntityEvent {

    public EntityMobGriefingEvent(Entity entity) {
        super(entity);
    }
}
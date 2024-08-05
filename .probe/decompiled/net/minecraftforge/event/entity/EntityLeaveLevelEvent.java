package net.minecraftforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityLeaveLevelEvent extends EntityEvent {

    private final Level level;

    public EntityLeaveLevelEvent(Entity entity, Level level) {
        super(entity);
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
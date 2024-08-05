package dev.latvian.mods.kubejs.entity;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@Info("Invoked when an entity is about to be added to the world.\n\nThis event also fires for existing entities when they are loaded from a save.\n")
public class EntitySpawnedEventJS extends EntityEventJS {

    private final Entity entity;

    private final Level level;

    public EntitySpawnedEventJS(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }

    @Info("The level the entity is being added to.")
    @Override
    public Level getLevel() {
        return this.level;
    }

    @Info("The entity being added to the world.")
    @Override
    public Entity getEntity() {
        return this.entity;
    }
}
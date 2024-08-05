package net.minecraftforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class EntityStruckByLightningEvent extends EntityEvent {

    private final LightningBolt lightning;

    public EntityStruckByLightningEvent(Entity entity, LightningBolt lightning) {
        super(entity);
        this.lightning = lightning;
    }

    public LightningBolt getLightning() {
        return this.lightning;
    }
}
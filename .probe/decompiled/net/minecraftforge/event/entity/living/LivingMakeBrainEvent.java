package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.BrainBuilder;

public class LivingMakeBrainEvent extends LivingEvent {

    private final BrainBuilder<?> brainBuilder;

    public LivingMakeBrainEvent(LivingEntity entity, BrainBuilder<?> brainBuilder) {
        super(entity);
        this.brainBuilder = brainBuilder;
    }

    public <E extends LivingEntity> BrainBuilder<E> getTypedBrainBuilder(E ignoredEntity) {
        return (BrainBuilder<E>) this.brainBuilder;
    }
}
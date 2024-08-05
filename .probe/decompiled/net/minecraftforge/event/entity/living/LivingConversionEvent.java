package net.minecraftforge.event.entity.living;

import java.util.function.Consumer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

public class LivingConversionEvent extends LivingEvent {

    public LivingConversionEvent(LivingEntity entity) {
        super(entity);
    }

    public static class Post extends LivingConversionEvent {

        private final LivingEntity outcome;

        public Post(LivingEntity entity, LivingEntity outcome) {
            super(entity);
            this.outcome = outcome;
        }

        public LivingEntity getOutcome() {
            return this.outcome;
        }
    }

    @Cancelable
    public static class Pre extends LivingConversionEvent {

        private final EntityType<? extends LivingEntity> outcome;

        private final Consumer<Integer> timer;

        public Pre(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
            super(entity);
            this.outcome = outcome;
            this.timer = timer;
        }

        public EntityType<? extends LivingEntity> getOutcome() {
            return this.outcome;
        }

        public void setConversionTimer(int ticks) {
            this.timer.accept(ticks);
        }
    }
}
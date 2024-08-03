package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.Nullable;

public class LivingEvent extends EntityEvent {

    private final LivingEntity livingEntity;

    public LivingEvent(LivingEntity entity) {
        super(entity);
        this.livingEntity = entity;
    }

    public LivingEntity getEntity() {
        return this.livingEntity;
    }

    public static class LivingJumpEvent extends LivingEvent {

        public LivingJumpEvent(LivingEntity e) {
            super(e);
        }
    }

    @Cancelable
    public static class LivingTickEvent extends LivingEvent {

        public LivingTickEvent(LivingEntity e) {
            super(e);
        }
    }

    public static class LivingVisibilityEvent extends LivingEvent {

        private double visibilityModifier;

        @Nullable
        private final Entity lookingEntity;

        public LivingVisibilityEvent(LivingEntity livingEntity, @Nullable Entity lookingEntity, double originalMultiplier) {
            super(livingEntity);
            this.visibilityModifier = originalMultiplier;
            this.lookingEntity = lookingEntity;
        }

        public void modifyVisibility(double mod) {
            this.visibilityModifier *= mod;
        }

        public double getVisibilityModifier() {
            return this.visibilityModifier;
        }

        @Nullable
        public Entity getLookingEntity() {
            return this.lookingEntity;
        }
    }
}
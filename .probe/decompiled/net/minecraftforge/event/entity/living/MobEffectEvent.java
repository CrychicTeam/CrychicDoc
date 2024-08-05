package net.minecraftforge.event.entity.living;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobEffectEvent extends LivingEvent {

    @Nullable
    protected final MobEffectInstance effectInstance;

    public MobEffectEvent(LivingEntity living, MobEffectInstance effectInstance) {
        super(living);
        this.effectInstance = effectInstance;
    }

    @Nullable
    public MobEffectInstance getEffectInstance() {
        return this.effectInstance;
    }

    public static class Added extends MobEffectEvent {

        private final MobEffectInstance oldEffectInstance;

        private final Entity source;

        public Added(LivingEntity living, MobEffectInstance oldEffectInstance, MobEffectInstance newEffectInstance, Entity source) {
            super(living, newEffectInstance);
            this.oldEffectInstance = oldEffectInstance;
            this.source = source;
        }

        @NotNull
        @Override
        public MobEffectInstance getEffectInstance() {
            return super.getEffectInstance();
        }

        @Nullable
        public MobEffectInstance getOldEffectInstance() {
            return this.oldEffectInstance;
        }

        @Nullable
        public Entity getEffectSource() {
            return this.source;
        }
    }

    @HasResult
    public static class Applicable extends MobEffectEvent {

        public Applicable(LivingEntity living, @NotNull MobEffectInstance effectInstance) {
            super(living, effectInstance);
        }

        @NotNull
        @Override
        public MobEffectInstance getEffectInstance() {
            return super.getEffectInstance();
        }
    }

    public static class Expired extends MobEffectEvent {

        public Expired(LivingEntity living, MobEffectInstance effectInstance) {
            super(living, effectInstance);
        }
    }

    @Cancelable
    public static class Remove extends MobEffectEvent {

        private final MobEffect effect;

        public Remove(LivingEntity living, MobEffect effect) {
            super(living, living.getEffect(effect));
            this.effect = effect;
        }

        public Remove(LivingEntity living, MobEffectInstance effectInstance) {
            super(living, effectInstance);
            this.effect = effectInstance.getEffect();
        }

        public MobEffect getEffect() {
            return this.effect;
        }

        @Nullable
        @Override
        public MobEffectInstance getEffectInstance() {
            return super.getEffectInstance();
        }
    }
}
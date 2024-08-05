package net.minecraftforge.event.entity.living;

import java.util.Collection;
import java.util.Collections;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class PotionColorCalculationEvent extends LivingEvent {

    private int color;

    private boolean hideParticle;

    private final Collection<MobEffectInstance> effectList;

    public PotionColorCalculationEvent(LivingEntity entity, int color, boolean hideParticle, Collection<MobEffectInstance> effectList) {
        super(entity);
        this.color = color;
        this.effectList = effectList;
        this.hideParticle = hideParticle;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean areParticlesHidden() {
        return this.hideParticle;
    }

    public void shouldHideParticles(boolean hideParticle) {
        this.hideParticle = hideParticle;
    }

    public Collection<MobEffectInstance> getEffects() {
        return Collections.unmodifiableCollection(this.effectList);
    }
}
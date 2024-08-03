package dev.latvian.mods.kubejs.entity;

import java.util.Collection;
import java.util.Map;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityPotionEffectsJS {

    private final LivingEntity entity;

    public EntityPotionEffectsJS(LivingEntity e) {
        this.entity = e;
    }

    public void clear() {
        this.entity.removeAllEffects();
    }

    public Collection<MobEffectInstance> getActive() {
        return this.entity.getActiveEffects();
    }

    public Map<MobEffect, MobEffectInstance> getMap() {
        return this.entity.getActiveEffectsMap();
    }

    public boolean isActive(MobEffect mobEffect) {
        return mobEffect != null && this.entity.hasEffect(mobEffect);
    }

    public int getDuration(MobEffect mobEffect) {
        if (mobEffect != null) {
            MobEffectInstance i = (MobEffectInstance) this.entity.getActiveEffectsMap().get(mobEffect);
            return i == null ? 0 : i.getDuration();
        } else {
            return 0;
        }
    }

    @Nullable
    public MobEffectInstance getActive(MobEffect mobEffect) {
        return mobEffect == null ? null : this.entity.getEffect(mobEffect);
    }

    public void add(MobEffect mobEffect) {
        this.add(mobEffect, 200);
    }

    public void add(MobEffect mobEffect, int duration) {
        this.add(mobEffect, duration, 0);
    }

    public void add(MobEffect mobEffect, int duration, int amplifier) {
        this.add(mobEffect, duration, amplifier, false, true);
    }

    public void add(MobEffect mobEffect, int duration, int amplifier, boolean ambient, boolean showParticles) {
        if (mobEffect != null) {
            this.entity.addEffect(new MobEffectInstance(mobEffect, duration, amplifier, ambient, showParticles));
        }
    }

    public boolean isApplicable(MobEffectInstance effect) {
        return this.entity.canBeAffected(effect);
    }
}
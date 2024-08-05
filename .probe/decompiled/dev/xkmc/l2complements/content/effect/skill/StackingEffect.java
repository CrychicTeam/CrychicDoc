package dev.xkmc.l2complements.content.effect.skill;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface StackingEffect<T extends InherentEffect> {

    default void addTo(LivingEntity target, int dur, int max, EffectUtil.AddReason reason, @Nullable Entity source) {
        MobEffectInstance old = target.getEffect(this.getThis());
        MobEffectInstance ins;
        if (old == null) {
            ins = new MobEffectInstance(this.getThis(), dur);
        } else {
            ins = new MobEffectInstance(this.getThis(), Math.max(dur, old.getDuration()), Math.min(max, old.getAmplifier() + 1));
        }
        EffectUtil.addEffect(target, ins, reason, source);
    }

    default T getThis() {
        return (T) Wrappers.cast(this);
    }
}
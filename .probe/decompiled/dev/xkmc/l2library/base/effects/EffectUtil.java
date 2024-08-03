package dev.xkmc.l2library.base.effects;

import dev.xkmc.l2library.base.effects.api.ForceEffect;
import java.util.Iterator;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class EffectUtil {

    private static final ThreadLocal<EffectUtil.AddReason> REASON = new ThreadLocal();

    private static void forceAddEffect(LivingEntity e, MobEffectInstance ins, @Nullable Entity source) {
        MobEffectInstance effectinstance = (MobEffectInstance) e.getActiveEffectsMap().get(ins.getEffect());
        ForceAddEffectEvent event = new ForceAddEffectEvent(e, ins);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != Result.DENY) {
            MinecraftForge.EVENT_BUS.post(new MobEffectEvent.Added(e, effectinstance, ins, source));
            if (effectinstance == null) {
                e.getActiveEffectsMap().put(ins.getEffect(), ins);
                e.onEffectAdded(ins, source);
            } else if (effectinstance.update(ins)) {
                e.onEffectUpdated(effectinstance, true, source);
            }
        }
    }

    public static void addEffect(LivingEntity entity, MobEffectInstance ins, EffectUtil.AddReason reason, @Nullable Entity source) {
        if (entity == source) {
            reason = EffectUtil.AddReason.SELF;
        }
        if (ins.getEffect() instanceof ForceEffect) {
            reason = EffectUtil.AddReason.FORCE;
        }
        ins = new MobEffectInstance(ins.getEffect(), ins.getDuration(), ins.getAmplifier(), ins.isAmbient(), reason != EffectUtil.AddReason.FORCE && ins.isVisible(), ins.showIcon());
        REASON.set(reason);
        if (ins.getEffect() instanceof ForceEffect) {
            forceAddEffect(entity, ins, source);
        } else if (ins.getEffect().isInstantenous()) {
            ins.getEffect().applyInstantenousEffect(null, null, entity, ins.getAmplifier(), 1.0);
        } else {
            entity.addEffect(ins, source);
        }
        REASON.set(EffectUtil.AddReason.NONE);
    }

    public static void refreshEffect(LivingEntity entity, MobEffectInstance ins, EffectUtil.AddReason reason, Entity source) {
        if (ins.duration < 40) {
            ins.duration = 40;
        }
        MobEffectInstance cur = entity.getEffect(ins.getEffect());
        if (cur == null || cur.getAmplifier() < ins.getAmplifier() || cur.getAmplifier() == ins.getAmplifier() && cur.getDuration() < ins.getDuration() / 2) {
            addEffect(entity, ins, reason, source);
        }
    }

    public static void removeEffect(LivingEntity entity, Predicate<MobEffectInstance> pred) {
        Iterator<MobEffectInstance> itr = entity.activeEffects.values().iterator();
        while (itr.hasNext()) {
            MobEffectInstance effect = (MobEffectInstance) itr.next();
            if (pred.test(effect) && !MinecraftForge.EVENT_BUS.post(new MobEffectEvent.Remove(entity, effect))) {
                entity.onEffectRemoved(effect);
                itr.remove();
                entity.effectsDirty = true;
            }
        }
    }

    public static EffectUtil.AddReason getReason() {
        EffectUtil.AddReason ans = (EffectUtil.AddReason) REASON.get();
        return ans == null ? EffectUtil.AddReason.NONE : ans;
    }

    public static enum AddReason {

        NONE, PROF, FORCE, SKILL, SELF
    }
}
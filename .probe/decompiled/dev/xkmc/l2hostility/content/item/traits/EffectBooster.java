package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.effects.EffectBuilder;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectBooster {

    public static void boostCharge(LivingEntity target) {
        double factor = 1.0 + LHConfig.COMMON.drainDuration.get();
        int maxTime = LHConfig.COMMON.drainDurationMax.get();
        int min = LHConfig.COMMON.witchChargeMinDuration.get();
        boost(target, e -> e.getCategory() == MobEffectCategory.HARMFUL, min, factor, maxTime);
    }

    public static void boostBottle(LivingEntity target) {
        double factor = 1.0 + LHConfig.COMMON.drainDuration.get();
        int maxTime = LHConfig.COMMON.drainDurationMax.get();
        int min = LHConfig.COMMON.witchChargeMinDuration.get();
        boost(target, e -> true, min, factor, maxTime);
    }

    public static void boostTrait(LivingEntity target, double factor, int maxTime) {
        boost(target, e -> e.getCategory() == MobEffectCategory.HARMFUL, 0, factor, maxTime);
    }

    private static void boost(LivingEntity target, Predicate<MobEffect> pred, int min, double factor, int maxTime) {
        for (MobEffectInstance e : new ArrayList(target.getActiveEffects())) {
            if (pred.test(e.getEffect())) {
                int current = e.getDuration();
                if (current >= min) {
                    int max = Math.min(maxTime, (int) ((double) current * factor));
                    if (max > current) {
                        new EffectBuilder(e).setDuration(max);
                    }
                    target.forceAddEffect(e, null);
                }
            }
        }
    }

    public static void boostInfinite(LivingEntity target) {
        int min = LHConfig.COMMON.witchChargeMinDuration.get();
        for (MobEffectInstance e : new ArrayList(target.getActiveEffects())) {
            if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                int current = e.getDuration();
                if (current >= min) {
                    new EffectBuilder(e).setDuration(-1);
                    target.forceAddEffect(e, null);
                }
            }
        }
    }
}
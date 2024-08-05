package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BlightEffect extends MagicMobEffect {

    public static final float DAMAGE_PER_LEVEL = -0.05F;

    public static final float HEALING_PER_LEVEL = -0.1F;

    public BlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void reduceHealing(LivingHealEvent event) {
        MobEffectInstance effect = event.getEntity().getEffect(MobEffectRegistry.BLIGHT.get());
        if (effect != null) {
            int lvl = effect.getAmplifier() + 1;
            float healingMult = 1.0F + -0.1F * (float) lvl;
            float before = event.getAmount();
            event.setAmount(event.getAmount() * healingMult);
        }
    }

    @SubscribeEvent
    public static void reduceDamageOutput(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingAttacker) {
            MobEffectInstance effect = livingAttacker.getEffect(MobEffectRegistry.BLIGHT.get());
            if (effect != null) {
                int lvl = effect.getAmplifier() + 1;
                float before = event.getAmount();
                float multiplier = 1.0F + -0.05F * (float) lvl;
                event.setAmount(event.getAmount() * multiplier);
            }
        }
    }
}
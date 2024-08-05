package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class OakskinEffect extends CustomDescriptionMobEffect {

    public static final float REDUCTION_PER_LEVEL = 0.05F;

    public static final float BASE_REDUCTION = 0.1F;

    public OakskinEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        float reductionAmount = getReductionAmount(amp);
        return Component.translatable("tooltip.irons_spellbooks.oakskin_description", (int) (reductionAmount * 100.0F)).withStyle(ChatFormatting.BLUE);
    }

    @SubscribeEvent
    public static void reduceDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(MobEffectRegistry.OAKSKIN.get());
        if (effect != null) {
            int lvl = effect.getAmplifier() + 1;
            float before = event.getAmount();
            float multiplier = 1.0F - getReductionAmount(lvl);
            event.setAmount(event.getAmount() * multiplier);
        }
    }

    public static float getReductionAmount(int level) {
        return 0.1F + 0.05F * (float) level;
    }
}
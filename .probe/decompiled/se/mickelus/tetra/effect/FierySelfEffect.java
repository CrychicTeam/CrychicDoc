package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

@ParametersAreNonnullByDefault
public class FierySelfEffect {

    public static void perform(LivingEntity entity, ItemStack itemStack, double multiplier) {
        if (!entity.m_9236_().isClientSide) {
            double fierySelfEfficiency = (double) EffectHelper.getEffectEfficiency(itemStack, ItemEffect.fierySelf);
            if (fierySelfEfficiency > 0.0) {
                BlockPos pos = entity.m_20183_();
                float temperature = ((Biome) entity.m_9236_().m_204166_(pos).value()).getTemperature(pos);
                if (entity.getRandom().nextDouble() < fierySelfEfficiency * (double) temperature * multiplier) {
                    entity.m_20254_(EffectHelper.getEffectLevel(itemStack, ItemEffect.fierySelf));
                }
            }
        }
    }
}
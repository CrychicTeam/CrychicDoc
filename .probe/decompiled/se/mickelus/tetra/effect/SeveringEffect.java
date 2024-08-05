package se.mickelus.tetra.effect;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import se.mickelus.tetra.effect.potion.SeveredPotionEffect;

@ParametersAreNonnullByDefault
public class SeveringEffect {

    public static void perform(ItemStack itemStack, int effectLevel, LivingEntity attacker, LivingEntity target) {
        if (attacker.getRandom().nextFloat() < (float) effectLevel / 100.0F) {
            int stackCap = (int) EffectHelper.getEffectEfficiency(itemStack, ItemEffect.severing) - 1;
            int currentAmplifier = (Integer) Optional.ofNullable(target.getEffect(SeveredPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
            target.addEffect(new MobEffectInstance(SeveredPotionEffect.instance, 1200, Math.min(currentAmplifier + 1, stackCap), false, false));
            if (!target.m_9236_().isClientSide) {
                RandomSource rand = target.getRandom();
                target.m_20193_().playSound(null, target.m_20185_(), target.m_20186_(), target.m_20189_(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.8F, 0.9F);
                ((ServerLevel) target.m_20193_()).sendParticles(new DustParticleOptions(new Vector3f(0.5F, 0.0F, 0.0F), 0.5F), target.m_20185_() + (double) target.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), target.m_20186_() + (double) target.m_20206_() * (0.2 + rand.nextGaussian() * 0.4), target.m_20189_() + (double) target.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), 20, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }
}
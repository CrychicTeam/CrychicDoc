package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.potion.StunPotionEffect;

@ParametersAreNonnullByDefault
public class StunEffect {

    public static void perform(ItemStack itemStack, int effectLevel, LivingEntity attacker, LivingEntity target) {
        if (!attacker.m_9236_().isClientSide && attacker.getRandom().nextFloat() < (float) effectLevel / 100.0F) {
            int duration = (int) (EffectHelper.getEffectEfficiency(itemStack, ItemEffect.stun) * 20.0F);
            target.addEffect(new MobEffectInstance(StunPotionEffect.instance, duration, 0, false, false));
            target.m_20193_().playSound(null, target.m_20185_(), target.m_20186_(), target.m_20189_(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.8F, 0.9F);
            ((ServerLevel) target.m_20193_()).sendParticles(ParticleTypes.ENTITY_EFFECT, target.m_20185_(), target.m_20188_(), target.m_20189_(), 5, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
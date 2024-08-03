package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.effect.potion.ExhaustedPotionEffect;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class OverpowerEffect extends ChargedAbilityEffect {

    public static final OverpowerEffect instance = new OverpowerEffect();

    private static final Cache<Integer, OverpowerEffect.DelayData> delayCache = CacheBuilder.newBuilder().maximumSize(20L).expireAfterWrite(1L, TimeUnit.MINUTES).build();

    OverpowerEffect() {
        super(10, 1.0, 10, 1.0, ItemEffect.overpower, ChargedAbilityEffect.TargetRequirement.none, UseAnim.SPEAR, "raised");
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, @Nullable LivingEntity target, @Nullable BlockPos targetPos, @Nullable Vec3 hitVec, int chargedTicks) {
        super.perform(attacker, hand, item, itemStack, target, targetPos, hitVec, chargedTicks);
        boolean isDefensive = this.isDefensive(item, itemStack, hand);
        int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        boolean overextended = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend) > 0;
        double exhaustDuration = (double) item.getEffectEfficiency(itemStack, ItemEffect.overpower);
        if (!attacker.m_9236_().isClientSide && !isDefensive) {
            int currentAmp = (Integer) Optional.ofNullable(attacker.m_21124_(ExhaustedPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
            int newAmp = 1;
            if (overchargeBonus > 0) {
                newAmp += (int) ((float) overchargeBonus * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge));
            }
            double comboEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityCombo);
            if (comboEfficiency > 0.0 && (double) attacker.m_20193_().getRandom().nextFloat() < comboEfficiency * (double) ComboPoints.get(attacker) / 100.0) {
                newAmp--;
                RandomSource rand = attacker.m_20193_().getRandom();
                ((ServerLevel) attacker.m_20193_()).sendParticles(ParticleTypes.HAPPY_VILLAGER, attacker.m_20185_(), attacker.m_20186_() + (double) (attacker.m_20206_() / 2.0F), attacker.m_20189_(), 10, rand.nextGaussian() * 0.3, rand.nextGaussian() * (double) attacker.m_20206_() * 0.8, rand.nextGaussian() * 0.3, 0.1F);
            }
            if (target != null && revengeLevel > 0 && RevengeTracker.canRevenge(attacker, target)) {
                newAmp--;
            }
            if (overextended && !attacker.getFoodData().needsFood()) {
                newAmp = -1;
            }
            if (newAmp > 0) {
                int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
                if (echoLevel > 0) {
                    this.delayExhaustion(attacker, item, itemStack, (int) (exhaustDuration * 20.0), newAmp);
                } else {
                    attacker.m_7292_(new MobEffectInstance(ExhaustedPotionEffect.instance, (int) (exhaustDuration * 20.0), newAmp + currentAmp, false, true));
                }
            }
        }
        attacker.causeFoodExhaustion(overextended ? 6.0F : 1.0F);
        attacker.m_21011_(hand, false);
        int cooldown = this.getCooldown(item, itemStack);
        if (isDefensive) {
            cooldown = (int) ((float) cooldown * (1.0F + item.getEffectEfficiency(itemStack, ItemEffect.abilityDefensive) / 100.0F));
        }
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
        if (target != null && revengeLevel > 0) {
            RevengeTracker.removeEnemy(attacker, target);
        }
        attacker.getCooldowns().addCooldown(item, cooldown);
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        boolean isDefensive = this.isDefensive(item, itemStack, hand);
        int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        double damageMultiplier = (double) ((float) item.getEffectLevel(itemStack, isDefensive ? ItemEffect.abilityDefensive : ItemEffect.overpower) / 100.0F);
        double efficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.overpower);
        if (overchargeBonus > 0) {
            damageMultiplier += (double) (overchargeBonus * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
        }
        int comboLevel = item.getEffectLevel(itemStack, ItemEffect.abilityCombo);
        if (comboLevel > 0) {
            damageMultiplier += (double) (comboLevel * ComboPoints.get(attacker)) / 100.0;
        }
        if (revengeLevel > 0 && RevengeTracker.canRevenge(attacker, target)) {
            damageMultiplier += (double) revengeLevel / 100.0;
        }
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, 0.1F, 0.1F);
        if (result != AbilityUseResult.fail) {
            int currentAmplifier = (Integer) Optional.ofNullable(target.getEffect(ExhaustedPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
            int amplifier = currentAmplifier + 2;
            if (isDefensive) {
                amplifier--;
            }
            if (overchargeBonus > 0) {
                amplifier += (int) ((float) overchargeBonus * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge));
            }
            int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
            if (momentumLevel > 0 && currentAmplifier > -1) {
                double momentumEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum);
                double velocity = (double) momentumLevel / 100.0;
                velocity += momentumEfficiency * (double) (currentAmplifier + 1);
                velocity += momentumEfficiency * (double) ((Integer) Optional.ofNullable(attacker.m_21124_(ExhaustedPotionEffect.instance)).map(MobEffectInstance::m_19564_).map(amp -> amp + 1).orElse(0)).intValue();
                velocity *= 1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                if (velocity > 0.0) {
                    target.m_5997_(0.0, velocity, 0.0);
                }
            }
            int exhilarationLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarationLevel > 0 && !target.isAlive()) {
                ServerScheduler.schedule(0, () -> attacker.m_21195_(ExhaustedPotionEffect.instance));
            }
            target.addEffect(new MobEffectInstance(ExhaustedPotionEffect.instance, (int) (efficiency * 20.0), amplifier, false, true));
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 0.8F);
        } else {
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.8F);
        }
        item.tickProgression(attacker, itemStack, result == AbilityUseResult.fail ? 1 : 2);
        item.applyDamage(2, itemStack, attacker);
    }

    private void delayExhaustion(Player attacker, ItemModularHandheld item, ItemStack itemStack, int duration, int amplifier) {
        int delay = this.getChargeTime(item, itemStack) + this.getCooldown(item, itemStack) + item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
        try {
            OverpowerEffect.DelayData data = (OverpowerEffect.DelayData) delayCache.get(attacker.m_19879_(), OverpowerEffect.DelayData::new);
            data.timestamp = attacker.m_9236_().getGameTime() + (long) delay;
            data.amplifier += amplifier;
        } catch (ExecutionException var8) {
            var8.printStackTrace();
        }
        ServerScheduler.schedule(delay + 1, () -> {
            OverpowerEffect.DelayData data = (OverpowerEffect.DelayData) delayCache.getIfPresent(attacker.m_19879_());
            if (attacker.m_6084_() && attacker.m_9236_() != null && data != null && attacker.m_9236_().getGameTime() > data.timestamp) {
                int currentAmp = (Integer) Optional.ofNullable(attacker.m_21124_(ExhaustedPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
                attacker.m_7292_(new MobEffectInstance(ExhaustedPotionEffect.instance, duration, currentAmp + data.amplifier, false, true));
                delayCache.invalidate(attacker.m_19879_());
            }
        });
    }

    static class DelayData {

        int amplifier;

        long timestamp;
    }
}
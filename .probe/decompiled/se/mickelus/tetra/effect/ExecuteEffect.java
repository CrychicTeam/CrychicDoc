package se.mickelus.tetra.effect;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.potion.SeveredPotionEffect;
import se.mickelus.tetra.effect.potion.SmallStrengthPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class ExecuteEffect extends ChargedAbilityEffect {

    public static final ExecuteEffect instance = new ExecuteEffect();

    ExecuteEffect() {
        super(20, 0.5, 40, 8.0, ItemEffect.execute, ChargedAbilityEffect.TargetRequirement.entity, UseAnim.SPEAR, "raised");
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        if (!target.m_9236_().isClientSide) {
            AbilityUseResult result;
            if (this.isDefensive(item, itemStack, hand)) {
                result = this.defensiveExecute(attacker, item, itemStack, target);
            } else {
                result = this.regularExecute(attacker, item, itemStack, target, chargedTicks);
            }
            this.playEffects(result != AbilityUseResult.fail, target, hitVec);
            item.tickProgression(attacker, itemStack, result == AbilityUseResult.fail ? 1 : 2);
        }
        attacker.causeFoodExhaustion(1.0F);
        attacker.m_21011_(hand, false);
        attacker.getCooldowns().addCooldown(item, this.getCooldown(item, itemStack));
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
        item.applyDamage(2, itemStack, attacker);
    }

    private AbilityUseResult regularExecute(Player attacker, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, int chargedTicks) {
        long harmfulCount = (long) target.getActiveEffects().stream().filter(effect -> effect.getEffect().getCategory() == MobEffectCategory.HARMFUL).mapToInt(MobEffectInstance::m_19564_).map(amp -> amp + 1).sum();
        if (target.m_6060_()) {
            harmfulCount++;
        }
        if (target.m_203117_()) {
            harmfulCount++;
        }
        float missingHealth = Mth.clamp(1.0F - target.getHealth() / target.getMaxHealth(), 0.0F, 1.0F);
        double efficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.execute);
        double damageMultiplier = (double) missingHealth + (double) harmfulCount * efficiency / 100.0;
        double comboLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityCombo);
        if (comboLevel > 0.0) {
            damageMultiplier *= 1.0 + comboLevel * (double) ComboPoints.get(attacker) / 100.0;
        }
        damageMultiplier++;
        if (this.canOvercharge(item, itemStack)) {
            damageMultiplier *= 1.0 + (double) (this.getOverchargeBonus(item, itemStack, chargedTicks) * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
        }
        double revengeMultiplier = this.getRevengeMultiplier(attacker, item, itemStack);
        if (revengeMultiplier > 0.0) {
            damageMultiplier *= revengeMultiplier;
        }
        int overextendLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
        if (overextendLevel > 0) {
            FoodData foodStats = attacker.getFoodData();
            float exhaustion = Math.min(40.0F, (float) foodStats.getFoodLevel() + foodStats.getSaturationLevel());
            damageMultiplier *= 1.0 + (double) ((float) overextendLevel * exhaustion) * 0.25 / 100.0;
            attacker.causeFoodExhaustion(exhaustion);
        }
        int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
        if (echoLevel > 0) {
            this.echoExecute(attacker, item, itemStack, target);
        }
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, 0.2F, 0.2F);
        if (result != AbilityUseResult.fail) {
            int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
            if (momentumLevel > 0) {
                int duration = (int) ((double) momentumLevel * damageMultiplier * 20.0);
                target.addEffect(new MobEffectInstance(StunPotionEffect.instance, duration, 0, false, false));
            }
            int exhilarationLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarationLevel > 0 && !target.isAlive()) {
                float maxHealth = target.getMaxHealth();
                int amplifier = Math.round((1.0F - missingHealth) / (float) exhilarationLevel * 100.0F) - 1;
                int duration = (int) (Math.min(200.0F, item.getEffectEfficiency(itemStack, ItemEffect.abilityExhilaration) * maxHealth) * 20.0F);
                if (amplifier >= 0 && duration > 0) {
                    attacker.m_7292_(new MobEffectInstance(SmallStrengthPotionEffect.instance, duration, amplifier, false, true));
                }
            }
        }
        return result;
    }

    private void echoExecute(Player attacker, ItemModularHandheld item, ItemStack itemStack, LivingEntity target) {
        EchoHelper.echo(attacker, 100, () -> {
            long harmfulCount = (long) target.getActiveEffects().stream().filter(effect -> effect.getEffect().getCategory() == MobEffectCategory.HARMFUL).mapToInt(MobEffectInstance::m_19564_).map(amp -> amp + 1).sum();
            if (target.m_6060_()) {
                harmfulCount++;
            }
            if (target.m_203117_()) {
                harmfulCount++;
            }
            float missingHealth = Mth.clamp(1.0F - target.getHealth() / target.getMaxHealth(), 0.0F, 1.0F);
            double damageMultiplier = (double) (missingHealth + (float) harmfulCount);
            if (damageMultiplier > 0.0) {
                AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, 0.2F, 0.2F);
                this.playEffects(result != AbilityUseResult.fail, target, target.m_20182_().add(0.0, (double) (target.m_20206_() / 2.0F), 0.0));
            }
        });
    }

    private AbilityUseResult defensiveExecute(Player attacker, ItemModularHandheld item, ItemStack itemStack, LivingEntity target) {
        boolean targetFullHealth = target.getMaxHealth() == target.getHealth();
        double damageMultiplier = (double) ((float) item.getEffectLevel(itemStack, ItemEffect.abilityDefensive) / 100.0F);
        if (targetFullHealth) {
            damageMultiplier += (double) (item.getEffectEfficiency(itemStack, ItemEffect.abilityDefensive) / 100.0F);
        }
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, damageMultiplier, 0.2F, 0.2F);
        if (result != AbilityUseResult.fail) {
            int amp = (Integer) Optional.ofNullable(target.getEffect(SeveredPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
            amp += targetFullHealth ? 2 : 1;
            amp = Math.min(amp, 2);
            target.addEffect(new MobEffectInstance(SeveredPotionEffect.instance, 1200, amp, false, false));
        }
        return result;
    }

    private double getRevengeMultiplier(Player player, ItemModularHandheld item, ItemStack itemStack) {
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        return revengeLevel <= 0 || !player.m_21220_().stream().anyMatch(effect -> effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) && !player.m_6060_() && !player.m_203117_() ? 0.0 : 1.0 + (double) revengeLevel / 100.0;
    }

    private void playEffects(boolean isSuccess, LivingEntity target, Vec3 hitVec) {
        if (isSuccess) {
            target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 0.8F);
            RandomSource rand = target.getRandom();
            CastOptional.cast(target.m_9236_(), ServerLevel.class).ifPresent(world -> world.sendParticles(new DustParticleOptions(new Vector3f(0.6F, 0.0F, 0.0F), 0.8F), hitVec.x, hitVec.y, hitVec.z, 10, rand.nextGaussian() * 0.3, rand.nextGaussian() * 0.3, rand.nextGaussian() * 0.3, 0.1F));
        } else {
            target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.8F);
        }
    }
}
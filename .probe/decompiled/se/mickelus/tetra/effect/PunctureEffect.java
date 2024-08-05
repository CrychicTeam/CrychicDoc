package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.effect.potion.BleedingPotionEffect;
import se.mickelus.tetra.effect.potion.PuncturedPotionEffect;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class PunctureEffect extends ChargedAbilityEffect {

    public static final PunctureEffect instance = new PunctureEffect();

    PunctureEffect() {
        super(20, 0.5, 40, 8.0, ItemEffect.puncture, ChargedAbilityEffect.TargetRequirement.entity, UseAnim.SPEAR, "raised");
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        if (!attacker.m_9236_().isClientSide) {
            int armorBefore = target.getArmorValue();
            int comboPoints = ComboPoints.get(attacker);
            boolean isSatiated = !attacker.getFoodData().needsFood();
            AbilityUseResult result;
            if (this.isDefensive(item, itemStack, hand)) {
                result = this.performDefensive(attacker, hand, item, itemStack, target);
            } else {
                result = this.performRegular(attacker, item, itemStack, target, chargedTicks, isSatiated, comboPoints);
            }
            boolean overextended = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend) > 0;
            attacker.causeFoodExhaustion(overextended ? 6.0F : 1.0F);
            if (item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration) <= 0 || armorBefore < 6 || target.getArmorValue() >= 6) {
                attacker.getCooldowns().addCooldown(item, this.getCooldown(item, itemStack) + target.getArmorValue() * 10);
            }
            item.tickProgression(attacker, itemStack, result == AbilityUseResult.fail ? 1 : 2);
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                this.performEcho(attacker, item, itemStack, target, chargedTicks, isSatiated, comboPoints);
            }
        }
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
        attacker.m_21011_(hand, false);
        item.applyDamage(2, itemStack, attacker);
    }

    public AbilityUseResult performRegular(Player attacker, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, int chargedTicks, boolean isSatiated, int comboPoints) {
        int armor = target.getArmorValue();
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, 1.0, 0.2F, 0.2F);
        if (result != AbilityUseResult.fail) {
            int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
            boolean isPunctured = target.getEffect(PuncturedPotionEffect.instance) != null;
            boolean reversal = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge) > 0 && armor > attacker.m_21230_();
            if (armor < 6 || isPunctured || reversal) {
                int duration = 80;
                if (overchargeBonus > 0) {
                    duration += (int) ((float) overchargeBonus * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge) * 10.0F);
                }
                double comboLevel = (double) item.getEffectLevel(itemStack, ItemEffect.abilityCombo);
                if (comboLevel > 0.0) {
                    duration = (int) ((double) duration + comboLevel * (double) comboPoints);
                }
                double overextendEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityOverextend);
                if (overextendEfficiency > 0.0 && isSatiated) {
                    duration = (int) ((double) duration + overextendEfficiency * 20.0);
                }
                int exhilarationLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
                if (exhilarationLevel > 0 && isPunctured) {
                    duration += exhilarationLevel;
                }
                target.addEffect(new MobEffectInstance(BleedingPotionEffect.instance, duration, 1, false, false));
            }
            if (armor >= 6 && !isPunctured || reversal) {
                int amplifier = item.getEffectLevel(itemStack, ItemEffect.puncture) - 1;
                int durationx = (int) (item.getEffectEfficiency(itemStack, ItemEffect.puncture) * 20.0F);
                if (overchargeBonus > 0) {
                    amplifier += overchargeBonus * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge);
                }
                int overextendLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
                if (overextendLevel > 0 && isSatiated) {
                    amplifier += overextendLevel;
                }
                target.addEffect(new MobEffectInstance(PuncturedPotionEffect.instance, durationx, amplifier, false, false));
            }
            if (!isPunctured) {
                int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
                if (momentumLevel > 0) {
                    double velocity = (double) momentumLevel / 100.0 + (double) (item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum) * (float) armor);
                    velocity *= 1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                    target.m_5997_(0.0, velocity, 0.0);
                }
            }
            target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 0.8F);
        } else {
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.8F);
        }
        return result;
    }

    public AbilityUseResult performDefensive(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target) {
        int armor = target.getArmorValue();
        float knockbackMultiplier = 0.3F;
        boolean isPunctured = target.getEffect(PuncturedPotionEffect.instance) != null;
        if (armor < 6 || isPunctured) {
            knockbackMultiplier += 0.6F;
        }
        AbilityUseResult result = item.hitEntity(itemStack, attacker, target, 0.3, 0.8F, knockbackMultiplier);
        if (result != AbilityUseResult.fail) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (item.getEffectEfficiency(itemStack, ItemEffect.abilityDefensive) * 20.0F), item.getEffectLevel(itemStack, ItemEffect.abilityDefensive), false, true));
            target.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 0.8F);
        } else {
            target.m_20193_().playSound(attacker, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.8F);
        }
        return result;
    }

    public void performEcho(Player attacker, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, int chargedTicks, boolean isSatiated, int comboPoints) {
        EchoHelper.echo(attacker, 60, () -> this.performRegular(attacker, item, itemStack, target, chargedTicks, isSatiated, comboPoints));
    }
}
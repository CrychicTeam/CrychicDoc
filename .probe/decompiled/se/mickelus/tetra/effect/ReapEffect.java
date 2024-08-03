package se.mickelus.tetra.effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.effect.potion.ExhaustedPotionEffect;
import se.mickelus.tetra.effect.potion.SmallAbsorbPotionEffect;
import se.mickelus.tetra.effect.potion.SmallHealthPotionEffect;
import se.mickelus.tetra.effect.potion.SmallStrengthPotionEffect;
import se.mickelus.tetra.effect.potion.SteeledPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.effect.potion.UnwaveringPotionEffect;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class ReapEffect extends ChargedAbilityEffect {

    public static final ReapEffect instance = new ReapEffect();

    ReapEffect() {
        super(20, 0.7F, 40, 8.0, ItemEffect.reap, ChargedAbilityEffect.TargetRequirement.none, UseAnim.SPEAR, "raised");
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, @Nullable LivingEntity target, @Nullable BlockPos targetPos, @Nullable Vec3 hitVec, int chargedTicks) {
        if (!attacker.m_9236_().isClientSide) {
            int overchargeBonus = this.canOvercharge(item, itemStack) ? this.getOverchargeBonus(item, itemStack, chargedTicks) : 0;
            double momentumEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityMomentum);
            int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
            int overextendLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
            boolean overextend = overextendLevel > 0 && !attacker.getFoodData().needsFood();
            ServerPlayer serverPlayer = (ServerPlayer) attacker;
            int cooldown = this.getCooldown(item, itemStack);
            double damageMultiplier = (double) EffectHelper.getEffectLevel(itemStack, ItemEffect.reap) / 100.0;
            double range = (double) EffectHelper.getEffectEfficiency(itemStack, ItemEffect.reap);
            if (overchargeBonus > 0) {
                damageMultiplier += (double) (overchargeBonus * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
                range += (double) overchargeBonus * 0.5;
            }
            int comboLevel = item.getEffectLevel(itemStack, ItemEffect.abilityCombo);
            int comboPoints = ComboPoints.get(attacker);
            if (comboLevel > 0) {
                damageMultiplier += (double) (comboLevel * comboPoints) / 100.0;
            }
            AtomicInteger kills = new AtomicInteger();
            AtomicInteger revengeKills = new AtomicInteger();
            AtomicInteger hits = new AtomicInteger();
            Vec3 targetVec;
            if (target != null) {
                targetVec = hitVec;
            } else {
                targetVec = Vec3.directionFromRotation(attacker.m_146909_(), attacker.m_146908_()).normalize().scale(range).add(attacker.m_20299_(0.0F));
            }
            AABB aoe = new AABB(targetVec, targetVec).inflate(range, 1.0, range);
            this.hitEntities(serverPlayer, item, itemStack, aoe, damageMultiplier, revengeLevel, overextend, overextendLevel, momentumEfficiency, kills, revengeKills, hits);
            this.applyBuff(attacker, kills.get(), hits.get(), hand, item, itemStack, chargedTicks, comboPoints, revengeKills.get());
            attacker.m_9236_().playSound(null, attacker.m_20185_(), attacker.m_20186_(), attacker.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.getSoundSource(), 1.0F, 1.0F);
            item.tickProgression(attacker, itemStack, 1 + kills.get());
            attacker.sweepAttack();
            attacker.causeFoodExhaustion(overextendLevel > 0 ? 6.0F : 1.0F);
            double exhilarationEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarationEfficiency > 0.0 && kills.get() > 0) {
                cooldown = (int) ((double) cooldown * (1.0 - exhilarationEfficiency / 100.0));
            }
            attacker.getCooldowns().addCooldown(item, cooldown);
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                this.echoReap(serverPlayer, hand, item, itemStack, chargedTicks, aoe, damageMultiplier, revengeLevel, overextend, overextendLevel, momentumEfficiency, comboPoints);
            }
        }
        attacker.m_21011_(hand, false);
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
        item.applyDamage(2, itemStack, attacker);
    }

    private void hitEntities(ServerPlayer player, ItemModularHandheld item, ItemStack itemStack, AABB aoe, double damageMultiplier, int revengeLevel, boolean overextend, int overextendLevel, double momentumEfficiency, AtomicInteger kills, AtomicInteger revengeKills, AtomicInteger hits) {
        Collection<LivingEntity> momentumTargets = new LinkedList();
        player.m_9236_().m_45976_(LivingEntity.class, aoe).stream().filter(entity -> entity != player).filter(entity -> !player.m_7307_(entity)).forEach(entity -> {
            double individualDamageMultiplier = damageMultiplier;
            boolean canRevenge = revengeLevel > 0 && RevengeTracker.canRevenge(player, entity);
            if (canRevenge) {
                individualDamageMultiplier = damageMultiplier + (double) revengeLevel / 100.0;
            }
            if (overextend && entity.getHealth() / entity.getMaxHealth() >= (float) overextendLevel / 100.0F) {
                individualDamageMultiplier *= 2.0;
            }
            AbilityUseResult result = item.hitEntity(itemStack, player, entity, individualDamageMultiplier, 0.5F, 0.2F);
            if (result != AbilityUseResult.fail) {
                if (!entity.isAlive()) {
                    kills.incrementAndGet();
                    if (canRevenge) {
                        revengeKills.incrementAndGet();
                        RevengeTracker.removeEnemySynced(player, entity);
                    }
                } else if (momentumEfficiency > 0.0) {
                    momentumTargets.add(entity);
                }
                hits.incrementAndGet();
            }
            if (result == AbilityUseResult.crit) {
                player.m_20193_().playSound((Player) player, entity.m_20183_(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.3F);
            }
        });
        if (momentumEfficiency > 0.0 && kills.get() > 0) {
            int stunDuration = (int) (momentumEfficiency * (double) kills.get() * 20.0);
            momentumTargets.forEach(entity -> entity.addEffect(new MobEffectInstance(StunPotionEffect.instance, stunDuration, 0, false, false)));
        }
    }

    private void applyBuff(Player attacker, int kills, int hits, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, int chargedTicks, int comboPoints, int revengeKills) {
        int defensiveLevel = item.getEffectLevel(itemStack, ItemEffect.abilityDefensive);
        if (defensiveLevel > 0) {
            if (hand == InteractionHand.OFF_HAND) {
                if (hits > 0) {
                    int duration = defensiveLevel * (1 + kills * 2);
                    attacker.m_7292_(new MobEffectInstance(SteeledPotionEffect.instance, duration, hits - 1, false, true));
                }
            } else if (kills > 0) {
                int duration = (int) (item.getEffectEfficiency(itemStack, ItemEffect.abilityDefensive) * 20.0F);
                attacker.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, kills - 1, false, true));
            }
        }
        if (kills > 0) {
            int overchargeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge);
            if (overchargeLevel > 0) {
                double duration = 600.0;
                duration *= (double) (1.0F + (float) this.getOverchargeBonus(item, itemStack, chargedTicks) * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge));
                attacker.m_7292_(new MobEffectInstance(SmallStrengthPotionEffect.instance, (int) duration, kills - 1, false, true));
            }
            int speedLevel = item.getEffectLevel(itemStack, ItemEffect.abilitySpeed);
            if (speedLevel > 0) {
                attacker.m_7292_(new MobEffectInstance(MobEffects.DIG_SPEED, (int) (item.getEffectEfficiency(itemStack, ItemEffect.abilitySpeed) * 20.0F), kills - 1, false, true));
            }
            int momentumLevel = item.getEffectLevel(itemStack, ItemEffect.abilityMomentum);
            if (momentumLevel > 0) {
                attacker.m_7292_(new MobEffectInstance(UnwaveringPotionEffect.instance, momentumLevel * kills * 20, 0, false, true));
            }
            double comboEfficiency = (double) item.getEffectEfficiency(itemStack, ItemEffect.abilityCombo);
            if (comboEfficiency > 0.0) {
                double duration = 300.0;
                duration += comboEfficiency * (double) comboPoints * 20.0;
                attacker.m_7292_(new MobEffectInstance(MobEffects.DIG_SPEED, (int) duration, kills - 1, false, true));
            }
            if (revengeKills > 0) {
                double duration = 400.0;
                duration += (double) (item.getEffectEfficiency(itemStack, ItemEffect.abilityRevenge) * (float) revengeKills * 20.0F);
                attacker.m_7292_(new MobEffectInstance(SmallStrengthPotionEffect.instance, (int) duration, kills - 1, false, true));
            }
            int exhilarationLevel = item.getEffectLevel(itemStack, ItemEffect.abilityExhilaration);
            if (exhilarationLevel > 0) {
                int currentAmplifier = (Integer) Optional.ofNullable(attacker.m_21124_(SmallAbsorbPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
                int amp = Math.max(currentAmplifier, kills - 1);
                attacker.m_7292_(new MobEffectInstance(SmallAbsorbPotionEffect.instance, 600, amp, false, true));
            }
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                int amp = (Integer) Optional.ofNullable(attacker.m_21124_(SmallStrengthPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
                amp = Math.min(echoLevel, amp + kills);
                attacker.m_7292_(new MobEffectInstance(SmallStrengthPotionEffect.instance, 600, amp, false, true));
            }
        }
        int overextendLevel = item.getEffectLevel(itemStack, ItemEffect.abilityOverextend);
        if (overextendLevel > 0) {
            if (kills > 0) {
                attacker.m_7292_(new MobEffectInstance(SmallHealthPotionEffect.instance, 900, kills - 1, false, true));
            } else if (!attacker.getFoodData().needsFood()) {
                attacker.m_7292_(new MobEffectInstance(ExhaustedPotionEffect.instance, 400, 4, false, true));
                attacker.causeFoodExhaustion(12.0F);
            }
        }
    }

    private void echoReap(ServerPlayer player, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, int chargedTicks, AABB aoe, double damageMultiplier, int revengeLevel, boolean overextend, int overextendLevel, double momentumEfficiency, int comboPoints) {
        EchoHelper.echo(player, 60, () -> {
            AtomicInteger kills = new AtomicInteger();
            AtomicInteger revengeKills = new AtomicInteger();
            AtomicInteger hits = new AtomicInteger();
            this.hitEntities(player, item, itemStack, aoe, damageMultiplier, revengeLevel, overextend, overextendLevel, momentumEfficiency, kills, revengeKills, hits);
            this.applyBuff(player, kills.get(), hits.get(), hand, item, itemStack, chargedTicks, comboPoints, revengeKills.get());
            player.m_36346_();
        });
    }
}
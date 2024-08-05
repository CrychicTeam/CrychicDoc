package io.redspace.ironsspellbooks.damage;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import java.util.HashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DamageSources {

    private static final HashMap<LivingEntity, Integer> knockbackImmunes = new HashMap();

    public static DamageSource get(Level level, ResourceKey<DamageType> damageType) {
        return level.damageSources().source(damageType);
    }

    public static Holder<DamageType> getHolderFromResource(Entity entity, ResourceKey<DamageType> damageTypeResourceKey) {
        Optional<Holder.Reference<DamageType>> option = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(damageTypeResourceKey);
        return option.isPresent() ? (Holder) option.get() : entity.level().damageSources().genericKill().typeHolder();
    }

    public static boolean applyDamage(Entity target, float baseAmount, DamageSource damageSource) {
        if (target instanceof LivingEntity livingTarget && damageSource instanceof SpellDamageSource spellDamageSource) {
            SpellDamageEvent e = new SpellDamageEvent(livingTarget, baseAmount, spellDamageSource);
            if (MinecraftForge.EVENT_BUS.post(e)) {
                return false;
            }
            baseAmount = e.getAmount();
            float adjustedDamage = baseAmount * getResist(livingTarget, spellDamageSource.spell.getSchoolType());
            MagicSummon fromSummon = damageSource.getDirectEntity() instanceof MagicSummon summonx ? summonx : (damageSource.getEntity() instanceof MagicSummon summon ? summon : null);
            if (fromSummon != null) {
                if (fromSummon.getSummoner() != null) {
                    adjustedDamage *= (float) fromSummon.getSummoner().getAttributeValue(AttributeRegistry.SUMMON_DAMAGE.get());
                }
            } else if (damageSource.getDirectEntity() instanceof NoKnockbackProjectile) {
                ignoreNextKnockback(livingTarget);
            }
            if (damageSource.getEntity() instanceof LivingEntity livingAttacker) {
                if (isFriendlyFireBetween(livingAttacker, livingTarget)) {
                    return false;
                }
                livingAttacker.setLastHurtMob(target);
            }
            boolean flag = livingTarget.hurt(damageSource, adjustedDamage);
            if (fromSummon instanceof LivingEntity livingSummon) {
                livingTarget.setLastHurtByMob(livingSummon);
            }
            return flag;
        }
        return target.hurt(damageSource, baseAmount);
    }

    public static void ignoreNextKnockback(LivingEntity livingEntity) {
        if (!livingEntity.f_19853_.isClientSide) {
            knockbackImmunes.put(livingEntity, livingEntity.f_19797_);
        }
    }

    @SubscribeEvent
    public static void cancelKnockback(LivingKnockBackEvent event) {
        if (knockbackImmunes.containsKey(event.getEntity())) {
            LivingEntity entity = event.getEntity();
            if (entity.f_19797_ - (Integer) knockbackImmunes.get(entity) <= 1) {
                event.setCanceled(true);
            }
            knockbackImmunes.remove(entity);
        }
    }

    @SubscribeEvent
    public static void postHitEffects(LivingDamageEvent event) {
        if (event.getSource() instanceof SpellDamageSource spellDamageSource && spellDamageSource.hasPostHitEffects()) {
            float actualDamage = event.getAmount();
            LivingEntity target = event.getEntity();
            if (event.getSource().getEntity() instanceof LivingEntity livingAttacker && spellDamageSource.getLifestealPercent() > 0.0F) {
                livingAttacker.heal(spellDamageSource.getLifestealPercent() * actualDamage);
            }
            if (spellDamageSource.getFreezeTicks() > 0 && target.canFreeze()) {
                target.m_146917_(target.m_146888_() + spellDamageSource.getFreezeTicks() * 2);
            }
            if (spellDamageSource.getFireTime() > 0) {
                target.m_20254_(spellDamageSource.getFireTime());
            }
        }
    }

    public static boolean isFriendlyFireBetween(Entity attacker, Entity target) {
        if (attacker == null || target == null) {
            return false;
        } else if (attacker.isPassengerOfSameVehicle(target)) {
            return true;
        } else {
            Team team = attacker.getTeam();
            return team == null ? attacker.isAlliedTo(target) : team.isAlliedTo(target.getTeam()) && !team.isAllowFriendlyFire();
        }
    }

    @Deprecated(since = "MC_1.20", forRemoval = true)
    public static DamageSource directDamageSource(DamageSource source, Entity attacker) {
        return new DamageSource(source.typeHolder(), attacker);
    }

    @Deprecated(since = "MC_1.20", forRemoval = true)
    public static DamageSource indirectDamageSource(DamageSource source, Entity projectile, @Nullable Entity attacker) {
        return new DamageSource(source.typeHolder(), attacker, projectile);
    }

    public static float getResist(LivingEntity entity, SchoolType damageSchool) {
        double baseResist = entity.getAttributeValue(AttributeRegistry.SPELL_RESIST.get());
        return damageSchool == null ? 2.0F - (float) Utils.softCapFormula(baseResist) : 2.0F - (float) Utils.softCapFormula(damageSchool.getResistanceFor(entity) * baseResist);
    }
}
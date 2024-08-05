package dev.xkmc.l2hostility.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.enchantments.HitTargetEnchantment;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.HostilityDamageState;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.Map.Entry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LHAttackListener implements AttackListener {

    private static boolean masterImmunity(AttackCache cache) {
        LivingAttackEvent event = cache.getLivingAttackEvent();
        if (event != null && event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            MobTraitCap attacker = null;
            MobTraitCap target = null;
            if (cache.getAttacker() instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
                attacker = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            }
            if (cache.getAttackTarget() instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
                target = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            }
            LivingEntity attackerMaster = null;
            LivingEntity targetMaster = null;
            if (attacker != null && attacker.asMinion != null) {
                attackerMaster = attacker.asMinion.master;
                if (cache.getAttackTarget() == attackerMaster) {
                    return true;
                }
            }
            if (target != null && target.asMinion != null) {
                targetMaster = target.asMinion.master;
                if (cache.getAttacker() == targetMaster) {
                    return true;
                }
            }
            return attackerMaster != null && attackerMaster == targetMaster ? true : target != null && target.isMasterProtected();
        }
    }

    public void onAttack(AttackCache cache, ItemStack weapon) {
        LivingAttackEvent event = cache.getLivingAttackEvent();
        assert event != null;
        if (masterImmunity(cache)) {
            event.setCanceled(true);
        }
    }

    public void onHurt(AttackCache cache, ItemStack weapon) {
        LivingHurtEvent event = cache.getLivingHurtEvent();
        assert event != null;
        if (!event.getSource().is(L2DamageTypes.NO_SCALE)) {
            LivingEntity mob = cache.getAttacker();
            LivingEntity target = cache.getAttackTarget();
            if (mob != target) {
                if (MobTraitCap.HOLDER.isProper(target)) {
                    MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(target);
                    for (Entry<Enchantment, Integer> e : weapon.getAllEnchantments().entrySet()) {
                        if (e.getKey() instanceof HitTargetEnchantment ench) {
                            ench.hitMob(target, cap, (Integer) e.getValue(), cache);
                        }
                    }
                }
                if (mob != null && MobTraitCap.HOLDER.isProper(mob)) {
                    MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
                    if (!mob.m_6095_().is(LHTagGen.NO_SCALING)) {
                        int lv = cap.getLevel();
                        double factor;
                        if (LHConfig.COMMON.exponentialDamage.get()) {
                            factor = Math.pow(1.0 + LHConfig.COMMON.damageFactor.get(), (double) lv);
                        } else {
                            factor = 1.0 + (double) lv * LHConfig.COMMON.damageFactor.get();
                        }
                        cache.addHurtModifier(DamageModifier.multTotal((float) factor));
                    }
                    TraitEffectCache traitCache = new TraitEffectCache(target);
                    cap.traitEvent((k, v) -> k.onHurtTarget(v, mob, cache, traitCache));
                }
                if (mob != null) {
                    for (GenericItemStack<CurseCurioItem> ex : CurseCurioItem.getFromPlayer(mob)) {
                        ex.item().onHurtTarget(ex.stack(), mob, cache);
                    }
                }
            }
        }
    }

    public void onDamage(AttackCache cache, ItemStack weapon) {
        LivingEntity mob = cache.getAttackTarget();
        if (MobTraitCap.HOLDER.isProper(mob)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            cap.traitEvent((k, v) -> k.onDamaged(v, mob, cache));
        }
        if (masterImmunity(cache)) {
            cache.addDealtModifier(DamageModifier.nonlinearFinal(10432, e -> 0.0F));
        }
    }

    public void onCreateSource(CreateSourceEvent event) {
        LivingEntity mob = event.getAttacker();
        if (MobTraitCap.HOLDER.isProper(mob)) {
            ((MobTraitCap) MobTraitCap.HOLDER.get(mob)).traitEvent((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
        }
        DamageTypeWrapper type = event.getResult();
        if (type != null) {
            DamageTypeWrapper root = type.toRoot();
            if (root == L2DamageTypes.MOB_ATTACK || root == L2DamageTypes.PLAYER_ATTACK) {
                if (CurioCompat.hasItemInCurioOrSlot(mob, (Item) LHItems.IMAGINE_BREAKER.get())) {
                    event.enable(DefaultDamageState.BYPASS_MAGIC);
                }
                if (CurioCompat.hasItemInCurio(mob, (Item) LHItems.PLATINUM_STAR.get())) {
                    event.enable(HostilityDamageState.BYPASS_COOLDOWN);
                }
            }
        }
    }
}
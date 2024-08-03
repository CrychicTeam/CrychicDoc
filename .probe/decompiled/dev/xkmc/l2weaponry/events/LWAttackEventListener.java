package dev.xkmc.l2weaponry.events;

import dev.xkmc.l2complements.content.enchantment.core.SourceModifierEnchantment;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.GenericTieredItem;
import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.content.item.base.DoubleWieldItem;
import dev.xkmc.l2weaponry.content.item.base.LWTieredItem;
import dev.xkmc.l2weaponry.content.item.legendary.LegendaryWeapon;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class LWAttackEventListener implements AttackListener {

    public void onCreateSource(CreateSourceEvent event) {
        if (!event.getOriginal().equals(DamageTypes.MOB_ATTACK) && !event.getOriginal().equals(DamageTypes.PLAYER_ATTACK)) {
            if (event.getDirect() instanceof BaseThrownWeaponEntity<?> thrown && thrown.getItem().getItem() instanceof LWTieredItem weapon && thrown.m_19749_() instanceof LivingEntity le) {
                weapon.modifySource(le, event, thrown.getItem(), thrown.targetCache);
                SourceModifierEnchantment.modifySource(thrown.getItem(), event);
            }
        } else {
            ItemStack stack = event.getAttacker().getMainHandItem();
            if (stack.getItem() instanceof LWTieredItem item) {
                Entity target = (Entity) Optional.of(event).map(CreateSourceEvent::getPlayerAttackCache).map(PlayerAttackCache::getPlayerAttackEntityEvent).map(AttackEntityEvent::getTarget).orElse(null);
                item.modifySource(event.getAttacker(), event, event.getAttacker().getMainHandItem(), target);
            }
        }
    }

    public void setupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setup) {
        if (cache.getLivingAttackEvent().getSource().getDirectEntity() instanceof BaseThrownWeaponEntity<?> thrown && thrown.m_19749_() instanceof LivingEntity le) {
            setup.accept(le, thrown.getItem());
        }
    }

    public void onHurt(AttackCache cache, ItemStack stack) {
        LivingHurtEvent event = cache.getLivingHurtEvent();
        assert event != null;
        if (event.getSource().getDirectEntity() instanceof LivingEntity le && le == cache.getAttacker()) {
            if (!stack.isEmpty() && stack.getItem() instanceof GenericTieredItem && le instanceof Player && cache.getCriticalHitEvent() != null && cache.getStrength() < 0.7F) {
                cache.addHurtModifier(DamageModifier.nonlinearFinal(10000, f -> 0.1F));
                return;
            }
            if (!stack.isEmpty() && stack.getItem() instanceof DoubleWieldItem claw) {
                claw.accumulateDamage(stack, cache.getAttacker());
            }
        }
        if (!stack.isEmpty() && stack.getItem() instanceof LWTieredItem w) {
            cache.addHurtModifier(DamageModifier.multAttr(w.getMultiplier(cache)));
            LivingEntity attacker = cache.getAttacker();
            if (attacker != null && w.getExtraConfig() instanceof LWExtraConfig config) {
                config.onHurt(cache, attacker, stack);
            }
        }
        if (cache.getAttacker() != null && stack.getItem() instanceof LegendaryWeapon weapon) {
            weapon.onHurt(cache, cache.getAttacker(), stack);
        }
    }

    public void onHurtMaximized(AttackCache cache, ItemStack stack) {
        LivingHurtEvent event = cache.getLivingHurtEvent();
        assert event != null;
        if (cache.getAttacker() != null && stack.getItem() instanceof LegendaryWeapon weapon) {
            weapon.onHurtMaximized(cache, cache.getAttacker());
        }
    }

    public void onDamageFinalized(AttackCache cache, ItemStack stack) {
        LivingDamageEvent event = cache.getLivingDamageEvent();
        assert event != null;
        if (cache.getAttacker() != null && stack.getItem() instanceof LegendaryWeapon weapon) {
            weapon.onDamageFinal(cache, cache.getAttacker());
        }
    }

    public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        if (!event.isVanillaCritical() && event.getResult() != Result.ALLOW) {
            return false;
        } else if (event.getEntity().m_9236_().isClientSide()) {
            return false;
        } else {
            if (cache.getWeapon().getItem() instanceof LegendaryWeapon weapon) {
                weapon.onCrit(event.getEntity(), event.getTarget());
            }
            return false;
        }
    }

    public void postAttack(AttackCache cache, LivingAttackEvent event, ItemStack stack) {
        LivingEntity attacker = cache.getAttacker();
        if (attacker != null) {
            if (stack.getItem() instanceof DoubleWieldItem item && stack.getEnchantmentLevel((Enchantment) LWEnchantments.GHOST_SLASH.get()) > 0 && (double) cache.getStrength() >= 0.9) {
                item.accumulateDamage(stack, attacker);
                if (attacker instanceof Player player && !player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, e -> e.m_21190_(InteractionHand.MAIN_HAND));
                }
            }
        }
    }
}
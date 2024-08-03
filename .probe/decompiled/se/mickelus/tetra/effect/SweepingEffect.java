package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class SweepingEffect {

    public static int getSweepingLevel(ItemStack itemStack) {
        return EffectHelper.getEffectLevel(itemStack, ItemEffect.sweeping) + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, itemStack);
    }

    public static void sweepAttack(ItemStack itemStack, LivingEntity target, LivingEntity attacker, int sweepingLevel) {
        boolean trueSweep = EffectHelper.getEffectLevel(itemStack, ItemEffect.truesweep) > 0;
        float damage = (float) Math.max(attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * (double) ((float) sweepingLevel * 0.125F), 1.0);
        float knockback = trueSweep ? (float) (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, itemStack) + 1) * 0.5F : 0.5F;
        double range = (double) (1.0F + EffectHelper.getEffectEfficiency(itemStack, ItemEffect.sweeping));
        double reach = attacker.getAttributeValue(ForgeMod.ENTITY_REACH.get());
        attacker.m_9236_().m_45976_(LivingEntity.class, target.m_20191_().inflate(range, 0.25, range)).stream().filter(entity -> entity != attacker).filter(entity -> entity != target).filter(entity -> !attacker.m_7307_(entity)).filter(entity -> attacker.m_20280_(entity) < (range + reach) * (range + reach)).forEach(entity -> {
            entity.knockback((double) knockback, (double) Mth.sin(attacker.m_146908_() * (float) Math.PI / 180.0F), (double) (-Mth.cos(attacker.m_146908_() * (float) Math.PI / 180.0F)));
            DamageSource damageSource = attacker instanceof Player ? attacker.m_269291_().playerAttack((Player) attacker) : attacker.m_269291_().mobAttack(attacker);
            if (trueSweep) {
                ItemEffectHandler.applyHitEffects(itemStack, entity, attacker);
                EffectHelper.applyEnchantmentHitEffects(itemStack, entity, attacker);
                causeTruesweepDamage(damageSource, damage, itemStack, attacker, entity);
            } else {
                entity.hurt(damageSource, damage);
            }
        });
        attacker.m_9236_().playSound(null, attacker.m_20185_(), attacker.m_20186_(), attacker.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.m_5720_(), 1.0F, 1.0F);
        CastOptional.cast(attacker, Player.class).ifPresent(Player::m_36346_);
    }

    public static void triggerTruesweep() {
        TetraMod.packetHandler.sendToServer(new TruesweepPacket());
    }

    public static void truesweep(ItemStack itemStack, LivingEntity attacker, boolean triggerVfx) {
        int sweepingLevel = getSweepingLevel(itemStack);
        float damage = (float) Math.max(attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * (double) ((float) sweepingLevel * 0.125F), 1.0);
        float knockback = 0.5F + (float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, itemStack) * 0.5F;
        double range = (double) (2.0F + EffectHelper.getEffectEfficiency(itemStack, ItemEffect.sweeping));
        Vec3 target = Vec3.directionFromRotation(attacker.m_146909_(), attacker.m_146908_()).normalize().scale(range).add(attacker.m_20299_(0.0F));
        AABB aoe = new AABB(target, target);
        attacker.m_9236_().m_45976_(LivingEntity.class, aoe.inflate(range, 1.0, range)).stream().filter(entity -> entity != attacker).filter(entity -> !attacker.m_7307_(entity)).forEach(entity -> {
            entity.knockback((double) knockback, (double) Mth.sin(attacker.m_146908_() * (float) Math.PI / 180.0F), (double) (-Mth.cos(attacker.m_146909_() * (float) Math.PI / 180.0F)));
            ItemEffectHandler.applyHitEffects(itemStack, entity, attacker);
            EffectHelper.applyEnchantmentHitEffects(itemStack, entity, attacker);
            DamageSource damageSource = attacker instanceof Player ? attacker.m_269291_().playerAttack((Player) attacker) : attacker.m_269291_().mobAttack(attacker);
            causeTruesweepDamage(damageSource, damage, itemStack, attacker, entity);
        });
        if (triggerVfx) {
            attacker.m_9236_().playSound(null, attacker.m_20185_(), attacker.m_20186_(), attacker.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.m_5720_(), 1.0F, 1.0F);
            CastOptional.cast(attacker, Player.class).ifPresent(Player::m_36346_);
        }
    }

    private static void causeTruesweepDamage(DamageSource damageSource, float baseDamage, ItemStack itemStack, LivingEntity attacker, LivingEntity target) {
        float targetModifier = EnchantmentHelper.getDamageBonus(itemStack, target.getMobType());
        float critMultiplier = (Float) CastOptional.cast(attacker, Player.class).map(player -> ForgeHooks.getCriticalHit(player, target, false, 1.5F)).map(CriticalHitEvent::getDamageModifier).orElse(1.0F);
        target.hurt(damageSource, (baseDamage + targetModifier) * critMultiplier);
        if (targetModifier > 0.0F) {
            CastOptional.cast(attacker, Player.class).ifPresent(player -> player.magicCrit(target));
        }
        if (critMultiplier > 1.0F) {
            attacker.m_20193_().playSound(null, target.m_20183_(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.3F);
            ((Player) attacker).crit(target);
        }
    }
}
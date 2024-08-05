package com.mna.items.artifice.curio;

import com.mna.api.items.ChargeableItem;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.MathUtils;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ItemEldritchOrb extends ChargeableItem {

    private final ArrayList<Enchantment> allowedEnchantments = new ArrayList();

    public ItemEldritchOrb() {
        super(new Item.Properties(), 1000.0F);
        this.allowedEnchantments.clear();
        this.allowedEnchantments.add(Enchantments.BANE_OF_ARTHROPODS);
        this.allowedEnchantments.add(Enchantments.FLAMING_ARROWS);
        this.allowedEnchantments.add(Enchantments.KNOCKBACK);
        this.allowedEnchantments.add(Enchantments.MULTISHOT);
        this.allowedEnchantments.add(Enchantments.SMITE);
        this.allowedEnchantments.add(Enchantments.QUICK_CHARGE);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        for (Enchantment ench : enchantments.keySet()) {
            if (!this.allowedEnchantments.contains(ench)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.allowedEnchantments.contains(enchantment);
    }

    public boolean shoot(Player caster, ItemStack orb) {
        if (caster.m_9236_().isClientSide()) {
            return false;
        } else {
            List<Entity> targets = (List<Entity>) caster.m_9236_().m_45976_(Mob.class, caster.m_20191_().inflate(5.0)).stream().filter(e -> e.m_6084_() && e.getSensing().hasLineOfSight(caster) && (e.getTarget() == caster || e.m_21188_() == caster)).collect(Collectors.toList());
            if (targets.size() <= 0) {
                return false;
            } else {
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(orb);
                int numTargets = enchants.containsKey(Enchantments.MULTISHOT) ? (Integer) enchants.get(Enchantments.MULTISHOT) + 1 : 1;
                for (int i = 0; i < numTargets; i++) {
                    Mob target = (Mob) targets.get((int) Math.floor(Math.random() * (double) targets.size()));
                    Vector3f translation = new Vector3f(1.0F, 2.0F, 0.0F);
                    translation.rotate(Axis.YN.rotationDegrees((float) ((double) (MathUtils.lerpf(caster.f_20886_, caster.f_20885_, 0.0F) / 180.0F) * Math.PI)));
                    ServerMessageDispatcher.sendParticleSpawn(caster.m_20185_() + (double) translation.x(), caster.m_20186_() + (double) translation.y(), caster.m_20189_() + (double) translation.z(), (double) target.m_20183_().m_123341_(), (double) ((float) target.m_20183_().m_123342_() + target.m_20206_() / 2.0F), (double) target.m_20183_().m_123343_(), 0, 64.0F, caster.m_9236_().dimension(), ParticleInit.LIGHTNING_BOLT.get());
                    caster.m_9236_().playSound(null, caster.m_20183_(), SFX.Spell.Cast.ARCANE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    this.applyOrbEnchantmentEffects(caster, target, enchants);
                    target.m_6469_(target.m_269291_().playerAttack(caster), 1.0F);
                }
                return true;
            }
        }
    }

    private void applyOrbEnchantmentEffects(Player caster, Mob target, Map<Enchantment, Integer> enchants) {
        for (Entry<Enchantment, Integer> e : enchants.entrySet()) {
            if ((target instanceof Spider || target instanceof Silverfish || target instanceof Endermite) && e.getKey() == Enchantments.BANE_OF_ARTHROPODS) {
                target.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, (Integer) e.getValue()));
            } else if (target.m_21222_() && e.getKey() == Enchantments.SMITE) {
                target.m_7292_(new MobEffectInstance(MobEffects.WEAKNESS, 60, (Integer) e.getValue()));
                target.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, (Integer) e.getValue()));
            } else if (e.getKey() == Enchantments.FLAMING_ARROWS) {
                target.m_20254_((Integer) e.getValue() + 1);
            } else if (e.getKey() == Enchantments.KNOCKBACK) {
                Vec3 dir = caster.m_20182_().subtract(target.m_20182_()).normalize();
                target.m_147240_((double) ((float) ((Integer) e.getValue()).intValue() / 3.0F), dir.x, dir.z);
            }
        }
    }

    @Override
    protected float manaPerRechargeTick() {
        return 10.0F;
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        long delay = enchants.containsKey(Enchantments.QUICK_CHARGE) ? (long) (20 - (Integer) enchants.get(Enchantments.QUICK_CHARGE)) : 20L;
        return world.getGameTime() % delay == 0L && this.shoot(player, stack);
    }
}
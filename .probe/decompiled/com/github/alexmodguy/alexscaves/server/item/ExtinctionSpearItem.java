package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.ExtinctionSpearEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExtinctionSpearItem extends SpearItem {

    public ExtinctionSpearItem(Item.Properties properties) {
        super(properties, 8.0);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1) {
        if (livingEntity instanceof Player player) {
            int i = this.m_8105_(itemStack) - i1;
            float f = this.getPowerForTime(i);
            if ((double) f > 0.1) {
                itemStack.hurtAndBreak(1, livingEntity, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                ExtinctionSpearEntity spearEntity = new ExtinctionSpearEntity(level, player, itemStack);
                spearEntity.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, f * 3.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    spearEntity.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                level.m_7967_(spearEntity);
                level.playSound((Player) null, spearEntity, ACSoundRegistry.EXTINCTION_SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            killGrottoGhostsFor(player, false);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing) {
        if (timeUsing == this.m_8105_(stack)) {
            level.playSound((Player) null, living, ACSoundRegistry.EXTINCTION_SPEAR_SUMMON.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            int grottoHeads = 3 + stack.getEnchantmentLevel(ACEnchantmentRegistry.HERD_PHALANX.get());
            float grottoRotateBy = 360.0F / (float) grottoHeads;
            for (int i = 0; i < grottoHeads; i++) {
                DinosaurSpiritEntity dinosaurSpirit = ACEntityRegistry.DINOSAUR_SPIRIT.get().create(level);
                dinosaurSpirit.m_20359_(living);
                dinosaurSpirit.setDinosaurType(DinosaurSpiritEntity.DinosaurType.GROTTOCERATOPS);
                dinosaurSpirit.setPlayerUUID(living.m_20148_());
                dinosaurSpirit.setRotateOffset((float) i * grottoRotateBy);
                level.m_7967_(dinosaurSpirit);
            }
        }
    }

    public static boolean killGrottoGhostsFor(Player player, boolean justTheClosest) {
        DinosaurSpiritEntity closest = null;
        for (DinosaurSpiritEntity spirit : player.m_9236_().m_45976_(DinosaurSpiritEntity.class, player.m_20191_().inflate(30.0, 30.0, 30.0))) {
            if (spirit.getPlayerUUID().equals(player.m_20148_()) && spirit.getDinosaurType() == DinosaurSpiritEntity.DinosaurType.GROTTOCERATOPS && !spirit.isFading()) {
                if (!justTheClosest) {
                    spirit.setFading(true);
                } else if (closest == null || closest.m_20270_(player) > spirit.m_20270_(closest)) {
                    closest = spirit;
                }
            }
        }
        if (justTheClosest && closest != null) {
            closest.setFading(true);
            return true;
        } else {
            return !justTheClosest;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurtEntity, LivingEntity player) {
        hurtEntity.m_20254_(5);
        DinosaurSpiritEntity dinosaurSpirit = ACEntityRegistry.DINOSAUR_SPIRIT.get().create(player.m_9236_());
        Vec3 between = player.m_20182_().add(hurtEntity.m_20182_()).scale(0.5);
        dinosaurSpirit.m_6034_(between.x, player.m_20186_() + 1.0, between.z);
        dinosaurSpirit.setDinosaurType(DinosaurSpiritEntity.DinosaurType.TREMORSAURUS);
        dinosaurSpirit.setPlayerUUID(player.m_20148_());
        dinosaurSpirit.setEnchantmentLevel(stack.getEnchantmentLevel(ACEnchantmentRegistry.CHOMPING_SPIRIT.get()));
        dinosaurSpirit.setAttackingEntityId(hurtEntity.m_19879_());
        dinosaurSpirit.m_7618_(EntityAnchorArgument.Anchor.EYES, hurtEntity.m_146892_());
        dinosaurSpirit.setDelaySpawn(5);
        player.m_9236_().m_7967_(dinosaurSpirit);
        return super.hurtEnemy(stack, hurtEntity, player);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public boolean isValidRepairItem(ItemStack item, ItemStack repairItem) {
        return repairItem.is(ACItemRegistry.TECTONIC_SHARD.get()) || super.m_6832_(item, repairItem);
    }
}
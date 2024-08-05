package dev.xkmc.l2weaponry.content.item.base;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;

public class DoubleWieldItem extends GenericWeaponItem {

    public DoubleWieldItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config, TagKey<Block> blocks) {
        super(tier, damage, speed, prop, config, blocks);
    }

    public void accumulateDamage(ItemStack stack, LivingEntity entity) {
    }

    @Override
    protected final boolean canSweep() {
        return true;
    }

    @Override
    public AABB getSweepHitBoxImpl(ItemStack stack, LivingEntity player, Entity target) {
        double r = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
        return player.getOffhandItem().getItem() == this ? player.m_20191_().inflate(r + 1.0, r + 0.25, r + 1.0) : super.getSweepHitBoxImpl(stack, player, target);
    }
}
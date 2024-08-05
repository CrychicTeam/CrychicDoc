package com.rekindled.embers.item;

import com.rekindled.embers.api.item.IEmberChargedTool;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.EmberInventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ClockworkToolItem extends DiggerItem implements IEmberChargedTool {

    public ClockworkToolItem(float attackDamageModifier, float attackSpeedModifier, Tier tier, TagKey<Block> blocks, Item.Properties properties) {
        super(attackDamageModifier, attackSpeedModifier, tier, blocks, properties);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (this.hasEmber(stack)) {
            entity.setSecondsOnFire(2);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return this.hasEmber(stack) ? super.getDestroySpeed(stack, state) : 0.0F;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.getOrCreateTag().putBoolean("didUse", true);
        if (target.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(GlowParticleOptions.EMBER, target.m_20185_(), target.m_20186_() + (double) target.m_20192_() / 1.5, target.m_20189_(), 70, 0.15, 0.15, 0.15, 0.6);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        stack.getOrCreateTag().putBoolean("didUse", true);
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
        return enchant.category != EnchantmentCategory.BREAKABLE && enchant != Enchantments.SWEEPING_EDGE;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.hasTag() && newStack.hasTag() ? slotChanged || oldStack.getTag().getBoolean("poweredOn") != newStack.getTag().getBoolean("poweredOn") || newStack.getItem() != oldStack.getItem() : slotChanged || newStack.getItem() != oldStack.getItem();
    }

    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.hasTag() && newStack.hasTag() ? oldStack.getTag().getBoolean("poweredOn") != newStack.getTag().getBoolean("poweredOn") : false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (selected && !world.isClientSide()) {
            if (!stack.hasTag()) {
                stack.getOrCreateTag();
                stack.getTag().putBoolean("poweredOn", false);
                stack.getTag().putBoolean("didUse", false);
            } else if (entity instanceof Player player) {
                if (world.getGameTime() % 5L == 0L) {
                    if (EmberInventoryUtil.getEmberTotal(player) > 5.0) {
                        if (!stack.getTag().getBoolean("poweredOn")) {
                            stack.getTag().putBoolean("poweredOn", true);
                        }
                    } else if (stack.getTag().getBoolean("poweredOn")) {
                        stack.getTag().putBoolean("poweredOn", false);
                    }
                }
                if (stack.getTag().getBoolean("didUse")) {
                    stack.getTag().putBoolean("didUse", false);
                    EmberInventoryUtil.removeEmber(player, 5.0);
                    if (EmberInventoryUtil.getEmberTotal(player) < 5.0) {
                        stack.getTag().putBoolean("poweredOn", false);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasEmber(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean("poweredOn");
    }
}
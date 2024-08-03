package dev.xkmc.l2weaponry.content.item.base;

import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public abstract class BaseThrowableWeaponItem extends GenericWeaponItem implements IThrowableCallback {

    public BaseThrowableWeaponItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config, TagKey<Block> blocks) {
        super(tier, damage, speed, prop, config, blocks);
        LWItems.THROW_DECO.add(this);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (user instanceof Player player) {
            int time = this.getUseDuration(stack) - timeLeft;
            if (time >= 10 && !level.isClientSide) {
                this.serverThrow(stack, level, player);
            }
        }
    }

    protected void serverThrow(ItemStack stack, Level level, Player player) {
        int slot = player.m_7655_() == InteractionHand.OFF_HAND ? 40 : player.getInventory().selected;
        boolean projection = stack.getEnchantmentLevel((Enchantment) LWEnchantments.PROJECTION.get()) > 0;
        boolean no_pickup = projection || player.getAbilities().instabuild;
        stack.hurtAndBreak(1, player, pl -> pl.m_21190_(player.m_7655_()));
        AbstractArrow proj = this.getProjectile(level, player, stack, slot);
        proj.setBaseDamage(player.m_21133_(Attributes.ATTACK_DAMAGE));
        proj.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, 2.5F, 1.0F);
        if (no_pickup) {
            proj.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        if (projection) {
            proj.getPersistentData().putInt("DespawnFactor", 20);
        }
        level.m_7967_(proj);
        level.playSound(null, proj, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!no_pickup) {
            player.getInventory().removeItem(stack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
        ItemStack stack = player.m_21120_(pHand);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(stack);
        } else {
            boolean instant = stack.getEnchantmentLevel((Enchantment) LWEnchantments.INSTANT_THROWING.get()) > 0 && !player.m_6144_();
            if (instant) {
                if (!level.isClientSide) {
                    this.serverThrow(stack, level, player);
                    player.getCooldowns().addCooldown(this, LWConfig.COMMON.instantThrowCooldown.get());
                }
            } else {
                player.m_6672_(pHand);
            }
            return InteractionResultHolder.consume(stack);
        }
    }

    public abstract BaseThrownWeaponEntity<?> getProjectile(Level var1, LivingEntity var2, ItemStack var3, int var4);

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.m_43314_() == LCMats.POSEIDITE.getTier() && enchantment == Enchantments.CHANNELING ? true : enchantment == Enchantments.LOYALTY || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
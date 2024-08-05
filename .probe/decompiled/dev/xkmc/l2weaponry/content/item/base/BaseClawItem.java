package dev.xkmc.l2weaponry.content.item.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class BaseClawItem extends DoubleWieldItem {

    private static final String KEY_COUNT = "hit_count";

    private static final String KEY_TIME = "last_hit_time";

    public static int getHitCount(ItemStack stack) {
        return stack.getOrCreateTag().getInt("hit_count");
    }

    public static long getLastTime(ItemStack stack) {
        return stack.getOrCreateTag().getLong("last_hit_time");
    }

    public BaseClawItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config, BlockTags.MINEABLE_WITH_HOE);
        LWItems.CLAW_DECO.add(this);
    }

    @Override
    public void accumulateDamage(ItemStack stack, LivingEntity entity) {
        long gameTime = entity.m_9236_().getGameTime();
        long last = stack.getOrCreateTag().getLong("last_hit_time");
        if (gameTime > last + (long) LWConfig.COMMON.claw_timeout.get().intValue()) {
            stack.getOrCreateTag().putInt("hit_count", 1);
        } else {
            int count = stack.getOrCreateTag().getInt("hit_count");
            count = Math.min(count + 1, this.getMaxStack(stack, entity));
            stack.getOrCreateTag().putInt("hit_count", count);
        }
        stack.getOrCreateTag().putLong("last_hit_time", gameTime);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.m_6883_(stack, level, entity, slot, selected);
        long gameTime = entity.level().getGameTime();
        long last = stack.getOrCreateTag().getLong("last_hit_time");
        if (gameTime > last + (long) LWConfig.COMMON.claw_timeout.get().intValue()) {
            stack.getOrCreateTag().remove("hit_count");
            stack.getOrCreateTag().remove("last_hit_time");
        }
    }

    public int getMaxStack(ItemStack stack, LivingEntity user) {
        int max = LWConfig.COMMON.claw_max.get();
        if (user.getOffhandItem().getItem() == this) {
            max *= 2;
        }
        return max;
    }

    @Override
    public float getMultiplier(AttackCache event) {
        int count = event.getWeapon().getOrCreateTag().getInt("hit_count");
        if (count > 1) {
            int max = this.getMaxStack(event.getWeapon(), event.getAttacker());
            return (float) (1.0 + LWConfig.COMMON.claw_bonus.get() * (double) Mth.clamp(count - 1, 0, max));
        } else {
            return super.getMultiplier(event);
        }
    }

    public float getBlockTime(LivingEntity player) {
        return 0.0F;
    }
}
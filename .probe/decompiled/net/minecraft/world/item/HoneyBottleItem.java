package net.minecraft.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HoneyBottleItem extends Item {

    private static final int DRINK_DURATION = 40;

    public HoneyBottleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        super.finishUsingItem(itemStack0, level1, livingEntity2);
        if (livingEntity2 instanceof ServerPlayer $$3) {
            CriteriaTriggers.CONSUME_ITEM.trigger($$3, itemStack0);
            $$3.m_36246_(Stats.ITEM_USED.get(this));
        }
        if (!level1.isClientSide) {
            livingEntity2.removeEffect(MobEffects.POISON);
        }
        if (itemStack0.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (livingEntity2 instanceof Player && !((Player) livingEntity2).getAbilities().instabuild) {
                ItemStack $$4 = new ItemStack(Items.GLASS_BOTTLE);
                Player $$5 = (Player) livingEntity2;
                if (!$$5.getInventory().add($$4)) {
                    $$5.drop($$4, false);
                }
            }
            return itemStack0;
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        return ItemUtils.startUsingInstantly(level0, player1, interactionHand2);
    }
}
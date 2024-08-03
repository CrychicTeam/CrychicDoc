package net.minecraft.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MilkBucketItem extends Item {

    private static final int DRINK_DURATION = 32;

    public MilkBucketItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        if (livingEntity2 instanceof ServerPlayer $$3) {
            CriteriaTriggers.CONSUME_ITEM.trigger($$3, itemStack0);
            $$3.m_36246_(Stats.ITEM_USED.get(this));
        }
        if (livingEntity2 instanceof Player && !((Player) livingEntity2).getAbilities().instabuild) {
            itemStack0.shrink(1);
        }
        if (!level1.isClientSide) {
            livingEntity2.removeAllEffects();
        }
        return itemStack0.isEmpty() ? new ItemStack(Items.BUCKET) : itemStack0;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        return ItemUtils.startUsingInstantly(level0, player1, interactionHand2);
    }
}
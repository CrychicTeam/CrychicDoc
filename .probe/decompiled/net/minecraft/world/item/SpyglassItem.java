package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SpyglassItem extends Item {

    public static final int USE_DURATION = 1200;

    public static final float ZOOM_FOV_MODIFIER = 0.1F;

    public SpyglassItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        player1.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        player1.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.startUsingInstantly(level0, player1, interactionHand2);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        this.stopUsing(livingEntity2);
        return itemStack0;
    }

    @Override
    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity2, int int3) {
        this.stopUsing(livingEntity2);
    }

    private void stopUsing(LivingEntity livingEntity0) {
        livingEntity0.m_5496_(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
    }
}
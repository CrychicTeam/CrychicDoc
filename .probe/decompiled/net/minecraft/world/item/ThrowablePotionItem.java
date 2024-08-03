package net.minecraft.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public class ThrowablePotionItem extends PotionItem {

    public ThrowablePotionItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (!level0.isClientSide) {
            ThrownPotion $$4 = new ThrownPotion(level0, player1);
            $$4.m_37446_($$3);
            $$4.m_37251_(player1, player1.m_146909_(), player1.m_146908_(), -20.0F, 0.5F, 1.0F);
            level0.m_7967_($$4);
        }
        player1.awardStat(Stats.ITEM_USED.get(this));
        if (!player1.getAbilities().instabuild) {
            $$3.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
    }
}
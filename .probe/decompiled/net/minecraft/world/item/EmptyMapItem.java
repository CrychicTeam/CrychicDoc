package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EmptyMapItem extends ComplexItem {

    public EmptyMapItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (level0.isClientSide) {
            return InteractionResultHolder.success($$3);
        } else {
            if (!player1.getAbilities().instabuild) {
                $$3.shrink(1);
            }
            player1.awardStat(Stats.ITEM_USED.get(this));
            player1.m_9236_().playSound(null, player1, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player1.getSoundSource(), 1.0F, 1.0F);
            ItemStack $$4 = MapItem.create(level0, player1.m_146903_(), player1.m_146907_(), (byte) 0, true, false);
            if ($$3.isEmpty()) {
                return InteractionResultHolder.consume($$4);
            } else {
                if (!player1.getInventory().add($$4.copy())) {
                    player1.drop($$4, false);
                }
                return InteractionResultHolder.consume($$3);
            }
        }
    }
}
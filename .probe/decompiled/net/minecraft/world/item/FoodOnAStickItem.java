package net.minecraft.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FoodOnAStickItem<T extends Entity & ItemSteerable> extends Item {

    private final EntityType<T> canInteractWith;

    private final int consumeItemDamage;

    public FoodOnAStickItem(Item.Properties itemProperties0, EntityType<T> entityTypeT1, int int2) {
        super(itemProperties0);
        this.canInteractWith = entityTypeT1;
        this.consumeItemDamage = int2;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (level0.isClientSide) {
            return InteractionResultHolder.pass($$3);
        } else {
            Entity $$4 = player1.m_275832_();
            if (player1.m_20159_() && $$4 instanceof ItemSteerable $$5 && $$4.getType() == this.canInteractWith && $$5.boost()) {
                $$3.hurtAndBreak(this.consumeItemDamage, player1, p_41312_ -> p_41312_.m_21190_(interactionHand2));
                if ($$3.isEmpty()) {
                    ItemStack $$6 = new ItemStack(Items.FISHING_ROD);
                    $$6.setTag($$3.getTag());
                    return InteractionResultHolder.success($$6);
                }
                return InteractionResultHolder.success($$3);
            }
            player1.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.pass($$3);
        }
    }
}
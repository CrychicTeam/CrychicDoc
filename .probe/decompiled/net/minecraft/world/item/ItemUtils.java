package net.minecraft.world.item;

import java.util.stream.Stream;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ItemUtils {

    public static InteractionResultHolder<ItemStack> startUsingInstantly(Level level0, Player player1, InteractionHand interactionHand2) {
        player1.m_6672_(interactionHand2);
        return InteractionResultHolder.consume(player1.m_21120_(interactionHand2));
    }

    public static ItemStack createFilledResult(ItemStack itemStack0, Player player1, ItemStack itemStack2, boolean boolean3) {
        boolean $$4 = player1.getAbilities().instabuild;
        if (boolean3 && $$4) {
            if (!player1.getInventory().contains(itemStack2)) {
                player1.getInventory().add(itemStack2);
            }
            return itemStack0;
        } else {
            if (!$$4) {
                itemStack0.shrink(1);
            }
            if (itemStack0.isEmpty()) {
                return itemStack2;
            } else {
                if (!player1.getInventory().add(itemStack2)) {
                    player1.drop(itemStack2, false);
                }
                return itemStack0;
            }
        }
    }

    public static ItemStack createFilledResult(ItemStack itemStack0, Player player1, ItemStack itemStack2) {
        return createFilledResult(itemStack0, player1, itemStack2, true);
    }

    public static void onContainerDestroyed(ItemEntity itemEntity0, Stream<ItemStack> streamItemStack1) {
        Level $$2 = itemEntity0.m_9236_();
        if (!$$2.isClientSide) {
            streamItemStack1.forEach(p_289504_ -> $$2.m_7967_(new ItemEntity($$2, itemEntity0.m_20185_(), itemEntity0.m_20186_(), itemEntity0.m_20189_(), p_289504_)));
        }
    }
}
package dev.architectury.hooks.item;

import dev.architectury.hooks.item.forge.ItemStackHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public final class ItemStackHooks {

    private ItemStackHooks() {
    }

    public static ItemStack copyWithCount(ItemStack stack, int count) {
        ItemStack copy = stack.copy();
        copy.setCount(count);
        return copy;
    }

    public static void giveItem(ServerPlayer player, ItemStack stack) {
        boolean bl = player.m_150109_().add(stack);
        if (bl && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity entity = player.m_36176_(stack, false);
            if (entity != null) {
                entity.makeFakeItem();
            }
            player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.m_217043_().nextFloat() - player.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.f_36095_.m_38946_();
        } else {
            ItemEntity entity = player.m_36176_(stack, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.m_20148_());
            }
        }
    }

    @ExpectPlatform
    @Transformed
    public static boolean hasCraftingRemainingItem(ItemStack stack) {
        return ItemStackHooksImpl.hasCraftingRemainingItem(stack);
    }

    @ExpectPlatform
    @Transformed
    public static ItemStack getCraftingRemainingItem(ItemStack stack) {
        return ItemStackHooksImpl.getCraftingRemainingItem(stack);
    }
}
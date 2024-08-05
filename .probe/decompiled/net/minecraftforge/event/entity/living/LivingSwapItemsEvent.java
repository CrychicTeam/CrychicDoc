package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class LivingSwapItemsEvent extends LivingEvent {

    @Internal
    public LivingSwapItemsEvent(LivingEntity entity) {
        super(entity);
    }

    @Cancelable
    public static class Hands extends LivingSwapItemsEvent {

        private ItemStack toMainHand;

        private ItemStack toOffHand;

        @Internal
        public Hands(LivingEntity entity) {
            super(entity);
            this.toMainHand = entity.getOffhandItem();
            this.toOffHand = entity.getMainHandItem();
        }

        public ItemStack getItemSwappedToMainHand() {
            return this.toMainHand;
        }

        public ItemStack getItemSwappedToOffHand() {
            return this.toOffHand;
        }

        public void setItemSwappedToMainHand(ItemStack item) {
            this.toMainHand = item;
        }

        public void setItemSwappedToOffHand(ItemStack item) {
            this.toOffHand = item;
        }
    }
}
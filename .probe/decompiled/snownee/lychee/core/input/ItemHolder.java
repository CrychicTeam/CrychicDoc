package snownee.lychee.core.input;

import java.util.function.Consumer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemHolder {

    public abstract ItemStack get();

    protected abstract void set(ItemStack var1);

    public ItemHolder replace(ItemStack item, Consumer<ItemStack> consumer) {
        if (!this.get().isEmpty()) {
            this.get().shrink(item.getCount());
            if (!this.get().isEmpty()) {
                consumer.accept(this.get());
            }
        }
        this.set(item);
        return this;
    }

    public ItemHolder split(int amount, Consumer<ItemStack> consumer) {
        ItemStack stack = this.get().split(amount);
        if (!this.get().isEmpty()) {
            consumer.accept(this.get());
        }
        this.set(stack);
        return this;
    }

    public static class InWorld extends ItemHolder {

        private final ItemEntity itemEntity;

        public InWorld(ItemEntity itemEntity) {
            this.itemEntity = itemEntity;
        }

        @Override
        public ItemStack get() {
            return this.itemEntity.getItem();
        }

        @Override
        protected void set(ItemStack stack) {
            this.itemEntity.setItem(stack);
        }

        public ItemEntity getEntity() {
            return this.itemEntity;
        }
    }

    public static class Simple extends ItemHolder {

        private ItemStack item;

        public Simple(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack get() {
            return this.item;
        }

        @Override
        protected void set(ItemStack stack) {
            this.item = stack;
        }
    }
}
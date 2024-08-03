package dev.xkmc.modulargolems.content.entity.humanoid;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public interface ItemWrapper {

    ItemWrapper EMPTY = simple(() -> ItemStack.EMPTY, e -> {
    });

    static ItemWrapper simple(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
        return new ItemWrapper.Simple(getter, setter);
    }

    ItemStack getItem();

    void setItem(ItemStack var1);

    public static record Simple(Supplier<ItemStack> getter, Consumer<ItemStack> setter) implements ItemWrapper {

        @Override
        public ItemStack getItem() {
            return (ItemStack) this.getter.get();
        }

        @Override
        public void setItem(ItemStack stack) {
            this.setter.accept(stack);
        }
    }
}
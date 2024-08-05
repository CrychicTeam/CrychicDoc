package dev.latvian.mods.kubejs.item.creativetab;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@FunctionalInterface
public interface CreativeTabIconSupplier {

    CreativeTabIconSupplier DEFAULT = () -> ItemStack.EMPTY;

    ItemStack getIcon();

    public static record Wrapper(CreativeTabIconSupplier supplier) implements Supplier<ItemStack> {

        public ItemStack get() {
            try {
                ItemStack i = ItemStackJS.of(this.supplier.getIcon());
                return i.isEmpty() ? Items.PURPLE_DYE.getDefaultInstance() : i;
            } catch (Exception var2) {
                var2.printStackTrace();
                return Items.PURPLE_DYE.getDefaultInstance();
            }
        }
    }
}
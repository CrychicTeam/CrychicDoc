package dev.latvian.mods.kubejs.item.creativetab;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@FunctionalInterface
public interface CreativeTabContentSupplier {

    CreativeTabContentSupplier DEFAULT = showRestrictedItems -> new ItemStack[0];

    ItemStack[] getContent(boolean var1);

    public static record Wrapper(CreativeTabContentSupplier supplier) implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            List<ItemStack> items = List.of();
            try {
                items = Arrays.stream(this.supplier.getContent(itemDisplayParameters.hasPermissions())).map(ItemStackJS::of).filter(is -> !is.isEmpty()).toList();
            } catch (Exception var6) {
                var6.printStackTrace();
            }
            if (items.isEmpty()) {
                output.accept(Items.PAPER.getDefaultInstance().kjs$withName(Component.literal("Use .content(showRestrictedItems => ['kubejs:example']) to add more items!")));
            } else {
                for (ItemStack item : items) {
                    output.accept(item);
                }
            }
        }
    }
}
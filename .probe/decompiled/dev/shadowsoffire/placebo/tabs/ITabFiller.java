package dev.shadowsoffire.placebo.tabs;

import java.util.function.Supplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface ITabFiller {

    void fillItemCategory(CreativeModeTab var1, CreativeModeTab.Output var2);

    static ITabFiller simple(ItemLike i) {
        return (tab, output) -> output.accept(i.asItem().getDefaultInstance());
    }

    static ITabFiller delegating(Supplier<? extends ItemLike> i) {
        return (tab, output) -> {
            Item item = ((ItemLike) i.get()).asItem();
            if (item instanceof ITabFiller filler) {
                filler.fillItemCategory(tab, output);
            } else {
                output.accept(item.getDefaultInstance());
            }
        };
    }
}
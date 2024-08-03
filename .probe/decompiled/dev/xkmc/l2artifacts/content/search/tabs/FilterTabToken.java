package dev.xkmc.l2artifacts.content.search.tabs;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FilterTabToken<T extends FilterTabBase<T>> {

    public final FilterTabToken.TabFactory<T> factory;

    public final FilterTabType type;

    private final Supplier<Item> item;

    private final Component title;

    public FilterTabToken(FilterTabToken.TabFactory<T> factory, Supplier<Item> item, Component component) {
        this.factory = factory;
        this.type = FilterTabType.RIGHT;
        this.item = item;
        this.title = component;
    }

    public T create(int index, FilterTabManager manager) {
        return this.factory.create(index, this, manager, ((Item) this.item.get()).getDefaultInstance(), this.title);
    }

    public interface TabFactory<T extends FilterTabBase<T>> {

        T create(int var1, FilterTabToken<T> var2, FilterTabManager var3, ItemStack var4, Component var5);
    }
}
package dev.xkmc.modulargolems.content.menu.tabs;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GolemTabToken<G extends GolemTabGroup<G>, T extends GolemTabBase<G, T>> {

    public final GolemTabToken.TabFactory<G, T> factory;

    public final GolemTabType type;

    private final Supplier<Item> item;

    private final Component title;

    public GolemTabToken(GolemTabToken.TabFactory<G, T> factory, Supplier<Item> item, Component component) {
        this.factory = factory;
        this.type = GolemTabType.RIGHT;
        this.item = item;
        this.title = component;
    }

    public T create(int index, GolemTabManager<G> manager) {
        return this.factory.create(index, this, manager, ((Item) this.item.get()).getDefaultInstance(), this.title);
    }

    public interface TabFactory<G extends GolemTabGroup<G>, T extends GolemTabBase<G, T>> {

        T create(int var1, GolemTabToken<G, T> var2, GolemTabManager<G> var3, ItemStack var4, Component var5);
    }
}
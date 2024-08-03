package dev.xkmc.l2weaponry.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.item.Item;

public class LWRegistrate extends L2Registrate {

    public LWRegistrate(String modid) {
        super(modid);
    }

    public <T extends Item> LWItemBuilder<T, L2Registrate> item(String name, NonNullFunction<Item.Properties, T> factory) {
        return this.item((L2Registrate) this.self(), name, factory);
    }

    public <T extends Item, P> LWItemBuilder<T, P> item(P parent, String name, NonNullFunction<Item.Properties, T> factory) {
        return (LWItemBuilder<T, P>) Wrappers.cast((ItemBuilder) this.entry(name, callback -> (ItemBuilder) new LWItemBuilder(this, parent, name, callback, factory).transform(builder -> builder.tab(LWItems.TAB.getKey()))));
    }
}
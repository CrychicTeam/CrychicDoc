package org.violetmoon.zeta.client.event.load;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZAddItemColorHandlers extends IZetaLoadEvent {

    void register(ItemColor var1, ItemLike... var2);

    void registerNamed(Function<Item, ItemColor> var1, String... var2);

    ItemColors getItemColors();

    ZAddItemColorHandlers.Post makePostEvent();

    public interface Post extends ZAddItemColorHandlers {

        Map<String, Function<Item, ItemColor>> getNamedItemColors();
    }
}
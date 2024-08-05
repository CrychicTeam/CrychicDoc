package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

@Info("Invoked after all items are registered to modify them.\n")
public class ItemModificationEventJS extends EventJS {

    @Info("Modifies items matching the given ingredient.\n\n**NOTE**: tag ingredients are not supported at this time.\n")
    public void modify(Ingredient in, Consumer<Item> c) {
        for (Item item : in.kjs$getItemTypes()) {
            c.accept(item);
        }
    }
}
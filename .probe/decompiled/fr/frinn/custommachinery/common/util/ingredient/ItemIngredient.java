package fr.frinn.custommachinery.common.util.ingredient;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemIngredient implements IIngredient<Item> {

    private final Item item;

    public ItemIngredient(Item item) {
        this.item = item;
    }

    @Override
    public List<Item> getAll() {
        return Collections.singletonList(this.item);
    }

    public boolean test(Item item) {
        return this.item == item;
    }

    public String toString() {
        return BuiltInRegistries.ITEM.getKey(this.item).toString();
    }
}
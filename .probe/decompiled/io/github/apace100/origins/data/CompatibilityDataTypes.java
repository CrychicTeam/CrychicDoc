package io.github.apace100.origins.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CompatibilityDataTypes {

    public static final SerializableDataType<ItemStack> ITEM_OR_ITEM_STACK = new SerializableDataType(ItemStack.class, SerializableDataTypes.ITEM_STACK::send, SerializableDataTypes.ITEM_STACK::receive, jsonElement -> {
        if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
            Item item = (Item) SerializableDataTypes.ITEM.read(jsonElement);
            return new ItemStack(item);
        } else {
            return (ItemStack) SerializableDataTypes.ITEM_STACK.read(jsonElement);
        }
    });
}
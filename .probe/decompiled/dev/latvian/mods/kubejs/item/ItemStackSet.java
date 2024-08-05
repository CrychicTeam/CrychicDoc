package dev.latvian.mods.kubejs.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemStackSet implements Iterable<ItemStack> {

    private final HashMap<ItemStackKey, ItemStack> map;

    public ItemStackSet(int initialSize) {
        this.map = new HashMap(initialSize);
    }

    public ItemStackSet() {
        this(2);
    }

    public ItemStackSet(ItemStack... items) {
        this(items.length);
        for (ItemStack stack : items) {
            this.add(stack);
        }
    }

    public void add(ItemStack stack) {
        ItemStackKey key = ItemStackKey.of(stack);
        if (key != ItemStackKey.EMPTY) {
            this.map.putIfAbsent(key, stack);
        }
    }

    public void addItem(Item item) {
        if (item != Items.AIR) {
            this.map.putIfAbsent(item.kjs$getTypeItemStackKey(), new ItemStack(item));
        }
    }

    public void remove(ItemStack stack) {
        ItemStackKey key = ItemStackKey.of(stack);
        if (key != ItemStackKey.EMPTY) {
            this.map.remove(key);
        }
    }

    public boolean contains(ItemStack stack) {
        ItemStackKey key = ItemStackKey.of(stack);
        return key != ItemStackKey.EMPTY && this.map.containsKey(key);
    }

    public List<ItemStack> toList() {
        return (List<ItemStack>) (this.map.isEmpty() ? List.of() : new ArrayList(this.map.values()));
    }

    public ItemStack[] toArray() {
        return this.map.isEmpty() ? ItemStackJS.EMPTY_ARRAY : (ItemStack[]) this.map.values().toArray(ItemStackJS.EMPTY_ARRAY);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public int size() {
        return this.map.size();
    }

    public Iterator<ItemStack> iterator() {
        return this.map.values().iterator();
    }

    public void forEach(Consumer<? super ItemStack> action) {
        this.map.values().forEach(action);
    }

    public ItemStack getFirst() {
        return this.map.isEmpty() ? ItemStack.EMPTY : (ItemStack) this.map.values().iterator().next();
    }
}
package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.LootEntry;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class AddLootAction implements ILootAction {

    private final LootEntry[] entries;

    private final AddLootAction.AddType type;

    public AddLootAction(LootEntry[] entries) {
        this.entries = entries;
        this.type = AddLootAction.AddType.DEFAULT;
    }

    public AddLootAction(LootEntry[] entries, AddLootAction.AddType type) {
        this.entries = entries;
        this.type = type;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (LootEntry itemStack : this.entries) {
            ItemStack item = itemStack.createItem(context);
            if (item != null) {
                loot.add(item);
                if (this.type == AddLootAction.AddType.ALTERNATIVES) {
                    return true;
                }
            } else if (this.type == AddLootAction.AddType.SEQUENCE) {
                return true;
            }
        }
        return true;
    }

    public static enum AddType {

        DEFAULT, SEQUENCE, ALTERNATIVES
    }
}
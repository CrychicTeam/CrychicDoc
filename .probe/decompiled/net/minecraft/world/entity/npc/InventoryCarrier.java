package net.minecraft.world.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface InventoryCarrier {

    String TAG_INVENTORY = "Inventory";

    SimpleContainer getInventory();

    static void pickUpItem(Mob mob0, InventoryCarrier inventoryCarrier1, ItemEntity itemEntity2) {
        ItemStack $$3 = itemEntity2.getItem();
        if (mob0.wantsToPickUp($$3)) {
            SimpleContainer $$4 = inventoryCarrier1.getInventory();
            boolean $$5 = $$4.canAddItem($$3);
            if (!$$5) {
                return;
            }
            mob0.m_21053_(itemEntity2);
            int $$6 = $$3.getCount();
            ItemStack $$7 = $$4.addItem($$3);
            mob0.m_7938_(itemEntity2, $$6 - $$7.getCount());
            if ($$7.isEmpty()) {
                itemEntity2.m_146870_();
            } else {
                $$3.setCount($$7.getCount());
            }
        }
    }

    default void readInventoryFromTag(CompoundTag compoundTag0) {
        if (compoundTag0.contains("Inventory", 9)) {
            this.getInventory().fromTag(compoundTag0.getList("Inventory", 10));
        }
    }

    default void writeInventoryToTag(CompoundTag compoundTag0) {
        compoundTag0.put("Inventory", this.getInventory().createTag());
    }
}
package com.mna.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ConstructCommandInventory extends SimpleContainer {

    public ConstructCommandInventory(int size) {
        super(size);
    }

    @Override
    public void fromTag(ListTag list) {
        for (int i = 0; i < list.size(); i++) {
            CompoundTag nbt = (CompoundTag) list.get(i);
            if (nbt.contains("item") && nbt.contains("index")) {
                ItemStack itemstack = ItemStack.of(nbt.getCompound("item"));
                int index = nbt.getInt("index");
                this.m_6836_(index, itemstack);
            }
        }
    }

    @Override
    public ListTag createTag() {
        ListTag listtag = new ListTag();
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack itemstack = this.m_8020_(i);
            if (!itemstack.isEmpty()) {
                CompoundTag item = itemstack.save(new CompoundTag());
                CompoundTag tag = new CompoundTag();
                tag.put("item", item);
                tag.putInt("index", i);
                listtag.add(tag);
            }
        }
        return listtag;
    }
}
package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import net.minecraft.world.item.ItemStack;

public class EquipmentsContainer extends BaseContainerMenu.BaseContainer<EquipmentsMenu> {

    public EquipmentsContainer(EquipmentsMenu menu) {
        super(0, menu);
    }

    @Override
    public ItemStack getItem(int index) {
        return this.parent.golem == null ? ItemStack.EMPTY : this.parent.golem.getItemBySlot(EquipmentsMenu.SLOTS[index]);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (this.parent.golem != null) {
            this.parent.golem.setItemSlot(EquipmentsMenu.SLOTS[index], stack);
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.parent.golem == null ? ItemStack.EMPTY : this.parent.golem.getItemBySlot(EquipmentsMenu.SLOTS[index]).split(count);
    }
}
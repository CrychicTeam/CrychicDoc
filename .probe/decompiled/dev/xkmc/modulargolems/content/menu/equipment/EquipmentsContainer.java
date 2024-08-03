package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.ItemWrapper;
import net.minecraft.world.item.ItemStack;

public class EquipmentsContainer extends BaseContainerMenu.BaseContainer<EquipmentsMenu> {

    public EquipmentsContainer(EquipmentsMenu menu) {
        super(0, menu);
    }

    private ItemWrapper getWrapper(int index) {
        if (this.parent.golem == null || index < 0) {
            return ItemWrapper.EMPTY;
        } else if (index < 6) {
            return this.parent.golem.getWrapperOfHand(EquipmentsMenu.SLOTS[index]);
        } else if (this.parent.golem instanceof HumanoidGolemEntity humanoid) {
            if (index == 6) {
                return humanoid.getBackupHand();
            } else {
                return index == 7 ? humanoid.getArrowSlot() : ItemWrapper.EMPTY;
            }
        } else {
            return ItemWrapper.EMPTY;
        }
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getWrapper(index).getItem();
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.getWrapper(index).setItem(stack);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.getWrapper(index).getItem().split(count);
    }
}
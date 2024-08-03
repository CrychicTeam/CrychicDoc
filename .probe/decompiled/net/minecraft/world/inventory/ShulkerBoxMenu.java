package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShulkerBoxMenu extends AbstractContainerMenu {

    private static final int CONTAINER_SIZE = 27;

    private final Container container;

    public ShulkerBoxMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, new SimpleContainer(27));
    }

    public ShulkerBoxMenu(int int0, Inventory inventory1, Container container2) {
        super(MenuType.SHULKER_BOX, int0);
        m_38869_(container2, 27);
        this.container = container2;
        container2.startOpen(inventory1.player);
        int $$3 = 3;
        int $$4 = 9;
        for (int $$5 = 0; $$5 < 3; $$5++) {
            for (int $$6 = 0; $$6 < 9; $$6++) {
                this.m_38897_(new ShulkerBoxSlot(container2, $$6 + $$5 * 9, 8 + $$6 * 18, 18 + $$5 * 18));
            }
        }
        for (int $$7 = 0; $$7 < 3; $$7++) {
            for (int $$8 = 0; $$8 < 9; $$8++) {
                this.m_38897_(new Slot(inventory1, $$8 + $$7 * 9 + 9, 8 + $$8 * 18, 84 + $$7 * 18));
            }
        }
        for (int $$9 = 0; $$9 < 9; $$9++) {
            this.m_38897_(new Slot(inventory1, $$9, 8 + $$9 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.container.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 < this.container.getContainerSize()) {
                if (!this.m_38903_($$4, this.container.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }
        }
        return $$2;
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.container.stopOpen(player0);
    }
}
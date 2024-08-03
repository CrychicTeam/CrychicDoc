package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HopperMenu extends AbstractContainerMenu {

    public static final int CONTAINER_SIZE = 5;

    private final Container hopper;

    public HopperMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, new SimpleContainer(5));
    }

    public HopperMenu(int int0, Inventory inventory1, Container container2) {
        super(MenuType.HOPPER, int0);
        this.hopper = container2;
        m_38869_(container2, 5);
        container2.startOpen(inventory1.player);
        int $$3 = 51;
        for (int $$4 = 0; $$4 < 5; $$4++) {
            this.m_38897_(new Slot(container2, $$4, 44 + $$4 * 18, 20));
        }
        for (int $$5 = 0; $$5 < 3; $$5++) {
            for (int $$6 = 0; $$6 < 9; $$6++) {
                this.m_38897_(new Slot(inventory1, $$6 + $$5 * 9 + 9, 8 + $$6 * 18, $$5 * 18 + 51));
            }
        }
        for (int $$7 = 0; $$7 < 9; $$7++) {
            this.m_38897_(new Slot(inventory1, $$7, 8 + $$7 * 18, 109));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.hopper.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 < this.hopper.getContainerSize()) {
                if (!this.m_38903_($$4, this.hopper.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 0, this.hopper.getContainerSize(), false)) {
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
        this.hopper.stopOpen(player0);
    }
}
package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ChestMenu extends AbstractContainerMenu {

    private static final int SLOTS_PER_ROW = 9;

    private final Container container;

    private final int containerRows;

    private ChestMenu(MenuType<?> menuType0, int int1, Inventory inventory2, int int3) {
        this(menuType0, int1, inventory2, new SimpleContainer(9 * int3), int3);
    }

    public static ChestMenu oneRow(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x1, int0, inventory1, 1);
    }

    public static ChestMenu twoRows(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x2, int0, inventory1, 2);
    }

    public static ChestMenu threeRows(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x3, int0, inventory1, 3);
    }

    public static ChestMenu fourRows(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x4, int0, inventory1, 4);
    }

    public static ChestMenu fiveRows(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x5, int0, inventory1, 5);
    }

    public static ChestMenu sixRows(int int0, Inventory inventory1) {
        return new ChestMenu(MenuType.GENERIC_9x6, int0, inventory1, 6);
    }

    public static ChestMenu threeRows(int int0, Inventory inventory1, Container container2) {
        return new ChestMenu(MenuType.GENERIC_9x3, int0, inventory1, container2, 3);
    }

    public static ChestMenu sixRows(int int0, Inventory inventory1, Container container2) {
        return new ChestMenu(MenuType.GENERIC_9x6, int0, inventory1, container2, 6);
    }

    public ChestMenu(MenuType<?> menuType0, int int1, Inventory inventory2, Container container3, int int4) {
        super(menuType0, int1);
        m_38869_(container3, int4 * 9);
        this.container = container3;
        this.containerRows = int4;
        container3.startOpen(inventory2.player);
        int $$5 = (this.containerRows - 4) * 18;
        for (int $$6 = 0; $$6 < this.containerRows; $$6++) {
            for (int $$7 = 0; $$7 < 9; $$7++) {
                this.m_38897_(new Slot(container3, $$7 + $$6 * 9, 8 + $$7 * 18, 18 + $$6 * 18));
            }
        }
        for (int $$8 = 0; $$8 < 3; $$8++) {
            for (int $$9 = 0; $$9 < 9; $$9++) {
                this.m_38897_(new Slot(inventory2, $$9 + $$8 * 9 + 9, 8 + $$9 * 18, 103 + $$8 * 18 + $$5));
            }
        }
        for (int $$10 = 0; $$10 < 9; $$10++) {
            this.m_38897_(new Slot(inventory2, $$10, 8 + $$10 * 18, 161 + $$5));
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
            if (int1 < this.containerRows * 9) {
                if (!this.m_38903_($$4, this.containerRows * 9, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 0, this.containerRows * 9, false)) {
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

    public Container getContainer() {
        return this.container;
    }

    public int getRowCount() {
        return this.containerRows;
    }
}
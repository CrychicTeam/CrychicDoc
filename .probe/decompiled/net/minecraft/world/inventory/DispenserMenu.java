package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DispenserMenu extends AbstractContainerMenu {

    private static final int SLOT_COUNT = 9;

    private static final int INV_SLOT_START = 9;

    private static final int INV_SLOT_END = 36;

    private static final int USE_ROW_SLOT_START = 36;

    private static final int USE_ROW_SLOT_END = 45;

    private final Container dispenser;

    public DispenserMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, new SimpleContainer(9));
    }

    public DispenserMenu(int int0, Inventory inventory1, Container container2) {
        super(MenuType.GENERIC_3x3, int0);
        m_38869_(container2, 9);
        this.dispenser = container2;
        container2.startOpen(inventory1.player);
        for (int $$3 = 0; $$3 < 3; $$3++) {
            for (int $$4 = 0; $$4 < 3; $$4++) {
                this.m_38897_(new Slot(container2, $$4 + $$3 * 3, 62 + $$4 * 18, 17 + $$3 * 18));
            }
        }
        for (int $$5 = 0; $$5 < 3; $$5++) {
            for (int $$6 = 0; $$6 < 9; $$6++) {
                this.m_38897_(new Slot(inventory1, $$6 + $$5 * 9 + 9, 8 + $$6 * 18, 84 + $$5 * 18));
            }
        }
        for (int $$7 = 0; $$7 < 9; $$7++) {
            this.m_38897_(new Slot(inventory1, $$7, 8 + $$7 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.dispenser.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 < 9) {
                if (!this.m_38903_($$4, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }
            $$3.onTake(player0, $$4);
        }
        return $$2;
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.dispenser.stopOpen(player0);
    }
}
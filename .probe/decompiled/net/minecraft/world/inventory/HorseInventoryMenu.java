package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HorseInventoryMenu extends AbstractContainerMenu {

    private final Container horseContainer;

    private final AbstractHorse horse;

    public HorseInventoryMenu(int int0, Inventory inventory1, Container container2, final AbstractHorse abstractHorse3) {
        super(null, int0);
        this.horseContainer = container2;
        this.horse = abstractHorse3;
        int $$4 = 3;
        container2.startOpen(inventory1.player);
        int $$5 = -18;
        this.m_38897_(new Slot(container2, 0, 8, 18) {

            @Override
            public boolean mayPlace(ItemStack p_39677_) {
                return p_39677_.is(Items.SADDLE) && !this.m_6657_() && abstractHorse3.isSaddleable();
            }

            @Override
            public boolean isActive() {
                return abstractHorse3.isSaddleable();
            }
        });
        this.m_38897_(new Slot(container2, 1, 8, 36) {

            @Override
            public boolean mayPlace(ItemStack p_39690_) {
                return abstractHorse3.isArmor(p_39690_);
            }

            @Override
            public boolean isActive() {
                return abstractHorse3.canWearArmor();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        if (this.hasChest(abstractHorse3)) {
            for (int $$6 = 0; $$6 < 3; $$6++) {
                for (int $$7 = 0; $$7 < ((AbstractChestedHorse) abstractHorse3).getInventoryColumns(); $$7++) {
                    this.m_38897_(new Slot(container2, 2 + $$7 + $$6 * ((AbstractChestedHorse) abstractHorse3).getInventoryColumns(), 80 + $$7 * 18, 18 + $$6 * 18));
                }
            }
        }
        for (int $$8 = 0; $$8 < 3; $$8++) {
            for (int $$9 = 0; $$9 < 9; $$9++) {
                this.m_38897_(new Slot(inventory1, $$9 + $$8 * 9 + 9, 8 + $$9 * 18, 102 + $$8 * 18 + -18));
            }
        }
        for (int $$10 = 0; $$10 < 9; $$10++) {
            this.m_38897_(new Slot(inventory1, $$10, 8 + $$10 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return !this.horse.hasInventoryChanged(this.horseContainer) && this.horseContainer.stillValid(player0) && this.horse.m_6084_() && this.horse.m_20270_(player0) < 8.0F;
    }

    private boolean hasChest(AbstractHorse abstractHorse0) {
        return abstractHorse0 instanceof AbstractChestedHorse && ((AbstractChestedHorse) abstractHorse0).hasChest();
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            int $$5 = this.horseContainer.getContainerSize();
            if (int1 < $$5) {
                if (!this.m_38903_($$4, $$5, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(1).mayPlace($$4) && !this.m_38853_(1).hasItem()) {
                if (!this.m_38903_($$4, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(0).mayPlace($$4)) {
                if (!this.m_38903_($$4, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$5 <= 2 || !this.m_38903_($$4, 2, $$5, false)) {
                int $$7 = $$5 + 27;
                int $$9 = $$7 + 9;
                if (int1 >= $$7 && int1 < $$9) {
                    if (!this.m_38903_($$4, $$5, $$7, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= $$5 && int1 < $$7) {
                    if (!this.m_38903_($$4, $$7, $$9, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_($$4, $$7, $$7, false)) {
                    return ItemStack.EMPTY;
                }
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
        this.horseContainer.stopOpen(player0);
    }
}
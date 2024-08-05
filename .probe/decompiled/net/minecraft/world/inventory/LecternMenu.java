package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LecternMenu extends AbstractContainerMenu {

    private static final int DATA_COUNT = 1;

    private static final int SLOT_COUNT = 1;

    public static final int BUTTON_PREV_PAGE = 1;

    public static final int BUTTON_NEXT_PAGE = 2;

    public static final int BUTTON_TAKE_BOOK = 3;

    public static final int BUTTON_PAGE_JUMP_RANGE_START = 100;

    private final Container lectern;

    private final ContainerData lecternData;

    public LecternMenu(int int0) {
        this(int0, new SimpleContainer(1), new SimpleContainerData(1));
    }

    public LecternMenu(int int0, Container container1, ContainerData containerData2) {
        super(MenuType.LECTERN, int0);
        m_38869_(container1, 1);
        m_38886_(containerData2, 1);
        this.lectern = container1;
        this.lecternData = containerData2;
        this.m_38897_(new Slot(container1, 0, 0, 0) {

            @Override
            public void setChanged() {
                super.setChanged();
                LecternMenu.this.m_6199_(this.f_40218_);
            }
        });
        this.m_38884_(containerData2);
    }

    @Override
    public boolean clickMenuButton(Player player0, int int1) {
        if (int1 >= 100) {
            int $$2 = int1 - 100;
            this.setData(0, $$2);
            return true;
        } else {
            switch(int1) {
                case 1:
                    int $$4 = this.lecternData.get(0);
                    this.setData(0, $$4 - 1);
                    return true;
                case 2:
                    int $$3 = this.lecternData.get(0);
                    this.setData(0, $$3 + 1);
                    return true;
                case 3:
                    if (!player0.mayBuild()) {
                        return false;
                    }
                    ItemStack $$5 = this.lectern.removeItemNoUpdate(0);
                    this.lectern.setChanged();
                    if (!player0.getInventory().add($$5)) {
                        player0.drop($$5, false);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setData(int int0, int int1) {
        super.setData(int0, int1);
        this.m_38946_();
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.lectern.stillValid(player0);
    }

    public ItemStack getBook() {
        return this.lectern.getItem(0);
    }

    public int getPage() {
        return this.lecternData.get(0);
    }
}
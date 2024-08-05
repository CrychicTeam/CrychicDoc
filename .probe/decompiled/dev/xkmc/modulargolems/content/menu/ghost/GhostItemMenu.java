package dev.xkmc.modulargolems.content.menu.ghost;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class GhostItemMenu extends AbstractContainerMenu {

    protected final Inventory inventory;

    public final MenuLayoutConfig sprite;

    protected final Container container;

    private int added = 0;

    protected GhostItemMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, Container container) {
        super(type, wid);
        this.inventory = plInv;
        this.sprite = manager.get();
        this.container = container;
        int x = this.sprite.getPlInvX();
        int y = this.sprite.getPlInvY();
        this.bindPlayerInventory(plInv, x, y);
    }

    protected void bindPlayerInventory(Inventory plInv, int x, int y) {
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(plInv, j + k * 9 + 9, x + j * 18, y + k * 18));
            }
        }
        for (int var6 = 0; var6 < 9; var6++) {
            this.m_38897_(new Slot(plInv, var6, x + var6 * 18, y + 58));
        }
    }

    protected void addSlot(String name, Predicate<ItemStack> pred) {
        this.sprite.getSlot(name, (x, y) -> new PredSlot(this.container, this.added++, x, y, pred), (n, i, j, s) -> this.m_38897_(s));
    }

    protected abstract IGhostContainer getContainer(int var1);

    protected ItemStack getSlotContent(int slot) {
        return this.getContainer(slot).getItem(slot);
    }

    public void setSlotContent(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            this.removeContent(slot);
        } else if (this.getContainer(slot).getItem(slot).isEmpty()) {
            this.tryAddContent(slot, stack);
        } else {
            stack = stack.copy();
            stack.setCount(1);
            if (this.getContainer(slot).internalMatch(stack)) {
                return;
            }
            this.getContainer(slot).set(slot, stack);
        }
    }

    protected void tryAddContent(int slot, ItemStack stack) {
        IGhostContainer cont = this.getContainer(slot);
        if (cont.listSize() < cont.getContainerSize()) {
            stack = stack.copy();
            stack.setCount(1);
            if (cont.internalMatch(stack)) {
                return;
            }
            cont.set(-1, stack);
        }
    }

    protected void removeContent(int slot) {
        if (slot >= 0 && !this.getContainer(slot).getItem(slot).isEmpty()) {
            this.getContainer(slot).set(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId < 36) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        } else if (clickTypeIn != ClickType.THROW) {
            ItemStack held = this.m_142621_();
            int slot = slotId - 36;
            if (clickTypeIn == ClickType.CLONE) {
                if (player.isCreative() && held.isEmpty()) {
                    ItemStack insert = this.getSlotContent(slot).copy();
                    insert.setCount(insert.getMaxStackSize());
                    this.m_142503_(insert);
                }
            } else {
                ItemStack insert;
                if (held.isEmpty()) {
                    insert = ItemStack.EMPTY;
                } else {
                    insert = held.copy();
                    insert.setCount(1);
                }
                this.setSlotContent(slot, insert);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.m_6084_();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (index < 36) {
            ItemStack stackToInsert = this.m_38853_(index).getItem();
            this.tryAddContent(0, stackToInsert);
        } else {
            this.removeContent(index - 36);
        }
        return ItemStack.EMPTY;
    }
}
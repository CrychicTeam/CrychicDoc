package dev.latvian.mods.kubejs.gui.chest;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class ChestMenuSlot {

    public final ChestMenuData gui;

    public final int index;

    public final int x;

    public final int y;

    private ItemStack item;

    public int clicked;

    public Map<String, Object> data;

    public final List<ChestMenuClickHandler> clickHandlers;

    public InventoryKJS inventory;

    public int inventorySlot;

    public ChestMenuSlot(ChestMenuData gui, int index) {
        this.gui = gui;
        this.index = index;
        this.x = index % 9;
        this.y = index / 9;
        this.item = ItemStack.EMPTY;
        this.clicked = 0;
        this.data = new HashMap();
        this.clickHandlers = new ArrayList(1);
        this.inventory = null;
        this.inventorySlot = -1;
    }

    public String toString() {
        return "Slot[" + this.x + "," + this.y + "]";
    }

    public void setItem(ItemStack stack) {
        if (this.inventory != null && this.inventorySlot >= 0) {
            this.inventory.kjs$setStackInSlot(this.inventorySlot, stack);
        } else {
            this.item = stack;
        }
    }

    public ItemStack getItem() {
        return this.inventory != null && this.inventorySlot >= 0 ? this.inventory.kjs$getStackInSlot(this.inventorySlot) : this.item;
    }

    public void resetClickHandlers() {
        this.clickHandlers.clear();
    }

    public void clicked(ClickType type, int button, ChestMenuClickEvent.Callback callback, boolean autoHandle) {
        this.clickHandlers.add(new ChestMenuClickHandler(type, button, callback, autoHandle));
    }

    public void setLeftClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.PICKUP, 0, callback, true);
    }

    public void setRightClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.PICKUP, 1, callback, true);
    }

    public void setMiddleClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.CLONE, 2, callback, true);
    }

    public void setSwapped(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.SWAP, -1, callback, true);
    }

    public void setThrown(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.THROW, -1, callback, true);
    }

    public void setShiftLeftClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.QUICK_MOVE, 0, callback, true);
    }

    public void setShiftRightClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.QUICK_MOVE, 1, callback, true);
    }

    public void setDoubleClicked(ChestMenuClickEvent.Callback callback) {
        this.clicked(ClickType.PICKUP_ALL, -1, callback, true);
    }
}
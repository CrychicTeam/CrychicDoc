package dev.latvian.mods.kubejs.gui.chest;

import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestMenuInventoryClickEvent {

    private final Slot slot;

    public final ClickType type;

    public final int button;

    public ChestMenuInventoryClickEvent(Slot slot, ClickType type, int button) {
        this.slot = slot;
        this.type = type;
        this.button = button;
    }

    public int getIndex() {
        return this.slot.getContainerSlot();
    }

    public ItemStack getItem() {
        return this.slot.getItem();
    }

    public void setItem(ItemStack item) {
        this.slot.set(item);
    }

    public interface Callback {

        void onClick(ChestMenuInventoryClickEvent var1);
    }
}
package net.minecraftforge.event;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public class ItemStackedOnOtherEvent extends Event {

    private final ItemStack carriedItem;

    private final ItemStack stackedOnItem;

    private final Slot slot;

    private final ClickAction action;

    private final Player player;

    private final SlotAccess carriedSlotAccess;

    @Internal
    public ItemStackedOnOtherEvent(ItemStack carriedItem, ItemStack stackedOnItem, Slot slot, ClickAction action, Player player, SlotAccess carriedSlotAccess) {
        this.carriedItem = carriedItem;
        this.stackedOnItem = stackedOnItem;
        this.slot = slot;
        this.action = action;
        this.player = player;
        this.carriedSlotAccess = carriedSlotAccess;
    }

    public ItemStack getCarriedItem() {
        return this.carriedItem;
    }

    public ItemStack getStackedOnItem() {
        return this.stackedOnItem;
    }

    public Slot getSlot() {
        return this.slot;
    }

    public ClickAction getClickAction() {
        return this.action;
    }

    public Player getPlayer() {
        return this.player;
    }

    public SlotAccess getCarriedSlotAccess() {
        return this.carriedSlotAccess;
    }
}
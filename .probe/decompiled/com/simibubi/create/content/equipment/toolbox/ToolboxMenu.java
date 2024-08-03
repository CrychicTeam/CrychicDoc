package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ToolboxMenu extends MenuBase<ToolboxBlockEntity> {

    public boolean renderPass;

    public ToolboxMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ToolboxMenu(MenuType<?> type, int id, Inventory inv, ToolboxBlockEntity be) {
        super(type, id, inv, be);
        be.startOpen(this.player);
    }

    public static ToolboxMenu create(int id, Inventory inv, ToolboxBlockEntity be) {
        return new ToolboxMenu((MenuType<?>) AllMenuTypes.TOOLBOX.get(), id, inv, be);
    }

    protected ToolboxBlockEntity createOnClient(FriendlyByteBuf extraData) {
        BlockPos readBlockPos = extraData.readBlockPos();
        CompoundTag readNbt = extraData.readNbt();
        ClientLevel world = Minecraft.getInstance().level;
        if (world.m_7702_(readBlockPos) instanceof ToolboxBlockEntity toolbox) {
            toolbox.readClient(readNbt);
            return toolbox;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot clickedSlot = this.m_38853_(index);
        if (!clickedSlot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = clickedSlot.getItem();
            int size = this.contentHolder.inventory.getSlots();
            boolean success = false;
            if (index < size) {
                success = !this.m_38903_(stack, size, this.f_38839_.size(), false);
                this.contentHolder.inventory.onContentsChanged(index);
            } else {
                success = !this.m_38903_(stack, 0, size - 1, false);
            }
            return success ? ItemStack.EMPTY : stack;
        }
    }

    protected void initAndReadInventory(ToolboxBlockEntity contentHolder) {
    }

    @Override
    public void clicked(int index, int flags, ClickType type, Player player) {
        int size = this.contentHolder.inventory.getSlots();
        if (index >= 0 && index < size) {
            ItemStack itemInClickedSlot = this.m_38853_(index).getItem();
            ItemStack carried = this.m_142621_();
            if (type == ClickType.PICKUP && !carried.isEmpty() && !itemInClickedSlot.isEmpty() && ToolboxInventory.canItemsShareCompartment(itemInClickedSlot, carried)) {
                int subIndex = index % 4;
                if (subIndex != 3) {
                    this.clicked(index - subIndex + 4 - 1, flags, type, player);
                    return;
                }
            }
            if (type == ClickType.PICKUP && carried.isEmpty() && itemInClickedSlot.isEmpty() && !player.m_9236_().isClientSide) {
                this.contentHolder.inventory.filters.set(index / 4, ItemStack.EMPTY);
                this.contentHolder.sendData();
            }
        }
        super.m_150399_(index, flags, type, player);
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return slot.index > this.contentHolder.inventory.getSlots() && super.m_5622_(slot);
    }

    public ItemStack getFilter(int compartment) {
        return (ItemStack) this.contentHolder.inventory.filters.get(compartment);
    }

    public int totalCountInCompartment(int compartment) {
        int count = 0;
        int baseSlot = compartment * 4;
        for (int i = 0; i < 4; i++) {
            count += this.m_38853_(baseSlot + i).getItem().getCount();
        }
        return count;
    }

    @Override
    protected void addSlots() {
        ToolboxInventory inventory = this.contentHolder.inventory;
        int x = 79;
        int y = 37;
        int[] xOffsets = new int[] { x, x + 33, x + 66, x + 66 + 6, x + 66, x + 33, x, x - 6 };
        int[] yOffsets = new int[] { y, y - 6, y, y + 33, y + 66, y + 66 + 6, y + 66, y + 33 };
        for (int compartment = 0; compartment < 8; compartment++) {
            int baseIndex = compartment * 4;
            this.m_38897_(new ToolboxSlot(this, inventory, baseIndex, xOffsets[compartment], yOffsets[compartment]));
            for (int i = 1; i < 4; i++) {
                this.m_38897_(new SlotItemHandler(inventory, baseIndex + i, -10000, -10000));
            }
        }
        this.addPlayerSlots(8, 165);
    }

    protected void saveData(ToolboxBlockEntity contentHolder) {
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (!playerIn.m_9236_().isClientSide) {
            this.contentHolder.stopOpen(playerIn);
        }
    }
}
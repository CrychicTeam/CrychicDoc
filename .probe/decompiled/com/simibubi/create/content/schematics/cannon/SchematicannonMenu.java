package com.simibubi.create.content.schematics.cannon;

import com.simibubi.create.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SchematicannonMenu extends MenuBase<SchematicannonBlockEntity> {

    public SchematicannonMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf buffer) {
        super(type, id, inv, buffer);
    }

    public SchematicannonMenu(MenuType<?> type, int id, Inventory inv, SchematicannonBlockEntity be) {
        super(type, id, inv, be);
    }

    public static SchematicannonMenu create(int id, Inventory inv, SchematicannonBlockEntity be) {
        return new SchematicannonMenu((MenuType<?>) AllMenuTypes.SCHEMATICANNON.get(), id, inv, be);
    }

    protected SchematicannonBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world.m_7702_(extraData.readBlockPos()) instanceof SchematicannonBlockEntity schematicannon) {
            schematicannon.readClient(extraData.readNbt());
            return schematicannon;
        } else {
            return null;
        }
    }

    protected void initAndReadInventory(SchematicannonBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
        int x = 0;
        int y = 0;
        this.m_38897_(new SlotItemHandler(this.contentHolder.inventory, 0, x + 15, y + 65));
        this.m_38897_(new SlotItemHandler(this.contentHolder.inventory, 1, x + 171, y + 65));
        this.m_38897_(new SlotItemHandler(this.contentHolder.inventory, 2, x + 134, y + 19));
        this.m_38897_(new SlotItemHandler(this.contentHolder.inventory, 3, x + 174, y + 19));
        this.m_38897_(new SlotItemHandler(this.contentHolder.inventory, 4, x + 15, y + 19));
        this.addPlayerSlots(37, 161);
    }

    protected void saveData(SchematicannonBlockEntity contentHolder) {
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot clickedSlot = this.m_38853_(index);
        if (!clickedSlot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = clickedSlot.getItem();
            if (index < 5) {
                this.m_38903_(stack, 5, this.f_38839_.size(), false);
            } else if (!this.m_38903_(stack, 0, 1, false) && !this.m_38903_(stack, 2, 3, false) && this.m_38903_(stack, 4, 5, false)) {
            }
            return ItemStack.EMPTY;
        }
    }
}
package com.simibubi.create.content.schematics.table;

import com.simibubi.create.AllItems;
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

public class SchematicTableMenu extends MenuBase<SchematicTableBlockEntity> {

    private Slot inputSlot;

    private Slot outputSlot;

    public SchematicTableMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public SchematicTableMenu(MenuType<?> type, int id, Inventory inv, SchematicTableBlockEntity be) {
        super(type, id, inv, be);
    }

    public static SchematicTableMenu create(int id, Inventory inv, SchematicTableBlockEntity be) {
        return new SchematicTableMenu((MenuType<?>) AllMenuTypes.SCHEMATIC_TABLE.get(), id, inv, be);
    }

    public boolean canWrite() {
        return this.inputSlot.hasItem() && !this.outputSlot.hasItem();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot clickedSlot = this.m_38853_(index);
        if (!clickedSlot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = clickedSlot.getItem();
            if (index < 2) {
                this.m_38903_(stack, 2, this.f_38839_.size(), false);
            } else {
                this.m_38903_(stack, 0, 1, false);
            }
            return ItemStack.EMPTY;
        }
    }

    protected SchematicTableBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world.m_7702_(extraData.readBlockPos()) instanceof SchematicTableBlockEntity schematicTable) {
            schematicTable.readClient(extraData.readNbt());
            return schematicTable;
        } else {
            return null;
        }
    }

    protected void initAndReadInventory(SchematicTableBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
        this.inputSlot = new SlotItemHandler(this.contentHolder.inventory, 0, 21, 57) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return AllItems.EMPTY_SCHEMATIC.isIn(stack) || AllItems.SCHEMATIC_AND_QUILL.isIn(stack) || AllItems.SCHEMATIC.isIn(stack);
            }
        };
        this.outputSlot = new SlotItemHandler(this.contentHolder.inventory, 1, 166, 57) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        };
        this.m_38897_(this.inputSlot);
        this.m_38897_(this.outputSlot);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new Slot(this.player.getInventory(), col + row * 9 + 9, 38 + col * 18, 105 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.m_38897_(new Slot(this.player.getInventory(), hotbarSlot, 38 + hotbarSlot * 18, 163));
        }
    }

    protected void saveData(SchematicTableBlockEntity contentHolder) {
    }
}
package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.AllMenuTypes;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AttributeFilterMenu extends AbstractFilterMenu {

    AttributeFilterMenu.WhitelistMode whitelistMode;

    List<Pair<ItemAttribute, Boolean>> selectedAttributes;

    public AttributeFilterMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public AttributeFilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack stack) {
        super(type, id, inv, stack);
    }

    public static AttributeFilterMenu create(int id, Inventory inv, ItemStack stack) {
        return new AttributeFilterMenu((MenuType<?>) AllMenuTypes.ATTRIBUTE_FILTER.get(), id, inv, stack);
    }

    public void appendSelectedAttribute(ItemAttribute itemAttribute, boolean inverted) {
        this.selectedAttributes.add(Pair.of(itemAttribute, inverted));
    }

    protected void init(Inventory inv, ItemStack contentHolder) {
        super.init(inv, contentHolder);
        ItemStack stack = new ItemStack(Items.NAME_TAG);
        stack.setHoverName(Components.literal("Selected Tags").withStyle(ChatFormatting.RESET, ChatFormatting.BLUE));
        this.ghostInventory.setStackInSlot(1, stack);
    }

    @Override
    protected int getPlayerInventoryXOffset() {
        return 51;
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 107;
    }

    @Override
    protected void addFilterSlots() {
        this.m_38897_(new SlotItemHandler(this.ghostInventory, 0, 16, 24));
        this.m_38897_(new SlotItemHandler(this.ghostInventory, 1, 22, 59) {

            @Override
            public boolean mayPickup(Player playerIn) {
                return false;
            }
        });
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(2);
    }

    @Override
    public void clearContents() {
        this.selectedAttributes.clear();
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId != 37) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        return slotIn.index == 37 ? false : super.m_5622_(slotIn);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.index == 37 ? false : super.m_5882_(stack, slotIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (index == 37) {
            return ItemStack.EMPTY;
        } else if (index == 36) {
            this.ghostInventory.setStackInSlot(37, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        } else {
            if (index < 36) {
                ItemStack stackToInsert = this.playerInventory.getItem(index);
                ItemStack copy = stackToInsert.copy();
                copy.setCount(1);
                this.ghostInventory.setStackInSlot(0, copy);
            }
            return ItemStack.EMPTY;
        }
    }

    protected void initAndReadInventory(ItemStack filterItem) {
        super.initAndReadInventory(filterItem);
        this.selectedAttributes = new ArrayList();
        this.whitelistMode = AttributeFilterMenu.WhitelistMode.values()[filterItem.getOrCreateTag().getInt("WhitelistMode")];
        ListTag attributes = filterItem.getOrCreateTag().getList("MatchedAttributes", 10);
        attributes.forEach(inbt -> {
            CompoundTag compound = (CompoundTag) inbt;
            this.selectedAttributes.add(Pair.of(ItemAttribute.fromNBT(compound), compound.getBoolean("Inverted")));
        });
    }

    @Override
    protected void saveData(ItemStack filterItem) {
        filterItem.getOrCreateTag().putInt("WhitelistMode", this.whitelistMode.ordinal());
        ListTag attributes = new ListTag();
        this.selectedAttributes.forEach(at -> {
            if (at != null) {
                CompoundTag compoundNBT = new CompoundTag();
                ((ItemAttribute) at.getFirst()).serializeNBT(compoundNBT);
                compoundNBT.putBoolean("Inverted", (Boolean) at.getSecond());
                attributes.add(compoundNBT);
            }
        });
        filterItem.getOrCreateTag().put("MatchedAttributes", attributes);
        if (attributes.isEmpty() && this.whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ) {
            filterItem.setTag(null);
        }
    }

    public static enum WhitelistMode {

        WHITELIST_DISJ, WHITELIST_CONJ, BLACKLIST
    }
}
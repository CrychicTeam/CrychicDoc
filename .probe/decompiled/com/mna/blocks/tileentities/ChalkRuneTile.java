package com.mna.blocks.tileentities;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.tools.MATags;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public class ChalkRuneTile extends TileEntityWithInventory implements IForgeBlockEntity {

    private static final int MAX_ITEMS = 1;

    public static final int INVENTORY_SLOT_INDEX = 0;

    private boolean readOnly = false;

    private boolean ghostItem = false;

    public ChalkRuneTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 1);
    }

    public ChalkRuneTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.CHALK_RUNE.get(), pos, state);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!this.readOnly && !this.ghostItem) {
            super.setItem(index, stack);
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!this.readOnly && !this.ghostItem) {
            ItemStack stack = super.removeItemNoUpdate(index);
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack out = super.removeItem(index, count);
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        return out;
    }

    public void clearStack() {
        super.removeItemNoUpdate(0);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setGhostItem(boolean ghost) {
        this.ghostItem = ghost;
    }

    public boolean isGhostItem() {
        return this.ghostItem;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        CompoundTag sub = new CompoundTag();
        this.m_8020_(0).save(sub);
        base.put("invSync", sub);
        return base;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        CompoundTag sub = tag.getCompound("invSync");
        this.setItem(0, ItemStack.of(sub));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag sub = pkt.getTag().getCompound("invSync");
        this.setItem(0, ItemStack.of(sub));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public ItemStack getDisplayedItem() {
        return this.m_8020_(0);
    }

    public boolean MatchesReagent(ResourceLocation rLoc) {
        return this.m_8020_(0).isEmpty() ? false : MATags.isItemEqual(this.m_8020_(0), rLoc);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("readOnly")) {
            this.readOnly = compound.getBoolean("readOnly");
        }
        if (compound.contains("ghostItem")) {
            this.ghostItem = compound.getBoolean("ghostItem");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("readOnly", this.readOnly);
        compound.putBoolean("ghostItem", this.ghostItem);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.m_8020_(0).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.m_8020_(0).isEmpty() && !this.isGhostItem();
    }
}
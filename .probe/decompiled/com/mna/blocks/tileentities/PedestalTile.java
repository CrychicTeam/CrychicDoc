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

public class PedestalTile extends TileEntityWithInventory implements IForgeBlockEntity {

    private static final int MAX_ITEMS = 1;

    public static final int INVENTORY_SLOT_INDEX = 0;

    public PedestalTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 1);
    }

    public PedestalTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.PEDESTAL.get(), pos, state);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        this.updateBlockState();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack out = super.removeItem(index, count);
        this.updateBlockState();
        return out;
    }

    private void updateBlockState() {
        this.updateBlockState("invChange", 3);
    }

    private void updateBlockState(String id, Integer data) {
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), data);
        if (this.m_58904_().isLoaded(this.m_58899_())) {
            this.m_58904_().getLightEngine().checkBlock(this.m_58899_());
        }
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
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.m_8020_(0).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.m_8020_(0).isEmpty();
    }
}
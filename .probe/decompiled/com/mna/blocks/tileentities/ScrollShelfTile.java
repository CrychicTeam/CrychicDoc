package com.mna.blocks.tileentities;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.tools.MATags;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ScrollShelfTile extends TileEntityWithInventory {

    private int scrollFlags = 0;

    private int bottleFlags = 0;

    public ScrollShelfTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SCROLLSHELF.get(), pos, state, 81);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        this.updateScrollFlags();
        CompoundTag tag = new CompoundTag();
        tag.putInt("scrollFlags", this.scrollFlags);
        tag.putInt("bottleFlags", this.bottleFlags);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("scrollFlags")) {
            this.scrollFlags = tag.getInt("scrollFlags");
        }
        if (tag.contains("bottleFlags")) {
            this.bottleFlags = tag.getInt("bottleFlags");
        }
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            this.scrollFlags = 0;
            this.bottleFlags = 0;
            if (compoundtag.contains("scrollFlags")) {
                this.scrollFlags = compoundtag.getInt("scrollFlags");
            }
            if (compoundtag.contains("bottleFlags")) {
                this.bottleFlags = compoundtag.getInt("bottleFlags");
            }
        }
    }

    public boolean isScrollDisplayable(int index) {
        return (this.scrollFlags & 1 << index) != 0;
    }

    public boolean displayAsBottle(int index) {
        return (this.bottleFlags & 1 << index) != 0;
    }

    private void updateScrollFlags() {
        this.scrollFlags = 0;
        this.bottleFlags = 0;
        for (int i = 0; i < 18; i++) {
            if (!this.m_8020_(i).isEmpty()) {
                this.scrollFlags |= 1 << i;
                if (MATags.isItemIn(this.m_8020_(i).getItem(), MATags.Items.SCROLL_SHELF_POTIONS)) {
                    this.bottleFlags |= 1 << i;
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] items = new int[this.m_6643_()];
        int i = 0;
        while (i < this.m_6643_()) {
            items[i] = i++;
        }
        return items;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.m_58899_()).expandTowards(0.0, 1.0, 0.0);
    }
}
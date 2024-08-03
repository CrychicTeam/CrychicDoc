package io.redspace.ironsspellbooks.block.pedestal;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalTile extends BlockEntity {

    private static final String NBT_HELD_ITEM = "heldItem";

    private ItemStack heldItem = ItemStack.EMPTY;

    public PedestalTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockRegistry.PEDESTAL_TILE.get(), pWorldPosition, pBlockState);
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(ItemStack newItem) {
        this.heldItem = newItem;
        this.m_6596_();
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(this.heldItem);
        Containers.dropContents(this.f_58857_, this.f_58858_, simpleContainer);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.readNBT(nbt);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        this.writeNBT(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.writeNBT(tag);
        return tag;
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        return super.triggerEvent(pId, pType);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = this.writeNBT(new CompoundTag());
        return ClientboundBlockEntityDataPacket.create(this, block -> nbt);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
    }

    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            this.load(tag);
        }
    }

    private CompoundTag writeNBT(CompoundTag nbt) {
        nbt.put("heldItem", this.heldItem.serializeNBT());
        return nbt;
    }

    private CompoundTag readNBT(CompoundTag nbt) {
        if (nbt.contains("heldItem")) {
            this.heldItem = ItemStack.of(nbt.getCompound("heldItem"));
        }
        return nbt;
    }
}
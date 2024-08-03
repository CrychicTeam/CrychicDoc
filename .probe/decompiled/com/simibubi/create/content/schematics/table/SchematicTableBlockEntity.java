package com.simibubi.create.content.schematics.table;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SchematicTableBlockEntity extends SmartBlockEntity implements MenuProvider, IInteractionChecker {

    public SchematicTableBlockEntity.SchematicTableInventory inventory = new SchematicTableBlockEntity.SchematicTableInventory();

    public boolean isUploading;

    public String uploadingSchematic = null;

    public float uploadingProgress = 0.0F;

    public boolean sendUpdate;

    public SchematicTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void sendToMenu(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.m_58899_());
        buffer.writeNbt(this.m_5995_());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (compound.contains("Uploading")) {
                this.isUploading = true;
                this.uploadingSchematic = compound.getString("Schematic");
                this.uploadingProgress = compound.getFloat("Progress");
            } else {
                this.isUploading = false;
                this.uploadingSchematic = null;
                this.uploadingProgress = 0.0F;
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.put("Inventory", this.inventory.serializeNBT());
        super.write(compound, clientPacket);
        if (clientPacket && this.isUploading) {
            compound.putBoolean("Uploading", true);
            compound.putString("Schematic", this.uploadingSchematic);
            compound.putFloat("Progress", this.uploadingProgress);
        }
    }

    @Override
    public void tick() {
        if (this.sendUpdate) {
            this.sendUpdate = false;
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 6);
        }
    }

    public void startUpload(String schematic) {
        this.isUploading = true;
        this.uploadingProgress = 0.0F;
        this.uploadingSchematic = schematic;
        this.sendUpdate = true;
        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void finishUpload() {
        this.isUploading = false;
        this.uploadingProgress = 0.0F;
        this.uploadingSchematic = null;
        this.sendUpdate = true;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return SchematicTableMenu.create(id, inv, this);
    }

    @Override
    public Component getDisplayName() {
        return Lang.translateDirect("gui.schematicTable.title");
    }

    @Override
    public boolean canPlayerUse(Player player) {
        return this.f_58857_ != null && this.f_58857_.getBlockEntity(this.f_58858_) == this ? player.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) <= 64.0 : false;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public class SchematicTableInventory extends ItemStackHandler {

        public SchematicTableInventory() {
            super(2);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            SchematicTableBlockEntity.this.m_6596_();
        }
    }
}
package com.simibubi.create.content.logistics.vault;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class ItemVaultBlockEntity extends SmartBlockEntity implements IMultiBlockEntityContainer.Inventory {

    protected LazyOptional<IItemHandler> itemCapability;

    protected ItemStackHandler inventory = new ItemStackHandler(AllConfigs.server().logistics.vaultCapacity.get()) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ItemVaultBlockEntity.this.updateComparators();
        }
    };

    protected BlockPos controller;

    protected BlockPos lastKnownPos;

    protected boolean updateConnectivity;

    protected int radius;

    protected int length;

    protected Direction.Axis axis;

    public ItemVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemCapability = LazyOptional.empty();
        this.radius = 1;
        this.length = 1;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    protected void updateConnectivity() {
        this.updateConnectivity = false;
        if (!this.f_58857_.isClientSide()) {
            if (this.isController()) {
                ConnectivityHandler.formMulti(this);
            }
        }
    }

    protected void updateComparators() {
        ItemVaultBlockEntity controllerBE = this.getControllerBE();
        if (controllerBE != null) {
            this.f_58857_.blockEntityChanged(controllerBE.f_58858_);
            BlockPos pos = controllerBE.m_58899_();
            for (int y = 0; y < controllerBE.radius; y++) {
                for (int z = 0; z < (controllerBE.axis == Direction.Axis.X ? controllerBE.radius : controllerBE.length); z++) {
                    for (int x = 0; x < (controllerBE.axis == Direction.Axis.Z ? controllerBE.radius : controllerBE.length); x++) {
                        this.f_58857_.updateNeighbourForOutputSignal(pos.offset(x, y, z), this.m_58900_().m_60734_());
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.lastKnownPos == null) {
            this.lastKnownPos = this.m_58899_();
        } else if (!this.lastKnownPos.equals(this.f_58858_) && this.f_58858_ != null) {
            this.onPositionChanged();
            return;
        }
        if (this.updateConnectivity) {
            this.updateConnectivity();
        }
    }

    @Override
    public BlockPos getLastKnownPos() {
        return this.lastKnownPos;
    }

    @Override
    public boolean isController() {
        return this.controller == null || this.f_58858_.m_123341_() == this.controller.m_123341_() && this.f_58858_.m_123342_() == this.controller.m_123342_() && this.f_58858_.m_123343_() == this.controller.m_123343_();
    }

    private void onPositionChanged() {
        this.removeController(true);
        this.lastKnownPos = this.f_58858_;
    }

    public ItemVaultBlockEntity getControllerBE() {
        if (this.isController()) {
            return this;
        } else {
            BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.controller);
            return blockEntity instanceof ItemVaultBlockEntity ? (ItemVaultBlockEntity) blockEntity : null;
        }
    }

    @Override
    public void removeController(boolean keepContents) {
        if (!this.f_58857_.isClientSide()) {
            this.updateConnectivity = true;
            this.controller = null;
            this.radius = 1;
            this.length = 1;
            BlockState state = this.m_58900_();
            if (ItemVaultBlock.isVault(state)) {
                state = (BlockState) state.m_61124_(ItemVaultBlock.LARGE, false);
                this.m_58904_().setBlock(this.f_58858_, state, 22);
            }
            this.itemCapability.invalidate();
            this.m_6596_();
            this.sendData();
        }
    }

    @Override
    public void setController(BlockPos controller) {
        if (!this.f_58857_.isClientSide || this.isVirtual()) {
            if (!controller.equals(this.controller)) {
                this.controller = controller;
                this.itemCapability.invalidate();
                this.m_6596_();
                this.sendData();
            }
        }
    }

    @Override
    public BlockPos getController() {
        return this.isController() ? this.f_58858_ : this.controller;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        BlockPos controllerBefore = this.controller;
        int prevSize = this.radius;
        int prevLength = this.length;
        this.updateConnectivity = compound.contains("Uninitialized");
        this.controller = null;
        this.lastKnownPos = null;
        if (compound.contains("LastKnownPos")) {
            this.lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        }
        if (compound.contains("Controller")) {
            this.controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));
        }
        if (this.isController()) {
            this.radius = compound.getInt("Size");
            this.length = compound.getInt("Length");
        }
        if (!clientPacket) {
            this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        } else {
            boolean changeOfController = controllerBefore == null ? this.controller != null : !controllerBefore.equals(this.controller);
            if (this.m_58898_() && (changeOfController || prevSize != this.radius || prevLength != this.length)) {
                this.f_58857_.setBlocksDirty(this.m_58899_(), Blocks.AIR.defaultBlockState(), this.m_58900_());
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        if (this.updateConnectivity) {
            compound.putBoolean("Uninitialized", true);
        }
        if (this.lastKnownPos != null) {
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(this.lastKnownPos));
        }
        if (!this.isController()) {
            compound.put("Controller", NbtUtils.writeBlockPos(this.controller));
        }
        if (this.isController()) {
            compound.putInt("Size", this.radius);
            compound.putInt("Length", this.length);
        }
        super.write(compound, clientPacket);
        if (!clientPacket) {
            compound.putString("StorageType", "CombinedInv");
            compound.put("Inventory", this.inventory.serializeNBT());
        }
    }

    public ItemStackHandler getInventoryOfBlock() {
        return this.inventory;
    }

    public void applyInventoryToBlock(ItemStackHandler handler) {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            this.inventory.setStackInSlot(i, i < handler.getSlots() ? handler.getStackInSlot(i) : ItemStack.EMPTY);
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.isItemHandlerCap(cap)) {
            this.initCapability();
            return this.itemCapability.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    private void initCapability() {
        if (!this.itemCapability.isPresent()) {
            if (!this.isController()) {
                ItemVaultBlockEntity controllerBE = this.getControllerBE();
                if (controllerBE != null) {
                    controllerBE.initCapability();
                    this.itemCapability = controllerBE.itemCapability;
                }
            } else {
                boolean alongZ = ItemVaultBlock.getVaultBlockAxis(this.m_58900_()) == Direction.Axis.Z;
                IItemHandlerModifiable[] invs = new IItemHandlerModifiable[this.length * this.radius * this.radius];
                for (int yOffset = 0; yOffset < this.length; yOffset++) {
                    for (int xOffset = 0; xOffset < this.radius; xOffset++) {
                        for (int zOffset = 0; zOffset < this.radius; zOffset++) {
                            BlockPos vaultPos = alongZ ? this.f_58858_.offset(xOffset, zOffset, yOffset) : this.f_58858_.offset(yOffset, xOffset, zOffset);
                            ItemVaultBlockEntity vaultAt = ConnectivityHandler.partAt((BlockEntityType<?>) AllBlockEntityTypes.ITEM_VAULT.get(), this.f_58857_, vaultPos);
                            invs[yOffset * this.radius * this.radius + xOffset * this.radius + zOffset] = vaultAt != null ? vaultAt.inventory : new ItemStackHandler();
                        }
                    }
                }
                IItemHandler itemHandler = new VersionedInventoryWrapper(new CombinedInvWrapper(invs));
                this.itemCapability = LazyOptional.of(() -> itemHandler);
            }
        }
    }

    public static int getMaxLength(int radius) {
        return radius * 3;
    }

    @Override
    public void preventConnectivityUpdate() {
        this.updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.m_58900_();
        if (ItemVaultBlock.isVault(state)) {
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) state.m_61124_(ItemVaultBlock.LARGE, this.radius > 2), 6);
        }
        this.itemCapability.invalidate();
        this.m_6596_();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return this.getMainAxisOf(this);
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        return longAxis == Direction.Axis.Y ? this.getMaxWidth() : getMaxLength(width);
    }

    @Override
    public int getMaxWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return this.length;
    }

    @Override
    public int getWidth() {
        return this.radius;
    }

    @Override
    public void setHeight(int height) {
        this.length = height;
    }

    @Override
    public void setWidth(int width) {
        this.radius = width;
    }

    @Override
    public boolean hasInventory() {
        return true;
    }
}
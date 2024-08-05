package com.simibubi.create.content.redstone.thresholdSwitch;

import com.simibubi.create.compat.storageDrawers.StorageDrawers;
import com.simibubi.create.content.redstone.DirectedDirectionalBlock;
import com.simibubi.create.content.redstone.FilteredDetectorFilterSlot;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.CapManipulationBehaviourBase;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.TankManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.utility.BlockFace;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class ThresholdSwitchBlockEntity extends SmartBlockEntity {

    public float onWhenAbove = 0.75F;

    public float offWhenBelow = 0.25F;

    public float currentLevel = -1.0F;

    private boolean redstoneState = false;

    private boolean inverted = false;

    private boolean poweredAfterDelay = false;

    private FilteringBehaviour filtering;

    private InvManipulationBehaviour observedInventory;

    private TankManipulationBehaviour observedTank;

    private VersionedInventoryTrackerBehaviour invVersionTracker;

    public ThresholdSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(10);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.onWhenAbove = compound.getFloat("OnAbove");
        this.offWhenBelow = compound.getFloat("OffBelow");
        this.currentLevel = compound.getFloat("Current");
        this.redstoneState = compound.getBoolean("Powered");
        this.inverted = compound.getBoolean("Inverted");
        this.poweredAfterDelay = compound.getBoolean("PoweredAfterDelay");
        super.read(compound, clientPacket);
    }

    protected void writeCommon(CompoundTag compound) {
        compound.putFloat("OnAbove", this.onWhenAbove);
        compound.putFloat("OffBelow", this.offWhenBelow);
        compound.putBoolean("Inverted", this.inverted);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        this.writeCommon(compound);
        compound.putFloat("Current", this.currentLevel);
        compound.putBoolean("Powered", this.redstoneState);
        compound.putBoolean("PoweredAfterDelay", this.poweredAfterDelay);
        super.write(compound, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag compound) {
        this.writeCommon(compound);
        super.writeSafe(compound);
    }

    public float getStockLevel() {
        return this.currentLevel;
    }

    public void updateCurrentLevel() {
        boolean changed = false;
        float occupied = 0.0F;
        float totalSpace = 0.0F;
        float prevLevel = this.currentLevel;
        this.observedInventory.findNewCapability();
        this.observedTank.findNewCapability();
        BlockPos target = this.f_58858_.relative(ThresholdSwitchBlock.getTargetDirection(this.m_58900_()));
        BlockEntity targetBlockEntity = this.f_58857_.getBlockEntity(target);
        if (targetBlockEntity instanceof ThresholdSwitchObservable observable) {
            this.currentLevel = observable.getPercent() / 100.0F;
        } else if (StorageDrawers.isDrawer(targetBlockEntity) && this.observedInventory.hasInventory()) {
            this.currentLevel = StorageDrawers.getTrueFillLevel(this.observedInventory.getInventory(), this.filtering);
        } else {
            if (!this.observedInventory.hasInventory() && !this.observedTank.hasInventory()) {
                if (this.currentLevel == -1.0F) {
                    return;
                }
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(ThresholdSwitchBlock.LEVEL, 0), 3);
                this.currentLevel = -1.0F;
                this.redstoneState = false;
                this.sendData();
                this.scheduleBlockTick();
                return;
            }
            if (this.observedInventory.hasInventory()) {
                IItemHandler inv = this.observedInventory.getInventory();
                if (this.invVersionTracker.stillWaiting(inv)) {
                    occupied = prevLevel;
                    totalSpace = 1.0F;
                } else {
                    this.invVersionTracker.awaitNewVersion(inv);
                    for (int slot = 0; slot < inv.getSlots(); slot++) {
                        ItemStack stackInSlot = inv.getStackInSlot(slot);
                        int space = Math.min(stackInSlot.getMaxStackSize(), inv.getSlotLimit(slot));
                        int count = stackInSlot.getCount();
                        if (space != 0) {
                            totalSpace++;
                            if (this.filtering.test(stackInSlot)) {
                                occupied += (float) count * (1.0F / (float) space);
                            }
                        }
                    }
                }
            }
            if (this.observedTank.hasInventory()) {
                IFluidHandler tank = this.observedTank.getInventory();
                for (int slotx = 0; slotx < tank.getTanks(); slotx++) {
                    FluidStack stackInSlot = tank.getFluidInTank(slotx);
                    int space = tank.getTankCapacity(slotx);
                    int count = stackInSlot.getAmount();
                    if (space != 0) {
                        totalSpace++;
                        if (this.filtering.test(stackInSlot)) {
                            occupied += (float) count * (1.0F / (float) space);
                        }
                    }
                }
            }
            this.currentLevel = occupied / totalSpace;
        }
        this.currentLevel = Mth.clamp(this.currentLevel, 0.0F, 1.0F);
        changed = this.currentLevel != prevLevel;
        boolean previouslyPowered = this.redstoneState;
        if (this.redstoneState && this.currentLevel <= this.offWhenBelow) {
            this.redstoneState = false;
        } else if (!this.redstoneState && this.currentLevel >= this.onWhenAbove) {
            this.redstoneState = true;
        }
        boolean update = previouslyPowered != this.redstoneState;
        int displayLevel = 0;
        if (this.currentLevel > 0.0F) {
            displayLevel = (int) (1.0F + this.currentLevel * 4.0F);
        }
        this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(ThresholdSwitchBlock.LEVEL, displayLevel), update ? 3 : 2);
        if (update) {
            this.scheduleBlockTick();
        }
        if (changed || update) {
            DisplayLinkBlock.notifyGatherers(this.f_58857_, this.f_58858_);
            this.notifyUpdate();
        }
    }

    protected void scheduleBlockTick() {
        Block block = this.m_58900_().m_60734_();
        if (!this.f_58857_.m_183326_().willTickThisTick(this.f_58858_, block)) {
            this.f_58857_.m_186464_(this.f_58858_, block, 2, TickPriority.NORMAL);
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.f_58857_.isClientSide) {
            this.updateCurrentLevel();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.filtering = new FilteringBehaviour(this, new FilteredDetectorFilterSlot(true)).withCallback($ -> {
            this.updateCurrentLevel();
            this.invVersionTracker.reset();
        }));
        behaviours.add(this.invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
        CapManipulationBehaviourBase.InterfaceProvider towardBlockFacing = (w, p, s) -> new BlockFace(p, DirectedDirectionalBlock.getTargetDirection(s));
        behaviours.add(this.observedInventory = new InvManipulationBehaviour(this, towardBlockFacing).bypassSidedness());
        behaviours.add(this.observedTank = new TankManipulationBehaviour(this, towardBlockFacing).bypassSidedness());
    }

    public float getLevelForDisplay() {
        return this.currentLevel == -1.0F ? 0.0F : this.currentLevel;
    }

    public boolean getState() {
        return this.redstoneState;
    }

    public boolean shouldBePowered() {
        return this.inverted != this.redstoneState;
    }

    public void updatePowerAfterDelay() {
        this.poweredAfterDelay = this.shouldBePowered();
        this.f_58857_.m_6289_(this.f_58858_, this.m_58900_().m_60734_());
    }

    public boolean isPowered() {
        return this.poweredAfterDelay;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public void setInverted(boolean inverted) {
        if (inverted != this.inverted) {
            this.inverted = inverted;
            this.updatePowerAfterDelay();
        }
    }
}
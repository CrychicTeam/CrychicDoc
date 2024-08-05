package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.sync.ContraptionFluidPacket;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;

public class MountedFluidStorage {

    SmartFluidTank tank;

    private boolean valid;

    private BlockEntity blockEntity;

    private int packetCooldown = 0;

    private boolean sendPacket = false;

    public static boolean canUseAsStorage(BlockEntity be) {
        return be instanceof FluidTankBlockEntity ? ((FluidTankBlockEntity) be).isController() : false;
    }

    public MountedFluidStorage(BlockEntity be) {
        this.assignBlockEntity(be);
    }

    public void assignBlockEntity(BlockEntity be) {
        this.blockEntity = be;
        this.tank = this.createMountedTank(be);
    }

    private SmartFluidTank createMountedTank(BlockEntity be) {
        if (be instanceof CreativeFluidTankBlockEntity) {
            return new CreativeFluidTankBlockEntity.CreativeSmartFluidTank(((FluidTankBlockEntity) be).getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier(), $ -> {
            });
        } else {
            return be instanceof FluidTankBlockEntity ? new SmartFluidTank(((FluidTankBlockEntity) be).getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier(), this::onFluidStackChanged) : null;
        }
    }

    public void tick(Entity entity, BlockPos pos, boolean isRemote) {
        if (!isRemote) {
            if (this.packetCooldown > 0) {
                this.packetCooldown--;
            } else if (this.sendPacket) {
                this.sendPacket = false;
                AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ContraptionFluidPacket(entity.getId(), pos, this.tank.getFluid()));
                this.packetCooldown = 8;
            }
        } else if (this.blockEntity instanceof FluidTankBlockEntity tank) {
            tank.getFluidLevel().tickChaser();
        }
    }

    public void updateFluid(FluidStack fluid) {
        this.tank.setFluid(fluid);
        if (this.blockEntity instanceof FluidTankBlockEntity) {
            float fillState = (float) this.tank.getFluidAmount() / (float) this.tank.getCapacity();
            FluidTankBlockEntity tank = (FluidTankBlockEntity) this.blockEntity;
            if (tank.getFluidLevel() == null) {
                tank.setFluidLevel(LerpedFloat.linear().startWithValue((double) fillState));
            }
            tank.getFluidLevel().chase((double) fillState, 0.5, LerpedFloat.Chaser.EXP);
            IFluidTank tankInventory = tank.getTankInventory();
            if (tankInventory instanceof SmartFluidTank) {
                ((SmartFluidTank) tankInventory).setFluid(fluid);
            }
        }
    }

    public void removeStorageFromWorld() {
        this.valid = false;
        if (this.blockEntity != null) {
            IFluidHandler teHandler = (IFluidHandler) this.blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
            if (teHandler instanceof SmartFluidTank smartTank) {
                this.tank.setFluid(smartTank.getFluid());
                this.sendPacket = false;
                this.valid = true;
            }
        }
    }

    private void onFluidStackChanged(FluidStack fs) {
        this.sendPacket = true;
    }

    public void addStorageToWorld(BlockEntity be) {
        if (!(this.tank instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank)) {
            LazyOptional<IFluidHandler> capability = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
            IFluidHandler teHandler = capability.orElse(null);
            if (teHandler instanceof SmartFluidTank inv) {
                inv.setFluid(this.tank.getFluid().copy());
            }
        }
    }

    public IFluidHandler getFluidHandler() {
        return this.tank;
    }

    public CompoundTag serialize() {
        if (!this.valid) {
            return null;
        } else {
            CompoundTag tag = this.tank.writeToNBT(new CompoundTag());
            tag.putInt("Capacity", this.tank.getCapacity());
            if (this.tank instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank) {
                NBTHelper.putMarker(tag, "Bottomless");
                tag.put("ProvidedStack", this.tank.getFluid().writeToNBT(new CompoundTag()));
            }
            return tag;
        }
    }

    public static MountedFluidStorage deserialize(CompoundTag nbt) {
        MountedFluidStorage storage = new MountedFluidStorage(null);
        if (nbt == null) {
            return storage;
        } else {
            int capacity = nbt.getInt("Capacity");
            storage.tank = new SmartFluidTank(capacity, storage::onFluidStackChanged);
            storage.valid = true;
            if (nbt.contains("Bottomless")) {
                FluidStack providedStack = FluidStack.loadFluidStackFromNBT(nbt.getCompound("ProvidedStack"));
                CreativeFluidTankBlockEntity.CreativeSmartFluidTank creativeSmartFluidTank = new CreativeFluidTankBlockEntity.CreativeSmartFluidTank(capacity, $ -> {
                });
                creativeSmartFluidTank.setContainedFluid(providedStack);
                storage.tank = creativeSmartFluidTank;
                return storage;
            } else {
                storage.tank.readFromNBT(nbt);
                return storage;
            }
        }
    }

    public boolean isValid() {
        return this.valid;
    }
}
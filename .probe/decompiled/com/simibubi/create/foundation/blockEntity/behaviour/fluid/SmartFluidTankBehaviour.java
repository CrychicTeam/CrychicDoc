package com.simibubi.create.foundation.blockEntity.behaviour.fluid;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;

public class SmartFluidTankBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<SmartFluidTankBehaviour> TYPE = new BehaviourType<>();

    public static final BehaviourType<SmartFluidTankBehaviour> INPUT = new BehaviourType<>("Input");

    public static final BehaviourType<SmartFluidTankBehaviour> OUTPUT = new BehaviourType<>("Output");

    private static final int SYNC_RATE = 8;

    protected int syncCooldown;

    protected boolean queuedSync;

    protected SmartFluidTankBehaviour.TankSegment[] tanks;

    protected LazyOptional<? extends IFluidHandler> capability;

    protected boolean extractionAllowed;

    protected boolean insertionAllowed = true;

    protected Runnable fluidUpdateCallback;

    private BehaviourType<SmartFluidTankBehaviour> behaviourType;

    public static SmartFluidTankBehaviour single(SmartBlockEntity be, int capacity) {
        return new SmartFluidTankBehaviour(TYPE, be, 1, capacity, false);
    }

    public SmartFluidTankBehaviour(BehaviourType<SmartFluidTankBehaviour> type, SmartBlockEntity be, int tanks, int tankCapacity, boolean enforceVariety) {
        super(be);
        this.extractionAllowed = true;
        this.behaviourType = type;
        this.tanks = new SmartFluidTankBehaviour.TankSegment[tanks];
        IFluidHandler[] handlers = new IFluidHandler[tanks];
        for (int i = 0; i < tanks; i++) {
            SmartFluidTankBehaviour.TankSegment tankSegment = new SmartFluidTankBehaviour.TankSegment(tankCapacity);
            this.tanks[i] = tankSegment;
            handlers[i] = tankSegment.tank;
        }
        this.capability = LazyOptional.of(() -> new SmartFluidTankBehaviour.InternalFluidHandler(handlers, enforceVariety));
        this.fluidUpdateCallback = () -> {
        };
    }

    public SmartFluidTankBehaviour whenFluidUpdates(Runnable fluidUpdateCallback) {
        this.fluidUpdateCallback = fluidUpdateCallback;
        return this;
    }

    public SmartFluidTankBehaviour allowInsertion() {
        this.insertionAllowed = true;
        return this;
    }

    public SmartFluidTankBehaviour allowExtraction() {
        this.extractionAllowed = true;
        return this;
    }

    public SmartFluidTankBehaviour forbidInsertion() {
        this.insertionAllowed = false;
        return this;
    }

    public SmartFluidTankBehaviour forbidExtraction() {
        this.extractionAllowed = false;
        return this;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.getWorld().isClientSide) {
            this.forEach(ts -> {
                ts.fluidLevel.forceNextSync();
                ts.onFluidStackChanged();
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.syncCooldown > 0) {
            this.syncCooldown--;
            if (this.syncCooldown == 0 && this.queuedSync) {
                this.updateFluids();
            }
        }
        this.forEach(be -> {
            LerpedFloat fluidLevel = be.getFluidLevel();
            if (fluidLevel != null) {
                fluidLevel.tickChaser();
            }
        });
    }

    public void sendDataImmediately() {
        this.syncCooldown = 0;
        this.queuedSync = false;
        this.updateFluids();
    }

    public void sendDataLazily() {
        if (this.syncCooldown > 0) {
            this.queuedSync = true;
        } else {
            this.updateFluids();
            this.queuedSync = false;
            this.syncCooldown = 8;
        }
    }

    protected void updateFluids() {
        this.fluidUpdateCallback.run();
        this.blockEntity.sendData();
        this.blockEntity.m_6596_();
    }

    @Override
    public void unload() {
        super.unload();
        this.capability.invalidate();
    }

    public SmartFluidTank getPrimaryHandler() {
        return this.getPrimaryTank().tank;
    }

    public SmartFluidTankBehaviour.TankSegment getPrimaryTank() {
        return this.tanks[0];
    }

    public SmartFluidTankBehaviour.TankSegment[] getTanks() {
        return this.tanks;
    }

    public boolean isEmpty() {
        for (SmartFluidTankBehaviour.TankSegment tankSegment : this.tanks) {
            if (!tankSegment.tank.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void forEach(Consumer<SmartFluidTankBehaviour.TankSegment> action) {
        for (SmartFluidTankBehaviour.TankSegment tankSegment : this.tanks) {
            action.accept(tankSegment);
        }
    }

    public LazyOptional<? extends IFluidHandler> getCapability() {
        return this.capability;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        ListTag tanksNBT = new ListTag();
        this.forEach(ts -> tanksNBT.add(ts.writeNBT()));
        nbt.put(this.getType().getName() + "Tanks", tanksNBT);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        MutableInt index = new MutableInt(0);
        NBTHelper.iterateCompoundList(nbt.getList(this.getType().getName() + "Tanks", 10), c -> {
            if (index.intValue() < this.tanks.length) {
                this.tanks[index.intValue()].readNBT(c, clientPacket);
                index.increment();
            }
        });
    }

    @Override
    public BehaviourType<?> getType() {
        return this.behaviourType;
    }

    public class InternalFluidHandler extends CombinedTankWrapper {

        public InternalFluidHandler(IFluidHandler[] handlers, boolean enforceVariety) {
            super(handlers);
            if (enforceVariety) {
                this.enforceVariety();
            }
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            return !SmartFluidTankBehaviour.this.insertionAllowed ? 0 : super.fill(resource, action);
        }

        public int forceFill(FluidStack resource, IFluidHandler.FluidAction action) {
            return super.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return !SmartFluidTankBehaviour.this.extractionAllowed ? FluidStack.EMPTY : super.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return !SmartFluidTankBehaviour.this.extractionAllowed ? FluidStack.EMPTY : super.drain(maxDrain, action);
        }
    }

    public class TankSegment {

        protected SmartFluidTank tank;

        protected LerpedFloat fluidLevel;

        protected FluidStack renderedFluid;

        public TankSegment(int capacity) {
            this.tank = new SmartFluidTank(capacity, f -> this.onFluidStackChanged());
            this.fluidLevel = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.25, LerpedFloat.Chaser.EXP);
            this.renderedFluid = FluidStack.EMPTY;
        }

        public void onFluidStackChanged() {
            if (SmartFluidTankBehaviour.this.blockEntity.m_58898_()) {
                this.fluidLevel.chase((double) ((float) this.tank.getFluidAmount() / (float) this.tank.getCapacity()), 0.25, LerpedFloat.Chaser.EXP);
                if (!SmartFluidTankBehaviour.this.getWorld().isClientSide) {
                    SmartFluidTankBehaviour.this.sendDataLazily();
                }
                if (SmartFluidTankBehaviour.this.blockEntity.isVirtual() && !this.tank.getFluid().isEmpty()) {
                    this.renderedFluid = this.tank.getFluid();
                }
            }
        }

        public FluidStack getRenderedFluid() {
            return this.renderedFluid;
        }

        public LerpedFloat getFluidLevel() {
            return this.fluidLevel;
        }

        public float getTotalUnits(float partialTicks) {
            return this.fluidLevel.getValue(partialTicks) * (float) this.tank.getCapacity();
        }

        public CompoundTag writeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.put("TankContent", this.tank.writeToNBT(new CompoundTag()));
            compound.put("Level", this.fluidLevel.writeNBT());
            return compound;
        }

        public void readNBT(CompoundTag compound, boolean clientPacket) {
            this.tank.readFromNBT(compound.getCompound("TankContent"));
            this.fluidLevel.readNBT(compound.getCompound("Level"), clientPacket);
            if (!this.tank.getFluid().isEmpty()) {
                this.renderedFluid = this.tank.getFluid();
            }
        }

        public boolean isEmpty(float partialTicks) {
            FluidStack renderedFluid = this.getRenderedFluid();
            if (renderedFluid.isEmpty()) {
                return true;
            } else {
                float units = this.getTotalUnits(partialTicks);
                return units < 1.0F;
            }
        }
    }
}
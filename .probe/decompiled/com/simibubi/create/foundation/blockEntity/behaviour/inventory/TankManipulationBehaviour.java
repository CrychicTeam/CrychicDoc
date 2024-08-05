package com.simibubi.create.foundation.blockEntity.behaviour.inventory;

import com.google.common.base.Predicates;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import java.util.function.Predicate;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankManipulationBehaviour extends CapManipulationBehaviourBase<IFluidHandler, TankManipulationBehaviour> {

    public static final BehaviourType<TankManipulationBehaviour> OBSERVE = new BehaviourType<>();

    private BehaviourType<TankManipulationBehaviour> behaviourType;

    public TankManipulationBehaviour(SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        this(OBSERVE, be, target);
    }

    private TankManipulationBehaviour(BehaviourType<TankManipulationBehaviour> type, SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        super(be, target);
        this.behaviourType = type;
    }

    public FluidStack extractAny() {
        if (!this.hasInventory()) {
            return FluidStack.EMPTY;
        } else {
            IFluidHandler inventory = this.getInventory();
            Predicate<FluidStack> filterTest = this.getFilterTest(Predicates.alwaysTrue());
            for (int i = 0; i < inventory.getTanks(); i++) {
                FluidStack fluidInTank = inventory.getFluidInTank(i);
                if (!fluidInTank.isEmpty() && filterTest.test(fluidInTank)) {
                    FluidStack drained = inventory.drain(fluidInTank, this.simulateNext ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
                    if (!drained.isEmpty()) {
                        return drained;
                    }
                }
            }
            return FluidStack.EMPTY;
        }
    }

    protected Predicate<FluidStack> getFilterTest(Predicate<FluidStack> customFilter) {
        Predicate<FluidStack> test = customFilter;
        FilteringBehaviour filter = this.blockEntity.getBehaviour(FilteringBehaviour.TYPE);
        if (filter != null) {
            test = customFilter.and(filter::test);
        }
        return test;
    }

    @Override
    protected Capability<IFluidHandler> capability() {
        return ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public BehaviourType<?> getType() {
        return this.behaviourType;
    }
}
package com.simibubi.create.compat.computercraft.implementation;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.peripherals.DisplayLinkPeripheral;
import com.simibubi.create.compat.computercraft.implementation.peripherals.SequencedGearshiftPeripheral;
import com.simibubi.create.compat.computercraft.implementation.peripherals.SpeedControllerPeripheral;
import com.simibubi.create.compat.computercraft.implementation.peripherals.SpeedGaugePeripheral;
import com.simibubi.create.compat.computercraft.implementation.peripherals.StationPeripheral;
import com.simibubi.create.compat.computercraft.implementation.peripherals.StressGaugePeripheral;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;

public class ComputerBehaviour extends AbstractComputerBehaviour {

    protected static final Capability<IPeripheral> PERIPHERAL_CAPABILITY = CapabilityManager.get(new CapabilityToken<IPeripheral>() {
    });

    LazyOptional<IPeripheral> peripheral;

    NonNullSupplier<IPeripheral> peripheralSupplier;

    public ComputerBehaviour(SmartBlockEntity te) {
        super(te);
        this.peripheralSupplier = getPeripheralFor(te);
    }

    public static NonNullSupplier<IPeripheral> getPeripheralFor(SmartBlockEntity be) {
        if (be instanceof SpeedControllerBlockEntity scbe) {
            return () -> new SpeedControllerPeripheral(scbe, scbe.targetSpeed);
        } else if (be instanceof DisplayLinkBlockEntity dlbe) {
            return () -> new DisplayLinkPeripheral(dlbe);
        } else if (be instanceof SequencedGearshiftBlockEntity sgbe) {
            return () -> new SequencedGearshiftPeripheral(sgbe);
        } else if (be instanceof SpeedGaugeBlockEntity sgbe) {
            return () -> new SpeedGaugePeripheral(sgbe);
        } else if (be instanceof StressGaugeBlockEntity sgbe) {
            return () -> new StressGaugePeripheral(sgbe);
        } else if (be instanceof StationBlockEntity sbe) {
            return () -> new StationPeripheral(sbe);
        } else {
            throw new IllegalArgumentException("No peripheral available for " + ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(be.m_58903_()));
        }
    }

    @Override
    public <T> boolean isPeripheralCap(Capability<T> cap) {
        return cap == PERIPHERAL_CAPABILITY;
    }

    @Override
    public <T> LazyOptional<T> getPeripheralCapability() {
        if (this.peripheral == null || !this.peripheral.isPresent()) {
            this.peripheral = LazyOptional.of(this.peripheralSupplier);
        }
        return this.peripheral.cast();
    }

    @Override
    public void removePeripheral() {
        if (this.peripheral != null) {
            this.peripheral.invalidate();
        }
    }
}
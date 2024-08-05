package com.simibubi.create.compat.computercraft.implementation.peripherals;

import com.simibubi.create.AllPackets;
import com.simibubi.create.compat.computercraft.AttachedComputerPacket;
import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SyncedPeripheral<T extends SmartBlockEntity> implements IPeripheral {

    protected final T blockEntity;

    private final AtomicInteger computers = new AtomicInteger();

    public SyncedPeripheral(T blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void attach(@NotNull IComputerAccess computer) {
        this.computers.incrementAndGet();
        this.updateBlockEntity();
    }

    public void detach(@NotNull IComputerAccess computer) {
        this.computers.decrementAndGet();
        this.updateBlockEntity();
    }

    private void updateBlockEntity() {
        boolean hasAttachedComputer = this.computers.get() > 0;
        this.blockEntity.getBehaviour(ComputerBehaviour.TYPE).setHasAttachedComputer(hasAttachedComputer);
        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new AttachedComputerPacket(this.blockEntity.m_58899_(), hasAttachedComputer));
    }

    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }
}
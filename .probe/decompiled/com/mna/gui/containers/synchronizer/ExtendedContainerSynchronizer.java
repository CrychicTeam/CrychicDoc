package com.mna.gui.containers.synchronizer;

import com.mna.network.ServerMessageDispatcher;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;

public class ExtendedContainerSynchronizer implements ContainerSynchronizer {

    private ServerPlayer forPlayer;

    public ExtendedContainerSynchronizer(ServerPlayer serverPlayer) {
        this.forPlayer = serverPlayer;
    }

    @Override
    public void sendInitialData(AbstractContainerMenu container, NonNullList<ItemStack> stacks, ItemStack carried, int[] dataSlots) {
        ServerMessageDispatcher.sendExtendedInitialContainer(this.forPlayer, container.containerId, container.incrementStateId(), stacks, carried);
        for (int i = 0; i < dataSlots.length; i++) {
            this.broadcastDataValue(container, i, dataSlots[i]);
        }
    }

    @Override
    public void sendSlotChange(AbstractContainerMenu container, int slotIndex, ItemStack stack) {
        ServerMessageDispatcher.sendExtendedItemStack(this.forPlayer, container.containerId, container.incrementStateId(), slotIndex, stack);
    }

    @Override
    public void sendCarriedChange(AbstractContainerMenu container, ItemStack carried) {
        this.forPlayer.connection.send(new ClientboundContainerSetSlotPacket(-1, container.incrementStateId(), -1, carried));
    }

    @Override
    public void sendDataChange(AbstractContainerMenu container, int dataSlotIndex, int dataSlotValue) {
        this.broadcastDataValue(container, dataSlotIndex, dataSlotValue);
    }

    private void broadcastDataValue(AbstractContainerMenu container, int dataSlotIndex, int dataSlotValue) {
        this.forPlayer.connection.send(new ClientboundContainerSetDataPacket(container.containerId, dataSlotIndex, dataSlotValue));
    }
}
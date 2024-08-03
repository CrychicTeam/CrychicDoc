package fr.frinn.custommachinery.api.component;

import fr.frinn.custommachinery.api.machine.MachineStatus;
import net.minecraft.network.chat.Component;

public interface IMachineComponent {

    MachineComponentType<?> getType();

    ComponentIOMode getMode();

    IMachineComponentManager getManager();

    default void init() {
    }

    default void onRemoved() {
    }

    default void onStatusChanged(MachineStatus oldStatus, MachineStatus newStatus, Component message) {
    }
}
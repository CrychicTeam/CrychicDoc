package fr.frinn.custommachinery.api.component;

public interface ITickableComponent extends IMachineComponent {

    default void serverTick() {
    }

    default void clientTick() {
    }
}
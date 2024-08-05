package fr.frinn.custommachinery.api.component.variant;

import fr.frinn.custommachinery.api.component.IMachineComponent;

public interface ITickableComponentVariant<T extends IMachineComponent> {

    void tick(T var1);
}
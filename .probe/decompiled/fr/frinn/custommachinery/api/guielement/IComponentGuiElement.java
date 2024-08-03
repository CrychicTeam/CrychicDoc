package fr.frinn.custommachinery.api.guielement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.component.handler.IComponentHandler;
import java.util.Optional;

public interface IComponentGuiElement<T extends IMachineComponent> {

    MachineComponentType<T> getComponentType();

    String getComponentId();

    default Optional<T> getComponent(IMachineComponentManager manager) {
        return manager.getComponent(this.getComponentType()).flatMap(component -> component instanceof IComponentHandler handler ? handler.getComponentForID(this.getComponentId()) : Optional.of(component));
    }
}
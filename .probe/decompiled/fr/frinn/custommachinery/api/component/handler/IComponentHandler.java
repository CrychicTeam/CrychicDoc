package fr.frinn.custommachinery.api.component.handler;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import java.util.List;
import java.util.Optional;

public interface IComponentHandler<T extends IMachineComponent> extends IMachineComponent {

    List<T> getComponents();

    Optional<T> getComponentForID(String var1);
}
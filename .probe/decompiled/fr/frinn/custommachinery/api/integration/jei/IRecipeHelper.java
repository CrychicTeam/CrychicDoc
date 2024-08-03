package fr.frinn.custommachinery.api.integration.jei;

import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.api.machine.ICustomMachine;
import java.util.Optional;
import mezz.jei.api.helpers.IJeiHelpers;

public interface IRecipeHelper {

    ICustomMachine getMachine();

    Optional<IMachineComponentTemplate<?>> getComponentForElement(IComponentGuiElement<?> var1);

    IMachineComponentManager getDummyManager();

    IJeiHelpers getJeiHelpers();
}
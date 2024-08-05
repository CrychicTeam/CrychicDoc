package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;

public interface ITickableRequirement<T extends IMachineComponent> extends IRequirement<T> {

    CraftingResult processTick(T var1, ICraftingContext var2);
}
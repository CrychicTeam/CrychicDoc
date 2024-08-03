package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;

public interface IDelayedRequirement<T extends IMachineComponent> extends IRequirement<T> {

    void setDelay(double var1);

    double getDelay();

    CraftingResult execute(T var1, ICraftingContext var2);
}
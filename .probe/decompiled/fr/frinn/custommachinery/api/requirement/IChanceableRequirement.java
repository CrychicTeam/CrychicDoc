package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import java.util.Random;

public interface IChanceableRequirement<T extends IMachineComponent> extends IRequirement<T> {

    void setChance(double var1);

    boolean shouldSkip(T var1, Random var2, ICraftingContext var3);
}
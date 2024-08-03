package fr.frinn.custommachinery.api.crafting;

import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import org.jetbrains.annotations.Nullable;

public interface ICraftingContext {

    MachineTile getMachineTile();

    IMachineRecipe getRecipe();

    double getRemainingTime();

    double getBaseSpeed();

    void setBaseSpeed(double var1);

    double getModifiedSpeed();

    double getModifiedValue(double var1, IRequirement<?> var3, @Nullable String var4);

    long getIntegerModifiedValue(double var1, IRequirement<?> var3, @Nullable String var4);

    double getPerTickModifiedValue(double var1, IRequirement<?> var3, @Nullable String var4);

    long getPerTickIntegerModifiedValue(double var1, IRequirement<?> var3, @Nullable String var4);
}
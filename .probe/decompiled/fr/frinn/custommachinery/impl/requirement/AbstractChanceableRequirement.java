package fr.frinn.custommachinery.impl.requirement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import java.util.Random;
import net.minecraft.util.Mth;

public abstract class AbstractChanceableRequirement<T extends IMachineComponent> extends AbstractRequirement<T> implements IChanceableRequirement<T> {

    private double chance = 1.0;

    public AbstractChanceableRequirement(RequirementIOMode mode) {
        super(mode);
    }

    public double getChance() {
        return this.chance;
    }

    @Override
    public void setChance(double chance) {
        this.chance = Mth.clamp(chance, 0.0, 1.0);
    }

    @Override
    public boolean shouldSkip(T component, Random rand, ICraftingContext context) {
        double chance = context.getModifiedValue(this.chance, this, "chance");
        return rand.nextDouble() > chance;
    }
}
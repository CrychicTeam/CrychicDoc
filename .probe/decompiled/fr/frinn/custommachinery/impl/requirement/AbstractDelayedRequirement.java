package fr.frinn.custommachinery.impl.requirement;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import net.minecraft.util.Mth;

public abstract class AbstractDelayedRequirement<T extends IMachineComponent> extends AbstractRequirement<T> implements IDelayedRequirement<T> {

    private double delay = 0.0;

    public AbstractDelayedRequirement(RequirementIOMode mode) {
        super(mode);
    }

    @Override
    public void setDelay(double delay) {
        this.delay = Mth.clamp(delay, 0.0, 1.0);
    }

    @Override
    public double getDelay() {
        return this.delay;
    }

    public boolean isDelayed() {
        return this.delay != 0.0;
    }
}
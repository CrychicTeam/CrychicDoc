package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHasFluidLevel extends ConstructConditional<ConstructHasFluidLevel> {

    private int percent;

    public ConstructHasFluidLevel(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        float pct = (float) this.percent / 100.0F;
        return this.construct.getFluidPct() >= pct;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("has_fluid_level.pct").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.percent = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskIntegerParameter("has_fluid_level.pct", 0, 100));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.HAS_FLUID);
    }

    public ConstructHasFluidLevel copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructHasFluidLevel) {
            this.percent = ((ConstructHasFluidLevel) other).percent;
        }
        return this;
    }

    public ConstructHasFluidLevel duplicate() {
        ConstructHasFluidLevel output = new ConstructHasFluidLevel(this.construct, this.guiIcon);
        output.percent = this.percent;
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}
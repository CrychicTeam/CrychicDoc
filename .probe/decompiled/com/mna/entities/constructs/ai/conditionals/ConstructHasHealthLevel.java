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
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHasHealthLevel extends ConstructConditional<ConstructHasHealthLevel> {

    private int percent;

    public ConstructHasHealthLevel(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        float pct = (float) this.percent / 100.0F;
        AbstractGolem c = this.construct.asEntity();
        return c.m_21223_() / c.m_21233_() >= pct;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("health_level.pct").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.percent = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskIntegerParameter("health_level.pct", 0, 100));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.HAS_HEALTH);
    }

    public ConstructHasHealthLevel copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructHasHealthLevel) {
            this.percent = ((ConstructHasHealthLevel) other).percent;
        }
        return this;
    }

    public ConstructHasHealthLevel duplicate() {
        ConstructHasHealthLevel output = new ConstructHasHealthLevel(this.construct, this.guiIcon);
        output.percent = this.percent;
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}
package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHasItemInHand extends ConstructConditional<ConstructHasItemInHand> {

    private DynamicItemFilter _filter = new DynamicItemFilter();

    public ConstructHasItemInHand(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        return this.construct.hasItem(this._filter);
    }

    @Override
    public void inflateParameters() {
        this.getParameter("has_item.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter) {
                this._filter = ((ConstructTaskFilterParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskFilterParameter("has_item.filter"));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.HAS_ITEM);
    }

    public ConstructHasItemInHand copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructHasItemInHand) {
            this._filter.copyFrom(((ConstructHasItemInHand) other)._filter);
        }
        return this;
    }

    public ConstructHasItemInHand duplicate() {
        ConstructHasItemInHand output = new ConstructHasItemInHand(this.construct, this.guiIcon);
        output._filter.copyFrom(this._filter);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this._filter != null && !this._filter.isEmpty();
    }
}
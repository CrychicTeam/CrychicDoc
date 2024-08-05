package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHasName extends ConstructConditional<ConstructHasName> {

    private ItemStack nameTagStack = ItemStack.EMPTY;

    private boolean caseInsensitive = false;

    public ConstructHasName(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        Component cName = this.construct.asEntity().m_7770_();
        if (cName == null || !this.nameTagStack.hasCustomHoverName()) {
            return false;
        } else {
            return this.caseInsensitive ? cName.getString().toLowerCase().equals(this.nameTagStack.getHoverName().getString().toLowerCase()) : cName.getString().equals(this.nameTagStack.getHoverName().getString());
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("has_name.stack").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.nameTagStack = ((ConstructTaskItemStackParameter) param).getStack().copy();
            }
        });
        this.getParameter("has_name.case_insensitive").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.caseInsensitive = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskItemStackParameter("has_name.stack"));
        output.add(new ConstructTaskBooleanParameter("has_name.case_insensitive"));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.HAS_NAME);
    }

    public ConstructHasName copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructHasName) {
            this.nameTagStack = ((ConstructHasName) other).nameTagStack.copy();
            this.caseInsensitive = ((ConstructHasName) other).caseInsensitive;
        }
        return this;
    }

    public ConstructHasName duplicate() {
        ConstructHasName output = new ConstructHasName(this.construct, this.guiIcon);
        output.nameTagStack = this.nameTagStack.copy();
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}
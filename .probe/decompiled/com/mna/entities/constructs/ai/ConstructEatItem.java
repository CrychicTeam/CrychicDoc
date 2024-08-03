package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructEatItem extends ConstructAITask<ConstructEatItem> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private DynamicItemFilter filter;

    private int eat_counter = -1;

    public ConstructEatItem(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.filter = new DynamicItemFilter();
    }

    @Override
    public void tick() {
        super.tick();
        InteractionHand[] hands = this.construct.getCarryingHands(this.filter);
        if (hands.length > 0) {
            InteractionHand hand = hands[0];
            if (this.eat_counter == -1) {
                this.getConstructAsEntity().m_6674_(hand);
                this.construct.setEating(hand);
                this.eat_counter = 30;
            } else if (--this.eat_counter <= 0) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.eat_success", new Object[0]));
                this.construct.asEntity().m_21008_(hand, ItemStack.EMPTY);
                this.construct.setHappy(100);
                this.construct.resetActions();
                this.exitCode = 0;
            }
        } else {
            this.exitCode = 1;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.clearMoveTarget();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.m_8036_();
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.EAT_ITEM);
    }

    public ConstructEatItem duplicate() {
        return new ConstructEatItem(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructEatItem copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructEatItem) {
            this.filter.copyFrom(((ConstructEatItem) other).filter);
        }
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("eat_item.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter) {
                this.filter.copyFrom(((ConstructTaskFilterParameter) param).getValue());
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskFilterParameter("eat_item.filter"));
        return parameters;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}
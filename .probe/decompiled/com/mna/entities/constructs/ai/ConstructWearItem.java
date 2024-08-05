package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructWearItem extends ConstructAITask<ConstructWearItem> {

    private ItemEntity target;

    public ConstructWearItem(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.target != null && this.target.m_6084_()) {
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, this.target.m_19879_());
            this.setMoveTarget(this.target);
            if (this.doMove()) {
                this.construct.asEntity().m_6674_(InteractionHand.MAIN_HAND);
                ItemStack hat = this.target.getItem().copy();
                hat.setCount(1);
                this.construct.setHat(hat);
                this.target.getItem().shrink(1);
                if (this.target.getItem().getCount() <= 0) {
                    this.target.m_142687_(Entity.RemovalReason.DISCARDED);
                }
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
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.WEAR_ITEM);
    }

    public ConstructWearItem duplicate() {
        return new ConstructWearItem(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructWearItem copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructWearItem) {
            this.target = ((ConstructWearItem) other).target;
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
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        return super.instantiateParameters();
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return new ConstructCapability[0];
    }

    @Override
    public boolean isFullyConfigured() {
        return this.target != null;
    }

    public void setTarget(ItemEntity target) {
        this.target = target;
    }
}
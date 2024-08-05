package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructEntityAreaTask;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.entities.passive.Magmoo;
import java.util.Collection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructMilkCow extends ConstructEntityAreaTask<Cow, ConstructMilkCow> {

    private static final String KEY_COW_MILK_TIME = "last_milk_time";

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE };

    private int interactTimer = this.getInteractTime(ConstructCapability.FLUID_DISPENSE);

    private FluidStack extractStack;

    public ConstructMilkCow(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, Cow.class);
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (!this.locateTarget()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.milk_no_target", new Object[0]), false);
            this.forceFail();
        } else {
            this.setMoveTarget(this.getSelectedTarget());
            if (!this.hasMoveTarget()) {
                this.forceFail();
            } else {
                if (this.doMove() && this.milkTarget()) {
                    this.releaseEntityMutex(this.getSelectedTarget());
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.milk_success", new Object[] { this.translate(this.getSelectedTarget()) }), false);
                    this.setSuccessCode();
                }
            }
        }
    }

    protected boolean entityPredicate(Cow candidate) {
        boolean baseline = candidate.m_6084_() && !candidate.m_6162_() && candidate.m_146764_() == 0 && this.canCowBeMilked(candidate);
        if (!baseline) {
            return baseline;
        } else if (candidate instanceof MushroomCow) {
            return false;
        } else {
            Fluid fluid = ForgeMod.MILK.get();
            if (candidate instanceof Magmoo) {
                fluid = Fluids.LAVA;
            }
            this.extractStack = new FluidStack(fluid, 1000);
            return this.construct.isFluidValid(0, this.extractStack);
        }
    }

    protected Cow selectTarget(Collection<Cow> entities) {
        if (entities.size() == 0) {
            return null;
        } else {
            Cow candidate = null;
            for (Cow var4 : entities) {
                if (this.claimEntityMutex(var4)) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.milk_target", new Object[] { this.translate(var4) }), false);
                    return var4;
                }
            }
            return null;
        }
    }

    private boolean canCowBeMilked(Cow cow) {
        return !cow.getPersistentData().contains("last_milk_time") || this.construct.asEntity().m_9236_().getGameTime() >= cow.getPersistentData().getLong("last_milk_time");
    }

    private void setNextMilkTime(Cow cow) {
        cow.getPersistentData().putLong("last_milk_time", this.construct.asEntity().m_9236_().getGameTime() + 12000L + (long) ((int) (Math.random() * 12000.0)));
    }

    private boolean milkTarget() {
        if (this.interactTimer > 0) {
            if (this.interactTimer == 5) {
                this.construct.getHandWithCapability(ConstructCapability.FLUID_DISPENSE).ifPresent(h -> this.construct.asEntity().m_6674_(h));
            }
            this.interactTimer--;
            return false;
        } else {
            this.setNextMilkTime(this.getSelectedTarget());
            this.construct.fill(this.extractStack, IFluidHandler.FluidAction.EXECUTE);
            this.interactTimer = this.getInteractTime(ConstructCapability.CARRY);
            return true;
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.clearMoveTarget();
    }

    @Override
    protected String getAreaIdentifier() {
        return "milk.area";
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.MILK);
    }

    public ConstructMilkCow duplicate() {
        return new ConstructMilkCow(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructMilkCow copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
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
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}
package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructBlockAreaTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCollectFluid extends ConstructBlockAreaTask<ConstructCollectFluid> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE };

    private ItemStack fluidStack;

    public ConstructCollectFluid(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.construct.getTankCapacity(1) == this.construct.getStoredFluidAmount()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_tank_full", new Object[0]));
            this.exitCode = 1;
        } else if (this.findBlockTarget()) {
            if (this.moveToCurrentTarget()) {
                this.harvest();
            }
        }
    }

    private void harvest() {
        AbstractGolem c = this.getConstructAsEntity();
        BlockState state = c.m_9236_().getBlockState(this.currentTarget);
        FluidState flState = state.m_60819_();
        if (flState != null && flState.isSource()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.collecting_fluid", new Object[] { this.translate(state), this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_() }));
            Fluid fluid = flState.getType();
            int fluidAmt = Math.max(flState.getAmount(), 1000);
            this.construct.fill(new FluidStack(fluid, fluidAmt), IFluidHandler.FluidAction.EXECUTE);
            if (state.m_60734_() instanceof BucketPickup) {
                BucketPickup bp = (BucketPickup) state.m_60734_();
                bp.pickupBlock(c.m_9236_(), this.currentTarget, state);
            }
            c.m_9236_().m_142346_(c, GameEvent.FLUID_PICKUP, this.currentTarget);
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.fluid_invalid", new Object[] { this.translate(state), this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_() }));
        }
        this.removeBlockTarget(this.currentTarget);
        this.currentTarget = null;
        this.exitCode = 0;
    }

    @Override
    protected boolean isValidBlock(BlockState state, BlockPos pos) {
        return blockValid(state);
    }

    public static boolean blockValid(BlockState state) {
        FluidState flState = state.m_60819_();
        return flState == null ? false : flState.isSource();
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.interactTimer = this.getInteractTime(ConstructCapability.HARVEST);
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("collect_fluid.stack").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.fluidStack = ((ConstructTaskItemStackParameter) param).getStack();
            }
        });
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskItemStackParameter("collect_fluid.stack"));
        return parameters;
    }

    public ConstructCollectFluid duplicate() {
        return new ConstructCollectFluid(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructCollectFluid copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructCollectFluid) {
            this.fluidStack = ((ConstructCollectFluid) other).fluidStack;
        }
        return this;
    }

    @Override
    protected int getInteractTimer() {
        return this.getInteractTime(ConstructCapability.FLUID_DISPENSE);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.GATHER_FLUID);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    @Override
    protected String getAreaIdentifier() {
        return "collect_fluid.area";
    }
}
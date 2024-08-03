package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructIsFluidInContainer extends ConstructConditional<ConstructIsFluidInContainer> {

    private DirectionalPoint pos;

    private ItemStack fluid = ItemStack.EMPTY;

    private int minPct;

    public ConstructIsFluidInContainer(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        if (this.pos == null) {
            return false;
        } else {
            FluidStack searchFluid = FluidStack.EMPTY;
            LazyOptional<IFluidHandlerItem> itemHandler = this.fluid.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (itemHandler.isPresent()) {
                IFluidHandlerItem flHItem = (IFluidHandlerItem) itemHandler.resolve().get();
                for (int i = 0; i < flHItem.getTanks(); i++) {
                    searchFluid = flHItem.getFluidInTank(i);
                    if (!searchFluid.isEmpty()) {
                        break;
                    }
                }
                if (searchFluid.isEmpty()) {
                    return false;
                }
            }
            Level world = this.construct.asEntity().m_9236_();
            if (!world.isLoaded(this.pos.getPosition())) {
                return false;
            } else {
                BlockState state = world.getBlockState(this.pos.getPosition());
                if (state.m_60734_() instanceof LayeredCauldronBlock) {
                    Fluid fluid = (Fluid) (state.m_60734_() == Blocks.LAVA_CAULDRON ? Fluids.LAVA : (state.m_60734_() == Blocks.WATER_CAULDRON ? Fluids.WATER : Fluids.EMPTY));
                    int amount = ((LayeredCauldronBlock) state.m_60734_()).isFull(state) ? 1000 : (Integer) state.m_61143_(LayeredCauldronBlock.LEVEL) * 333;
                    FluidStack contained = new FluidStack(fluid, amount);
                    if ((searchFluid.isEmpty() || searchFluid.isFluidEqual(contained)) && amount > 0) {
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                        return true;
                    }
                }
                LazyOptional<IFluidHandler> handler = FluidUtil.getFluidHandler(world, this.pos.getPosition(), this.pos.getDirection());
                if (!handler.isPresent()) {
                    this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                    return false;
                } else {
                    IFluidHandler tank = (IFluidHandler) handler.resolve().get();
                    for (int ix = 0; ix < tank.getTanks(); ix++) {
                        FluidStack contained = tank.getFluidInTank(ix);
                        int capacity = tank.getTankCapacity(ix);
                        float pct = (float) contained.getAmount() / (float) capacity * 100.0F;
                        if (searchFluid.isEmpty()) {
                            if (!contained.isEmpty() && pct >= (float) this.minPct) {
                                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                                return true;
                            }
                        } else if (contained.getFluid() == searchFluid.getFluid() && pct >= (float) this.minPct) {
                            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                            return true;
                        }
                    }
                    this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                    return false;
                }
            }
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("fluid_in_container.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.fluid = ((ConstructTaskItemStackParameter) param).getStack();
            }
        });
        this.getParameter("fluid_in_container.pos").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.pos = ((ConstructTaskPointParameter) param).getPoint();
            }
        });
        this.getParameter("fluid_in_container.pct").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.minPct = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskItemStackParameter("fluid_in_container.filter"));
        output.add(new ConstructTaskPointParameter("fluid_in_container.pos"));
        output.add(new ConstructTaskIntegerParameter("fluid_in_container.pct", 0, 100));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.CONTAINER_FLUID);
    }

    public ConstructIsFluidInContainer copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructIsFluidInContainer flOther) {
            this.pos = flOther.pos;
            this.fluid = flOther.fluid.copy();
            this.minPct = flOther.minPct;
        }
        return this;
    }

    public ConstructIsFluidInContainer duplicate() {
        ConstructIsFluidInContainer output = new ConstructIsFluidInContainer(this.construct, this.guiIcon);
        output.copyFrom(this);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.pos != null && this.pos.isValid();
    }
}
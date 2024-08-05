package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.entities.constructs.ai.base.ConstructCommandBlockInteract;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructPlaceFluidInTank extends ConstructCommandBlockInteract<ConstructPlaceFluidInTank> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE };

    private int interactTimer = 20;

    private int containerTank = -1;

    public ConstructPlaceFluidInTank(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        BlockState state = this.getBlockState();
        if (this.interactTimer < 0) {
            this.interactTimer--;
            if (this.interactTimer <= -16) {
                this.exitCode = 1;
            }
        } else if (this.construct.getFluidInTank(1).getAmount() == 0) {
            this.exitCode = 1;
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_tank_empty", new Object[] { this.translate(this.getBlockState()) }));
        } else {
            if (state != null) {
                if (this.doMove()) {
                    if (this.interactTimer > 0) {
                        if (this.interactTimer == 5) {
                            this.construct.getHandWithCapability(ConstructCapability.FLUID_DISPENSE).ifPresent(h -> c.m_6674_(h));
                        }
                        this.interactTimer--;
                    } else if (this.interactTimer == 0) {
                        if (state.m_60734_() instanceof AbstractCauldronBlock) {
                            this.placeFluidInCauldron(c, state);
                        } else {
                            this.placeFluidInHandler(c, state);
                        }
                    }
                }
            } else {
                this.exitCode = 1;
                c.f_21345_.removeGoal(this);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.block_missing", new Object[0]));
            }
        }
    }

    private void placeFluidInCauldron(AbstractGolem c, BlockState state) {
        Fluid fluid = (Fluid) (state.m_60734_() == Blocks.LAVA_CAULDRON ? Fluids.LAVA : (state.m_60734_() == Blocks.WATER_CAULDRON ? Fluids.WATER : Fluids.EMPTY));
        int amount = ((AbstractCauldronBlock) state.m_60734_()).isFull(state) ? 1000 : 0;
        FluidStack containedFluid = new FluidStack(fluid, amount);
        if (containedFluid.isEmpty()) {
            FluidStack constructFluid = this.construct.getFluidInTank(0);
            if (!constructFluid.isFluidEqual(new FluidStack(Fluids.LAVA, 1000)) && !constructFluid.isFluidEqual(new FluidStack(Fluids.WATER, 1000))) {
                this.exitCode = 1;
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_invalid_type", new Object[] { this.translate(state) }));
                return;
            }
            FluidStack drainStack = new FluidStack(constructFluid.getFluid(), 1000);
            if (constructFluid.getAmount() >= 1000 && this.construct.drain(drainStack, IFluidHandler.FluidAction.SIMULATE).getAmount() == drainStack.getAmount()) {
                this.construct.drain(drainStack, IFluidHandler.FluidAction.EXECUTE);
                c.m_9236_().setBlock(this.blockPos, constructFluid.isFluidEqual(new FluidStack(Fluids.LAVA, 1000)) ? (BlockState) Blocks.LAVA_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3) : (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3), 3);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_success", new Object[] { this.translate(containedFluid), this.translate(state) }));
                this.exitCode = 0;
            }
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_fail", new Object[] { this.translate(state) }));
            this.exitCode = 1;
        }
    }

    private void placeFluidInHandler(AbstractGolem c, BlockState state) {
        LazyOptional<IFluidHandler> handler = FluidUtil.getFluidHandler(c.m_9236_(), this.blockPos, this.side);
        if (handler.isPresent()) {
            IFluidHandler tank = (IFluidHandler) handler.resolve().get();
            this.resolveTank(tank);
            if (this.containerTank == -1) {
                this.exitCode = 1;
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_invalid_type", new Object[] { this.translate(state) }));
                return;
            }
            if (this.doMove()) {
                if (this.interactTimer > 0) {
                    if (this.interactTimer == 5) {
                        this.construct.getHandWithCapability(ConstructCapability.CARRY).ifPresent(h -> c.m_6674_(h));
                    }
                    this.interactTimer--;
                } else if (this.interactTimer == 0) {
                    FluidStack stack = this.construct.getFluidInTank(1).copy();
                    if (!stack.isEmpty()) {
                        FluidStack transferred = FluidUtil.tryFluidTransfer(tank, this.construct, 1000, true);
                        if (transferred.isEmpty()) {
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_fail", new Object[] { this.translate(state) }));
                            this.exitCode = 1;
                        } else {
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_success", new Object[] { this.translate(stack), this.translate(state) }));
                            this.exitCode = 0;
                        }
                    } else {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_in_tank_fail", new Object[] { this.translate(state) }));
                        this.exitCode = 1;
                    }
                    this.interactTimer = -1;
                }
            }
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.fluid_missing_cap", new Object[] { this.translate(this.getBlockState()) }));
        }
    }

    private void resolveTank(IFluidHandler tankContainer) {
        for (int i = 0; i < tankContainer.getTanks(); i++) {
            if (tankContainer.isFluidValid(i, this.construct.getFluidInTank(1))) {
                this.containerTank = i;
                return;
            }
        }
        this.containerTank = -1;
    }

    @Override
    public void start() {
        super.m_8056_();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PLACE_FLUID_IN_CONTAINER);
    }

    public ConstructPlaceFluidInTank duplicate() {
        return new ConstructPlaceFluidInTank(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructPlaceFluidInTank copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        super.readNBT(nbt);
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return super.writeInternal(nbt);
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        return super.instantiateParameters();
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}
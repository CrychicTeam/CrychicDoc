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

public class ConstructTakeFluid extends ConstructCommandBlockInteract<ConstructTakeFluid> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE };

    private int interactTimer = 20;

    private int containerTank = -1;

    public ConstructTakeFluid(IConstruct<?> construct, ResourceLocation guiIcon) {
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
        } else if (this.construct.getFluidInTank(1).getAmount() == this.construct.getTankCapacity(1)) {
            this.exitCode = 1;
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_tank_full", new Object[] { this.translate(state) }));
        } else {
            if (state != null) {
                if (this.doMove()) {
                    if (this.interactTimer > 0) {
                        if (this.interactTimer == 5) {
                            this.construct.getHandWithCapability(ConstructCapability.FLUID_DISPENSE).ifPresent(h -> c.m_6674_(h));
                        }
                        this.interactTimer--;
                    } else if (this.interactTimer == 0) {
                        if (state.m_60734_() instanceof LayeredCauldronBlock) {
                            this.takeFluidFromCauldron(state, c);
                        } else {
                            this.takeFluidFromHandler(state, c);
                        }
                        this.interactTimer = -1;
                    }
                }
            } else {
                this.exitCode = 1;
                c.f_21345_.removeGoal(this);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.block_missing", new Object[0]));
            }
        }
    }

    private void takeFluidFromCauldron(BlockState state, AbstractGolem c) {
        Fluid fluid = (Fluid) (state.m_60734_() == Blocks.LAVA_CAULDRON ? Fluids.LAVA : (state.m_60734_() == Blocks.WATER_CAULDRON ? Fluids.WATER : Fluids.EMPTY));
        int amount = ((LayeredCauldronBlock) state.m_60734_()).isFull(state) ? 1000 : 0;
        FluidStack containedFluid = new FluidStack(fluid, amount);
        if (containedFluid.isEmpty() || !((LayeredCauldronBlock) state.m_60734_()).isFull(state)) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_fail", new Object[] { this.translate(state) }));
            this.exitCode = 1;
        } else if (this.construct.isFluidValid(0, containedFluid)) {
            this.construct.fill(containedFluid, IFluidHandler.FluidAction.EXECUTE);
            c.m_9236_().setBlock(this.blockPos, (BlockState) state.m_61124_(LayeredCauldronBlock.LEVEL, 0), 3);
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_in_tank_success", new Object[] { this.translate(containedFluid), this.translate(state) }));
            this.exitCode = 0;
        } else {
            this.exitCode = 1;
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_invalid_type", new Object[] { this.translate(state) }));
        }
    }

    private void takeFluidFromHandler(BlockState state, AbstractGolem c) {
        LazyOptional<IFluidHandler> handler = FluidUtil.getFluidHandler(c.m_9236_(), this.blockPos, this.side);
        if (handler.isPresent()) {
            IFluidHandler tank = (IFluidHandler) handler.resolve().get();
            this.resolveTank(tank);
            if (this.containerTank == -1) {
                this.exitCode = 1;
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_invalid_type", new Object[] { this.translate(state) }));
                return;
            }
            FluidStack containedFluid = tank.getFluidInTank(this.containerTank).copy();
            if (!containedFluid.isEmpty()) {
                FluidStack transferred = FluidUtil.tryFluidTransfer(this.construct, tank, 1000, true);
                if (transferred.isEmpty()) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_in_tank_fail", new Object[] { this.translate(state) }));
                    this.exitCode = 1;
                } else {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_in_tank_success", new Object[] { this.translate(containedFluid), this.translate(state) }));
                    this.exitCode = 0;
                }
            } else {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fluid_fail", new Object[] { this.translate(state) }));
                this.exitCode = 1;
            }
        }
    }

    private void resolveTank(IFluidHandler tankContainer) {
        for (int i = 0; i < tankContainer.getTanks(); i++) {
            if (this.construct.isFluidValid(1, tankContainer.getFluidInTank(i))) {
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
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.TAKE_FLUID_FROM_CONTAINER);
    }

    public ConstructTakeFluid duplicate() {
        return new ConstructTakeFluid(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructTakeFluid copyFrom(ConstructAITask<?> other) {
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
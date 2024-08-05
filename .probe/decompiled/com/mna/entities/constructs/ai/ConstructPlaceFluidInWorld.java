package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructPlaceFluidInWorld extends ConstructAITask<ConstructPlaceFluidInWorld> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE };

    private int interactTimer = 20;

    protected BlockPos blockPos;

    protected Direction side;

    public ConstructPlaceFluidInWorld(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.blockPos != null && this.side != null;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.construct.getFluidInTank(0).getAmount() < 1000) {
            if (!this.isSuccess()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_tank_empty", new Object[0]));
            }
        } else {
            BlockPos pos = this.blockPos;
            if (!c.m_9236_().isLoaded(pos) || !c.m_9236_().m_46859_(pos)) {
                pos = pos.offset(this.side.getNormal());
                if (!c.m_9236_().isLoaded(pos) || !c.m_9236_().m_46859_(pos)) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_not_clear", new Object[0]));
                    this.exitCode = 1;
                    return;
                }
            }
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(pos));
            this.setMoveTarget(pos);
            if (this.doMove(2.0F)) {
                if (this.interactTimer > 0) {
                    this.interactTimer--;
                } else if (this.interactTimer == 0) {
                    FluidStack fluid = this.construct.getFluidInTank(0);
                    String blockTranslated = this.translate(fluid);
                    boolean placed = FluidUtil.tryPlaceFluid(null, this.construct.asEntity().m_9236_(), InteractionHand.MAIN_HAND, pos, this.construct, fluid);
                    if (!placed) {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_failed", new Object[0]));
                        this.exitCode = 1;
                    } else {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fluid_success", new Object[] { blockTranslated, pos.m_123341_(), pos.m_123342_(), pos.m_123343_() }));
                        this.exitCode = 0;
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PLACE_FLUID);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructPlaceFluidInWorld copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructPlaceFluidInWorld) {
            this.side = ((ConstructPlaceFluidInWorld) other).side;
            this.blockPos = ((ConstructPlaceFluidInWorld) other).blockPos;
        }
        return this;
    }

    public ConstructPlaceFluidInWorld duplicate() {
        return new ConstructPlaceFluidInWorld(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.blockPos != null) {
            nbt.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
        }
        if (this.side != null) {
            nbt.putInt("direction", this.side.get3DDataValue());
        }
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("blockPos")) {
            this.blockPos = NbtUtils.readBlockPos(nbt.getCompound("blockPos"));
        }
        if (nbt.contains("direction")) {
            this.side = Direction.from3DDataValue(nbt.getInt("direction"));
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("place_fluid.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
                this.side = ((ConstructTaskPointParameter) param).getDirection();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("place_fluid.point"));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null && this.side != null;
    }
}
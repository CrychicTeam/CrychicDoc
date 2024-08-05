package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructMove extends ConstructAITask<ConstructMove> {

    protected BlockPos blockPos;

    public ConstructMove(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
        this.setMoveTarget(this.blockPos);
        if (this.doMove(1.0F)) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_success", new Object[0]));
            this.exitCode = 0;
        }
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.MOVE);
    }

    public ConstructMove duplicate() {
        return new ConstructMove(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructMove copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructMove) {
            this.blockPos = ((ConstructMove) other).blockPos;
        }
        return this;
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.blockPos != null) {
            nbt.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
        }
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("blockPos")) {
            this.blockPos = NbtUtils.readBlockPos(nbt.getCompound("blockPos"));
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("move.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("move.point"));
        return parameters;
    }

    public void setDesiredLocation(BlockPos pos) {
        this.blockPos = pos;
        this.getParameter("move.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                ((ConstructTaskPointParameter) param).setPoint(new DirectionalPoint(pos, Direction.UP, ""));
            }
        });
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null;
    }
}
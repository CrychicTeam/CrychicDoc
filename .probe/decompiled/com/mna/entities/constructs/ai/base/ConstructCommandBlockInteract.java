package com.mna.entities.constructs.ai.base;

import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class ConstructCommandBlockInteract<V extends ConstructAITask<?>> extends ConstructAITask<ConstructCommandBlockInteract<V>> {

    protected BlockPos blockPos;

    protected Direction side;

    private Vec3 lastConstructPos;

    private float stuckPosThreshold = 0.25F;

    private int stuckCount = 0;

    private Direction[] offsetDirections = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

    public ConstructCommandBlockInteract(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    public boolean canUse() {
        AbstractGolem c = this.getConstructAsEntity();
        return super.canUse() && this.blockPos != null && c.m_9236_().isLoaded(this.blockPos);
    }

    @Override
    public boolean canContinueToUse() {
        AbstractGolem c = this.getConstructAsEntity();
        return super.canContinueToUse() && this.blockPos != null && c.m_9236_().isLoaded(this.blockPos);
    }

    @Override
    public boolean doMove() {
        AbstractGolem c = this.getConstructAsEntity();
        Vec3 invPos = Vec3.atCenterOf(this.blockPos);
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, invPos);
        double dist = c.m_20238_(invPos);
        if (dist > 2.5) {
            if (this.moveCooldown == 0) {
                Vec3 constructPos = c.m_20182_();
                if (this.lastConstructPos != null) {
                    if (constructPos.distanceToSqr(this.lastConstructPos) < (double) (this.stuckPosThreshold * this.stuckPosThreshold)) {
                        this.stuckCount++;
                        if (this.stuckCount >= 2) {
                            Direction dir = this.offsetDirections[(int) (Math.random() * (double) this.offsetDirections.length)];
                            c.m_21573_().stop();
                            if (c.m_21573_().moveTo(c.m_20185_() + (double) (dir.getStepX() * 3), c.m_20186_() + (double) (dir.getStepY() * 3), c.m_20189_() + (double) (dir.getStepZ() * 3), c.m_21133_(Attributes.MOVEMENT_SPEED))) {
                                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_success", new Object[] { invPos.x, invPos.y, invPos.z }), false);
                            } else {
                                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.move_fail", new Object[] { invPos.x, invPos.y, invPos.z }), false);
                            }
                            this.moveCooldown = 20;
                            this.stuckCount = 0;
                            return false;
                        }
                    } else {
                        this.stuckCount = 0;
                    }
                }
                this.lastConstructPos = constructPos;
                this.setPathToXYZ(invPos, 1.0F);
                this.moveCooldown = 20;
            }
            return false;
        } else {
            c.m_21573_().stop();
            this.faceBlockPos(this.blockPos);
            return true;
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter(this.getPointIdentifier()).ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
                this.side = ((ConstructTaskPointParameter) param).getDirection();
            }
        });
    }

    @Nullable
    protected BlockState getBlockState() {
        return this.construct.asEntity().m_9236_().isLoaded(this.blockPos) ? this.construct.asEntity().m_9236_().getBlockState(this.blockPos) : null;
    }

    public ConstructCommandBlockInteract<V> copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCommandBlockInteract) {
            this.side = ((ConstructCommandBlockInteract) other).side;
            this.blockPos = ((ConstructCommandBlockInteract) other).blockPos;
        }
        return this;
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
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter(this.getPointIdentifier()));
        return parameters;
    }

    protected String getPointIdentifier() {
        return "teinteract.point";
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null && this.side != null;
    }
}
package com.mna.api.entities.construct.ai;

import com.mna.api.blocks.IRequirePlayerReference;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public abstract class ConstructCommandTileEntityInteract<T extends BlockEntity, V extends ConstructAITask<?>> extends ConstructAITask<ConstructCommandTileEntityInteract<T, V>> {

    protected BlockPos blockPos;

    protected Direction side;

    private Class<T> teClass;

    private T _cached_te_ref;

    private Vec3 lastConstructPos;

    private float stuckPosThreshold = 0.25F;

    private int stuckCount = 0;

    private Direction[] offsetDirections = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

    public ConstructCommandTileEntityInteract(IConstruct<?> construct, ResourceLocation guiIcon, Class<T> tileEntityClass) {
        super(construct, guiIcon);
        this.teClass = tileEntityClass;
    }

    public void setTileEntity(BlockPos pos, Direction side) {
        this.blockPos = pos;
        this.side = side;
        this._cached_te_ref = null;
    }

    @Override
    public boolean canUse() {
        AbstractGolem c = this.getConstructAsEntity();
        if (!super.canUse()) {
            return false;
        } else if (this.blockPos == null) {
            return false;
        } else if (!c.m_9236_().isLoaded(this.blockPos)) {
            return false;
        } else {
            BlockEntity be = c.m_9236_().getBlockEntity(this.blockPos);
            return be == null ? false : this.teClass.isInstance(c.m_9236_().getBlockEntity(this.blockPos));
        }
    }

    @Override
    public boolean canContinueToUse() {
        AbstractGolem c = this.getConstructAsEntity();
        if (!super.canContinueToUse()) {
            return false;
        } else if (this.blockPos == null) {
            return false;
        } else if (!c.m_9236_().isLoaded(this.blockPos)) {
            return false;
        } else {
            BlockEntity be = c.m_9236_().getBlockEntity(this.blockPos);
            return be == null ? false : this.teClass.isInstance(c.m_9236_().getBlockEntity(this.blockPos));
        }
    }

    @Override
    public void start() {
        super.start();
        this._cached_te_ref = null;
        BlockEntity te = this.getTileEntity();
        if (te != null && this.teClass.isInstance(te)) {
            this._cached_te_ref = (T) te;
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
        }
    }

    @Nullable
    protected T getTileEntity() {
        if (this.blockPos == null) {
            return null;
        } else {
            AbstractGolem c = this.getConstructAsEntity();
            if (this._cached_te_ref == null && c.m_9236_().isLoaded(this.blockPos)) {
                BlockEntity te = c.m_9236_().getBlockEntity(this.blockPos);
                if (this.teClass.isInstance(te)) {
                    this._cached_te_ref = (T) te;
                }
            }
            return this._cached_te_ref;
        }
    }

    protected void preInteract() {
        if (this.getTileEntity() instanceof IRequirePlayerReference) {
            ((IRequirePlayerReference) this.getTileEntity()).setPlayerReference(this.createFakePlayer());
        }
    }

    @Override
    public boolean doMove() {
        if (this.getTileEntity() == null) {
            return false;
        } else {
            AbstractGolem c = this.getConstructAsEntity();
            BlockPos tePos = this.getTileEntity().getBlockPos();
            Vec3 invPos = new Vec3((double) tePos.m_123341_() + 0.5, (double) tePos.m_123342_() + 0.5, (double) tePos.m_123343_() + 0.5);
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
                this.faceBlockPos(tePos);
                return true;
            }
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

    public ConstructCommandTileEntityInteract<T, V> copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCommandTileEntityInteract && this.teClass == ((ConstructCommandTileEntityInteract) other).teClass) {
            this.side = ((ConstructCommandTileEntityInteract) other).side;
            this.blockPos = ((ConstructCommandTileEntityInteract) other).blockPos;
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
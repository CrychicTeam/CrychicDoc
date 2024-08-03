package com.mna.api.entities.construct.ai;

import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskAreaParameter;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class ConstructBlockAreaTask<T extends ConstructBlockAreaTask<?>> extends ConstructAITask<ConstructBlockAreaTask<T>> {

    protected static final int MAX_SIZE = 32;

    private static final int MAX_BLOCKS_CHECKED_PER_TICK = 50;

    protected AABB area = null;

    protected NonNullList<BlockPos> knownTargets;

    protected BlockPos currentTarget;

    protected int lastLocation = 0;

    protected boolean tooBig = false;

    protected int interactTimer;

    protected boolean searchingSurroundings = false;

    protected int x;

    protected int y;

    protected int z;

    public ConstructBlockAreaTask(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.knownTargets = NonNullList.create();
    }

    protected abstract boolean isValidBlock(BlockState var1, BlockPos var2);

    protected abstract int getInteractTimer();

    protected boolean findBlockTarget() {
        if (this.currentTarget != null) {
            return true;
        } else if (this.area == null) {
            return false;
        } else {
            if (!this.searchingSurroundings && this.knownTargets.size() == 0) {
                this.setupRecheck();
            }
            if (this.searchingSurroundings) {
                this.searchSurroundings();
                return false;
            } else {
                this.pickLocation();
                if (this.currentTarget == null) {
                    this.forceFail();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean moveToCurrentTarget() {
        this.setMoveTarget(this.currentTarget);
        if (this.doMove()) {
            if (this.interactTimer > 0) {
                this.interactTimer--;
                if (this.interactTimer != 4) {
                    return false;
                }
            }
            AbstractGolem c = this.getConstructAsEntity();
            if (c.m_9236_().isLoaded(this.currentTarget)) {
                BlockState state = c.m_9236_().getBlockState(this.currentTarget);
                if (this.isValidBlock(state, this.currentTarget)) {
                    if (this.interactTimer == 4) {
                        this.construct.getHandWithCapability(this.getSwingArmCap()).ifPresent(h -> c.m_6674_(h));
                        return false;
                    }
                    return true;
                }
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_badstate", new Object[] { this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_(), this.translate(state) }));
                this.removeBlockTarget(this.currentTarget);
                this.currentTarget = null;
                this.exitCode = 1;
            }
        }
        return false;
    }

    protected ConstructCapability getSwingArmCap() {
        return ConstructCapability.HARVEST;
    }

    protected void removeBlockTarget(BlockPos pos) {
        this.knownTargets.remove(pos);
        this.lastLocation = 0;
    }

    private void setupRecheck() {
        this.searchingSurroundings = true;
        this.knownTargets.clear();
        this.lastLocation = 0;
        this.x = (int) this.area.minX;
        this.y = (int) this.area.minY;
        this.z = (int) this.area.minZ;
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_scan_starting", new Object[0]));
    }

    private void searchSurroundings() {
        AbstractGolem c = this.getConstructAsEntity();
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, this.area);
        for (int count = 0; (double) this.x < this.area.maxX; this.x++) {
            while ((double) this.y < this.area.maxY) {
                while ((double) this.z < this.area.maxZ) {
                    BlockPos curSearch = new BlockPos(this.x, this.y, this.z);
                    if (c.m_9236_().isLoaded(curSearch) && this.isValidBlock(c.m_9236_().getBlockState(curSearch), curSearch)) {
                        this.knownTargets.add(curSearch);
                    }
                    if (++count >= 50) {
                        return;
                    }
                    this.z++;
                }
                this.z = (int) this.area.minZ;
                this.y++;
            }
            this.y = (int) this.area.minY;
        }
        this.searchingSurroundings = false;
        if (this.knownTargets.size() == 0) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_scan_no_results", new Object[0]));
            this.exitCode = 1;
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_scan_success", new Object[0]));
        }
    }

    private void pickLocation() {
        int count = 0;
        if (this.knownTargets.size() == 0) {
            this.exitCode = 1;
        } else {
            for (AbstractGolem c = this.getConstructAsEntity(); this.lastLocation < this.knownTargets.size(); this.lastLocation++) {
                BlockPos curSearch = this.knownTargets.get(this.lastLocation);
                if (c.m_9236_().isLoaded(curSearch)) {
                    if (this.isValidBlock(c.m_9236_().getBlockState(curSearch), curSearch)) {
                        if (this.claimBlockMutex(curSearch)) {
                            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(curSearch));
                            this.currentTarget = curSearch;
                            return;
                        }
                    } else {
                        this.knownTargets.remove(this.lastLocation);
                        this.lastLocation--;
                    }
                }
                if (++count >= 50) {
                    return;
                }
            }
            this.lastLocation = 0;
        }
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            this.forceFail();
            return false;
        } else if (this.area == null) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_not_configured", new Object[0]), false);
            this.forceFail();
            return false;
        } else if (this.tooBig) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.area_too_big", new Object[] { 32, this.area.getXsize(), this.area.getYsize(), this.area.getZsize() }), false);
            this.forceFail();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }

    @Override
    public void inflateParameters() {
        this.getParameter(this.getAreaIdentifier()).ifPresent(param -> {
            if (param instanceof ConstructTaskAreaParameter) {
                this.area = ((ConstructTaskAreaParameter) param).getArea();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskAreaParameter(this.getAreaIdentifier()));
        return parameters;
    }

    protected abstract String getAreaIdentifier();

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.interactTimer = this.getInteractTimer();
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    public ConstructBlockAreaTask<T> copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructBlockAreaTask) {
            this.area = ((ConstructBlockAreaTask) other).area;
        }
        return this;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.area != null;
    }

    public void setAreaManually(BlockPos cornerA, BlockPos cornerB) {
        this.getParameter(this.getAreaIdentifier()).ifPresent(param -> {
            if (param instanceof ConstructTaskAreaParameter) {
                ((ConstructTaskAreaParameter) param).setPoints(new DirectionalPoint(cornerA, Direction.UP, ""), new DirectionalPoint(cornerB, Direction.UP, ""));
                this.area = ((ConstructTaskAreaParameter) param).getArea();
            }
        });
    }
}
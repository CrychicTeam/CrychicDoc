package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructPatrol extends ConstructAITask<ConstructPatrol> {

    private static final int LEASH_RANGE = 1024;

    protected static final int NUM_POINTS = 5;

    private int patrolWaitTime = 100;

    private int targetRate = 20;

    private List<BlockPos> patrol_points;

    private int patrolPointIndex = 0;

    private int nextPathWaitTime = 0;

    private long targetCounter = 0L;

    private boolean leashReturning = false;

    public ConstructPatrol(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
        this.patrol_points = new ArrayList();
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.patrol_points.size() == 0) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.patrol_no_points", new Object[0]));
        }
        if (this.construct.getConstructData().isAnyCapabilityEnabled(ConstructCapability.MELEE_ATTACK, ConstructCapability.RANGED_ATTACK) && !this.leashReturning && this.targetCounter++ >= (long) this.targetRate) {
            this.targetNearby();
        }
        if (c.m_5448_() != null && !this.leashReturning) {
            this.doAttackTarget();
        } else {
            this.doPatrol();
        }
    }

    private void doPatrol() {
        List<BlockPos> configured_points = (List<BlockPos>) this.patrol_points.stream().filter(p -> p != null).collect(Collectors.toList());
        if (configured_points.size() != 0 && this.patrolPointIndex < configured_points.size()) {
            BlockPos pos = (BlockPos) configured_points.get(this.patrolPointIndex);
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(pos));
            this.setMoveTarget(pos);
            if (this.doMove()) {
                this.leashReturning = false;
                if (this.nextPathWaitTime > 0) {
                    this.nextPathWaitTime--;
                    return;
                }
                this.releaseBlockMutex(pos);
                this.patrolPointIndex++;
                while (this.patrolPointIndex < configured_points.size() && this.claimBlockMutex((BlockPos) configured_points.get(this.patrolPointIndex))) {
                    this.patrolPointIndex++;
                }
                this.nextPathWaitTime = this.patrolWaitTime * 20;
                if (this.patrolPointIndex >= configured_points.size()) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.patrol_complete", new Object[0]));
                    this.exitCode = 0;
                    return;
                }
            }
        } else {
            this.forceFail();
        }
    }

    private void doAttackTarget() {
        AbstractGolem c = this.getConstructAsEntity();
        if (!c.m_5448_().isAlive()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.victory", new Object[] { this.translate(c.m_5448_()) }));
            c.m_6710_(null);
        } else if (!this.isOutsideLeashRange()) {
            this.doAttack();
        }
    }

    private boolean isOutsideLeashRange() {
        List<BlockPos> configured_points = (List<BlockPos>) this.patrol_points.stream().filter(p -> p != null).collect(Collectors.toList());
        if (configured_points.size() != 0 && this.patrolPointIndex < configured_points.size()) {
            BlockPos pos = (BlockPos) configured_points.get(this.patrolPointIndex);
            if (this.getConstructAsEntity().m_20238_(Vec3.atCenterOf(pos)) > 1024.0) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.leash_distance_exceeded", new Object[0]));
                this.getConstructAsEntity().m_6710_(null);
                this.setMoveTarget(pos);
                this.leashReturning = true;
                return true;
            } else {
                return false;
            }
        } else {
            this.forceFail();
            return true;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.patrolPointIndex = 0;
    }

    @Override
    public void start() {
        super.start();
        this.patrolPointIndex = 0;
    }

    private void targetNearby() {
        this.targetCounter = 0L;
        AbstractGolem c = this.getConstructAsEntity();
        if (this.owner != null && this.owner.m_6084_() && (c.m_5448_() == null || !c.m_5448_().isAlive())) {
            List<LivingEntity> entities = this.construct.getValidAttackTargets();
            if (entities.size() != 0) {
                entities.sort(new Comparator<LivingEntity>() {

                    public int compare(LivingEntity o1, LivingEntity o2) {
                        Double o1Dist = o1.m_20280_(ConstructPatrol.this.owner);
                        Double o2Dist = o2.m_20280_(ConstructPatrol.this.owner);
                        return o1Dist.compareTo(o2Dist);
                    }
                });
                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, ((LivingEntity) entities.get(0)).m_19879_());
                c.m_6710_((LivingEntity) entities.get(0));
                this.setMoveTarget(c.m_5448_());
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.attack", new Object[] { this.translate(c.m_5448_().m_6095_().getDescriptionId(), new Object[0]) }), false);
            }
        }
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PATROL);
    }

    public ConstructPatrol copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructPatrol) {
            this.patrol_points.clear();
            this.patrol_points.addAll(((ConstructPatrol) other).patrol_points);
            this.patrolWaitTime = ((ConstructPatrol) other).patrolWaitTime;
        }
        return this;
    }

    public ConstructPatrol duplicate() {
        return new ConstructPatrol(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return new CompoundTag();
    }

    @Override
    public void inflateParameters() {
        this.patrol_points.clear();
        for (int i = 0; i < 5; i++) {
            this.getParameter("patrol.point_" + i).ifPresent(param -> {
                if (param instanceof ConstructTaskPointParameter) {
                    this.patrol_points.add(((ConstructTaskPointParameter) param).getPosition());
                }
            });
        }
        this.getParameter("patrol.wait_time").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.patrolWaitTime = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        for (int i = 0; i < 5; i++) {
            parameters.add(new ConstructTaskPointParameter("patrol.point_" + i));
        }
        parameters.add(new ConstructTaskIntegerParameter("patrol.wait_time", 0, 30));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.patrol_points.size() > 0;
    }
}
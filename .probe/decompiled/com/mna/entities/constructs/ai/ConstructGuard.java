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
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructGuard extends ConstructAITask<ConstructGuard> {

    private BlockPos guard_point;

    private Direction guard_direction;

    private int guard_time = 100;

    private static final int LEASH_RANGE = 1024;

    private int targetRate = 20;

    private int guard_counter = 0;

    private long targetCounter = 0L;

    private boolean leashReturning = false;

    public ConstructGuard(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGolem c = this.getConstructAsEntity();
        if (!this.claimBlockMutex(this.guard_point)) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.guard_block_claimed", new Object[0]));
            this.forceFail();
        } else {
            if (this.construct.getConstructData().isAnyCapabilityEnabled(ConstructCapability.MELEE_ATTACK, ConstructCapability.RANGED_ATTACK) && !this.leashReturning && this.targetCounter++ >= (long) this.targetRate) {
                this.targetNearby();
            }
            if (c.m_5448_() != null && !this.leashReturning) {
                this.doAttackTarget();
            } else {
                this.doGuard();
            }
        }
    }

    private void doGuard() {
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.guard_point));
        this.setMoveTarget(this.guard_point);
        if (this.doMove(1.0F)) {
            AbstractGolem c = this.construct.asEntity();
            Vec3 desiredPoint = Vec3.atBottomCenterOf(this.guard_point);
            BlockHitResult bhr = c.m_9236_().m_45547_(new ClipContext(Vec3.atBottomCenterOf(this.guard_point.above(Math.min((int) c.getStepHeight(), 1) + 1)), desiredPoint, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, c));
            Vec3 guardPos = bhr != null && bhr.getType() != HitResult.Type.MISS ? bhr.m_82450_() : desiredPoint;
            this.construct.asEntity().m_146884_(guardPos);
            this.leashReturning = false;
            Vec3 lookPos = Vec3.atCenterOf(this.guard_point.offset(this.guard_direction.getNormal().multiply(5)));
            this.getConstructAsEntity().m_7618_(EntityAnchorArgument.Anchor.FEET, lookPos);
            this.construct.getDiagnostics().setMovePos(lookPos);
            this.guard_counter++;
            if (this.guard_counter >= this.guard_time) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.patrol_complete", new Object[0]));
                this.setSuccessCode();
            }
        }
    }

    private void doAttackTarget() {
        AbstractGolem c = this.getConstructAsEntity();
        if (!c.m_5448_().isAlive()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.victory", new Object[] { this.translate(c.m_5448_()) }));
            c.m_6710_(null);
            this.setMoveTarget(this.guard_point);
        } else if (this.isOutsideLeashRange()) {
            this.getConstructAsEntity().m_6710_(null);
            this.setMoveTarget(this.guard_point);
            this.leashReturning = true;
        } else {
            this.doAttack();
        }
    }

    private boolean isOutsideLeashRange() {
        int leash_range = this.construct.getConstructData().calculatePerception();
        if (this.getConstructAsEntity().m_20238_(Vec3.atCenterOf(this.guard_point)) > (double) (leash_range * leash_range)) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.leash_distance_exceeded", new Object[0]));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.leashReturning = false;
        this.guard_counter = 0;
    }

    @Override
    public void start() {
        super.start();
        this.leashReturning = false;
        this.guard_counter = 0;
    }

    private void targetNearby() {
        this.targetCounter = 0L;
        AbstractGolem c = this.getConstructAsEntity();
        if (this.owner != null && this.owner.m_6084_() && (c.m_5448_() == null || !c.m_5448_().isAlive())) {
            List<LivingEntity> entities = this.construct.getValidAttackTargets();
            if (entities.size() != 0) {
                entities.sort(new Comparator<LivingEntity>() {

                    public int compare(LivingEntity o1, LivingEntity o2) {
                        Double o1Dist = o1.m_20280_(ConstructGuard.this.owner);
                        Double o2Dist = o2.m_20280_(ConstructGuard.this.owner);
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
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.GUARD);
    }

    public ConstructGuard copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructGuard otherGuard) {
            this.guard_point = otherGuard.guard_point;
            this.guard_direction = otherGuard.guard_direction;
            this.guard_time = otherGuard.guard_time;
        }
        return this;
    }

    public ConstructGuard duplicate() {
        return new ConstructGuard(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return new CompoundTag();
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("guard.point"));
        parameters.add(new ConstructTaskIntegerParameter("guard.wait_time", 1, 30));
        return parameters;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("guard.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter point) {
                this.guard_point = point.getPosition();
                this.guard_direction = point.getDirection();
            }
        });
        this.getParameter("guard.wait_time").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter wait) {
                this.guard_time = wait.getValue() * 20;
            }
        });
    }

    @Override
    public boolean isFullyConfigured() {
        return this.guard_point != null && this.guard_direction != null && this.guard_time > 0;
    }
}
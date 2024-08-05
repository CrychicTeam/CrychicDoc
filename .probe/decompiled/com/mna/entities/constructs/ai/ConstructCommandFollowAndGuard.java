package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.SummonUtils;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCommandFollowAndGuard extends ConstructAITask<ConstructCommandFollowAndGuard> {

    private int leashRange = 32;

    private int targetRate = 20;

    private long targetCounter = 0L;

    private boolean guard = true;

    private int followTarget = -1;

    private LivingEntity cachedFollowTarget;

    public ConstructCommandFollowAndGuard(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.getFollowTarget() != null;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity toFollow = this.getFollowTarget();
        if (toFollow != null && toFollow.isAlive()) {
            AbstractGolem c = this.getConstructAsEntity();
            if (!c.m_20160_() && this.guard) {
                if (this.construct.getConstructData().isAnyCapabilityEnabled(ConstructCapability.MELEE_ATTACK, ConstructCapability.RANGED_ATTACK, ConstructCapability.FLUID_DISPENSE, ConstructCapability.CAST_SPELL)) {
                    this.targetCounter++;
                    if (this.targetCounter >= (long) this.targetRate) {
                        this.targetNearby();
                    }
                }
            } else {
                c.m_6710_(null);
            }
            double followTargetDist = c.m_20238_(toFollow.m_20182_());
            this.checkLeashRange(followTargetDist);
            if (c.m_5448_() != null && this.guard) {
                if (SummonUtils.isTargetFriendly(c.m_5448_(), toFollow)) {
                    c.m_6710_(null);
                } else if (!c.m_20160_()) {
                    this.doAttackTarget();
                }
            } else {
                this.doFollow(followTargetDist);
            }
        }
    }

    public void setFollowTarget(LivingEntity target) {
        this.followTarget = target != null ? target.m_19879_() : -1;
    }

    public void setShouldGuard(boolean guard) {
        this.guard = guard;
    }

    private void checkLeashRange(double followTargetDist) {
        if (followTargetDist > (double) (this.leashRange * this.leashRange)) {
            AbstractGolem c = this.getConstructAsEntity();
            if (c.m_5448_() != null) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.leashed", new Object[] { this.translate(c.m_5448_()) }));
            }
            c.m_6710_(null);
        }
    }

    private void doFollow(double followTargetDist) {
        AbstractGolem c = this.getConstructAsEntity();
        if (!c.m_20160_()) {
            if (followTargetDist < 4.0) {
                c.m_21573_().stop();
            } else {
                this.setMoveTarget(this.getFollowTarget());
                this.doMove();
            }
        }
    }

    private void doAttackTarget() {
        AbstractGolem c = this.getConstructAsEntity();
        if (!c.m_5448_().isAlive()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.victory", new Object[] { this.translate(c.m_5448_()) }));
            c.m_6710_(null);
        } else {
            this.doAttack();
        }
    }

    @Override
    public boolean allowSteeringMountedConstructsDuringTask() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        if (this.guard) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.follow_guard", new Object[0]), false);
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.follow", new Object[0]), false);
        }
    }

    private void targetNearby() {
        this.targetCounter = 0L;
        AbstractGolem c = this.getConstructAsEntity();
        final LivingEntity toFollow = this.getFollowTarget();
        if (toFollow != null && toFollow.isAlive() && (c.m_5448_() == null || !c.m_5448_().isAlive())) {
            List<LivingEntity> entities = this.construct.getValidAttackTargets();
            if (entities.size() != 0) {
                if (entities.size() > 1) {
                    try {
                        entities.sort(new Comparator<LivingEntity>() {

                            public int compare(LivingEntity o1, LivingEntity o2) {
                                Double o1Dist = o1.m_20280_(toFollow);
                                Double o2Dist = o2.m_20280_(toFollow);
                                return o1Dist.compareTo(o2Dist);
                            }
                        });
                    } catch (Exception var5) {
                        return;
                    }
                }
                c.m_6710_((LivingEntity) entities.get(0));
                if (c.m_5448_() != null) {
                    this.setMoveTarget(c.m_5448_());
                    this.doMove();
                    if (c.m_5448_() != this.construct.getOwner()) {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.attack", new Object[] { this.translate(c.m_5448_().m_6095_().getDescriptionId(), new Object[0]) }), false);
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.FOLLOW_DEFEND);
    }

    public ConstructCommandFollowAndGuard duplicate() {
        return new ConstructCommandFollowAndGuard(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructCommandFollowAndGuard copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCommandFollowAndGuard) {
            this.followTarget = ((ConstructCommandFollowAndGuard) other).followTarget;
            this.guard = ((ConstructCommandFollowAndGuard) other).guard;
        }
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("guard")) {
            this.guard = nbt.getBoolean("guard");
        }
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        nbt.putBoolean("guardTarget", this.guard);
        return nbt;
    }

    @Nullable
    public LivingEntity getFollowTarget() {
        if (this.cachedFollowTarget == null) {
            if (this.followTarget > -1) {
                Entity entity = this.construct.asEntity().m_9236_().getEntity(this.followTarget);
                if (entity != null && entity instanceof LivingEntity living) {
                    this.cachedFollowTarget = living;
                }
            } else {
                this.cachedFollowTarget = this.owner;
            }
        }
        return this.cachedFollowTarget;
    }

    @Override
    public void inflateParameters() {
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}
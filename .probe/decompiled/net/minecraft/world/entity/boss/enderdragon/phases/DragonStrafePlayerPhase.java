package net.minecraft.world.entity.boss.enderdragon.phases;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class DragonStrafePlayerPhase extends AbstractDragonPhaseInstance {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int FIREBALL_CHARGE_AMOUNT = 5;

    private int fireballCharge;

    @Nullable
    private Path currentPath;

    @Nullable
    private Vec3 targetLocation;

    @Nullable
    private LivingEntity attackTarget;

    private boolean holdingPatternClockwise;

    public DragonStrafePlayerPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doServerTick() {
        if (this.attackTarget == null) {
            LOGGER.warn("Skipping player strafe phase because no player was found");
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        } else {
            if (this.currentPath != null && this.currentPath.isDone()) {
                double $$0 = this.attackTarget.m_20185_();
                double $$1 = this.attackTarget.m_20189_();
                double $$2 = $$0 - this.f_31176_.m_20185_();
                double $$3 = $$1 - this.f_31176_.m_20189_();
                double $$4 = Math.sqrt($$2 * $$2 + $$3 * $$3);
                double $$5 = Math.min(0.4F + $$4 / 80.0 - 1.0, 10.0);
                this.targetLocation = new Vec3($$0, this.attackTarget.m_20186_() + $$5, $$1);
            }
            double $$6 = this.targetLocation == null ? 0.0 : this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
            if ($$6 < 100.0 || $$6 > 22500.0) {
                this.findNewTarget();
            }
            double $$7 = 64.0;
            if (this.attackTarget.m_20280_(this.f_31176_) < 4096.0) {
                if (this.f_31176_.m_142582_(this.attackTarget)) {
                    this.fireballCharge++;
                    Vec3 $$8 = new Vec3(this.attackTarget.m_20185_() - this.f_31176_.m_20185_(), 0.0, this.attackTarget.m_20189_() - this.f_31176_.m_20189_()).normalize();
                    Vec3 $$9 = new Vec3((double) Mth.sin(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)), 0.0, (double) (-Mth.cos(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)))).normalize();
                    float $$10 = (float) $$9.dot($$8);
                    float $$11 = (float) (Math.acos((double) $$10) * 180.0F / (float) Math.PI);
                    $$11 += 0.5F;
                    if (this.fireballCharge >= 5 && $$11 >= 0.0F && $$11 < 10.0F) {
                        double $$12 = 1.0;
                        Vec3 $$13 = this.f_31176_.m_20252_(1.0F);
                        double $$14 = this.f_31176_.head.m_20185_() - $$13.x * 1.0;
                        double $$15 = this.f_31176_.head.m_20227_(0.5) + 0.5;
                        double $$16 = this.f_31176_.head.m_20189_() - $$13.z * 1.0;
                        double $$17 = this.attackTarget.m_20185_() - $$14;
                        double $$18 = this.attackTarget.m_20227_(0.5) - $$15;
                        double $$19 = this.attackTarget.m_20189_() - $$16;
                        if (!this.f_31176_.m_20067_()) {
                            this.f_31176_.m_9236_().m_5898_(null, 1017, this.f_31176_.m_20183_(), 0);
                        }
                        DragonFireball $$20 = new DragonFireball(this.f_31176_.m_9236_(), this.f_31176_, $$17, $$18, $$19);
                        $$20.m_7678_($$14, $$15, $$16, 0.0F, 0.0F);
                        this.f_31176_.m_9236_().m_7967_($$20);
                        this.fireballCharge = 0;
                        if (this.currentPath != null) {
                            while (!this.currentPath.isDone()) {
                                this.currentPath.advance();
                            }
                        }
                        this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
                    }
                } else if (this.fireballCharge > 0) {
                    this.fireballCharge--;
                }
            } else if (this.fireballCharge > 0) {
                this.fireballCharge--;
            }
        }
    }

    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isDone()) {
            int $$0 = this.f_31176_.findClosestNode();
            int $$1 = $$0;
            if (this.f_31176_.m_217043_().nextInt(8) == 0) {
                this.holdingPatternClockwise = !this.holdingPatternClockwise;
                $$1 = $$0 + 6;
            }
            if (this.holdingPatternClockwise) {
                $$1++;
            } else {
                $$1--;
            }
            if (this.f_31176_.getDragonFight() != null && this.f_31176_.getDragonFight().getCrystalsAlive() > 0) {
                $$1 %= 12;
                if ($$1 < 0) {
                    $$1 += 12;
                }
            } else {
                $$1 -= 12;
                $$1 &= 7;
                $$1 += 12;
            }
            this.currentPath = this.f_31176_.findPath($$0, $$1, null);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isDone()) {
            Vec3i $$0 = this.currentPath.getNextNodePos();
            this.currentPath.advance();
            double $$1 = (double) $$0.getX();
            double $$2 = (double) $$0.getZ();
            double $$3;
            do {
                $$3 = (double) ((float) $$0.getY() + this.f_31176_.m_217043_().nextFloat() * 20.0F);
            } while ($$3 < (double) $$0.getY());
            this.targetLocation = new Vec3($$1, $$3, $$2);
        }
    }

    @Override
    public void begin() {
        this.fireballCharge = 0;
        this.targetLocation = null;
        this.currentPath = null;
        this.attackTarget = null;
    }

    public void setTarget(LivingEntity livingEntity0) {
        this.attackTarget = livingEntity0;
        int $$1 = this.f_31176_.findClosestNode();
        int $$2 = this.f_31176_.findClosestNode(this.attackTarget.m_20185_(), this.attackTarget.m_20186_(), this.attackTarget.m_20189_());
        int $$3 = this.attackTarget.m_146903_();
        int $$4 = this.attackTarget.m_146907_();
        double $$5 = (double) $$3 - this.f_31176_.m_20185_();
        double $$6 = (double) $$4 - this.f_31176_.m_20189_();
        double $$7 = Math.sqrt($$5 * $$5 + $$6 * $$6);
        double $$8 = Math.min(0.4F + $$7 / 80.0 - 1.0, 10.0);
        int $$9 = Mth.floor(this.attackTarget.m_20186_() + $$8);
        Node $$10 = new Node($$3, $$9, $$4);
        this.currentPath = this.f_31176_.findPath($$1, $$2, $$10);
        if (this.currentPath != null) {
            this.currentPath.advance();
            this.navigateToNextPathNode();
        }
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonStrafePlayerPhase> getPhase() {
        return EnderDragonPhase.STRAFE_PLAYER;
    }
}
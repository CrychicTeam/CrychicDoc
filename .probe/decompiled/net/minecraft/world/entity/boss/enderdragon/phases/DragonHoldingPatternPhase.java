package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DragonHoldingPatternPhase extends AbstractDragonPhaseInstance {

    private static final TargetingConditions NEW_TARGET_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();

    @Nullable
    private Path currentPath;

    @Nullable
    private Vec3 targetLocation;

    private boolean clockwise;

    public DragonHoldingPatternPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public EnderDragonPhase<DragonHoldingPatternPhase> getPhase() {
        return EnderDragonPhase.HOLDING_PATTERN;
    }

    @Override
    public void doServerTick() {
        double $$0 = this.targetLocation == null ? 0.0 : this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
        if ($$0 < 100.0 || $$0 > 22500.0 || this.f_31176_.f_19862_ || this.f_31176_.f_19863_) {
            this.findNewTarget();
        }
    }

    @Override
    public void begin() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() {
        if (this.currentPath != null && this.currentPath.isDone()) {
            BlockPos $$0 = this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(EndPodiumFeature.getLocation(this.f_31176_.getFightOrigin())));
            int $$1 = this.f_31176_.getDragonFight() == null ? 0 : this.f_31176_.getDragonFight().getCrystalsAlive();
            if (this.f_31176_.m_217043_().nextInt($$1 + 3) == 0) {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.LANDING_APPROACH);
                return;
            }
            Player $$2 = this.f_31176_.m_9236_().m_45949_(NEW_TARGET_TARGETING, this.f_31176_, (double) $$0.m_123341_(), (double) $$0.m_123342_(), (double) $$0.m_123343_());
            double $$3;
            if ($$2 != null) {
                $$3 = $$0.m_203193_($$2.m_20182_()) / 512.0;
            } else {
                $$3 = 64.0;
            }
            if ($$2 != null && (this.f_31176_.m_217043_().nextInt((int) ($$3 + 2.0)) == 0 || this.f_31176_.m_217043_().nextInt($$1 + 2) == 0)) {
                this.strafePlayer($$2);
                return;
            }
        }
        if (this.currentPath == null || this.currentPath.isDone()) {
            int $$5 = this.f_31176_.findClosestNode();
            int $$6 = $$5;
            if (this.f_31176_.m_217043_().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                $$6 = $$5 + 6;
            }
            if (this.clockwise) {
                $$6++;
            } else {
                $$6--;
            }
            if (this.f_31176_.getDragonFight() != null && this.f_31176_.getDragonFight().getCrystalsAlive() >= 0) {
                $$6 %= 12;
                if ($$6 < 0) {
                    $$6 += 12;
                }
            } else {
                $$6 -= 12;
                $$6 &= 7;
                $$6 += 12;
            }
            this.currentPath = this.f_31176_.findPath($$5, $$6, null);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
    }

    private void strafePlayer(Player player0) {
        this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
        this.f_31176_.getPhaseManager().getPhase(EnderDragonPhase.STRAFE_PLAYER).setTarget(player0);
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
    public void onCrystalDestroyed(EndCrystal endCrystal0, BlockPos blockPos1, DamageSource damageSource2, @Nullable Player player3) {
        if (player3 != null && this.f_31176_.canAttack(player3)) {
            this.strafePlayer(player3);
        }
    }
}
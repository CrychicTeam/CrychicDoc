package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DragonLandingApproachPhase extends AbstractDragonPhaseInstance {

    private static final TargetingConditions NEAR_EGG_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();

    @Nullable
    private Path currentPath;

    @Nullable
    private Vec3 targetLocation;

    public DragonLandingApproachPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public EnderDragonPhase<DragonLandingApproachPhase> getPhase() {
        return EnderDragonPhase.LANDING_APPROACH;
    }

    @Override
    public void begin() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    public void doServerTick() {
        double $$0 = this.targetLocation == null ? 0.0 : this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
        if ($$0 < 100.0 || $$0 > 22500.0 || this.f_31176_.f_19862_ || this.f_31176_.f_19863_) {
            this.findNewTarget();
        }
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isDone()) {
            int $$0 = this.f_31176_.findClosestNode();
            BlockPos $$1 = this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.f_31176_.getFightOrigin()));
            Player $$2 = this.f_31176_.m_9236_().m_45949_(NEAR_EGG_TARGETING, this.f_31176_, (double) $$1.m_123341_(), (double) $$1.m_123342_(), (double) $$1.m_123343_());
            int $$4;
            if ($$2 != null) {
                Vec3 $$3 = new Vec3($$2.m_20185_(), 0.0, $$2.m_20189_()).normalize();
                $$4 = this.f_31176_.findClosestNode(-$$3.x * 40.0, 105.0, -$$3.z * 40.0);
            } else {
                $$4 = this.f_31176_.findClosestNode(40.0, (double) $$1.m_123342_(), 0.0);
            }
            Node $$6 = new Node($$1.m_123341_(), $$1.m_123342_(), $$1.m_123343_());
            this.currentPath = this.f_31176_.findPath($$0, $$4, $$6);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
        if (this.currentPath != null && this.currentPath.isDone()) {
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
        }
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
}
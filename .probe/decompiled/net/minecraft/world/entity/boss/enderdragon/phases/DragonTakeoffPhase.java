package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DragonTakeoffPhase extends AbstractDragonPhaseInstance {

    private boolean firstTick;

    @Nullable
    private Path currentPath;

    @Nullable
    private Vec3 targetLocation;

    public DragonTakeoffPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doServerTick() {
        if (!this.firstTick && this.currentPath != null) {
            BlockPos $$0 = this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.f_31176_.getFightOrigin()));
            if (!$$0.m_203195_(this.f_31176_.m_20182_(), 10.0)) {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
            }
        } else {
            this.firstTick = false;
            this.findNewTarget();
        }
    }

    @Override
    public void begin() {
        this.firstTick = true;
        this.currentPath = null;
        this.targetLocation = null;
    }

    private void findNewTarget() {
        int $$0 = this.f_31176_.findClosestNode();
        Vec3 $$1 = this.f_31176_.getHeadLookVector(1.0F);
        int $$2 = this.f_31176_.findClosestNode(-$$1.x * 40.0, 105.0, -$$1.z * 40.0);
        if (this.f_31176_.getDragonFight() != null && this.f_31176_.getDragonFight().getCrystalsAlive() > 0) {
            $$2 %= 12;
            if ($$2 < 0) {
                $$2 += 12;
            }
        } else {
            $$2 -= 12;
            $$2 &= 7;
            $$2 += 12;
        }
        this.currentPath = this.f_31176_.findPath($$0, $$2, null);
        this.navigateToNextPathNode();
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null) {
            this.currentPath.advance();
            if (!this.currentPath.isDone()) {
                Vec3i $$0 = this.currentPath.getNextNodePos();
                this.currentPath.advance();
                double $$1;
                do {
                    $$1 = (double) ((float) $$0.getY() + this.f_31176_.m_217043_().nextFloat() * 20.0F);
                } while ($$1 < (double) $$0.getY());
                this.targetLocation = new Vec3((double) $$0.getX(), $$1, (double) $$0.getZ());
            }
        }
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonTakeoffPhase> getPhase() {
        return EnderDragonPhase.TAKEOFF;
    }
}
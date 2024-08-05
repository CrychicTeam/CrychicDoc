package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DragonFlyingPhase extends PatchedDragonPhase {

    private Path currentPath;

    private Vec3 targetLocation;

    private boolean clockwise;

    private boolean executeAirstrike;

    public DragonFlyingPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public EnderDragonPhase<DragonFlyingPhase> getPhase() {
        return PatchedPhases.FLYING;
    }

    @Override
    public void doServerTick() {
        double d0 = this.targetLocation == null ? 0.0 : this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
        if (d0 < 100.0 || d0 > 22500.0 || this.f_31176_.f_19862_ || this.f_31176_.f_19863_ && this.f_31176_.getDragonFight() != null) {
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
        return this.dragonpatch.getEntityState().inaction() ? null : this.targetLocation;
    }

    public void enableAirstrike() {
        this.executeAirstrike = false;
    }

    private void findNewTarget() {
        if (this.currentPath != null && this.currentPath.isDone()) {
            for (Player player : this.getPlayersNearbyWithin(100.0)) {
                if (isValidTarget(player)) {
                    if (!this.executeAirstrike && this.f_31176_.m_217043_().nextFloat() > (float) this.dragonpatch.getNearbyCrystals() * 0.1F) {
                        if (isInEndSpikes(player)) {
                            this.executeAirstrike = true;
                        }
                        this.dragonpatch.setAttakTargetSync(player);
                        this.f_31176_.getPhaseManager().setPhase(PatchedPhases.AIRSTRIKE);
                    } else if (isInEndSpikes(player)) {
                        this.f_31176_.getPhaseManager().setPhase(PatchedPhases.LANDING);
                    }
                    return;
                }
            }
        }
        if (this.currentPath == null || this.currentPath.isDone()) {
            int j = this.f_31176_.findClosestNode();
            int k = j;
            if (this.f_31176_.m_217043_().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                k = j + 6;
            }
            if (this.clockwise) {
                k++;
            } else {
                k--;
            }
            if (this.f_31176_.getDragonFight() != null && this.dragonpatch.getNearbyCrystals() >= 0) {
                k %= 12;
                if (k < 0) {
                    k += 12;
                }
            } else {
                k -= 12;
                k &= 7;
                k += 12;
            }
            this.currentPath = this.f_31176_.findPath(j, k, null);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isDone()) {
            Vec3i vec3i = this.currentPath.getNextNodePos();
            this.currentPath.advance();
            double d0 = (double) vec3i.getX();
            double d1 = (double) vec3i.getZ();
            double d2;
            do {
                d2 = (double) ((float) vec3i.getY() + this.f_31176_.m_217043_().nextFloat() * 20.0F);
            } while (d2 < (double) vec3i.getY());
            this.targetLocation = new Vec3(d0, d2, d1);
        }
    }
}
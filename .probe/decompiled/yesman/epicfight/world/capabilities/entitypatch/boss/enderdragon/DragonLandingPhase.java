package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.Animations;

public class DragonLandingPhase extends PatchedDragonPhase {

    private final BlockPos[] landingCandidates = new BlockPos[3];

    private Vec3 landingPosition;

    private boolean actualLandingPhase;

    public DragonLandingPhase(EnderDragon enderdragon) {
        super(enderdragon);
        this.landingCandidates[0] = enderdragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, new BlockPos(-3, 0, -11));
        this.landingCandidates[1] = enderdragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, new BlockPos(17, 0, 0));
        this.landingCandidates[2] = enderdragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, new BlockPos(0, 0, 17));
    }

    @Override
    public void begin() {
        this.actualLandingPhase = false;
        this.landingPosition = this.getFarthestLandingPosition();
        if (!this.dragonpatch.isLogicalClient()) {
            this.dragonpatch.getOriginal().getPhaseManager().getPhase(PatchedPhases.GROUND_BATTLE).resetFlyCooldown();
        }
    }

    public Vec3 getFarthestLandingPosition() {
        double max = 0.0;
        Vec3 result = null;
        for (int i = 0; i < this.landingCandidates.length; i++) {
            Vec3 vec3d = new Vec3((double) this.landingCandidates[i].m_123341_(), (double) this.landingCandidates[i].m_123342_(), (double) this.landingCandidates[i].m_123343_());
            double distanceSqr = vec3d.distanceToSqr(this.f_31176_.m_20182_());
            if (distanceSqr > max) {
                max = distanceSqr;
                result = vec3d;
            }
        }
        return result;
    }

    @Override
    public void doServerTick() {
        double dx = this.landingPosition.x - this.f_31176_.m_20185_();
        double dy = this.landingPosition.y - this.f_31176_.m_20186_();
        double dz = this.landingPosition.z - this.f_31176_.m_20189_();
        double squaredD = dx * dx + dy * dy + dz * dz;
        double squaredHorizontalD = dx * dx + dz * dz;
        if (this.actualLandingPhase) {
            if (squaredHorizontalD < 50.0) {
                this.f_31176_.getPhaseManager().setPhase(PatchedPhases.GROUND_BATTLE);
            }
        } else {
            float f5 = this.m_7072_();
            double horizontalD = Math.sqrt(squaredHorizontalD);
            double yMove = dy;
            if (horizontalD > 0.0) {
                yMove = Mth.clamp(dy, (double) (-f5), (double) f5) * Mth.clamp((Math.abs(dy) - 13.0) * 0.01, 0.01, 0.03);
            }
            this.f_31176_.m_20256_(this.f_31176_.m_20184_().add(0.0, yMove, 0.0));
            this.f_31176_.m_146922_(Mth.wrapDegrees(this.f_31176_.m_146908_()));
            Vec3 vec32 = this.landingPosition.subtract(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_()).normalize();
            Vec3 vec33 = new Vec3((double) Mth.sin(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)), this.f_31176_.m_20184_().y, (double) (-Mth.cos(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)))).normalize();
            float f6 = Math.max(((float) vec33.dot(vec32) + 0.5F) / 1.5F, 0.0F);
            if (Math.abs(dx) > 1.0E-5F || Math.abs(dz) > 1.0E-5F) {
                double d5 = Mth.clamp(Mth.wrapDegrees(180.0 - Mth.atan2(dx, dz) * 180.0F / (float) Math.PI - (double) this.f_31176_.m_146908_()), -50.0, 50.0);
                this.f_31176_.yRotA *= 0.8F;
                this.f_31176_.yRotA = (float) ((double) this.f_31176_.yRotA + d5 * (double) this.m_7089_());
                this.f_31176_.m_146922_(this.f_31176_.m_146908_() + this.f_31176_.yRotA * 0.1F);
            }
            float f18 = (float) (2.0 / (squaredD + 1.0));
            this.f_31176_.m_19920_(0.06F * (f6 * f18 + (1.0F - f18)), new Vec3(0.0, 0.0, -1.0));
            if (this.f_31176_.inWall) {
                this.f_31176_.m_6478_(MoverType.SELF, this.f_31176_.m_20184_().scale(0.8F));
            } else {
                this.f_31176_.m_6478_(MoverType.SELF, this.f_31176_.m_20184_());
            }
            Vec3 vec34 = this.f_31176_.m_20184_().normalize();
            double d6 = 0.8 + 0.15 * (vec34.dot(vec33) + 1.0) / 2.0;
            this.f_31176_.m_20256_(this.f_31176_.m_20184_().multiply(d6, 0.91F, d6));
            if (squaredD < 400.0 && Math.abs(dy) < 14.0 && new Vec3(dx, 0.0, dz).normalize().dot(Vec3.directionFromRotation(new Vec2(0.0F, 180.0F + this.f_31176_.m_146908_()))) > 0.95) {
                this.dragonpatch.playAnimationSynchronized(Animations.DRAGON_FLY_TO_GROUND, 0.0F);
                this.actualLandingPhase = true;
            }
        }
    }

    public Vec3 getLandingPosition() {
        return this.landingPosition;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return PatchedPhases.LANDING;
    }
}
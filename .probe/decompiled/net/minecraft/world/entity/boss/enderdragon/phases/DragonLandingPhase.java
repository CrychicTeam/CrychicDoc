package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;

public class DragonLandingPhase extends AbstractDragonPhaseInstance {

    @Nullable
    private Vec3 targetLocation;

    public DragonLandingPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doClientTick() {
        Vec3 $$0 = this.f_31176_.getHeadLookVector(1.0F).normalize();
        $$0.yRot((float) (-Math.PI / 4));
        double $$1 = this.f_31176_.head.m_20185_();
        double $$2 = this.f_31176_.head.m_20227_(0.5);
        double $$3 = this.f_31176_.head.m_20189_();
        for (int $$4 = 0; $$4 < 8; $$4++) {
            RandomSource $$5 = this.f_31176_.m_217043_();
            double $$6 = $$1 + $$5.nextGaussian() / 2.0;
            double $$7 = $$2 + $$5.nextGaussian() / 2.0;
            double $$8 = $$3 + $$5.nextGaussian() / 2.0;
            Vec3 $$9 = this.f_31176_.m_20184_();
            this.f_31176_.m_9236_().addParticle(ParticleTypes.DRAGON_BREATH, $$6, $$7, $$8, -$$0.x * 0.08F + $$9.x, -$$0.y * 0.3F + $$9.y, -$$0.z * 0.08F + $$9.z);
            $$0.yRot((float) (Math.PI / 16));
        }
    }

    @Override
    public void doServerTick() {
        if (this.targetLocation == null) {
            this.targetLocation = Vec3.atBottomCenterOf(this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.f_31176_.getFightOrigin())));
        }
        if (this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_()) < 1.0) {
            this.f_31176_.getPhaseManager().getPhase(EnderDragonPhase.SITTING_FLAMING).resetFlameCount();
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
        }
    }

    @Override
    public float getFlySpeed() {
        return 1.5F;
    }

    @Override
    public float getTurnSpeed() {
        float $$0 = (float) this.f_31176_.m_20184_().horizontalDistance() + 1.0F;
        float $$1 = Math.min($$0, 40.0F);
        return $$1 / $$0;
    }

    @Override
    public void begin() {
        this.targetLocation = null;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonLandingPhase> getPhase() {
        return EnderDragonPhase.LANDING;
    }
}
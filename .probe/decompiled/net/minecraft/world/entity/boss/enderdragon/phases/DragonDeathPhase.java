package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;

public class DragonDeathPhase extends AbstractDragonPhaseInstance {

    @Nullable
    private Vec3 targetLocation;

    private int time;

    public DragonDeathPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doClientTick() {
        if (this.time++ % 10 == 0) {
            float $$0 = (this.f_31176_.m_217043_().nextFloat() - 0.5F) * 8.0F;
            float $$1 = (this.f_31176_.m_217043_().nextFloat() - 0.5F) * 4.0F;
            float $$2 = (this.f_31176_.m_217043_().nextFloat() - 0.5F) * 8.0F;
            this.f_31176_.m_9236_().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.f_31176_.m_20185_() + (double) $$0, this.f_31176_.m_20186_() + 2.0 + (double) $$1, this.f_31176_.m_20189_() + (double) $$2, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void doServerTick() {
        this.time++;
        if (this.targetLocation == null) {
            BlockPos $$0 = this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.f_31176_.getFightOrigin()));
            this.targetLocation = Vec3.atBottomCenterOf($$0);
        }
        double $$1 = this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
        if (!($$1 < 100.0) && !($$1 > 22500.0) && !this.f_31176_.f_19862_ && !this.f_31176_.f_19863_) {
            this.f_31176_.m_21153_(1.0F);
        } else {
            this.f_31176_.m_21153_(0.0F);
        }
    }

    @Override
    public void begin() {
        this.targetLocation = null;
        this.time = 0;
    }

    @Override
    public float getFlySpeed() {
        return 3.0F;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonDeathPhase> getPhase() {
        return EnderDragonPhase.DYING;
    }
}
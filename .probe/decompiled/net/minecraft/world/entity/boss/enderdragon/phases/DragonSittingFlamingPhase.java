package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;

public class DragonSittingFlamingPhase extends AbstractDragonSittingPhase {

    private static final int FLAME_DURATION = 200;

    private static final int SITTING_FLAME_ATTACKS_COUNT = 4;

    private static final int WARMUP_TIME = 10;

    private int flameTicks;

    private int flameCount;

    @Nullable
    private AreaEffectCloud flame;

    public DragonSittingFlamingPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doClientTick() {
        this.flameTicks++;
        if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
            Vec3 $$0 = this.f_31176_.getHeadLookVector(1.0F).normalize();
            $$0.yRot((float) (-Math.PI / 4));
            double $$1 = this.f_31176_.head.m_20185_();
            double $$2 = this.f_31176_.head.m_20227_(0.5);
            double $$3 = this.f_31176_.head.m_20189_();
            for (int $$4 = 0; $$4 < 8; $$4++) {
                double $$5 = $$1 + this.f_31176_.m_217043_().nextGaussian() / 2.0;
                double $$6 = $$2 + this.f_31176_.m_217043_().nextGaussian() / 2.0;
                double $$7 = $$3 + this.f_31176_.m_217043_().nextGaussian() / 2.0;
                for (int $$8 = 0; $$8 < 6; $$8++) {
                    this.f_31176_.m_9236_().addParticle(ParticleTypes.DRAGON_BREATH, $$5, $$6, $$7, -$$0.x * 0.08F * (double) $$8, -$$0.y * 0.6F, -$$0.z * 0.08F * (double) $$8);
                }
                $$0.yRot((float) (Math.PI / 16));
            }
        }
    }

    @Override
    public void doServerTick() {
        this.flameTicks++;
        if (this.flameTicks >= 200) {
            if (this.flameCount >= 4) {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            } else {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
            }
        } else if (this.flameTicks == 10) {
            Vec3 $$0 = new Vec3(this.f_31176_.head.m_20185_() - this.f_31176_.m_20185_(), 0.0, this.f_31176_.head.m_20189_() - this.f_31176_.m_20189_()).normalize();
            float $$1 = 5.0F;
            double $$2 = this.f_31176_.head.m_20185_() + $$0.x * 5.0 / 2.0;
            double $$3 = this.f_31176_.head.m_20189_() + $$0.z * 5.0 / 2.0;
            double $$4 = this.f_31176_.head.m_20227_(0.5);
            double $$5 = $$4;
            BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos($$2, $$4, $$3);
            while (this.f_31176_.m_9236_().m_46859_($$6)) {
                if (--$$5 < 0.0) {
                    $$5 = $$4;
                    break;
                }
                $$6.set($$2, $$5, $$3);
            }
            $$5 = (double) (Mth.floor($$5) + 1);
            this.flame = new AreaEffectCloud(this.f_31176_.m_9236_(), $$2, $$5, $$3);
            this.flame.setOwner(this.f_31176_);
            this.flame.setRadius(5.0F);
            this.flame.setDuration(200);
            this.flame.setParticle(ParticleTypes.DRAGON_BREATH);
            this.flame.addEffect(new MobEffectInstance(MobEffects.HARM));
            this.f_31176_.m_9236_().m_7967_(this.flame);
        }
    }

    @Override
    public void begin() {
        this.flameTicks = 0;
        this.flameCount++;
    }

    @Override
    public void end() {
        if (this.flame != null) {
            this.flame.m_146870_();
            this.flame = null;
        }
    }

    @Override
    public EnderDragonPhase<DragonSittingFlamingPhase> getPhase() {
        return EnderDragonPhase.SITTING_FLAMING;
    }

    public void resetFlameCount() {
        this.flameCount = 0;
    }
}
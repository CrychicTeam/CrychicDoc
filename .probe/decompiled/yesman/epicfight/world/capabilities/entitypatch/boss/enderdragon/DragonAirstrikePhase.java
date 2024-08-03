package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.entity.AreaEffectBreath;

public class DragonAirstrikePhase extends PatchedDragonPhase {

    private Vec3 startpos;

    private boolean isActuallyAttacking;

    public DragonAirstrikePhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void begin() {
        this.startpos = this.f_31176_.m_20182_();
        this.isActuallyAttacking = false;
        this.f_31176_.m_9236_().playLocalSound(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), SoundEvents.ENDER_DRAGON_GROWL, this.f_31176_.getSoundSource(), 5.0F, 0.8F + this.f_31176_.m_217043_().nextFloat() * 0.3F, false);
    }

    @Override
    public void end() {
        this.dragonpatch.setAttakTargetSync(null);
        if (this.dragonpatch.isLogicalClient()) {
            Minecraft.getInstance().getSoundManager().stop(EpicFightSounds.ENDER_DRAGON_BREATH.get().getLocation(), SoundSource.HOSTILE);
            this.f_31176_.m_9236_().playLocalSound(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), EpicFightSounds.ENDER_DRAGON_BREATH_FINALE.get(), this.f_31176_.getSoundSource(), 5.0F, 1.0F, false);
        }
    }

    @Override
    public void doClientTick() {
        super.doClientTick();
        Vec3 dragonpos = this.f_31176_.m_20182_();
        OpenMatrix4f mouthpos = this.dragonpatch.getArmature().getBindedTransformFor(this.dragonpatch.getArmature().getPose(1.0F), Armatures.DRAGON.upperMouth);
        float f = (float) this.f_31176_.getLatencyPos(7, 1.0F)[0];
        float f1 = (float) (this.f_31176_.getLatencyPos(5, 1.0F)[1] - this.f_31176_.getLatencyPos(10, 1.0F)[1]);
        float f2 = MathUtils.rotWrap(this.f_31176_.getLatencyPos(5, 1.0F)[0] - this.f_31176_.getLatencyPos(10, 1.0F)[0]);
        OpenMatrix4f modelMatrix = MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, f1, f, f, 1.0F, 1.0F, 1.0F, 1.0F).rotateDeg(-f2 * 1.5F, Vec3f.Z_AXIS);
        mouthpos.mulFront(modelMatrix);
        if (this.f_31176_.m_5448_() != null) {
            Vec3 vec31 = this.f_31176_.m_5448_().m_20182_().add(0.0, 12.0, 0.0);
            if (!this.isActuallyAttacking && vec31.subtract(this.f_31176_.m_20182_()).lengthSqr() < 900.0) {
                this.f_31176_.m_9236_().playLocalSound(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), EpicFightSounds.ENDER_DRAGON_BREATH.get(), this.f_31176_.getSoundSource(), 5.0F, 1.0F, false);
                this.isActuallyAttacking = true;
            }
        }
        if (this.isActuallyAttacking) {
            for (int i = 0; i < 60; i++) {
                Vec3f particleDelta = new Vec3f(0.0F, -1.0F, 0.0F);
                float xDeg = this.f_31176_.m_217043_().nextFloat() * 60.0F - 30.0F;
                float zDeg = this.f_31176_.m_217043_().nextFloat() * 60.0F - 30.0F;
                float speed = Math.min((60.0F - (Math.abs(xDeg) + Math.abs(zDeg))) / 20.0F, 1.0F);
                particleDelta.rotate(xDeg, Vec3f.X_AXIS);
                particleDelta.rotate(zDeg, Vec3f.Z_AXIS);
                particleDelta.scale(speed);
                this.f_31176_.m_9236_().addAlwaysVisibleParticle(EpicFightParticles.BREATH_FLAME.get(), (double) mouthpos.m30 + dragonpos.x, (double) mouthpos.m31 + dragonpos.y, (double) mouthpos.m32 + dragonpos.z, (double) particleDelta.x, (double) particleDelta.y, (double) particleDelta.z);
            }
        }
    }

    @Override
    public void doServerTick() {
        LivingEntity target = this.f_31176_.m_5448_();
        if (target == null) {
            this.f_31176_.getPhaseManager().setPhase(PatchedPhases.FLYING);
        } else if (isValidTarget(target)) {
            Vec3 startToDragon = this.f_31176_.m_20182_().subtract(this.startpos);
            Vec3 startToTarget = target.m_20182_().subtract(this.startpos);
            if (startToDragon.horizontalDistanceSqr() < startToTarget.horizontalDistanceSqr()) {
                Vec3 vec31 = target.m_20182_().add(0.0, 12.0, 0.0);
                if (!this.isActuallyAttacking && vec31.subtract(this.f_31176_.m_20182_()).lengthSqr() < 900.0) {
                    this.isActuallyAttacking = true;
                }
                double d8 = vec31.x - this.f_31176_.m_20185_();
                double d9 = vec31.y - this.f_31176_.m_20186_();
                double d10 = vec31.z - this.f_31176_.m_20189_();
                float f5 = this.getFlySpeed();
                double d4 = Math.sqrt(d8 * d8 + d10 * d10);
                if (d4 > 0.0) {
                    d9 = Mth.clamp(d9 / d4, (double) (-f5), (double) f5);
                }
                this.f_31176_.m_20256_(this.f_31176_.m_20184_().add(0.0, d9 * 0.1, 0.0));
                this.f_31176_.m_146922_(Mth.wrapDegrees(this.f_31176_.m_146908_()));
                Vec3 vec32 = vec31.subtract(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_()).normalize();
                Vec3 vec33 = new Vec3((double) Mth.sin(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)), this.f_31176_.m_20184_().y, (double) (-Mth.cos(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)))).normalize();
                float f6 = Math.max(((float) vec33.dot(vec32) + 0.5F) / 1.5F, 0.0F);
                if (Math.abs(d8) > 1.0E-5F || Math.abs(d10) > 1.0E-5F) {
                    double dx = target.m_20185_() - this.f_31176_.m_20185_();
                    double dz = target.m_20189_() - this.f_31176_.m_20189_();
                    float yRot = 180.0F - (float) Math.toDegrees(Mth.atan2(dx, dz));
                    this.f_31176_.m_146922_(MathUtils.rotlerp(this.f_31176_.m_146908_(), yRot, 6.0F));
                    double speed = (-0.5 - 1.0 / (1.0 + Math.pow(Math.E, -(d4 / 10.0 - 4.0)))) * (double) f6;
                    Vec3 forward = this.f_31176_.m_20156_().scale(speed);
                    this.f_31176_.m_6478_(MoverType.SELF, forward);
                }
                if (this.f_31176_.inWall) {
                    this.f_31176_.m_6478_(MoverType.SELF, this.f_31176_.m_20184_().scale(0.8F));
                } else {
                    this.f_31176_.m_6478_(MoverType.SELF, this.f_31176_.m_20184_());
                }
                Vec3 vec34 = this.f_31176_.m_20184_().normalize();
                double d6 = 0.8 + 0.15 * (vec34.dot(vec33) + 1.0) / 2.0;
                this.f_31176_.m_20256_(this.f_31176_.m_20184_().multiply(d6, 0.91F, d6));
                if (this.isActuallyAttacking && this.f_31176_.f_19797_ % 5 == 0) {
                    Vec3 createpos = this.f_31176_.m_20182_().add(this.f_31176_.m_20154_().scale(-4.5));
                    AreaEffectBreath breatharea = new AreaEffectBreath(this.f_31176_.m_9236_(), createpos.x, createpos.y, createpos.z);
                    breatharea.m_19718_(this.f_31176_);
                    breatharea.m_19740_(0);
                    breatharea.m_19712_(0.5F);
                    breatharea.m_19734_(15);
                    breatharea.m_19738_(0.2F);
                    breatharea.m_19716_(new MobEffectInstance(MobEffects.HARM, 1, 1));
                    breatharea.m_20334_(0.0, -1.0, 0.0);
                    this.f_31176_.m_9236_().m_7967_(breatharea);
                }
            } else {
                this.f_31176_.getPhaseManager().setPhase(PatchedPhases.FLYING);
            }
        } else {
            this.f_31176_.getPhaseManager().setPhase(PatchedPhases.FLYING);
        }
    }

    public boolean isActuallyAttacking() {
        return this.isActuallyAttacking;
    }

    @Override
    public float getFlySpeed() {
        return 2.0F;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return PatchedPhases.AIRSTRIKE;
    }
}
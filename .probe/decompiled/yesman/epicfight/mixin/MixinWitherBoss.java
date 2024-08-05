package yesman.epicfight.mixin;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.boss.WitherPatch;

@Mixin({ WitherBoss.class })
public abstract class MixinWitherBoss extends Monster implements PowerableMob, RangedAttackMob {

    @Shadow
    @Final
    private final int[] nextHeadUpdate = new int[2];

    @Shadow
    @Final
    private final int[] idleHeadUpdates = new int[2];

    @Shadow
    @Final
    private ServerBossEvent bossEvent;

    @Shadow
    private int destroyBlocksTick;

    @Unique
    private WitherPatch epicfightPatch;

    protected MixinWitherBoss(EntityType<? extends WitherBoss> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = { @At("RETURN") }, method = { "<init>" })
    private void epicfight_witherBossInit(CallbackInfo info) {
        this.epicfightPatch = EpicFightCapabilities.getEntityPatch((WitherBoss) this, WitherPatch.class);
    }

    @Inject(at = { @At("HEAD") }, method = { "aiStep()V" }, cancellable = true)
    private void epicfight_aiStep(CallbackInfo info) {
        if (this.epicfightPatch != null) {
            info.cancel();
            WitherBoss self = this.epicfightPatch.getOriginal();
            super.aiStep();
            for (int i = 0; i < 2; i++) {
                self.yRotOHeads[i] = self.yRotHeads[i];
                self.xRotOHeads[i] = self.xRotHeads[i];
            }
            for (int j = 0; j < 2; j++) {
                int k = self.getAlternativeTarget(j + 1);
                Entity entity1 = null;
                if (k > 0) {
                    entity1 = this.m_9236_().getEntity(k);
                }
                if (this.epicfightPatch.getLaserTargetEntity(j + 1) != null) {
                    Entity laserTarget = this.epicfightPatch.getLaserTargetEntity(j + 1);
                    this.lookAt(j, laserTarget.getX(), laserTarget.getEyeY(), laserTarget.getZ(), 360.0F, 360.0F);
                } else if (isValid(this.epicfightPatch.getLaserTargetPosition(j + 1))) {
                    Vec3 laserTargetPosition = this.epicfightPatch.getLaserTargetPosition(j + 1);
                    this.lookAt(j, laserTargetPosition.x, laserTargetPosition.y, laserTargetPosition.z, 360.0F, 360.0F);
                } else if (this.epicfightPatch.getEntityState().inaction()) {
                    self.xRotHeads[j] = this.rotlerp(self.xRotHeads[j], 0.0F, 40.0F);
                    self.yRotHeads[j] = this.rotlerp(self.yRotHeads[j], this.f_20883_, 10.0F);
                } else if (entity1 != null) {
                    this.lookAt(j, entity1.getX(), entity1.getEyeY(), entity1.getZ(), 40.0F, 10.0F);
                } else {
                    self.yRotHeads[j] = this.rotlerp(self.yRotHeads[j], this.f_20883_, 10.0F);
                }
            }
            boolean powered = this.m_7090_();
            for (int l = 0; l < 3; l++) {
                double subHeadX = self.getHeadX(l);
                double subHeadY = self.getHeadY(l);
                double subHeadZ = self.getHeadZ(l);
                if (!this.epicfightPatch.isGhost()) {
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, subHeadX + this.f_19796_.nextGaussian() * 0.3F, subHeadY + this.f_19796_.nextGaussian() * 0.3F, subHeadZ + this.f_19796_.nextGaussian() * 0.3F, 0.0, 0.0, 0.0);
                    if (powered && this.m_9236_().random.nextInt(4) == 0) {
                        this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, subHeadX + this.f_19796_.nextGaussian() * 0.3F, subHeadY + this.f_19796_.nextGaussian() * 0.3F, subHeadZ + this.f_19796_.nextGaussian() * 0.3F, 0.7F, 0.7F, 0.5);
                    }
                }
            }
            if (self.getInvulnerableTicks() > 0) {
                for (int i1 = 0; i1 < 3; i1++) {
                    this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + this.f_19796_.nextGaussian(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * 3.3F), this.m_20189_() + this.f_19796_.nextGaussian(), 0.7F, 0.7F, 0.9F);
                }
            }
        }
    }

    @Unique
    private void lookAt(int head, double x, double y, double z, float lerpX, float lerpY) {
        WitherBoss self = this.epicfightPatch.getOriginal();
        double d9 = self.getHeadX(head + 1);
        double d1 = self.getHeadY(head + 1);
        double d3 = self.getHeadZ(head + 1);
        double d4 = x - d9;
        double d5 = y - d1;
        double d6 = z - d3;
        double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        float f = (float) (Mth.atan2(d6, d4) * (180.0 / Math.PI)) - 90.0F;
        float f1 = (float) (-(Mth.atan2(d5, d7) * (180.0 / Math.PI)));
        self.xRotHeads[head] = this.rotlerp(self.xRotHeads[head], f1, lerpX);
        self.yRotHeads[head] = this.rotlerp(self.yRotHeads[head], f, lerpY);
    }

    @Inject(at = { @At("HEAD") }, method = { "customServerAiStep()V" }, cancellable = true)
    private void epicfight_customServerAiStep(CallbackInfo info) {
        if (this.epicfightPatch != null) {
            info.cancel();
            WitherBoss self = this.epicfightPatch.getOriginal();
            if (self.getInvulnerableTicks() > 0) {
                int k1 = self.getInvulnerableTicks() - 1;
                this.bossEvent.setProgress(1.0F - (float) k1 / 220.0F);
                if (k1 <= 0) {
                    Level.ExplosionInteraction explosion$blockinteraction = ForgeEventFactory.getMobGriefingEvent(self.m_9236_(), this) ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
                    self.m_9236_().explode(this, self.m_20185_(), self.m_20188_(), self.m_20189_(), 7.0F, false, explosion$blockinteraction);
                    if (!self.m_20067_()) {
                        self.m_9236_().globalLevelEvent(1023, self.m_20183_(), 0);
                    }
                }
                self.setInvulnerableTicks(k1);
                if (self.f_19797_ % 10 == 0) {
                    self.m_5634_(10.0F);
                }
            } else {
                super.m_8024_();
                for (int i = 1; i < 3; i++) {
                    if (self.f_19797_ >= this.nextHeadUpdate[i - 1]) {
                        this.nextHeadUpdate[i - 1] = self.f_19797_ + 10 + self.m_217043_().nextInt(10);
                        if ((self.m_9236_().m_46791_() == Difficulty.NORMAL || self.m_9236_().m_46791_() == Difficulty.HARD) && !this.epicfightPatch.getEntityState().inaction()) {
                            int i3 = i - 1;
                            int j3 = this.idleHeadUpdates[i - 1];
                            this.idleHeadUpdates[i3] = this.idleHeadUpdates[i - 1] + 1;
                            if (j3 > 15) {
                                double d0 = Mth.nextDouble(self.m_217043_(), self.m_20185_() - 10.0, self.m_20185_() + 10.0);
                                double d1 = Mth.nextDouble(self.m_217043_(), self.m_20186_() - 5.0, self.m_20186_() + 5.0);
                                double d2 = Mth.nextDouble(self.m_217043_(), self.m_20189_() - 10.0, self.m_20189_() + 10.0);
                                self.performRangedAttack(i + 1, d0, d1, d2, true);
                                this.idleHeadUpdates[i - 1] = 0;
                            }
                        }
                        int l1 = self.getAlternativeTarget(i);
                        if (this.epicfightPatch.getEntityState().inaction()) {
                            this.nextHeadUpdate[i - 1] = self.f_19797_ + 30;
                        }
                        if (l1 > 0) {
                            LivingEntity livingentity = (LivingEntity) self.m_9236_().getEntity(l1);
                            if (livingentity == null || !self.m_6779_(livingentity) || self.m_20280_(livingentity) > 900.0 || !self.m_142582_(livingentity)) {
                                self.setAlternativeTarget(i, 0);
                            } else if (!this.epicfightPatch.getEntityState().inaction()) {
                                self.performRangedAttack(i + 1, livingentity);
                                this.nextHeadUpdate[i - 1] = self.f_19797_ + 40 + self.m_217043_().nextInt(20);
                                this.idleHeadUpdates[i - 1] = 0;
                            }
                        } else {
                            List<LivingEntity> list = self.m_9236_().m_45971_(LivingEntity.class, WitherPatch.WTIHER_TARGETING_CONDITIONS, this, self.m_20191_().inflate(20.0, 8.0, 20.0));
                            if (!list.isEmpty()) {
                                LivingEntity livingentity1 = (LivingEntity) list.get(self.m_217043_().nextInt(list.size()));
                                self.setAlternativeTarget(i, livingentity1.m_19879_());
                            }
                        }
                    }
                }
                if (self.m_5448_() != null) {
                    self.setAlternativeTarget(0, self.m_5448_().m_19879_());
                } else {
                    self.setAlternativeTarget(0, 0);
                }
                if (this.destroyBlocksTick > 0) {
                    this.destroyBlocksTick--;
                    if (this.destroyBlocksTick == 0 && ForgeEventFactory.getMobGriefingEvent(self.m_9236_(), this)) {
                        int j1 = Mth.floor(self.m_20186_());
                        int i2 = Mth.floor(self.m_20185_());
                        int j2 = Mth.floor(self.m_20189_());
                        boolean flag = false;
                        for (int j = -1; j <= 1; j++) {
                            for (int k2 = -1; k2 <= 1; k2++) {
                                for (int k = 0; k <= 3; k++) {
                                    int l2 = i2 + j;
                                    int l = j1 + k;
                                    int i1 = j2 + k2;
                                    BlockPos blockpos = new BlockPos(l2, l, i1);
                                    BlockState blockstate = self.m_9236_().getBlockState(blockpos);
                                    if (blockstate.canEntityDestroy(self.m_9236_(), blockpos, this) && ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                        flag = self.m_9236_().m_46953_(blockpos, true, this) || flag;
                                    }
                                }
                            }
                        }
                        if (flag) {
                            self.m_9236_().m_5898_(null, 1022, self.m_20183_(), 0);
                        }
                    }
                }
                this.bossEvent.setProgress(self.m_21223_() / self.m_21233_());
            }
        }
    }

    @Unique
    @Override
    public boolean isSpectator() {
        return this.epicfightPatch != null ? this.epicfightPatch.isGhost() : super.m_5833_();
    }

    @Unique
    @Override
    protected SoundEvent getAmbientSound() {
        return this.epicfightPatch != null ? (this.epicfightPatch.isGhost() ? null : SoundEvents.WITHER_AMBIENT) : null;
    }

    @Shadow
    private float rotlerp(float float0, float float1, float float2) {
        throw new AbstractMethodError("Shadow");
    }

    private static boolean isValid(Vec3 vec) {
        return !Double.isNaN(vec.x) && !Double.isNaN(vec.y) && !Double.isNaN(vec.z);
    }
}
package net.minecraft.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class Squid extends WaterAnimal {

    public float xBodyRot;

    public float xBodyRotO;

    public float zBodyRot;

    public float zBodyRotO;

    public float tentacleMovement;

    public float oldTentacleMovement;

    public float tentacleAngle;

    public float oldTentacleAngle;

    private float speed;

    private float tentacleSpeed;

    private float rotateSpeed;

    private float tx;

    private float ty;

    private float tz;

    public Squid(EntityType<? extends Squid> entityTypeExtendsSquid0, Level level1) {
        super(entityTypeExtendsSquid0, level1);
        this.f_19796_.setSeed((long) this.m_19879_());
        this.tentacleSpeed = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new Squid.SquidRandomMovementGoal(this));
        this.f_21345_.addGoal(1, new Squid.SquidFleeGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.SQUID_SQUIRT;
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return !this.m_21523_();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;
        this.oldTentacleMovement = this.tentacleMovement;
        this.oldTentacleAngle = this.tentacleAngle;
        this.tentacleMovement = this.tentacleMovement + this.tentacleSpeed;
        if ((double) this.tentacleMovement > Math.PI * 2) {
            if (this.m_9236_().isClientSide) {
                this.tentacleMovement = (float) (Math.PI * 2);
            } else {
                this.tentacleMovement -= (float) (Math.PI * 2);
                if (this.f_19796_.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.2F;
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 19);
            }
        }
        if (this.m_20072_()) {
            if (this.tentacleMovement < (float) Math.PI) {
                float $$0 = this.tentacleMovement / (float) Math.PI;
                this.tentacleAngle = Mth.sin($$0 * $$0 * (float) Math.PI) * (float) Math.PI * 0.25F;
                if ((double) $$0 > 0.75) {
                    this.speed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.speed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }
            if (!this.m_9236_().isClientSide) {
                this.m_20334_((double) (this.tx * this.speed), (double) (this.ty * this.speed), (double) (this.tz * this.speed));
            }
            Vec3 $$1 = this.m_20184_();
            double $$2 = $$1.horizontalDistance();
            this.f_20883_ = this.f_20883_ + (-((float) Mth.atan2($$1.x, $$1.z)) * (180.0F / (float) Math.PI) - this.f_20883_) * 0.1F;
            this.m_146922_(this.f_20883_);
            this.zBodyRot = this.zBodyRot + (float) Math.PI * this.rotateSpeed * 1.5F;
            this.xBodyRot = this.xBodyRot + (-((float) Mth.atan2($$2, $$1.y)) * (180.0F / (float) Math.PI) - this.xBodyRot) * 0.1F;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * (float) Math.PI * 0.25F;
            if (!this.m_9236_().isClientSide) {
                double $$3 = this.m_20184_().y;
                if (this.m_21023_(MobEffects.LEVITATION)) {
                    $$3 = 0.05 * (double) (this.m_21124_(MobEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.m_20068_()) {
                    $$3 -= 0.08;
                }
                this.m_20334_(0.0, $$3 * 0.98F, 0.0);
            }
            this.xBodyRot = this.xBodyRot + (-90.0F - this.xBodyRot) * 0.02F;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (super.m_6469_(damageSource0, float1) && this.m_21188_() != null) {
            if (!this.m_9236_().isClientSide) {
                this.spawnInk();
            }
            return true;
        } else {
            return false;
        }
    }

    private Vec3 rotateVector(Vec3 vec0) {
        Vec3 $$1 = vec0.xRot(this.xBodyRotO * (float) (Math.PI / 180.0));
        return $$1.yRot(-this.f_20884_ * (float) (Math.PI / 180.0));
    }

    private void spawnInk() {
        this.m_5496_(this.getSquirtSound(), this.getSoundVolume(), this.m_6100_());
        Vec3 $$0 = this.rotateVector(new Vec3(0.0, -1.0, 0.0)).add(this.m_20185_(), this.m_20186_(), this.m_20189_());
        for (int $$1 = 0; $$1 < 30; $$1++) {
            Vec3 $$2 = this.rotateVector(new Vec3((double) this.f_19796_.nextFloat() * 0.6 - 0.3, -1.0, (double) this.f_19796_.nextFloat() * 0.6 - 0.3));
            Vec3 $$3 = $$2.scale(0.3 + (double) (this.f_19796_.nextFloat() * 2.0F));
            ((ServerLevel) this.m_9236_()).sendParticles(this.getInkParticle(), $$0.x, $$0.y + 0.5, $$0.z, 0, $$3.x, $$3.y, $$3.z, 0.1F);
        }
    }

    protected ParticleOptions getInkParticle() {
        return ParticleTypes.SQUID_INK;
    }

    @Override
    public void travel(Vec3 vec0) {
        this.m_6478_(MoverType.SELF, this.m_20184_());
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 19) {
            this.tentacleMovement = 0.0F;
        } else {
            super.m_7822_(byte0);
        }
    }

    public void setMovementVector(float float0, float float1, float float2) {
        this.tx = float0;
        this.ty = float1;
        this.tz = float2;
    }

    public boolean hasMovementVector() {
        return this.tx != 0.0F || this.ty != 0.0F || this.tz != 0.0F;
    }

    class SquidFleeGoal extends Goal {

        private static final float SQUID_FLEE_SPEED = 3.0F;

        private static final float SQUID_FLEE_MIN_DISTANCE = 5.0F;

        private static final float SQUID_FLEE_MAX_DISTANCE = 10.0F;

        private int fleeTicks;

        @Override
        public boolean canUse() {
            LivingEntity $$0 = Squid.this.m_21188_();
            return Squid.this.m_20069_() && $$0 != null ? Squid.this.m_20280_($$0) < 100.0 : false;
        }

        @Override
        public void start() {
            this.fleeTicks = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.fleeTicks++;
            LivingEntity $$0 = Squid.this.m_21188_();
            if ($$0 != null) {
                Vec3 $$1 = new Vec3(Squid.this.m_20185_() - $$0.m_20185_(), Squid.this.m_20186_() - $$0.m_20186_(), Squid.this.m_20189_() - $$0.m_20189_());
                BlockState $$2 = Squid.this.m_9236_().getBlockState(BlockPos.containing(Squid.this.m_20185_() + $$1.x, Squid.this.m_20186_() + $$1.y, Squid.this.m_20189_() + $$1.z));
                FluidState $$3 = Squid.this.m_9236_().getFluidState(BlockPos.containing(Squid.this.m_20185_() + $$1.x, Squid.this.m_20186_() + $$1.y, Squid.this.m_20189_() + $$1.z));
                if ($$3.is(FluidTags.WATER) || $$2.m_60795_()) {
                    double $$4 = $$1.length();
                    if ($$4 > 0.0) {
                        $$1.normalize();
                        double $$5 = 3.0;
                        if ($$4 > 5.0) {
                            $$5 -= ($$4 - 5.0) / 5.0;
                        }
                        if ($$5 > 0.0) {
                            $$1 = $$1.scale($$5);
                        }
                    }
                    if ($$2.m_60795_()) {
                        $$1 = $$1.subtract(0.0, $$1.y, 0.0);
                    }
                    Squid.this.setMovementVector((float) $$1.x / 20.0F, (float) $$1.y / 20.0F, (float) $$1.z / 20.0F);
                }
                if (this.fleeTicks % 10 == 5) {
                    Squid.this.m_9236_().addParticle(ParticleTypes.BUBBLE, Squid.this.m_20185_(), Squid.this.m_20186_(), Squid.this.m_20189_(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    class SquidRandomMovementGoal extends Goal {

        private final Squid squid;

        public SquidRandomMovementGoal(Squid squid0) {
            this.squid = squid0;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int $$0 = this.squid.m_21216_();
            if ($$0 > 100) {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.m_217043_().nextInt(m_186073_(50)) == 0 || !this.squid.f_19798_ || !this.squid.hasMovementVector()) {
                float $$1 = this.squid.m_217043_().nextFloat() * (float) (Math.PI * 2);
                float $$2 = Mth.cos($$1) * 0.2F;
                float $$3 = -0.1F + this.squid.m_217043_().nextFloat() * 0.2F;
                float $$4 = Mth.sin($$1) * 0.2F;
                this.squid.setMovementVector($$2, $$3, $$4);
            }
        }
    }
}
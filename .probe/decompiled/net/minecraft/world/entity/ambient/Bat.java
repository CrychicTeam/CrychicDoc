package net.minecraft.world.entity.ambient;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Bat extends AmbientCreature {

    public static final float FLAP_DEGREES_PER_TICK = 74.48451F;

    public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);

    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Bat.class, EntityDataSerializers.BYTE);

    private static final int FLAG_RESTING = 1;

    private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0);

    @Nullable
    private BlockPos targetPosition;

    public Bat(EntityType<? extends Bat> entityTypeExtendsBat0, Level level1) {
        super(entityTypeExtendsBat0, level1);
        if (!level1.isClientSide) {
            this.setResting(true);
        }
    }

    @Override
    public boolean isFlapping() {
        return !this.isResting() && this.f_19797_ % TICKS_PER_FLAP == 0;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_ID_FLAGS, (byte) 0);
    }

    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    @Override
    public float getVoicePitch() {
        return super.m_6100_() * 0.95F;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return this.isResting() && this.f_19796_.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity0) {
    }

    @Override
    protected void pushEntities() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }

    public boolean isResting() {
        return (this.f_19804_.get(DATA_ID_FLAGS) & 1) != 0;
    }

    public void setResting(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_ID_FLAGS);
        if (boolean0) {
            this.f_19804_.set(DATA_ID_FLAGS, (byte) ($$1 | 1));
        } else {
            this.f_19804_.set(DATA_ID_FLAGS, (byte) ($$1 & -2));
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.isResting()) {
            this.m_20256_(Vec3.ZERO);
            this.m_20343_(this.m_20185_(), (double) Mth.floor(this.m_20186_()) + 1.0 - (double) this.m_20206_(), this.m_20189_());
        } else {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.6, 1.0));
        }
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        BlockPos $$0 = this.m_20183_();
        BlockPos $$1 = $$0.above();
        if (this.isResting()) {
            boolean $$2 = this.m_20067_();
            if (this.m_9236_().getBlockState($$1).m_60796_(this.m_9236_(), $$0)) {
                if (this.f_19796_.nextInt(200) == 0) {
                    this.f_20885_ = (float) this.f_19796_.nextInt(360);
                }
                if (this.m_9236_().m_45946_(BAT_RESTING_TARGETING, this) != null) {
                    this.setResting(false);
                    if (!$$2) {
                        this.m_9236_().m_5898_(null, 1025, $$0, 0);
                    }
                }
            } else {
                this.setResting(false);
                if (!$$2) {
                    this.m_9236_().m_5898_(null, 1025, $$0, 0);
                }
            }
        } else {
            if (this.targetPosition != null && (!this.m_9236_().m_46859_(this.targetPosition) || this.targetPosition.m_123342_() <= this.m_9236_().m_141937_())) {
                this.targetPosition = null;
            }
            if (this.targetPosition == null || this.f_19796_.nextInt(30) == 0 || this.targetPosition.m_203195_(this.m_20182_(), 2.0)) {
                this.targetPosition = BlockPos.containing(this.m_20185_() + (double) this.f_19796_.nextInt(7) - (double) this.f_19796_.nextInt(7), this.m_20186_() + (double) this.f_19796_.nextInt(6) - 2.0, this.m_20189_() + (double) this.f_19796_.nextInt(7) - (double) this.f_19796_.nextInt(7));
            }
            double $$3 = (double) this.targetPosition.m_123341_() + 0.5 - this.m_20185_();
            double $$4 = (double) this.targetPosition.m_123342_() + 0.1 - this.m_20186_();
            double $$5 = (double) this.targetPosition.m_123343_() + 0.5 - this.m_20189_();
            Vec3 $$6 = this.m_20184_();
            Vec3 $$7 = $$6.add((Math.signum($$3) * 0.5 - $$6.x) * 0.1F, (Math.signum($$4) * 0.7F - $$6.y) * 0.1F, (Math.signum($$5) * 0.5 - $$6.z) * 0.1F);
            this.m_20256_($$7);
            float $$8 = (float) (Mth.atan2($$7.z, $$7.x) * 180.0F / (float) Math.PI) - 90.0F;
            float $$9 = Mth.wrapDegrees($$8 - this.m_146908_());
            this.f_20902_ = 0.5F;
            this.m_146922_(this.m_146908_() + $$9);
            if (this.f_19796_.nextInt(100) == 0 && this.m_9236_().getBlockState($$1).m_60796_(this.m_9236_(), $$1)) {
                this.setResting(true);
            }
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            if (!this.m_9236_().isClientSide && this.isResting()) {
                this.setResting(false);
            }
            return super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.f_19804_.set(DATA_ID_FLAGS, compoundTag0.getByte("BatFlags"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putByte("BatFlags", this.f_19804_.get(DATA_ID_FLAGS));
    }

    public static boolean checkBatSpawnRules(EntityType<Bat> entityTypeBat0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        if (blockPos3.m_123342_() >= levelAccessor1.m_5736_()) {
            return false;
        } else {
            int $$5 = levelAccessor1.m_46803_(blockPos3);
            int $$6 = 4;
            if (isHalloween()) {
                $$6 = 7;
            } else if (randomSource4.nextBoolean()) {
                return false;
            }
            return $$5 > randomSource4.nextInt($$6) ? false : m_217057_(entityTypeBat0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
        }
    }

    private static boolean isHalloween() {
        LocalDate $$0 = LocalDate.now();
        int $$1 = $$0.get(ChronoField.DAY_OF_MONTH);
        int $$2 = $$0.get(ChronoField.MONTH_OF_YEAR);
        return $$2 == 10 && $$1 >= 20 || $$2 == 11 && $$1 <= 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height / 2.0F;
    }
}
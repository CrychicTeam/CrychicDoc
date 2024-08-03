package net.minecraft.world.entity.monster;

import java.util.EnumSet;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class Vex extends Monster implements TraceableEntity {

    public static final float FLAP_DEGREES_PER_TICK = 45.836624F;

    public static final int TICKS_PER_FLAP = Mth.ceil((float) (Math.PI * 5.0 / 4.0));

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Vex.class, EntityDataSerializers.BYTE);

    private static final int FLAG_IS_CHARGING = 1;

    private static final double RIDING_OFFSET = 0.4;

    @Nullable
    Mob owner;

    @Nullable
    private BlockPos boundOrigin;

    private boolean hasLimitedLife;

    private int limitedLifeTicks;

    public Vex(EntityType<? extends Vex> entityTypeExtendsVex0, Level level1) {
        super(entityTypeExtendsVex0, level1);
        this.f_21342_ = new Vex.VexMoveControl(this);
        this.f_21364_ = 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height - 0.28125F;
    }

    @Override
    public boolean isFlapping() {
        return this.f_19797_ % TICKS_PER_FLAP == 0;
    }

    @Override
    public void move(MoverType moverType0, Vec3 vec1) {
        super.m_6478_(moverType0, vec1);
        this.m_20101_();
    }

    @Override
    public void tick() {
        this.f_19794_ = true;
        super.m_8119_();
        this.f_19794_ = false;
        this.m_20242_(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.m_6469_(this.m_269291_().starve(), 1.0F);
        }
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(4, new Vex.VexChargeAttackGoal());
        this.f_21345_.addGoal(8, new Vex.VexRandomMoveGoal());
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(2, new Vex.VexCopyOwnerTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compoundTag0.getInt("BoundX"), compoundTag0.getInt("BoundY"), compoundTag0.getInt("BoundZ"));
        }
        if (compoundTag0.contains("LifeTicks")) {
            this.setLimitedLife(compoundTag0.getInt("LifeTicks"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        if (this.boundOrigin != null) {
            compoundTag0.putInt("BoundX", this.boundOrigin.m_123341_());
            compoundTag0.putInt("BoundY", this.boundOrigin.m_123342_());
            compoundTag0.putInt("BoundZ", this.boundOrigin.m_123343_());
        }
        if (this.hasLimitedLife) {
            compoundTag0.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    @Nullable
    public Mob getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos blockPos0) {
        this.boundOrigin = blockPos0;
    }

    private boolean getVexFlag(int int0) {
        int $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        return ($$1 & int0) != 0;
    }

    private void setVexFlag(int int0, boolean boolean1) {
        int $$2 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean1) {
            $$2 |= int0;
        } else {
            $$2 &= ~int0;
        }
        this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$2 & 0xFF));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setIsCharging(boolean boolean0) {
        this.setVexFlag(1, boolean0);
    }

    public void setOwner(Mob mob0) {
        this.owner = mob0;
    }

    public void setLimitedLife(int int0) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = int0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        this.populateDefaultEquipmentSlots($$5, difficultyInstance1);
        this.m_213946_($$5, difficultyInstance1);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.m_21409_(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.4;
    }

    class VexChargeAttackGoal extends Goal {

        public VexChargeAttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = Vex.this.m_5448_();
            return $$0 != null && $$0.isAlive() && !Vex.this.m_21566_().hasWanted() && Vex.this.f_19796_.nextInt(m_186073_(7)) == 0 ? Vex.this.m_20280_($$0) > 4.0 : false;
        }

        @Override
        public boolean canContinueToUse() {
            return Vex.this.m_21566_().hasWanted() && Vex.this.isCharging() && Vex.this.m_5448_() != null && Vex.this.m_5448_().isAlive();
        }

        @Override
        public void start() {
            LivingEntity $$0 = Vex.this.m_5448_();
            if ($$0 != null) {
                Vec3 $$1 = $$0.m_146892_();
                Vex.this.f_21342_.setWantedPosition($$1.x, $$1.y, $$1.z, 1.0);
            }
            Vex.this.setIsCharging(true);
            Vex.this.m_5496_(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            Vex.this.setIsCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = Vex.this.m_5448_();
            if ($$0 != null) {
                if (Vex.this.m_20191_().intersects($$0.m_20191_())) {
                    Vex.this.m_7327_($$0);
                    Vex.this.setIsCharging(false);
                } else {
                    double $$1 = Vex.this.m_20280_($$0);
                    if ($$1 < 9.0) {
                        Vec3 $$2 = $$0.m_146892_();
                        Vex.this.f_21342_.setWantedPosition($$2.x, $$2.y, $$2.z, 1.0);
                    }
                }
            }
        }
    }

    class VexCopyOwnerTargetGoal extends TargetGoal {

        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public VexCopyOwnerTargetGoal(PathfinderMob pathfinderMob0) {
            super(pathfinderMob0, false);
        }

        @Override
        public boolean canUse() {
            return Vex.this.owner != null && Vex.this.owner.getTarget() != null && this.m_26150_(Vex.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        @Override
        public void start() {
            Vex.this.m_6710_(Vex.this.owner.getTarget());
            super.start();
        }
    }

    class VexMoveControl extends MoveControl {

        public VexMoveControl(Vex vex0) {
            super(vex0);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.f_24975_ - Vex.this.m_20185_(), this.f_24976_ - Vex.this.m_20186_(), this.f_24977_ - Vex.this.m_20189_());
                double $$1 = $$0.length();
                if ($$1 < Vex.this.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    Vex.this.m_20256_(Vex.this.m_20184_().scale(0.5));
                } else {
                    Vex.this.m_20256_(Vex.this.m_20184_().add($$0.scale(this.f_24978_ * 0.05 / $$1)));
                    if (Vex.this.m_5448_() == null) {
                        Vec3 $$2 = Vex.this.m_20184_();
                        Vex.this.m_146922_(-((float) Mth.atan2($$2.x, $$2.z)) * (180.0F / (float) Math.PI));
                        Vex.this.f_20883_ = Vex.this.m_146908_();
                    } else {
                        double $$3 = Vex.this.m_5448_().m_20185_() - Vex.this.m_20185_();
                        double $$4 = Vex.this.m_5448_().m_20189_() - Vex.this.m_20189_();
                        Vex.this.m_146922_(-((float) Mth.atan2($$3, $$4)) * (180.0F / (float) Math.PI));
                        Vex.this.f_20883_ = Vex.this.m_146908_();
                    }
                }
            }
        }
    }

    class VexRandomMoveGoal extends Goal {

        public VexRandomMoveGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !Vex.this.m_21566_().hasWanted() && Vex.this.f_19796_.nextInt(m_186073_(7)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos $$0 = Vex.this.getBoundOrigin();
            if ($$0 == null) {
                $$0 = Vex.this.m_20183_();
            }
            for (int $$1 = 0; $$1 < 3; $$1++) {
                BlockPos $$2 = $$0.offset(Vex.this.f_19796_.nextInt(15) - 7, Vex.this.f_19796_.nextInt(11) - 5, Vex.this.f_19796_.nextInt(15) - 7);
                if (Vex.this.m_9236_().m_46859_($$2)) {
                    Vex.this.f_21342_.setWantedPosition((double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_() + 0.5, (double) $$2.m_123343_() + 0.5, 0.25);
                    if (Vex.this.m_5448_() == null) {
                        Vex.this.m_21563_().setLookAt((double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_() + 0.5, (double) $$2.m_123343_() + 0.5, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }
}